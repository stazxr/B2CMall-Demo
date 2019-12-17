package com.sanfumall.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.User;

public interface UserDao extends BaseDao<User, Long> {
	
	/**
	 * 分页获取User列表
	 * @param startIndex 开始的索引
	 * @param pageSize 每页显示的条数
	 * @return List<User>
	 * @throws Exception
	 */
	@Query(value="select * from sys_user u order by u.role_id asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<User> findUserListByPage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize);
	
	/**
	 * 分页模糊查询
	 * 注意：参数顺序   where --> order --> limit
	 * @param startIndex 分页开始的下标
	 * @param pageSize 每页显示的条数
	 * @param keyword 模糊查询的参数(username,login_name,gender,role_id,status_id)
	 * @return List<User>
	 */
	@Query(value="select * from sys_user u where u.user_no=:keyword or u.username like CONCAT('%',:keyword,'%') or u.gender=:keyword or u.role_id=:keyword or u.status_id=:keyword order by u.role_id asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<User> findUserListByLikePage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize, @Param("keyword")String keyword);
	
	/**
	 * 不分页的情况下进行模糊查询
	 * @param keyword
	 * @return List<User>
	 */
	@Query(value="select * from sys_user u where u.user_no=:keyword or u.username like CONCAT('%',:keyword,'%') or u.gender=:keyword or u.role_id=:keyword or u.status_id=:keyword order by u.role_id asc", nativeQuery = true)
	public List<User> findUserListByLike(@Param("keyword")String keyword);
	
	/**
	 * 通过LoginName查找User
	 * @param loginName
	 * @return User
	 */
	public User findByLoginName(String loginName) throws Exception;
}