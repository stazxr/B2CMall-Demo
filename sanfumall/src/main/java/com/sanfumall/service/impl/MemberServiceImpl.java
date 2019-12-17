package com.sanfumall.service.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.common.pojo.entity.Member;
import com.sanfumall.dao.MemberDao;
import com.sanfumall.service.MemberService;

@Service(value="memberService")
@Transactional
public class MemberServiceImpl implements MemberService {

	@Resource(name="memberDao")
	private MemberDao memberDao;
	
	/**
	 * 根据会员昵称获取会员对象
	 * @param nickname
	 * @return Member
	 * @throws Exception
	 */
	public Member getMemberByNickname(String nickname) throws Exception {
		return memberDao.findByNickname(nickname);
	}

	/**
	 * 根据邮箱获取会员对象
	 * @param email
	 * @return Member
	 * @throws Exception
	 */
	public Member getMemberByEmail(String email) throws Exception {
		return memberDao.findByEmail(email);
	}

	public Boolean savaMember(Member member) throws Exception {
		if (member.getMemberId() == null) {
			// 保存操作
			try {
				memberDao.save(member);
				String memberName = "SF" + System.currentTimeMillis();
				member.setMemberName(memberName);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 修改操作
			try {
				memberDao.save(member);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 根据会员ID获取会员信息
	 * @param memberId
	 * @return Member
	 * @throws Exception
	 */
	public Member getMemberById(Long memberId) throws Exception {
		return memberDao.getOne(memberId);
	}

}