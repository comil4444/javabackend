package com.cn.eric.backend.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.eric.backend.common.ResponseCode;
import com.cn.eric.backend.common.ServerResponse;
import com.cn.eric.backend.dao.CategoryMapper;
import com.cn.eric.backend.pojo.Category;
import com.cn.eric.backend.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
	public ServerResponse addCategory(String categoryName, Integer parentId) {
		if(StringUtils.isBlank(categoryName)||parentId<0)
			return ServerResponse.createErrorResponseByCode(ResponseCode.ILLEGAL_PARAM);
		Category record = new Category();
		record.setParentId(parentId);
		record.setName(categoryName);
		int count = categoryMapper.insert(record);
		if(count==0)
			return ServerResponse.createErrorResponseByMsg("创建分类失败");
		return ServerResponse.createSuccessResonse();
	}

	public CategoryMapper getCategoryMapper() {
		return categoryMapper;
	}

	public void setCategoryMapper(CategoryMapper categoryMapper) {
		this.categoryMapper = categoryMapper;
	}

	@Override
	public ServerResponse updateCategory(String categoryName, Integer categoryId) {
		if(StringUtils.isBlank(categoryName))
			return ServerResponse.createErrorResponseByCode(ResponseCode.ILLEGAL_PARAM);
		Category record = new Category();
		record.setId(categoryId);
		record.setName(categoryName);
		int count = categoryMapper.updateByPrimaryKeySelective(record);
		if(count==0)
			return ServerResponse.createErrorResponseByMsg("更新分类失败");
		return ServerResponse.createSuccessResonse();
	}

	@Override
	public ServerResponse<List<Integer>> fetchChildCategory(Integer parentId) {
		if(parentId<0)
			return ServerResponse.createErrorResponseByCode(ResponseCode.ILLEGAL_PARAM);
		Set<Category> categories = categoryMapper.fetchChildCategory(parentId);
		Set<Integer> result = new HashSet<Integer>();
		//add itself
		result.add(parentId);
		for(Category c:categories) {
			result.add(c.getId());
		}
		return ServerResponse.createSuccessResponseByData(result);
	}

	@Override
	public ServerResponse<Set<Integer>> fetchDeepChildCategory(Integer parentId) {
		if(parentId<0)
			return ServerResponse.createErrorResponseByCode(ResponseCode.ILLEGAL_PARAM);
		Set<Category> categories = new HashSet<Category>();
		__fetchDeepChildCategory(categories,parentId);
		Set<Integer> result = new HashSet<Integer>();
		//add itself
		result.add(parentId);
		for(Category c:categories) {
			result.add(c.getId());
		}
		return ServerResponse.createSuccessResponseByData(result);
		
	}
	
	
	@Override
	public ServerResponse getCategoryById(Integer categoryId) {
		if(categoryId<0)
			return ServerResponse.createErrorResponseByCode(ResponseCode.ILLEGAL_PARAM);
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if(null==category)
			return ServerResponse.createErrorResponseByMsg("查询数据失败！");
		return ServerResponse.createSuccessResponseByData(category);
	}
	
	
	private void __fetchDeepChildCategory(Set<Category> set,int parentId) {
		Set<Category> categories = categoryMapper.fetchChildCategory(parentId);
		set.addAll(categories);
		if(!CollectionUtils.isEmpty(categories)) {
			for(Category c:categories) {
				__fetchDeepChildCategory(set,c.getId());
			}
		}
	}

	
	

}
