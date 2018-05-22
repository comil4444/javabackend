package com.cn.eric.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.eric.backend.common.ResponseCode;
import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.dao.CategoryMapper;
import com.cn.eric.backend.dao.ProductMapper;
import com.cn.eric.backend.pojo.Category;
import com.cn.eric.backend.pojo.Product;
import com.cn.eric.backend.service.ProductService;
import com.cn.eric.backend.util.DateUtil;
import com.cn.eric.backend.util.PropertiesUtil;
import com.cn.eric.backend.vo.ProductDetailVO;
import com.cn.eric.backend.vo.ProductListVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
	public ServerResponse saveOrUpdatePro(Product product) {
		if(null==product) {
			if(StringUtils.isNotBlank(product.getSubImages())){
				
				//Front End need to combine using comma
				String[] imgs = product.getSubImages().split(",");
				if(imgs.length>0)
					product.setMainImage(imgs[0]);
				
				//insert
				if(null == product.getId()) {
					int count = productMapper.insert(product);
					if(count==0)
						return ServerResponse.createErrorResponseByMsg("插入新商品失败！");
					return ServerResponse.createSuccessResponseByMsg("插入新产品成功！");
				}
				//update 
				else {
					int count = productMapper.updateByPrimaryKeySelective(product);
					if(count==0)
						return ServerResponse.createErrorResponseByMsg("更新商品失败！");
					return ServerResponse.createSuccessResponseByMsg("更新产品成功！");
				}
			}
		}
		return ServerResponse.createErrorResponseByMsg("更新或添加产品失败！");
	}

	@Override
	public ServerResponse updateProStatus(Integer productId, Integer status) {
		if(null==productId||null==status)
			return ServerResponse.createErrorResponseByCode(ResponseCode.ILLEGAL_PARAM);
		Product pro = new Product();
		pro.setId(productId);
		pro.setStatus(status);
		int count = productMapper.updateByPrimaryKeySelective(pro);
		if(count==0)
			return ServerResponse.createErrorResponseByMsg("更新产品失败！");
		return ServerResponse.createSuccessResponseByMsg("更新产品成功");
	}

	@Override
	public ServerResponse<ProductDetailVO> getProductDetail(Integer productId) {
		Product prod = productMapper.selectByPrimaryKey(productId);
		if(prod==null) {
			return ServerResponse.createErrorResponseByMsg("产品不存在或已经下架！");
		}
		ProductDetailVO vo = productWrapper(prod);
		return ServerResponse.createSuccessResponseByData(vo);
	}
	
	
	private ProductDetailVO productWrapper(Product prod) {
		ProductDetailVO vo = new ProductDetailVO();
		vo.setCategoryId(prod.getCategoryId());
		vo.setCreateTime(DateUtil.date2Str(prod.getCreateTime()));
		vo.setDetail(prod.getDetail());
		vo.setId(prod.getId());
		vo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://image.eric.com/"));
		vo.setMainImage(prod.getMainImage());
		vo.setName(prod.getName());
		Category cate = categoryMapper.selectByPrimaryKey(prod.getCategoryId());
		if(cate.getParentId()!=null)
			vo.setParentCategoryId(cate.getParentId());
		else
			vo.setParentCategoryId(0);
		vo.setPrice(prod.getPrice());
		vo.setStatus(prod.getStatus());
		vo.setStock(prod.getStock());
		vo.setSubImages(prod.getSubImages());
		vo.setSubtitle(prod.getSubtitle());
		vo.setUpdateTime(DateUtil.date2Str(prod.getUpdateTime()));
		return vo;
	}

	public ProductMapper getProductMapper() {
		return productMapper;
	}

	public void setProductMapper(ProductMapper productMapper) {
		this.productMapper = productMapper;
	}

	@Override
	public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		List<Product> products = productMapper.selectProducts();
		if(CollectionUtils.isEmpty(products))
			return ServerResponse.createErrorResponseByMsg("没有产品！");
		
		List<ProductListVO> target = new ArrayList<ProductListVO>();
		for(Product pro:products) {
			ProductListVO vo = new ProductListVO();
			vo.setCategoryId(pro.getCategoryId());
			vo.setId(pro.getId());
			vo.setMainImage(pro.getMainImage());
			vo.setName(pro.getName());
			vo.setPrice(pro.getPrice());
			vo.setStatus(pro.getStatus());
			vo.setSubtitle(pro.getSubtitle());
			target.add(vo);
		}
		
		PageInfo pageInfo = new PageInfo(products);
		pageInfo.setList(target);
		return ServerResponse.createSuccessResponseByData(pageInfo);
	}

	@Override
	public ServerResponse<PageInfo> searchProducts(String productName, Integer productId, Integer pageNum,
			Integer pageSize) {
		if(null!=productId) {
			PageHelper.startPage(pageNum,pageSize);
			Product pro = productMapper.selectByPrimaryKey(productId);
			List<Product> list = new ArrayList<Product>();
			list.add(pro);
			PageInfo pageInfo = new PageInfo(list);
			
			List<ProductDetailVO> target = new ArrayList<ProductDetailVO>();
			target.add(productWrapper(pro));
			pageInfo.setList(target);
			return ServerResponse.createSuccessResponseByData(pageInfo);
		}else if(null!=productName) {
			PageHelper.startPage(pageNum,pageSize);
			List<Product> pros = productMapper.selectProductsByVagueName(productName);
		}
		return null;
	}


}
