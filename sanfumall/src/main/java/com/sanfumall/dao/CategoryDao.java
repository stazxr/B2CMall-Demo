package com.sanfumall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Category;

public interface CategoryDao extends BaseDao<Category, Long> {

	/**
	 * 获取一级类别集合
	 * @param statusCode
	 * @return List<Category> 
	 * @throws Exception
	 */
	@Query("from Category c where c.parent is null and c.status.statusCode=:statusCode")
	public List<Category> findFirstCategoryList(@Param("statusCode")String statusCode) throws Exception;

	/**
	 * 获取一级类别对应的二级类别集合
	 * @param categoryId
	 * @param statusCode
	 * @return List<Category>
	 * @throws Exception
	 */
	@Query("from Category c where c.parent.categoryId=:categoryId and c.status.statusCode=:statusCode")
	public List<Category> findSecondCategoryList(@Param("categoryId")Long categoryId, @Param("statusCode")String statusCode) throws Exception;
	
}