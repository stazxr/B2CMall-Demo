package com.sanfumall.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.admin.dao.CategoryDao;
import com.sanfumall.admin.dao.StatusDao;
import com.sanfumall.admin.service.CategoryService;
import com.sanfumall.common.pojo.entity.Category;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.vo.Page;
import com.sanfumall.common.util.ConstantUtil;

@Service("categoryService")
@Transactional
public class CategoryServiceImpl implements CategoryService {
	
	@Resource(name="categoryDao")
	private CategoryDao categoryDao;
	@Resource(name="statusDao")
	private StatusDao statusDao;
	
	/**
	 * 获取类别分页列表
	 * @param page 分页信息
	 * @return Page<Category>
	 * @throws Exception
	 */
	public Page<Category> getCategoryListByPage(Page<Category> page) throws Exception {
		// 根据page携带的分页信息获取分页查询limit的两个参数
		Integer pageSize = page.getPageSize();
		Integer startIndex = (page.getPageNum() - 1) * pageSize;
		// 获取分页列表，首先判断是否为模糊查询
		String keyword = page.getKeyword();
		if (keyword != null && !"".equals(keyword)) {
			// 模糊查询
			// 判断是否是按状态名称查询
			Status status = statusDao.findByStatusName(keyword);
			if (status != null) {
				keyword = Long.toString(status.getStatusId());
			}
			// 模糊分页查询
			List<Category> categoryList = categoryDao.findCategoryListByLikePage(startIndex, pageSize, keyword.toUpperCase());
			page.setList(categoryList);
			// 获取在不分页的情况下进行模糊查询的总数量，并回填到page对象中
			Integer totalSize = categoryDao.findCategoryListByLike(keyword).size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		} else {
			// 普通分页查询
			List<Category> categoryList = categoryDao.findCategoryListByPage(startIndex, pageSize);
			page.setList(categoryList);
			// 获取其他分页信息，并回填到page对象中
			Integer totalSize = categoryDao.findAll().size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		}
		return page;
	}

	/**
	 * 获取所有的启用状态的一级类别
	 */
	public List<Category> getFirstCategoryList() throws Exception {
		return categoryDao.findFirstCategoryList(ConstantUtil.STATUS_ENABLE);
	}
	
	/**
	 * 根据一级类别Id获取启用的二级类别集合
	 * @param categoryId
	 * @return List<Category>
	 * @throws Exception
	 */
	public List<Category> getSecondCategoryListByCategoryId(Long categoryId) throws Exception {
		return categoryDao.findSecondCategoryListById(categoryId, ConstantUtil.STATUS_ENABLE);
	}

	/**
	 * 根据类别名称查询类别
	 * @param categoryName
	 * @return Category
	 * @throws Exception
	 */
	public Category getCategoryByName(String categoryName) throws Exception {
		return categoryDao.findByCategoryName(categoryName);
	}

	/**
	 * 根据类别ID查询类别
	 * @param categoryId
	 * @return Category
	 * @throws Exception
	 */
	public Category getCategoryById(Long categoryId) throws Exception {
		return categoryDao.getOne(categoryId);
	}

	/**
	 * 根据类别信息进行保存或修改操作
	 * @param category，如果包含ID，则为修改，否则，为保存
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaCategory(Category category) throws Exception {
		try {
			categoryDao.save(category);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}