package com.sanfumall.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Menu;

public interface MenuDao extends BaseDao<Menu, Long>{
	
	/**
	 * 根据角色ID获取对应的一级菜单
	 * @param roleId 角色ID
	 * @return 一级菜单列表 List<Menu>
	 * @throws Exception
	 */
	@Query("from Menu m join fetch m.roleList r where r.roleId=:roleId and m.parent is null order by m.sortOrder asc")
	public List<Menu> findFirstMenuList(@Param("roleId")Long roleId) throws Exception;
	
	/**
	 * 根据角色ID和菜单Id获取二级菜单列表
	 * @param roleId
	 * @param menuId
	 * @return 子菜单列表
	 * @throws Exception
	 */
	@Query("from Menu m join fetch m.roleList r where r.roleId=:roleId and m.parent.menuId=:menuId order by m.sortOrder asc")
	public List<Menu> findSubMenuList(@Param("roleId")Long roleId, @Param("menuId")Long menuId) throws Exception;

}