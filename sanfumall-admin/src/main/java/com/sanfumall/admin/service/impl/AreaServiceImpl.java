package com.sanfumall.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.admin.dao.AreaDao;
import com.sanfumall.admin.service.AreaService;
import com.sanfumall.common.pojo.entity.Area;
import com.sanfumall.common.pojo.vo.Page;

@Service(value="areaService")
@Transactional
public class AreaServiceImpl implements AreaService {
	
	@Resource(name="areaDao")
	private AreaDao areaDao;
	
	/**
	 * 分页获取区域列表
	 * @param page 分页信息
	 * @return 带有分页列表的Page对象
	 * @throws Exception
	 */
	public Page<Area> getAreaListByPage(Page<Area> page) throws Exception {
		// 根据page携带的分页信息获取分页查询limit的两个参数
		Integer pageSize = page.getPageSize();
		Integer startIndex = (page.getPageNum() - 1) * pageSize;
		// 首先判断是否为模糊查询
		String keyword = page.getKeyword();
		if (keyword != null && !"".equals(keyword)) {
			// 模糊查询
			// 如果是按照区域名称查询，获取对应的编码，并赋值给keyword
			// 作用：可以查询出来该地区下的子地区
			Area area = areaDao.findByAreaName(keyword);
			if (area != null) {
				keyword = String.valueOf(area.getAreaCode());
			}
			// 模糊分页查询
			List<Area> areaList = areaDao.findAreaListByLike(startIndex, pageSize, keyword);
			page.setList(areaList);
			// 获取在不分页的情况下进行模糊查询的总数量，并回填到page对象中
			Integer totalSize = areaDao.findAllAreaListByLike(keyword).size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
			
		} else {
			// 进行普通的分页查询
			List<Area> areaList = areaDao.findAreaListByPage(startIndex, pageSize);
			page.setList(areaList);
			// 获取分页信息，并回填到page对象中
			Integer totalSize = areaDao.findAll().size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		}
		return page;
	}

	/**
	 * 获取一级的区域信息
	 * @return List<Area>
	 * @throws Exception
	 */
	public List<Area> getFirstAreaList() throws Exception {
		return areaDao.findFirstAreaList();
	}

	/**
	 * 根据parentCode获取子区域集合
	 * @param parentCode
	 * @return List<Area>
	 * @throws Exception
	 */
	public List<Area> getSubAreaList(String parentCode) throws Exception {
		return areaDao.findSubAreaList(parentCode);
	}

}