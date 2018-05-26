package com.cn.eric.backend.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.eric.backend.common.Constant;
import com.cn.eric.backend.common.ResponseCode;
import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.dao.CartMapper;
import com.cn.eric.backend.dao.ProductMapper;
import com.cn.eric.backend.pojo.Cart;
import com.cn.eric.backend.pojo.Product;
import com.cn.eric.backend.service.CartService;
import com.cn.eric.backend.util.BigDecimalUtil;
import com.cn.eric.backend.util.PropertiesUtil;
import com.cn.eric.backend.vo.CartListVO;
import com.cn.eric.backend.vo.CartVO;
import com.google.common.collect.Lists;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private ProductMapper productMapper;

	@Override
	public ServerResponse<CartVO> list(Integer userId) {
		if(null==userId) {
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		}
		CartVO vo = listByUserId(userId);
		return ServerResponse.createSuccessResponseByData(vo);
	}

	private CartVO listByUserId(Integer userId) {
		CartVO cartVO = new CartVO();
		List<CartListVO> cartListVOs = Lists.newArrayList();
		BigDecimal cartTotalPrice = new BigDecimal("0");
		
		List<Cart> list = cartMapper.getCartsByUserId(userId);
		
		for(Cart cart:list) {
			CartListVO cartListVO = new CartListVO();
			Product product = productMapper.selectByPrimaryKey(cart.getProductId());
			
			cartListVO.setCartId(cart.getId());
			cartListVO.setUserId(userId);
			cartListVO.setProductId(cart.getProductId());
			cartListVO.setProductChecked(cart.getChecked());
			cartListVO.setProductMainImage(product.getMainImage());
			cartListVO.setProductName(product.getName());
			cartListVO.setProductPrice(product.getPrice().doubleValue());
			cartListVO.setProductStock(product.getStock());
			cartListVO.setSubTitle(product.getSubtitle());
			int buyLimitCount = 0;
            if(product.getStock() >= cart.getQuantity()){
                //库存充足的时候
                buyLimitCount = cart.getQuantity();
                cartListVO.setLimitQuantity(Constant.Cart.LIMIT_NUM_SUCCESS);
            }else {
            		buyLimitCount = product.getStock();
            		Cart temp = new Cart();
            		temp.setId(cart.getId());
            		temp.setQuantity(buyLimitCount);
            		cartMapper.updateByPrimaryKeySelective(temp);
            		cartListVO.setLimitQuantity(Constant.Cart.UN_LIMIT_NUM_SUCCESS);
            }
            cartListVO.setQuantity(buyLimitCount);
            cartListVO.setProductTotalPrice(BigDecimalUtil.mul(cartListVO.getProductPrice(),new Double(cartListVO.getQuantity())).doubleValue());
            
            if(cartListVO.getProductChecked()==Constant.Cart.CHECKED)
            		cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartListVO.getProductTotalPrice());
            
            cartListVOs.add(cartListVO);
		}
		
		cartVO.setCartProductVoList(cartListVOs);
		cartVO.setAllChecked(cartMapper.isAllChecked(userId)==0);
		cartVO.setHostImage(PropertiesUtil.getProperty("ftp.server.http.prefix"));
		cartVO.setCartTotalPrice(cartTotalPrice.doubleValue());
		
		return cartVO;
	}


	@Override
	public ServerResponse<CartVO> add(Integer userId, Integer productId, int count) {
		if(null==userId||null==productId)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		Cart cart = cartMapper.getCartByUserIdProductId(userId,productId);
		
		if(null==cart) {
			Cart newCart = new Cart();
			newCart.setProductId(productId);
			newCart.setUserId(userId);
			newCart.setQuantity(count);
			newCart.setChecked(Constant.Cart.CHECKED);
			cartMapper.insert(newCart);
		}else {
			cart.setQuantity(cart.getQuantity()+count);
			cartMapper.updateByPrimaryKeySelective(cart);
		}
		return this.list(userId);
	}
	
	@Override
	public ServerResponse<CartVO> update(Integer userId, Integer productId, int count) {
		if(null==userId||null==productId)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		Cart cart = cartMapper.getCartByUserIdProductId(userId,productId);
		
		if(null!=cart) {
			cart.setQuantity(cart.getQuantity()+count);
			cartMapper.updateByPrimaryKeySelective(cart);
		}
		return this.list(userId);
	}
	
	@Override
	public ServerResponse<CartVO> deleteProduct(Integer userId, String productIds) {
		if(null==userId||null==productIds)
			return ServerResponse.createErrorResponseByCode(ResponseCode.NEED_LOGIN);
		cartMapper.deleteByUserIdProId(userId,productIds);
		return this.list(userId);
	}

	@Override
	public Integer getProductCountInCart(Integer userId) {
		if(null==userId)
			return 0;
		return cartMapper.getCountInCart(userId);
	}

	@Override
	public ServerResponse<CartVO> selectProduct(Integer userId, int productId) {
		cartMapper.updateChecked(userId, productId, Constant.Cart.CHECKED);
		return this.list(userId);
	}

	@Override
	public ServerResponse<CartVO> selectAll(Integer userId) {
		cartMapper.updateChecked(userId, null, Constant.Cart.CHECKED);
		return this.list(userId);
	}

	@Override
	public ServerResponse<CartVO> unSelectProduct(Integer userId, int productId) {
		cartMapper.updateChecked(userId, productId, Constant.Cart.UN_CHECKED);
		return this.list(userId);
	}

	@Override
	public ServerResponse<CartVO> unSelectAll(Integer userId) {
		cartMapper.updateChecked(userId, null, Constant.Cart.UN_CHECKED);
		return this.list(userId);
	}
	
	public CartMapper getCartMapper() {
		return cartMapper;
	}
	
	public void setCartMapper(CartMapper cartMapper) {
		this.cartMapper = cartMapper;
	}
	
	public ProductMapper getProductMapper() {
		return productMapper;
	}
	
	public void setProductMapper(ProductMapper productMapper) {
		this.productMapper = productMapper;
	}
	
}
