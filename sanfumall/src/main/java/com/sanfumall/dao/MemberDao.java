package com.sanfumall.dao;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Member;

public interface MemberDao extends BaseDao<Member, Long> {

	/**
	 * 根据会员昵称获取会员对象
	 * @param nickname
	 * @return Member
	 * @throws Exception
	 */
	public Member findByNickname(String nickname) throws Exception;

	/**
	 * 根据邮箱获取会员对象
	 * @param email
	 * @return Member
	 * @throws Exception
	 */
	public Member findByEmail(String email) throws Exception;

}
