package com.sanfumall.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.common.pojo.entity.Area;
import com.sanfumall.dao.AreaDao;
import com.sanfumall.service.AreaService;

@Service("areaService")
@Transactional
public class AreaServiceImpl implements AreaService {

	@Resource(name="areaDao")
	private AreaDao areaDao;
	
	/**
	 * 获取一级的区域信息
	 * @return List<Area>
	 * @throws Exception
	 */
	public List<Area> getFirstAreaList() throws Exception {
		return areaDao.findFirstAreaList();
	}
	
	/**
	 * 根据parentCode获取其下的子区域列表
	 * @param parentCode
	 * @return List<Area>
	 * @throws Exception
	 */
	public List<Area> getSubAreaList(String parentCode) throws Exception {
		return areaDao.findSubAreaList(parentCode);
	}

	/**
	 * 根据areaCode获取Area对象
	 * @param areaCode
	 * @return Area
	 * @throws Exception
	 */
	public Area getAreaByAreaCode(String areaCode) throws Exception {
		return areaDao.findByAreaCode(areaCode);
	}

	/**
	 * 根据areaName获取Area对象
	 * @param areaName
	 * @return Area
	 * @throws Exception
	 */
	public Area getAreaByAreaName(String areaName) throws Exception {
		return areaDao.findByAreaName(areaName);
	}

}
