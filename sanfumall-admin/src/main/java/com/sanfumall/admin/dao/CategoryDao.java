package com.sanfumall.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Category;

public interface CategoryDao extends BaseDao<Category, Long> {

	/**
	 * 根据类别名称查询Category对象
	 * @param categoryName
	 * @return Category
	 * @throws Exception
	 */
	public Category findByCategoryName(String categoryName) throws Exception;
	
	/**
	 * 分页获取类别列表
	 * @param startIndex
	 * @param pageSize
	 * @return List<Category>
	 * @throws Exception
	 */
	@Query(value="select * from sys_category c order by c.sort_order asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Category> findCategoryListByPage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize) throws Exception;

	/**
	 * 模糊分页查询
	 * @param startIndex
	 * @param pageSize
	 * @param keyword
	 * @return List<Category>
	 * @throws Exception
	 */
	@Query(value="select * from sys_category c where c.category_name like CONCAT('%',:keyword,'%') or c.status_id=:keyword order by c.sort_order asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Category> findCategoryListByLikePage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize, @Param("keyword")String keyword) throws Exception;

	/**
	 * 模糊查询
	 * @param keyword
	 * @return List<Category>
	 * @throws Exception
	 */
	@Query(value="select * from sys_category c where c.category_name like CONCAT('%',:keyword,'%') or c.status_id=:keyword order by c.sort_order asc", nativeQuery = true)
	public List<Category> findCategoryListByLike(@Param("keyword")String keyword) throws Exception;

	/**
	 * 获取启用状态的一级类别列表
	 * @return List<Category>
	 * @throws Exception
	 */
	@Query("from Category c where c.parent is null and c.status.statusCode=:statusCode order by c.sortOrder asc")
	public List<Category> findFirstCategoryList(@Param("statusCode")String statusCode) throws Exception;

	/**
	 * 根据一级类别Id获取二级类别集合
	 * @param categoryId
	 * @param statusCode ENABLE 启用状态
	 * @return List<Category>
	 * @throws Exception
	 */
	@Query("from Category c where c.parent.categoryId=:categoryId and c.status.statusCode=:statusCode order by c.sortOrder asc")
	public List<Category> findSecondCategoryListById(@Param("categoryId")Long categoryId, @Param("statusCode")String statusCode) throws Exception;

}