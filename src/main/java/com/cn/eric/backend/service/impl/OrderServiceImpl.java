package com.cn.eric.backend.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.cn.eric.backend.common.Constant;
import com.cn.eric.backend.common.Constant.PaymentType;
import com.cn.eric.backend.common.ResponseCode;
import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.dao.CartMapper;
import com.cn.eric.backend.dao.OrderItemMapper;
import com.cn.eric.backend.dao.OrderMapper;
import com.cn.eric.backend.dao.PayInfoMapper;
import com.cn.eric.backend.dao.ProductMapper;
import com.cn.eric.backend.dao.ShippingMapper;
import com.cn.eric.backend.pojo.Cart;
import com.cn.eric.backend.pojo.Order;
import com.cn.eric.backend.pojo.OrderItem;
import com.cn.eric.backend.pojo.PayInfo;
import com.cn.eric.backend.pojo.Product;
import com.cn.eric.backend.pojo.Shipping;
import com.cn.eric.backend.service.OrderService;
import com.cn.eric.backend.util.BigDecimalUtil;
import com.cn.eric.backend.util.DateUtil;
import com.cn.eric.backend.util.FTPUtil;
import com.cn.eric.backend.util.PropertiesUtil;
import com.cn.eric.backend.vo.OrderCartVO;
import com.cn.eric.backend.vo.OrderItemVO;
import com.cn.eric.backend.vo.OrderVO;
import com.cn.eric.backend.vo.ShippingVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class OrderServiceImpl implements OrderService {

	private final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private PayInfoMapper payInfoMapper;
	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private ShippingMapper shippingMapper;

	@Override
	public ServerResponse pay(Integer userId, Long orderNo, String path) {
		Map<String, String> targetMap = Maps.newHashMap();
		targetMap.put("orderNo", orderNo.toString());

		Order order = orderMapper.getOrderByIdUserId(userId, orderNo);
		List<OrderItem> orderItems = orderItemMapper.getOrderItemsByUserId(userId);

		// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
		// 需保证商户系统端不能重复，建议通过数据库sequence生成，
		String outTradeNo = orderNo.toString();

		// (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
		String subject = "ericMall消费账单：" + orderNo;

		// (必填) 订单总金额，单位为元，不能超过1亿元
		// 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
		String totalAmount = order.getPayment().toString();

		// (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
		// 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
		String undiscountableAmount = "0";

		// 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
		// 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
		String sellerId = "";

		// 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
		String providerId = "ericmall2088100200300400500";
		ExtendParams extendParams = new ExtendParams();
		extendParams.setSysServiceProviderId(providerId);

		// 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
		String body = new StringBuffer().append("共买产品：").append(orderItems.size()).append(" 总计:")
				.append(order.getPayment().toString()).append("元").toString();

		// 商户操作员编号，添加此参数可以为商户操作员做销售统计
		String operatorId = "test_operator_id";

		// (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
		String storeId = "test_store_id";

		// 支付超时，定义为120分钟
		String timeoutExpress = "120m";

		// 商品明细列表，需填写购买商品详细信息，
		List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

		for (OrderItem item : orderItems) {
			GoodsDetail goods = GoodsDetail.newInstance(item.getProductId().toString(), item.getProductName(),
					item.getCurrentUnitPrice().longValue(), item.getQuantity());
			goodsDetailList.add(goods);
		}

		// 创建扫码支付请求builder，设置请求参数
		AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder().setSubject(subject)
				.setTotalAmount(totalAmount).setOutTradeNo(outTradeNo).setUndiscountableAmount(undiscountableAmount)
				.setSellerId(sellerId).setBody(body).setOperatorId(operatorId).setStoreId(storeId)
				.setExtendParams(extendParams).setTimeoutExpress(timeoutExpress)
				// TODO:回调地址
				.setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))// 支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
				.setGoodsDetailList(goodsDetailList);

		AlipayTradeService tradeService;
		/**
		 * 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
		 * Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
		 */
		Configs.init("zfbinfo.properties");

		/**
		 * 使用Configs提供的默认参数 AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
		 */
		tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

		AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
		switch (result.getTradeStatus()) {
		case SUCCESS:
			logger.info("支付宝预下单成功: )");

			AlipayTradePrecreateResponse response = result.getResponse();
			dumpResponse(response);

			File dir = new File(path);
			if (!dir.exists()) {
				dir.setWritable(true);
				dir.mkdirs();
			}

			String qrCodePath = dir.getAbsolutePath();
			String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());

			logger.info("filePath:" + qrCodePath + "/" + qrFileName);

			File qrFile = ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrCodePath + "/" + qrFileName);

			try {
				FTPUtil.uploadFile(Lists.newArrayList(qrFile));
			} catch (UnknownHostException e) {
				logger.error("上传二维码失败！", e);
			}
			String returnPath = PropertiesUtil.getProperty("ftp.server.http.prefix") + qrFile.getName();
			targetMap.put("qrPath", returnPath);

			return ServerResponse.createSuccessResponseByData(targetMap);
		case FAILED:
			logger.error("支付宝预下单失败!!!");
			return ServerResponse.createErrorResponseByMsg("支付宝预下单失败!!!");

		case UNKNOWN:
			logger.error("系统异常，预下单状态未知!!!");
			return ServerResponse.createErrorResponseByMsg("系统异常，预下单状态未知!!!");

		default:
			logger.error("不支持的交易状态，交易返回异常!!!");
			return ServerResponse.createErrorResponseByMsg("不支持的交易状态，交易返回异常!!!");
		}
	}

	// 简单打印应答
	private void dumpResponse(AlipayResponse response) {
		if (response != null) {
			logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
			if (StringUtils.isNotEmpty(response.getSubCode())) {
				logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(), response.getSubMsg()));
			}
			logger.info("body:" + response.getBody());
		}
	}

	@Override
	public ServerResponse callBack(Map<String,String> params) {
		Long orderNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            return ServerResponse.createErrorResponseByMsg("非Eric商城的订单,回调忽略");
        }
        if(order.getStatus() >= Constant.PayInfoStatus.PAID.getCode()){
            return ServerResponse.createErrorResponseByMsg("支付宝重复调用");
        }
        if(Constant.AlipayCallBack.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
            order.setPaymentTime(DateUtil.str2Date(params.get("gmt_payment")));
            order.setStatus(Constant.PayInfoStatus.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }

        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(Constant.PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);

        payInfoMapper.insert(payInfo);
        return ServerResponse.createSuccessResonse();
	}

	public ServerResponse queryOrderPayStatus(Integer userId,Long orderNo){
        Order order = orderMapper.getOrderByIdUserId(userId,orderNo);
        if(order == null){
            return ServerResponse.createErrorResponseByMsg("用户没有该订单");
        }
        if(order.getStatus() >= Constant.PayInfoStatus.PAID.getCode()){
            return ServerResponse.createSuccessResonse();
        }
        return ServerResponse.createErrorResonse();
    }

	@Override
	public ServerResponse createOrder(Integer userId, Long shippingId) {
		if(null==userId || null==shippingId)
			return ServerResponse.createErrorResponseByCode(ResponseCode.ILLEGAL_PARAM);
		List<Cart> cart = cartMapper.getCartsByUserId(userId);
		if(CollectionUtils.isEmpty(cart))
			return ServerResponse.createErrorResponseByMsg("创建订单失败！");
		ServerResponse<OrderVO> orderVO = wrapOrder(cart,shippingId);
		return orderVO;
	}

	private ServerResponse<OrderVO> wrapOrder(List<Cart> carts,Long shippingId) {
		List<OrderItemVO> orderItems = Lists.newArrayList();
		OrderVO orderVO = new OrderVO();
		
		orderVO.setCreateTime(new Date());
		orderVO.setOrderNo(createOrderNo());
		orderVO.setPostage(0);
		orderVO.setShippingId(shippingId);
		orderVO.setStatus(Constant.PayInfoStatus.NO_PAY.getCode());
		orderVO.setPaymentType(Constant.PaymentType.ON_LINE.getCode());
		
		for(Cart cart:carts) {
			if(cart.getChecked().equals(Constant.Cart.UN_CHECKED))
				continue;
			OrderItemVO item =wrapOrderItem(cart);
			item.setOrderNo(orderVO.getOrderNo());
			orderItems.add(item);
			cartMapper.deleteByPrimaryKey(cart.getId());
		}
		
		Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId.intValue());
		if(shipping == null)
			return ServerResponse.createErrorResponseByMsg("失败，shipping为null!");
		ShippingVO shippingVO = wrapShipping(shipping);
		orderVO.setShippingVO(shippingVO);
		return ServerResponse.createSuccessResponseByData(orderVO);
	}
	
	
	private OrderItemVO wrapOrderItem(Cart cart) {
		OrderItemVO item= new OrderItemVO();
		Product product = productMapper.selectByPrimaryKey(cart.getProductId());
		item.setCurrentUnitPrice(product.getPrice());
		item.setProductId(product.getId());
		item.setProductName(product.getName());
		item.setProductImage(product.getMainImage());
		item.setQuantity(cart.getQuantity());
		item.setTotalPrice(BigDecimalUtil.mul(item.getQuantity().doubleValue(), item.getCurrentUnitPrice().doubleValue()));
		return item;
	}

	private ShippingVO wrapShipping(Shipping shipping) {
		if(null==shipping)
			return null;
		ShippingVO vo = new ShippingVO();
		
		vo.setReceiverAddress(shipping.getReceiverAddress());
		vo.setReceiverCity(shipping.getReceiverCity());
		vo.setReceiverDistrict(shipping.getReceiverDistrict());
		vo.setReceiverMobile(shipping.getReceiverMobile());
		vo.setReceiverName(shipping.getReceiverName());
		vo.setReceiverPhone(shipping.getReceiverPhone());
		vo.setReceiverProvince(shipping.getReceiverProvince());
		vo.setReceiverZip(shipping.getReceiverZip());
		return vo;
	}

	private long createOrderNo() {
		return System.currentTimeMillis()+(new Random().nextInt()%100);
	}

	@Override
	public ServerResponse getOrderCartProduct(Integer userId) {
		if(null==userId)
			return ServerResponse.createErrorResponseByCode(ResponseCode.ILLEGAL_PARAM);
		List<Cart> carts = cartMapper.getCartsByUserId(userId);
		List<OrderItemVO> orderItems = Lists.newArrayList();
		for(Cart cart:carts) {
			if(cart.getChecked().equals(Constant.Cart.UN_CHECKED))
				continue;
			orderItems.add(wrapOrderItem(cart));
		}
		
		OrderCartVO orderCartVO = new OrderCartVO();
		orderCartVO.setOrderItemVoList(orderItems);
		orderCartVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
		BigDecimal totalPrice = new BigDecimal("0");
		for(OrderItemVO item:orderItems) {
			totalPrice = BigDecimalUtil.add(totalPrice.doubleValue(), item.getTotalPrice().doubleValue());
		}
		orderCartVO.setProductTotalPrice(totalPrice.doubleValue());
		return ServerResponse.createSuccessResponseByData(orderCartVO);
	}

	@Override
	public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Order> orders = orderMapper.getOrderByUserId(userId);
		PageInfo pageInfo = new PageInfo(orders);
		List<OrderVO> orderVOs = Lists.newArrayList();
		for(Order order:orders) {
			orderVOs.add(wrapOrder(order));
		}
		pageInfo.setList(orderVOs);
		return ServerResponse.createSuccessResponseByData(pageInfo);
	}
	
	private OrderVO wrapOrder(Order order) {
		OrderVO vo = new OrderVO();
		vo.setOrderNo(order.getOrderNo());
		vo.setPayment(order.getPayment());
		vo.setPaymentType(order.getPaymentType());
		vo.setPaymentDesc(PaymentType.codeFor(order.getPaymentType()).getValue());
		vo.setPostage(order.getPostage());
		vo.setStatus(order.getStatus());
		vo.setPayment(order.getPayment());
		vo.setSendTime(order.getSendTime());
		vo.setEndTime(order.getEndTime());
		vo.setCloseTime(order.getCloseTime());
		vo.setCreateTime(order.getCreateTime());
		
		vo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
		vo.setShippingId(order.getShippingId());
		ShippingVO shippingVO = wrapShipping(shippingMapper.selectByPrimaryKey(order.getShippingId()));
		vo.setShippingVO(shippingVO);
		
		vo.setReceiverName(shippingMapper.selectByPrimaryKey(order.getShippingId()).getReceiverName());
		
		List<OrderItem> items = orderItemMapper.getOrderItemByUserIdOrderNo(order.getUserId(),order.getOrderNo());
		List<OrderItemVO> orderItemVOs = wrapOrderItem(items);
		vo.setOrderItemVoList(orderItemVOs);
		
		return vo;
	}

	private List<OrderItemVO> wrapOrderItem(List<OrderItem> items) {
		List<OrderItemVO> orderItemVOs = Lists.newArrayList();
		for(OrderItem item:items) {
			OrderItemVO vo = new OrderItemVO();
			vo.setOrderNo(item.getOrderNo());
			vo.setProductId(item.getProductId());
			vo.setProductName(item.getProductName());
			vo.setProductImage(item.getProductImage());
			vo.setCurrentUnitPrice(item.getCurrentUnitPrice());
			vo.setQuantity(item.getQuantity());
			vo.setTotalPrice(item.getTotalPrice());
			vo.setCreateTime(item.getCreateTime());
			orderItemVOs.add(vo);
		}
		return orderItemVOs;
	}

	@Override
	public ServerResponse detail(Integer userId, Long orderNo) {
		if(null==userId||null==orderNo) {
			return ServerResponse.createErrorResponseByCode(ResponseCode.ILLEGAL_PARAM);
		}
		
		Order order = orderMapper.getOrderByIdUserId(userId, orderNo);
		if(null==order)
			return ServerResponse.createErrorResponseByMsg("沒有改訂單！");
		return ServerResponse.createSuccessResponseByData(wrapOrder(order));
	}

	@Override
	public ServerResponse cancel(Integer userId, Long orderNo) {
		if(null==userId||null==orderNo)
			return ServerResponse.createErrorResponseByCode(ResponseCode.ILLEGAL_PARAM);
		
		Order order = orderMapper.getOrderByIdUserId(userId, orderNo);
		if(null==order)
			return ServerResponse.createErrorResponseByMsg("该用户没有此订单");
		if(order.getStatus()>Constant.PayInfoStatus.PAID.getCode())
			return ServerResponse.createErrorResponseByMsg("此訂單已經付款，無法取消！");
		
		order.setStatus(Constant.PayInfoStatus.CANCELED.getCode());
		orderMapper.updateByPrimaryKeySelective(order);
		return ServerResponse.createSuccessResonse();
	}

	@Override
	public ServerResponse sendGoods(Integer userId, Long orderNo) {
		if(null==userId||null==orderNo)
			return ServerResponse.createErrorResponseByCode(ResponseCode.ILLEGAL_PARAM);
		
		Order order = orderMapper.getOrderByIdUserId(userId, orderNo);
		if(null==order)
			return ServerResponse.createErrorResponseByMsg("该用户没有此订单");
		if(order.getStatus()>Constant.PayInfoStatus.PAID.getCode())
			return ServerResponse.createErrorResponseByMsg("此訂單已經付款，無法取消！");
		if(order.getStatus()==Constant.PayInfoStatus.PAID.getCode()) {
			order.setStatus(Constant.PayInfoStatus.SHIPPED.getCode());
			order.setSendTime(new Date());
			orderMapper.updateByPrimaryKeySelective(order);
			return ServerResponse.createSuccessResponseByData("發貨成功！");
		}
		return ServerResponse.createErrorResponseByMsg("發貨失敗！");
	}
	
}
