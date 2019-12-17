package com.sanfumall.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.admin.dao.MemberDao;
import com.sanfumall.admin.dao.StatusDao;
import com.sanfumall.admin.service.MemberService;
import com.sanfumall.common.pojo.entity.Member;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.vo.Page;

@Service(value="memberService")
@Transactional
public class MemberServiceImpl implements MemberService {

	@Resource(name="memberDao")
	private MemberDao memberDao;
	@Resource(name="statusDao")
	private StatusDao statusDao;
	
	/**
	 * 分页展示会员列表
	 * @param page
	 * @return 分页对象
	 * @throws Exception
	 */
	public Page<Member> getMemberListByPage(Page<Member> page) throws Exception {
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
			List<Member> memberList = memberDao.findMemberListByLikePage(startIndex, pageSize, keyword);
			page.setList(memberList);
			// 获取在不分页的情况下进行模糊查询的总数量，总页数，并set到page对象中
			Integer totalSize = memberDao.findMemberListByLike(keyword).size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		} else {
			// 进行普通的分页查询
			// 获取分页信息，并set到page对象中
			List<Member> memberList = memberDao.findMemberListByPage(startIndex, pageSize);
			page.setList(memberList);
			// 获取总页数与总数量
			Integer totalSize = memberDao.findAll().size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		}
		return page;
	}

	/**
	 * 根据会员Id获取会员对象
	 * @param memberId
	 * @return Member
	 * @throws Exception
	 */
	public Member getMemberByMemberId(Long memberId) throws Exception {
		return memberDao.getOne(memberId);
	}

	/**
	 * 保存、修改会员信息
	 * @param member
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaMember(Member member) throws Exception {
		try {
			memberDao.save(member);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
