package com.sanfumall.admin.service;

import com.sanfumall.common.pojo.entity.User;
import com.sanfumall.common.pojo.vo.Page;

public interface UserService {
	
	/**
	 * 分页获取管理员列表
	 * @param page 分页信息
	 * @param keyword 模糊查询参数，如果为null,则进行普通的查询，否则，进行模糊查询 
	 * @return 带有分页列表的Page对象
	 * @throws Exception
	 */
	public Page<User> getUserListByPage(Page<User> page) throws Exception;
	
	/**
	 * 保存或修改角色
	 * @param user
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaUser(User user) throws Exception;

	/**
	 * 通过loginName查找User
	 * @param loginName
	 * @return User对象
	 * @throws Exception
	 */
	public User getUserByLoginName(String loginName) throws Exception;
	
	/**
	 * 通过管理员ID查找User
	 * @param userId
	 * @return User对象
	 * @throws Exception
	 */
	public User getUserByUserId(Long userId) throws Exception;

}