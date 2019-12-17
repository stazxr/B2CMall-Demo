package com.sanfumall.admin.service;


import java.util.List;

import com.sanfumall.common.pojo.entity.Role;
import com.sanfumall.common.pojo.vo.Page;

public interface RoleService {
	
	/**
	 * 分页获取角色列表
	 * @param page 分页信息
	 * @return Page<Role>
	 * @throws Exception
	 */
	public Page<Role> getRoleListByPage(Page<Role> page) throws Exception;

	/**
	 * 根据角色ID获取角色对象
	 * @param roleId
	 * @return Role
	 * @throws Exception
	 */
	public Role getRoleByRoleId(Long roleId) throws Exception;

	/**
	 * 获取所有的角色
	 * @return List<Role>
	 * @throws Exception
	 */
	public List<Role> getAllRole() throws Exception;
	
	/**
	 * 保存或修改角色
	 * 如果角色有ID，则为修改，否则为保存
	 * @param role 待保存或修改到角色对象
	 * @return 保存成功，返回true，否则，返回false
	 * @throws Exception
	 */
	public Boolean savaRole(Role role) throws Exception;

	/**
	 * 校验用户输入的角色名称或者角色编码是否唯一
	 * @param roleNameOrRoleCode 角色名称或者角色编码
	 * @return Role
	 * @throws Exception
	 */
	public Role checkRoleNameOrRoleCode(String roleNameOrRoleCode) throws Exception;
	
}