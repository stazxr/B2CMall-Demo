package com.sanfumall.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sanfumall.admin.service.StatusService;
import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.vo.Page;

@Controller("statusController")
@RequestMapping("/status")
public class StatusController extends BaseController {
	
	@Resource(name="statusService")
	private StatusService statusService;
	
	/**
	 * 加载状态列表页面
	 * @param pageNum 页码
	 * @param pageSize 每页显示的数量
	 * @param keyword 模糊查询阐述，如果为null,则进行普通的查询，否则进行模糊查询
	 * @return status_index.html
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView getUserIndex(Integer pageNum, Integer pageSize, String keyword) throws Exception {
		// 创建分页对象
		Page<Status> page = new Page<>(pageNum, pageSize, keyword);
		page = statusService.getStatusListByPage(page);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("page", page);
		return new ModelAndView("status/status_index", resultMap);
	}
}
