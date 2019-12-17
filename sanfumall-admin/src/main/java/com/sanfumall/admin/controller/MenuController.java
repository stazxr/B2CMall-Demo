package com.sanfumall.admin.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sanfumall.admin.service.MenuService;
import com.sanfumall.admin.service.RoleService;
import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Role;
import com.sanfumall.common.pojo.vo.Node;

@Controller("menuController")
@RequestMapping("/menu")
public class MenuController extends BaseController {
	@Resource(name="menuService")
	private MenuService menuService;
	@Resource(name="roleService")
	private RoleService roleService;
	
	/**
	 * 根据roleId获取对应角色，然后获得对应的Node集合
	 * 在页面前端生成角色对应的权限树
	 * @param roleId
	 * @return List<Node>
	 */
	@RequestMapping(value="/authc/{roleId}", method=RequestMethod.GET)
	@ResponseBody
	public List<Node> getNodeListForAuthc(@PathVariable("roleId")Long roleId) throws Exception {
		Role role = roleService.getRoleByRoleId(roleId);
		return menuService.getNodeListForAuthc(role);
	}
	
}
