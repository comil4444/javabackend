package com.cn.eric.backend.service;

import com.cn.eric.backend.common.ServerResponse;

public interface CategoryService {

	ServerResponse addCategory(String categoryName, Integer parentId);

	ServerResponse updateCategory(String categoryName, Integer categoryId);

	ServerResponse fetchChildCategory(Integer parentId);

	ServerResponse fetchDeepChildCategory(Integer parentId);

}
