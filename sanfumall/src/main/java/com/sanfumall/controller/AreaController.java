package com.sanfumall.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Area;
import com.sanfumall.service.AreaService;

@Controller(value="areaController")
@RequestMapping("/area")
public class AreaController extends BaseController {
	
	@Resource(name="areaService")
	private AreaService areaService;
	
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