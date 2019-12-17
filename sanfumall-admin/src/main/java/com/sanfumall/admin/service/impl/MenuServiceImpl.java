package com.sanfumall.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.admin.dao.MenuDao;
import com.sanfumall.admin.service.MenuService;
import com.sanfumall.common.pojo.entity.Menu;
import com.sanfumall.common.pojo.entity.Role;
import com.sanfumall.common.pojo.vo.Node;

@Service(value="menuService")
@Transactional
public class MenuServiceImpl implements MenuService {
	
	@Resource(name="menuDao")
	private MenuDao menuDao;
	
	/**
	 * 根据角色信息获取roleId,然后通过roleId到数据库查询所对应的菜单列表
	 * @param role 
	 * @return List<Menu>
	 * @throws Exception
	 */
	public List<Menu> getMenuListByRole(Role role) throws Exception {
		// 获取一级菜单列表
		List<Menu> menuList = menuDao.findFirstMenuList(role.getRoleId());
		// 遍历一级菜单列表，获取对应的二级菜单列表
		for (Menu secondMenu : menuList) {
			List<Menu> secondMenuList = menuDao.findSubMenuList(role.getRoleId(), secondMenu.getMenuId());
			secondMenu.setMenuList(secondMenuList);
		}
		return menuList;
	}

	/**
	 * 根据menuId获取Menu对象
	 * @param menuId
	 * @return Menu
	 * @throws Exception
	 */
	public Menu GetMenuByMenuId(long menuId) throws Exception {
		return menuDao.getOne(menuId);
	}
	
	/**
	 * 根据Role对象获取对应的菜单节点集合
	 * @param role
	 * @return List<Node>
	 * @throws Exception
	 */
	public List<Node> getNodeListForAuthc(Role role) throws Exception {
		// 因为页面前端要展示所有的菜单节点，所以首先获取所有的菜单集合
		List<Menu> allMenuList = menuDao.findAll();
		// 获取角色对应的所有菜单集合(角色此时为持久态，可以直接获取菜单列表)
		List<Menu> menuListOfRole = role.getMenuList();
		// 开始对比两个菜单集合，并且将allMenuList转换为Node集合
		List<Node> nodes = new ArrayList<Node>();
		for (Menu menu : allMenuList) {
			// 创建Node对象
			Node node = new Node();
			// 设定属性
			node.setId(menu.getMenuId());
			node.setName(menu.getMenuName());
			if (menu.getParent() != null) {
				// 二级菜单
				node.setpId(menu.getParent().getMenuId());
			} else {
				// 一级菜单
				node.setpId(0L);
				// 将一级菜单展开
				node.setOpen(true);
			}
			// 个人信息菜单是所有角色默认有的
			if ("个人信息".equals(menu.getMenuName()) || (menu.getParent() != null && "个人信息".equals(menu.getParent().getMenuName()))) {
				// 默认选中
				node.setChecked(true);  
				// 不允许随意勾选
				node.setDoCheck(false);
			}
			// 判断菜单是否被选中
			if (menuListOfRole != null && menuListOfRole.size() > 0) {
			/// ？contains，如果参数是引用类型的话，是地址是否被包含,还是内容是否被包含
			/// if (menuListOfRole.contains(menu)) {}
				for (Menu roleMenu : menuListOfRole) {
					// 由于上面已经存在个人信息的判断了，所以这里的循环直接跳过
					if ("个人信息".equals(roleMenu.getMenuName()) || (roleMenu.getParent() != null && "个人信息".equals(roleMenu.getParent().getMenuName()))) {
						continue;
					}
					if (menu.getMenuId() == roleMenu.getMenuId()) {
						node.setChecked(true);
						break;
					}
				}
			}
			nodes.add(node);
		}
		return nodes;
	}

}