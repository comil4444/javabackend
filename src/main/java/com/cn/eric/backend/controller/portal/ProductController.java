package com.cn.eric.backend.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.service.ProductService;
import com.cn.eric.backend.vo.ProductDetailVO;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/product/")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping("detail.do")
	@ResponseBody
	public ServerResponse<ProductDetailVO> getDetail(int productId){
		return productService.getProductOnSaleDetail(productId);
	}

	@RequestMapping("detail.do")
	@ResponseBody
	public ServerResponse<PageInfo> list(@RequestParam(value="keyword",required=false)String keyword,
			@RequestParam(value="categoryId",required=false)int categoryId,
			@RequestParam(value="pageNum",defaultValue="1")int pageNum,
			@RequestParam(value="pageSize",defaultValue="10")int pageSize,
			@RequestParam(value="orderBy",defaultValue="")String orderBy){
		return productService.getProductListByCategoryKeyword(keyword,categoryId,pageNum,pageSize,orderBy);
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	
}
