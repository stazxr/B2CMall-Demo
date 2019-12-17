package com.sanfumall.admin.service;

import com.sanfumall.common.pojo.entity.Member;
import com.sanfumall.common.pojo.vo.Page;

public interface MemberService {

	/**
	 * 分页展示会员列表
	 * @param page
	 * @return 分页对象
	 * @throws Exception
	 */
	public Page<Member> getMemberListByPage(Page<Member> page) throws Exception;

	/**
	 * 根据会员Id获取会员对象
	 * @param memberId
	 * @return Member
	 * @throws Exception
	 */
	public Member getMemberByMemberId(Long memberId) throws Exception;

	/**
	 * 保存、修改会员信息
	 * @param member
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaMember(Member member) throws Exception;

}
