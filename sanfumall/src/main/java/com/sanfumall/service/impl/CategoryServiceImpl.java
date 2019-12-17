package com.sanfumall.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.common.pojo.entity.Category;
import com.sanfumall.common.util.ConstantUtil;
import com.sanfumall.dao.CategoryDao;
import com.sanfumall.service.CategoryService;

@Service(value="categoryService")
@Transactional
public class CategoryServiceImpl implements CategoryService {

	@Resource(name="categoryDao")
	private CategoryDao categoryDao;
	
	/**
	 * 获取一级类别集合
	 * @return List<Category>
	 * @throws Exception
	 */
	public List<Category> findFirstCategoryList() throws Exception {
		List<Category> categoryList = categoryDao.findFirstCategoryList(ConstantUtil.STATUS_ENABLE);
		if (categoryList != null && categoryList.size() > 0) {
			for (Category category : categoryList) {
				List<Category> secondCategoryList = categoryDao.findSecondCategoryList(category.getCategoryId(), ConstantUtil.STATUS_ENABLE);
				category.setCategoryList(secondCategoryList);
			}
		}
		return categoryList;
	}

	/**
	 * 获取所有的类别信息
	 * @return List<Category>
	 * @throws Exception
	 */
	public List<Category> findAllCategoryList() throws Exception {
		return categoryDao.findAll();
	}

	/**
	 * 根据类别Id获取类别信息
	 * @param categoryId
	 * @return Category对象
	 * @throws Exception
	 */
	public Category getCategoryById(Long categoryId) throws Exception {
		return categoryDao.getOne(categoryId);
	}

	/**
	 * 根据一级类别Id获取其对应的二级类别集合
	 * @param categoryId
	 * @return List<Category>
	 * @throws Exception
	 */
	public List<Category> getCategoryListByParentId(Long categoryId) throws Exception {
		List<Category> categoryList = categoryDao.findSecondCategoryList(categoryId, ConstantUtil.STATUS_ENABLE);
		return categoryList;
	}

}