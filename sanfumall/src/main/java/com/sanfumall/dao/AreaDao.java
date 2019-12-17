package com.sanfumall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Area;

public interface AreaDao extends BaseDao<Area, Long> {

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

	/**
	 * 根据areaCode获取Area对象
	 * @param areaCode
	 * @return Area
	 * @throws Exception
	 */
	public Area findByAreaCode(String areaCode) throws Exception;

	/**
	 * 根据areaName获取Area对象
	 * @param areaName
	 * @return Area
	 * @throws Exception
	 */
	public Area findByAreaName(String areaName) throws Exception;
	
}
