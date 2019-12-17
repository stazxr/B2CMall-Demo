package com.sanfumall.admin.service;

import java.util.List;

import com.sanfumall.common.pojo.entity.Area;
import com.sanfumall.common.pojo.vo.Page;

public interface AreaService {

	/**
	 * 分页获取区域列表
	 * @param page 分页信息
	 * @return Page<Area>
	 * @throws Exception
	 */
	public Page<Area> getAreaListByPage(Page<Area> page) throws Exception;

	/**
	 * 获取一级的区域信息
	 * @return List<Area>
	 * @throws Exception
	 */
	public List<Area> getFirstAreaList() throws Exception;

	/**
	 * 根据areaCode获取子区域集合
	 * @param parentCode
	 * @return List<Area>
	 * @throws Exception
	 */
	public List<Area> getSubAreaList(String parentCode) throws Exception;

}