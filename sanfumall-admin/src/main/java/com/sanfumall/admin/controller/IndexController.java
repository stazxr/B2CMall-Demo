package com.sanfumall.admin.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sanfumall.admin.service.MenuService;
import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Menu;
import com.sanfumall.common.pojo.entity.User;
import com.sanfumall.common.util.ConstantUtil;

@Controller("indexController")
public class IndexController extends BaseController {
	
	@Resource(name="menuService")
	private MenuService menuService; 
	
	/**
	 * 管理员登录验证成功加载后台管理首页
	 * @return admin.html
	 * @throws Exception
	 */
	@RequestMapping(value="/")
	public ModelAndView getAdminIndex() throws Exception{
		// 从Session获得当前登陆的对象
		User user = (User) session.getAttribute("user");
		// 根据登录对象的角色查询所对应的菜单集合
		List<Menu> menuList = menuService.getMenuListByRole(user.getRole());
		// 如果登录用户所对应的角色被禁用，则不显示任何菜单
		if (ConstantUtil.STATUS_DISABLE.equals(user.getRole().getStatus().getStatusCode())) {
			menuList = null;
		}
		return new ModelAndView("admin", "menuList", menuList);
	}
	
}
