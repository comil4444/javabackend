package com.cn.eric.backend.dao;

import java.util.Set;

import com.cn.eric.backend.pojo.Category;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

	Set<Category> fetchChildCategory(Integer parentId);
}