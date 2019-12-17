package com.sanfumall.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanfumall.admin.service.AreaService;
import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Area;
import com.sanfumall.common.pojo.vo.Page;

@Controller("areaController")
@RequestMapping("/area")
public class AreaController extends BaseController {

	@Resource(name="areaService")
	private AreaService areaService;
	
	/**
	 * 分页展示区域列表
	 * @param pageNum 页码
	 * @param pageSize 每页显示的条数
	 * @param keyword 模糊查询的参数，如果为null,则进行普通分页查询，否则进行模糊分页查询
	 * @return area_index.html
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView getRoleIndex(Integer pageNum, Integer pageSize, String keyword) throws Exception {
		// 首先，创建分页对象
		Page<Area> page = new Page<>(pageNum, pageSize, keyword);
		page = areaService.getAreaListByPage(page);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("page", page);
		return new ModelAndView("area/area_index", resultMap);
	}
	
	/**
	 * 加载三级联动效果展示页面
	 * @return area_show.html
	 * @throws Exception
	 */
	@RequestMapping(value="/show", method=RequestMethod.GET)
	public ModelAndView getShowPage() throws Exception {
		List<Area> fistAreaList = areaService.getFirstAreaList();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("fistAreaList", fistAreaList);
		return new ModelAndView("area/area_show", resultMap);
	}
	
	/**
	 * 获取下级区域列表
	 * @return List<Area>
	 * @throws Exception
	 */
	@RequestMapping(value="/getSubAreaList", method=RequestMethod.GET)
	@ResponseBody
	public List<Area> getSubAreaList(String parentCode) throws Exception {
		return areaService.getSubAreaList(parentCode);
	}
	
}