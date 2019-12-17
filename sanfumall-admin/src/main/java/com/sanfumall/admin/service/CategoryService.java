package com.sanfumall.admin.service;

import java.util.List;

import com.sanfumall.common.pojo.entity.Category;
import com.sanfumall.common.pojo.vo.Page;

public interface CategoryService {

	/**
	 * 获取类别分页列表
	 * @param page 分页信息
	 * @return Page<Category>
	 * @throws Exception
	 */
	public Page<Category> getCategoryListByPage(Page<Category> page) throws Exception;
	
	/**
	 * 获取所有启用的一级类别
	 * @return List<Category>
	 * @throws Exception
	 */
	public List<Category> getFirstCategoryList() throws Exception;

	/**
	 * 根据categoryId获取该类别下的所有二级类别集合
	 * @param categoryId
	 * @return List<Category>
	 * @throws Exception
	 */
	public List<Category> getSecondCategoryListByCategoryId(Long categoryId) throws Exception;
	
	/**
	 * 根据类别名称查询类别
	 * @param categoryName
	 * @return Category
	 * @throws Exception
	 */
	public Category getCategoryByName(String categoryName) throws Exception;

	/**
	 * 根据类别ID查询类别
	 * @param categoryId
	 * @return Category
	 * @throws Exception
	 */
	public Category getCategoryById(Long categoryId) throws Exception;

	/**
	 * 根据类别信息进行保存操作
	 * @param category
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaCategory(Category category) throws Exception;

}