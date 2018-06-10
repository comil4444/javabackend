package com.cn.eric.backend.service;

import java.util.List;
import java.util.Set;

import com.cn.eric.backend.common.ServerResponse;

public interface CategoryService {

	ServerResponse addCategory(String categoryName, Integer parentId);

	ServerResponse updateCategory(String categoryName, Integer categoryId);

	ServerResponse<List<Integer>> fetchChildCategory(Integer parentId);

	ServerResponse<Set<Integer>> fetchDeepChildCategory(Integer parentId);

	ServerResponse getCategoryById(Integer categoryId);

}
