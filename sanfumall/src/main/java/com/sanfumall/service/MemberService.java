package com.sanfumall.service;

import com.sanfumall.common.pojo.entity.Member;

public interface MemberService {

	/**
	 * 根据会员昵称获取会员对象
	 * @param nickname
	 * @return Member
	 * @throws Exception
	 */
	public Member getMemberByNickname(String nickname) throws Exception;

	/**
	 * 根据邮箱获取会员对象
	 * @param email
	 * @return Member
	 * @throws Exception
	 */
	public Member getMemberByEmail(String email) throws Exception;

	/**
	 * 注册会员
	 * @param member
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaMember(Member member) throws Exception;

	/**
	 * 根据会员ID获取会员信息
	 * @param memberId
	 * @return Member
	 * @throws Exception
	 */
	public Member getMemberById(Long memberId) throws Exception;
	
}