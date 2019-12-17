package com.sanfumall.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Area;

public interface AreaDao extends BaseDao<Area, Long> {
	
	/**
	 * 根据区域名称获取Area对象
	 * @param areaName
	 * @return Area
	 */
	public Area findByAreaName(String areaName);
	
	/**
	 * 分页获取区域列表
	 * @param startIndex 开始的索引
	 * @param pageSize 每页显示的条数
	 * @return List<Area>
	 * @throws Exception
	 */
	@Query(value="select * from sys_area a order by a.area_id asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Area> findAreaListByPage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize) throws Exception;
	
	/**
	 * 模糊分页查询
	 * @param startIndex
	 * @param pageSize
	 * @param keyword
	 * @return
	 */
	@Query(value="select * from sys_area a where a.area_name like CONCAT('%',:keyword,'%') or a.area_code =:keyword or a.parent_code=:keyword order by a.area_id asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Area> findAreaListByLike(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize, @Param("keyword")String keyword) throws Exception;
	
	/**
	 * 不分页进行模糊查询
	 * @param keyword
	 * @return
	 */
	@Query(value="select * from sys_area a where a.area_name like CONCAT('%',:keyword,'%') or a.area_code=:keyword or a.parent_code=:keyword order by a.area_id asc", nativeQuery = true)
	public List<Area> findAllAreaListByLike(@Param("keyword")String keyword) throws Exception;

	/**
	 * 获取一级的区域信息
	 * @return List<Area>
	 * @throws Exception
	 */
	@Query("from Area a where a.parentCode='0' order by a.areaId asc")
	public List<Area> findFirstAreaList() throws Exception;

	/**
	 * 根据areaCode获取子区域集合
	 * @param parentCode
	 * @return List<Area>
	 * @throws Exception
	 */
	@Query("from Area a where a.parentCode=:parentCode order by a.areaId asc")
	public List<Area> findSubAreaList(@Param("parentCode")String parentCode) throws Exception;
	
}