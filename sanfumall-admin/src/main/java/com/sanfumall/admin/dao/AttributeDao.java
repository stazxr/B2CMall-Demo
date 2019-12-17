package com.sanfumall.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Attribute;

public interface AttributeDao extends BaseDao<Attribute, Long> {

	/**
	 * 根据属性名称获取属性
	 * @param attributeName
	 * @return Attribute 
	 * @throws Exception
	 */
	public Attribute findByAttributeName(String attributeName) throws Exception;

	
	/**
	 * 模糊分页查询
	 * @param startIndex
	 * @param pageSize
	 * @param keyword
	 * @return List<Attribute>
	 * @throws Exception
	 */
	@Query(value="select * from sys_attribute a where a.attribute_name like CONCAT('%',:keyword,'%') or a.category_id=:keyword or a.status_id=:keyword order by a.category_id asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Attribute> findAttributeListByLikePage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize, @Param("keyword")String keyword) throws Exception;
	
	/**
	 * 模糊查询
	 * @param keyword
	 * @return List<Attribute>
	 * @throws Exception
	 */
	@Query(value="select * from sys_attribute a where a.attribute_name like CONCAT('%',:keyword,'%') or a.category_id=:keyword or a.status_id=:keyword order by a.category_id asc", nativeQuery = true)
	public List<Attribute> findAttributeListByLike(@Param("keyword")String keyword) throws Exception;

	/**
	 * 分页获取属性列表
	 * @param startIndex
	 * @param pageSize
	 * @return List<Attribute>
	 * @throws Exception
	 */
	@Query(value="select * from sys_attribute a order by a.category_id asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Attribute> findAttributeListByPage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize);

	/**
	 * 根据attributeName与categoryId获取属性（方法用于检测属性名称的唯一性）
	 * @param attributeName
	 * @param categoryId
	 * @return Attribute 
	 * @throws Exception
	 */
	@Query("from Attribute a where a.attributeName=:attributeName and a.category.categoryId=:categoryId")
	public Attribute findByAttributeNameAndCategoryId(@Param("attributeName")String attributeName, @Param("categoryId")Long categoryId) throws Exception;

	/**
	 * 根据categoryId获取该类别下的所有属性
	 * @param categoryId
	 * @return List<Attribute>
	 */
	@Query("from Attribute a where a.category.categoryId=:categoryId and a.status.statusCode=:statusCode")
	public List<Attribute> findAttributeListByCategory(@Param("categoryId")Long categoryId, @Param("statusCode")String statusCode) throws Exception;

}