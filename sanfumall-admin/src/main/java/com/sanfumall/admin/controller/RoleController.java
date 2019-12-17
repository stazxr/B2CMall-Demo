package com.sanfumall.admin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanfumall.admin.service.MenuService;
import com.sanfumall.admin.service.RoleService;
import com.sanfumall.admin.service.StatusService;
import com.sanfumall.admin.service.UserService;
import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Menu;
import com.sanfumall.common.pojo.entity.Role;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.entity.User;
import com.sanfumall.common.pojo.vo.Page;
import com.sanfumall.common.util.ConstantUtil;

@Controller("roleController")
@RequestMapping("/role")
public class RoleController extends BaseController {
	
	@Resource(name="roleService")
	private RoleService roleService;
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="statusService")
	private StatusService statusService;
	@Resource(name="menuService")
	private MenuService menuService;
	
	/**
	 * 分页展示角色列表
	 * @param pageNum 页码
	 * @param pageSize 每页显示的条数
	 * @param keyword 模糊查询的参数，如果为null,则进行普通分页查询，否则进行模糊分页查询
	 * @return role_index.html
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView getRoleIndex(Integer pageNum, Integer pageSize, String keyword) throws Exception {
		// 首先，创建分页对象
		Page<Role> page = new Page<>(pageNum, pageSize, keyword);
		page = roleService.getRoleListByPage(page);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("page", page);
		return new ModelAndView("role/role_index", resultMap);
	}
	
	/**
	 * 加载角色添加页面
	 * @return role_add.html
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String getAddPage() throws Exception {
		return "role/role_add";
	}
	
	/**
	 * 添加角色
	 * @param role
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public Boolean addRole(Role role) throws Exception {
		// 校验Role对象是否合法（前端校验有可能失效）
		if (checkRole(role)) {
			// 将角色编码转换为大写,并保存带role对象中
			role.setRoleCode(role.getRoleCode().trim().toUpperCase());
			// 校验成功，获取当前登录用户，记录创建者
			User user = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
			role.setCreateUser(user);
			role.setCreateTime(new Date());
			// 获取启用状态对象，并保存在角色中
			Status status = statusService.getStatusByStatusCode(ConstantUtil.STATUS_ENABLE);
			role.setStatus(status);
			// 调用业务层保存角色
			return roleService.savaRole(role);
		}
		return false;
	}

	/**
	 * 加载详情页面
	 * @param roleId
	 * @return role_detail.html
	 * @throws Exception
	 */
	@RequestMapping(value="/detail/{roleId}", method=RequestMethod.GET)
	public ModelAndView getDetailPage(@PathVariable("roleId")Long roleId) throws Exception {
		Role role = roleService.getRoleByRoleId(roleId);
		return new ModelAndView("role/role_detail", "role", role);
	}
	
	/**
	 * 加载修改页面
	 * @param roleId 修改角色的ID
	 * @return role_update.html
	 * @throws Exception
	 */
	@RequestMapping(value="/update/{roleId}", method=RequestMethod.GET)
	public ModelAndView getUpdatePage(@PathVariable("roleId")Long roleId) throws Exception {
		Role role = roleService.getRoleByRoleId(roleId);
		return new ModelAndView("role/role_update", "role", role);
	}
	
	/**
	 * 修改角色
	 * @param role
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean updateRole(Role role) throws Exception {
		// 校验Role对象是否合法（前端校验有可能失效）
		if (checkRole(role)) {
			// 根据roleId获得数据库中原有的角色信息，并将未修改的信息进行回填
			Role origRole = roleService.getRoleByRoleId(role.getRoleId());
			// 将排序字段,状态,创建人,创建时间回填
			role.setStatus(origRole.getStatus());
			role.setCreateUser(origRole.getCreateUser());
			role.setCreateTime(origRole.getCreateTime());
			// 将新的角色编码转换为大写,并保存带role对象中
			role.setRoleCode(role.getRoleCode().trim().toUpperCase());
			// 记录修改者，修改时间
			User user = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
			role.setUpdateUser(user);
			role.setUpdateTime(new Date());
			// 调用业务层保存角色
			return roleService.savaRole(role);
			}
		return false;
	}
	
	/**
	 * 修改角色状态
	 * @param roleId 修改角色的ID
	 * @param statusCode 欲修改的状态编码
	 * @return boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/changeStatus/{roleId}/{statusCode}", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean changeStatus(@PathVariable("roleId")Long roleId, @PathVariable("statusCode")String statusCode) throws Exception {
		if (statusCode != null && !"".equals(statusCode) && roleId != null) {
			// 根据角色ID获取角色对象
			Role role = roleService.getRoleByRoleId(roleId);
			// 根据状态编码获取状态对象,并保存带角色对象中
			Status status = statusService.getStatusByStatusCode(statusCode);
			role.setStatus(status);
			// 调用业务层保存角色
			return roleService.savaRole(role);
		}
		return false;
	}
	
	/**
	 * 加载授权页面
	 * @param roleId
	 * @return role_authc.html
	 * @throws Exception
	 */
	@RequestMapping(value="/authc/{roleId}", method=RequestMethod.GET)
	public ModelAndView getAuthcPage(@PathVariable("roleId")Long roleId) throws Exception {
		// 加载页面的时候是不需要使用roleId的，只有在获取角色对应的权限列表和保存权限的时候才需要，所以不需要获取role角色
		return new ModelAndView("role/role_authc", "roleId", roleId);
	}
	
	/**
	 * 根据角色ID与ids进行授权操作
	 * @param roleId 角色ID
	 * @param ids 所有菜单ID组成的字符串
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/authc/{roleId}/{ids}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean roleAuthc(@PathVariable("roleId")Long roleId, @PathVariable("ids")String ids) throws Exception {
		// 校验参数合法性	
		if (roleId != null && roleId > 0L && ids != null && !"".equals(ids.trim())) {
			// 根据角色ID获取角色对象
			Role role = roleService.getRoleByRoleId(roleId);
			// 根据"-"分隔ID字符串
			String[] idArray = ids.split("-");
			if (idArray.length > 0) {
				List<Menu> menuList = new ArrayList<Menu>();
				for (String menuId : idArray) {
					Menu menu = menuService.GetMenuByMenuId(Long.parseLong(menuId));
					menuList.add(menu);
				}
				role.setMenuList(menuList);
				return roleService.savaRole(role);
			}
		}
		return false;
	}
	
	/**
	 * 在添加或者修改过程中对角色编码或者角色名称进行唯一性校验
	 * @param roleNameOrRoleCode 角色名称或者角色编码
	 * @param roleId 如果是修改，则存在ID
	 * @return Boolean 如果违反了唯一性规则，则返回false,否则,返回true
	 * @throws Exception
	 */
	@RequestMapping(value="/remoteValidateRoleInfo", method=RequestMethod.GET)
	@ResponseBody
	public Boolean remoteValidateRoleInfo(Long roleId, String roleNameOrRoleCode) throws Exception {
		// 非空校验
		if (roleNameOrRoleCode != null && !"".equals(roleNameOrRoleCode.trim())) {
			Role role = roleService.checkRoleNameOrRoleCode(roleNameOrRoleCode.trim());
			if (role == null || (roleId != null && role.getRoleId() == roleId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 在添加和修改角色的过程中，进行角色信息的校验
	 * 
	 * @param role
	 * @return 校验成功返回true, 否则返回false
	 */
	private boolean checkRole(Role role) throws Exception {
		// 获取角色ID、角色名称、角色编码
		Long roleId = role.getRoleId();
		String roleName = role.getRoleName();
		String roleCode = role.getRoleCode();
		// 1、非空校验
		if (roleName != null && !"".equals(roleName.trim()) && roleCode != null && !"".equals(roleCode.trim())) {
			// 2、校验角色名称与角色编码中是否含有空格
			if (roleName.indexOf(" ") == -1 && roleCode.indexOf(" ") == -1) {
				// 3、校验roleName的唯一性
				role = roleService.checkRoleNameOrRoleCode(roleName.trim());
				if (role == null || (roleId != null && role.getRoleId() == roleId)) {
					return true;
				}
		 		// 4、校验roleCode的唯一性
				role = roleService.checkRoleNameOrRoleCode(roleCode.trim());
				if (role == null || (roleId != null && role.getRoleId() == roleId)) {
					return true;
				}
			}
		}
		return false;
	}
	
}