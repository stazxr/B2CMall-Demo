package com.sanfumall.common.pojo.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 菜单
 * @author SunTao
 */
@Entity
@Table(name="sys_menu")
public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long menuId;			// 菜单Id
	private Menu parent;			// 上级菜单：多对一
	private String menuName;		// 菜单名称
	private String url;				// 链接地址
	private String icon;			// 菜单图标
	private Long sortOrder;			// 排序字段
	private List<Menu> menuList;	// 对应的子菜单集合：一对多
	private List<Role> roleList;	// 每个权限分配给的角色集合：多对多
	
	public Menu() {}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	@ManyToOne(targetEntity=Menu.class, fetch=FetchType.LAZY,
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="parent_id")
	public Menu getParent() {
		return parent;
	}
	public void setParent(Menu parent) {
		this.parent = parent;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Long getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}
	@OneToMany(targetEntity=Menu.class, mappedBy="parent", fetch=FetchType.LAZY,
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	public List<Menu> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}
	@ManyToMany(targetEntity=Role.class, fetch=FetchType.LAZY,
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="sys_role_menu",
			joinColumns= {@JoinColumn(name="menu_id")},
			inverseJoinColumns= {@JoinColumn(name="role_id")})
	public List<Role> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public String toString() {
		String msg = "menuId:" + menuId + "\tmenuName:" + menuName + "\tparent:[" + parent + "]";
		return msg;
	}
	
}
