package com.cn.eric.backend.service;

import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.pojo.Product;
import com.cn.eric.backend.vo.ProductDetailVO;
import com.github.pagehelper.PageInfo;

public interface ProductService {

	ServerResponse saveOrUpdatePro(Product product);

	ServerResponse updateProStatus(Integer productId, Integer status);

	ServerResponse getProductDetail(Integer productId);

	ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);

	ServerResponse<PageInfo> searchProducts(String productName, Integer productId, Integer pageNum, Integer pageSize);

	ServerResponse<ProductDetailVO> getProductOnSaleDetail(int productId);

	ServerResponse<PageInfo> getProductListByCategoryKeyword(String keyword, Integer categoryId, int pageNum, int pageSize,String orderBy);

}
