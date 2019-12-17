package com.sanfumall.admin.service;

import java.util.List;

import com.sanfumall.common.pojo.entity.Menu;
import com.sanfumall.common.pojo.entity.Role;
import com.sanfumall.common.pojo.vo.Node;

public interface MenuService {
	
	/**
	 * 根据Role对象获取对应的菜单列表
	 * 在实现类中可以获得角色的ID，通过ID在数据库中查询角色的菜单列表
	 * @param role 
	 * @return 角色所对应的菜单列表 List<Menu>
	 * @throws Exception
	 */
	public List<Menu> getMenuListByRole(Role role) throws Exception;
	
	/**
	 * 根据菜单Id获取菜单
	 * @param menuId
	 * @return Menu
	 * @throws Exception
	 */
	public Menu GetMenuByMenuId(long menuId) throws Exception;
	
	/**
	 * 根据Role对象获取对应的菜单节点集合
	 * @param role
	 * @return List<Node>
	 * @throws Exception
	 */
	public List<Node> getNodeListForAuthc(Role role) throws Exception;
	
}