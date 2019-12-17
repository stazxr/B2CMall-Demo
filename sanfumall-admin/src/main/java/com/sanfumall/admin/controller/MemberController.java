package com.sanfumall.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanfumall.admin.service.MemberService;
import com.sanfumall.admin.service.StatusService;
import com.sanfumall.admin.service.UserService;
import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Member;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.vo.Page;

@Controller("memberController")
@RequestMapping("/member")
public class MemberController extends BaseController {

	@Resource(name="userService")
	private UserService userService;
	@Resource(name="statusService")
	private StatusService statusService;
	@Resource(name="memberService")
	private MemberService memberService;
	
	/**
	 * 分页展示角色列表
	 * @param pageNum 页码
	 * @param pageSize 每页显示的条数
	 * @param keyword 模糊查询的参数，如果为null,则进行普通分页查询，否则进行模糊分页查询
	 * @return member_index.html
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView getMemberIndex(Integer pageNum, Integer pageSize, String keyword) throws Exception {
		// 首先，创建分页对象
		Page<Member> page = new Page<>(pageNum, pageSize, keyword);
		page = memberService.getMemberListByPage(page);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("page", page);
		return new ModelAndView("member/member_index", resultMap);
	}
	
	/**
	 * 加载详情页面
	 * @param memberId
	 * @return member_detail.html
	 * @throws Exception
	 */
	@RequestMapping(value="/detail/{memberId}", method=RequestMethod.GET)
	public ModelAndView getDetailPage(@PathVariable("memberId")Long memberId) throws Exception {
		Member member = memberService.getMemberByMemberId(memberId);
		return new ModelAndView("member/member_detail", "member", member);
	}
	
	/**
	 * 修改会员状态
	 * @param memberId
	 * @param statusCode 欲修改的状态编码
	 * @return boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/changeStatus/{memberId}/{statusCode}", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean changeStatus(@PathVariable("memberId")Long memberId, @PathVariable("statusCode")String statusCode) throws Exception {
		if (statusCode != null && !"".equals(statusCode) && memberId != null) {
			// 根据memberId获取会员对象
			Member member = memberService.getMemberByMemberId(memberId);
			// 根据状态编码获取状态对象,并保存带会员对象中
			Status status = statusService.getStatusByStatusCode(statusCode);
			member.setStatus(status);
			// 调用业务层保存会员
			return memberService.savaMember(member);
		}
		return false;
	}
	
}