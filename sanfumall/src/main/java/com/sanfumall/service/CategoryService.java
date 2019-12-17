package com.sanfumall.service;

import java.util.List;

import com.sanfumall.common.pojo.entity.Category;

public interface CategoryService {

	/**
	 * 获取一级类别集合
	 * @return List<Category>
	 * @throws Exception
	 */
	public List<Category> findFirstCategoryList() throws Exception;

	/**
	 * 获取所有的类别信息
	 * @return List<Category>
	 * @throws Exception
	 */
	public List<Category> findAllCategoryList() throws Exception;

	/**
	 * 根据类别Id获取类别信息
	 * @param categoryId
	 * @return Category对象
	 * @throws Exception
	 */
	public Category getCategoryById(Long categoryId) throws Exception;

	/**
	 * 根据一级类别Id获取其对应的二级类别集合
	 * @param categoryId
	 * @return List<Category>
	 * @throws Exception
	 */
	public List<Category> getCategoryListByParentId(Long categoryId) throws Exception;

}
