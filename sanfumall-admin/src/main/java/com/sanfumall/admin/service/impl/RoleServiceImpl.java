package com.sanfumall.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.admin.dao.RoleDao;
import com.sanfumall.admin.dao.StatusDao;
import com.sanfumall.admin.service.RoleService;
import com.sanfumall.common.pojo.entity.Role;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.vo.Page;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {
	
	@Resource(name="roleDao")
	private RoleDao roleDao;
	@Resource(name="statusDao")
	private StatusDao statusDao;
	
	/**
	 * 分页获取角色列表，根据page中的keyword来判断是普通的分页查询，还是分页模糊查询
	 * @param page 分页信息
	 * @return Page<Role>
	 * @throws Exception
	 */
	public Page<Role> getRoleListByPage(Page<Role> page) throws Exception {
		// 根据page携带的分页信息获取分页查询limit的两个参数startIndex,pageSize 
		Integer pageSize = page.getPageSize();
		Integer startIndex = (page.getPageNum() - 1) * pageSize;
		// 首先判断是否为模糊查询,如果keyword为空或空字符串，则为普通查询，否则为模糊查询
		String keyword = page.getKeyword();
		if (keyword != null && !"".equals(keyword)) {
			// 进行分页模糊查询
			// 首先，判断是否是按状态名称查询，如果是按照状态名称查询，则将状态Id赋值给keyword
			Status status = statusDao.findByStatusName(keyword);
			if (status != null) {
				keyword = Long.toString(status.getStatusId());
			}
			// 然后，进行分页模糊查询， 并将查询的结果set到page对象中
			List<Role> roleList = roleDao.findRoleListByLikePage(startIndex, pageSize, keyword.toUpperCase());
			page.setList(roleList);
			// 获取在不分页的情况下进行模糊查询的总数量，总页数，并set到page对象中
			Integer totalSize = roleDao.findRoleListByLike(keyword).size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		} else {
			// 进行普通的分页查询
			// 获取分页信息，并set到page对象中
			List<Role> roleList = roleDao.findRoleListByPage(startIndex, pageSize);
			page.setList(roleList);
			// 获取总页数与总数量
			Integer totalSize = roleDao.findAll().size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		}
		return page;
	}
	
	/**
	 * 根据角色ID获取角色对象
	 * @param roleId
	 * @return Role
	 * @throws Exception
	 */
	public Role getRoleByRoleId(Long roleId) throws Exception {
		return roleDao.getOne(roleId);
	}
	
	/**
	 * 获取所有的角色
	 * @return List<Role>
	 * @throws Exception
	 */
	public List<Role> getAllRole() throws Exception {
		return roleDao.findAll();
	}

	/**
	 * 保存或修改角色
	 * 如果role的roleId不为null，则为修改，否则为保存
	 * @param role
	 * @return 保存成功，返回true，否则，返回false
	 * @throws Exception
	 */
	public Boolean savaRole(Role role) throws Exception {
		try {
			roleDao.save(role);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 校验用户输 入的角色名称或者角色编码的唯一性
	 * @param roleNameOrRoleCode 角色名称或者角色编码
	 * @return Role
	 * @throws Exception
	 */
	public Role checkRoleNameOrRoleCode(String roleNameOrRoleCode) throws Exception {
		// 如果角色名称存在英文小写字母，直接对其转换为大写可能会得到意想不到的结果，所以先直接按照角色名称查询
		Role role = roleDao.findByRoleName(roleNameOrRoleCode);
		if (role != null) {
			return role;
		}
		// 如果按照角色名称没有查询到，因为角色编码是大写，所以对其转换为大写，然后按照角色编码查询
		// 所以无论是角色名称（汉字.toUpperCase()返回汉字本身）还是角色编码都转换为大写
		role = roleDao.findRoleByRoleCode(roleNameOrRoleCode);
		return role;
	}

}