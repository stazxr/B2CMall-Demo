package com.sanfumall.service;

import java.util.List;

import com.sanfumall.common.pojo.entity.Area;

public interface AreaService {

	/**
	 * 获取一级的区域信息
	 * @return List<Area>
	 * @throws Exception
	 */
	public List<Area> getFirstAreaList() throws Exception;
	
	/**
	 * 根据parentCode获取其下的子区域列表
	 * @param parentCode
	 * @return List<Area>
	 * @throws Exception
	 */
	public List<Area> getSubAreaList(String parentCode) throws Exception;

	/**
	 * 根据areaCode获取Area对象
	 * @param areaCode
	 * @return Area
	 * @throws Exception
	 */
	public Area getAreaByAreaCode(String areaCode) throws Exception;

	/**
	 * 根据areaName获取Area对象
	 * @param areaName
	 * @return Area
	 * @throws Exception
	 */
	public Area getAreaByAreaName(String areaName) throws Exception;

}
