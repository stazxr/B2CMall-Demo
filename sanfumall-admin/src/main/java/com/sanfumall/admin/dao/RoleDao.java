package com.sanfumall.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Role;

public interface RoleDao extends BaseDao<Role, Long>{
	
	/**
	 * 普通的分页查询
	 * @param startIndex 开始的索引
	 * @param pageSize 每页显示的条数
	 * @return List<Role>
	 * @throws Exception
	 */
	@Query(value="select * from sys_role r order by r.role_id asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Role> findRoleListByPage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize) throws Exception;
	
	/**
	 * 分页模糊查询
	 * 注意：参数顺序   where --> order --> limit
	 * @param startIndex 分页开始的下标
	 * @param pageSize 每页显示的条数
	 * @param keyword 模糊查询的参数(role_name,role_code,status_id)
	 * @return List<Role>
	 */
	@Query(value="select * from sys_role r where r.role_name like CONCAT('%',:keyword,'%') or r.role_code like CONCAT('%',:keyword,'%') or r.status_id=:keyword order by r.role_id asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Role> findRoleListByLikePage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize, @Param("keyword")String keyword);
	
	/**
	 * 在不分页的情况下进行模糊查询
	 * @param keyword 模糊参数
	 * @return List<Role>
	 */
	@Query(value="select * from sys_role r where r.role_name like CONCAT('%',:keyword,'%') or r.role_code like CONCAT('%',:keyword,'%') or r.status_id=:keyword order by r.role_id asc", nativeQuery = true)
	public List<Role> findRoleListByLike(@Param("keyword")String keyword);
	
	/**
	 * 根据角色编码查找角色
	 * @param roleCode
	 * @return Role
	 */
	@Query("from Role r where r.roleCode=:roleCode")
	public Role findRoleByRoleCode(@Param("roleCode")String roleCode) throws Exception;

	/**
	 * 根据角色名称查找角色
	 * @param roleName
	 * @return Role
	 */
	public Role findByRoleName(String roleName) throws Exception;
	
}