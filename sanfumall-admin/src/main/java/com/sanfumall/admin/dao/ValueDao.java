package com.sanfumall.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Value;

public interface ValueDao extends BaseDao<Value, Long> {

	/**
	 * 分页获取类别列表
	 * @param startIndex
	 * @param pageSize
	 * @return List<Value>
	 * @throws Exception
	 */
	@Query(value="select * from sys_value v order by v.attribute_id asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Value> findValueListByPage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize) throws Exception;
	
	/**
	 * 模糊分页查询
	 * @param startIndex
	 * @param pageSize
	 * @param keyword
	 * @return List<Value>
	 * @throws Exception
	 */
	@Query(value="select * from sys_value v where v.value_name like CONCAT('%',:keyword,'%') or v.status_id=:keyword order by v.attribute_id asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Value> findValueListByLikePage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize, @Param("keyword")String keyword) throws Exception;

	/**
	 * 模糊查询
	 * @param keyword
	 * @return List<Value>
	 * @throws Exception
	 */
	@Query(value="select * from sys_value v where v.value_name like CONCAT('%',:keyword,'%') or v.status_id=:keyword order by v.attribute_id asc", nativeQuery = true)
	public List<Value> findValueListByLike(@Param("keyword")String keyword) throws Exception;

	/**
	 * 根据valueName与attributeId获取Value对象（方法用于检测属性值名称的唯一性）
	 * @param valueName
	 * @param attributeId
	 * @return Value
	 */
	@Query("from Value v where v.valueName=:valueName and v.attribute.attributeId=:attributeId")
	public Value findValueByNameAndAttributeId(@Param("valueName")String valueName, @Param("attributeId")Long attributeId) throws Exception;

	/**
	 * 根据attributeId获取该属性下的所有属性值
	 * @param attributeId
	 * @return List<Value>
	 * @throws Exception
	 */
	@Query("from Value v where v.attribute.attributeId=:attributeId and v.status.statusCode=:statusCode")
	public List<Value> findValueListByAttributeId(@Param("attributeId")Long attributeId, @Param("statusCode")String statusCode) throws Exception;

}