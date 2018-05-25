package com.cn.eric.backend.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.eric.backend.pojo.Product;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
    
    List<Product> selectProducts();

	List<Product> selectProductsByVagueName(String productName);

	List<Product> selectProductsByKeyCategoryIds(@Param("keyword") String keyword, @Param("categoryIds") List<Integer> categoryIds);
}