package com.sanfumall.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.admin.dao.RoleDao;
import com.sanfumall.admin.dao.StatusDao;
import com.sanfumall.admin.dao.UserDao;
import com.sanfumall.admin.service.UserService;
import com.sanfumall.common.pojo.entity.Role;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.entity.User;
import com.sanfumall.common.pojo.vo.Page;
import com.sanfumall.common.util.NoGenerateUtil;

@Service(value="userService")
@Transactional
public class UserServiceImpl implements UserService {
	
	@Resource(name="userDao")
	private UserDao userDao;
	@Resource(name="statusDao")
	private StatusDao statusDao;
	@Resource(name="roleDao")
	private RoleDao roleDao;
	
	/**
	 * 分页获取管理员列表
	 * @param page 分页信息
	 * keyword 模糊查询参数，如果为null,则进行普通的查询，否则，进行模糊查询 (管理员姓名、登录名、性别、管理员主键)
	 * @return 带有分页列表的Page对象
	 * @throws Exception
	 */
	public Page<User> getUserListByPage(Page<User> page) throws Exception {
		// 根据page携带的分页信息获取分页查询limit的两个参数
		Integer pageSize = page.getPageSize();
		Integer startIndex = (page.getPageNum() - 1) * pageSize;
		// 首先根据keyword,判断是否为模糊查询
		String keyword = page.getKeyword();
		if (keyword != null && !"".equals(keyword)) {
			// 进行分页模糊查询
			// 判断是否是按状态名称查询
			Status status = statusDao.findByStatusName(keyword);
			if (status != null) {
				keyword = Long.toString(status.getStatusId());
			}
			// 判断是否是按角色名称查询
			Role role = roleDao.findByRoleName(keyword);
			if (role != null) {
				keyword = Long.toString(role.getRoleId());
			}
			// 判断是否是按照性别查询
			if("男".equals(keyword)) {
				keyword = "M";
			} else if ("女".equals(keyword)) {
				keyword = "F";
			}
			// 模糊分页查询
			List<User> userList = userDao.findUserListByLikePage(startIndex, pageSize, keyword);
			page.setList(userList);
			// 获取其他分页信息，并回填到page对象中
			Integer totalSize = userDao.findUserListByLike(keyword).size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		} else {
			// 进行普通的分页查询
			List<User> userList = userDao.findUserListByPage(startIndex, pageSize);
			page.setList(userList);
			// 获取其他分页信息，并回填到page对象中
			Integer totalSize = userDao.findAll().size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		}
		return page;
	}
	
	/**
	 * 保存或修改角色
	 * 如果角色有ID，则为修改，否则为保存
	 * @param role 待保存到角色对象
	 * @return 保存成功，返回true，否则，返回false
	 * @throws Exception
	 */
	public Boolean savaUser(User user) throws Exception {
		// 判断是新增管理员还是修改管理员
		if (user.getUserId() == null) {
			// 新增管理员
			try {
				user = userDao.save(user);
				if (user != null){
					// 保存成功，根据管理员Id生成管理员编号,并保存到user对象中，重新保存user对象
					user.setUserNo(NoGenerateUtil.getUserNoByUserId(user.getUserId()));
					userDao.save(user);
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 修改管理员
			try {
				// 根据ID获取原来的对象
				User origUser = userDao.getOne(user.getUserId());
				user = userDao.save(user);
				// 记录修改者，然后重新保存User对象
				user.setUpdateUser(origUser.getUpdateUser());
				user.setUpdateTime(origUser.getUpdateTime());
				userDao.save(user);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 通过loginName查找User
	 * @param loginName
	 * @return User对象
	 * @throws Exception
	 */
	public User getUserByLoginName(String loginName) throws Exception {
		return userDao.findByLoginName(loginName);
	}
	
	/**
	 * 通过userId查找User
	 * @param userId
	 * @return User对象
	 * @throws Exception
	 */
	public User getUserByUserId(Long userId) throws Exception {
		return userDao.getOne(userId);
	}

}