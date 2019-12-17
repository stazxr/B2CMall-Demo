package com.sanfumall.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Grade;
import com.sanfumall.common.pojo.entity.Member;
import com.sanfumall.common.util.ConstantUtil;
import com.sanfumall.common.util.email.EmailComponent;
import com.sanfumall.common.util.security.MD5Util;
import com.sanfumall.service.GradeService;
import com.sanfumall.service.MemberService;
import com.sanfumall.service.StatusService;

@Controller(value="memberController")
@RequestMapping("/member")
public class MemberController extends BaseController {

	@Resource(name="memberService")
	private MemberService memberService;
	@Resource(name="statusService")
	private StatusService statusService;
	@Resource(name="gradeService")
	private GradeService gradeService;
	@Resource(name="emailComponent")
	private EmailComponent emailComponent;
	
	/**
	 * 加载登录界面
	 * @return member_login.html
	 * @throws Exception
	 */
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String getLoginPage() throws Exception {
		return "member/member_login";
	}
	
	/**
	 * shiro认证失败，重新跳转到登录界面
	 * @return "/member/logout",重定向到退出过滤器
	 * @throws Exception
	 */
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ModelAndView loginError() throws Exception {
		RedirectView view = new RedirectView(request.getContextPath() + "/member/logout");
		return new ModelAndView(view);
	}
	
	/**
	 * 加载注册界面
	 * @return member_register.html
	 * @throws Exception
	 */
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String getRegisterPage() throws Exception {
		return "member/member_register";
	}
	
	/**
	 * 会员注册
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public Boolean memberRegister(Member member) throws Exception {
		if (checkMember(member)) {
			// 对密码进行加密并set到member对象中
			member.setPassword(MD5Util.encrypt(member.getPassword()));
			// 获取启用状态，并set到member对象中
			member.setStatus(statusService.getStatusByStatusCode(ConstantUtil.STATUS_ENABLE));
			// 根据当前时间生成注册时间，并set到member对象中
			member.setCreateTime(new Date());
			// 默认积分为0，并set到member对象中
			member.setPoint(0L);
			// 生成会员等级，获取会员等级列表
			List<Grade> gradeList = gradeService.getAllGrade();
			for (Grade grade : gradeList) {
				if (member.getPoint() >= grade.getMinPoint() && member.getPoint() <= grade.getMaxPoint()) {
					member.setGrade(grade);
				}
			}
			// 设置默认头像
			member.setImgUrl("/static/images/default_img.png");
			// 调用业务层保存会员信息
			return memberService.savaMember(member);
		}
		return false;
	}
	
	/**
	 * 加载忘记密码界面
	 * @return member_forgetPwd.html
	 * @throws Exception
	 */
	@RequestMapping(value="/forgetPwd", method=RequestMethod.GET)
	public String getForgetPwdPage() throws Exception {
		return "member/member_forgetPwd";
	}
	
	/**
	 * 根据email发送邮箱验证码
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/checkEmailIsExist", method=RequestMethod.GET)
	@ResponseBody
	public Boolean checkEmailIsExist(String email) throws Exception {
		// 首先进行非空校验
		if (email != null && !"".equals(email.trim())) {
			// 根据邮箱获取会员信息
			Member member = memberService.getMemberByEmail(email);
			if (member != null) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据email发送邮箱验证码
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/sendEmail", method=RequestMethod.GET)
	@ResponseBody
	public Boolean sendRegisterEmail(String email) throws Exception {
		// 首先进行非空校验
		if (email != null && !"".equals(email.trim())) {
			// 邮箱校验通过，发送邮件
			String successEmailCode = emailComponent.sendSimpleMail(email);
			// 绑定正确的验证码，方便接下来的验证码验证
			session.setAttribute("successEmailCode", successEmailCode);
			return true;
		}
		return false;
	}
	
	/**
	 * 校验邮箱验证码是否填写正确
	 * @param emailCode 用户填写的验证码
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/checkEmailCode/{emailCode}", method=RequestMethod.GET)
	@ResponseBody
	public Boolean checkEmailCode(@PathVariable("emailCode")String emailCode) throws Exception {
		// 首先进行非空校验
		if (emailCode != null && !"".equals(emailCode.trim())) {
			// 获取session中绑定的验证码
			String successEmailCode = (String) session.getAttribute("successEmailCode");
			if (emailCode.trim().equals(successEmailCode)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 保存新密码
	 * @param email
	 * @param password
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/savaNewPwd", method=RequestMethod.POST)
	@ResponseBody
	public Boolean savaNewPwd(String email, String password) throws Exception {
		// 根据邮箱获取会员信息
		Member member = memberService.getMemberByEmail(email);
		if (member != null) {
			// 对密码进行加密并set到member对象中
			member.setPassword(MD5Util.encrypt(password));
			return memberService.savaMember(member);
		}
		return false;
	}
	
	/**
	 * 加载忘记密码界面
	 * @return member_forgetPwd.html
	 * @throws Exception
	 */
	@RequestMapping(value="/changeImg", method=RequestMethod.GET)
	public String changeImg(Long memberId) throws Exception {
		return "member/member_changeImg";
	}
	
	/**
	 * 校验邮箱的唯一性
	 * @param memberId
	 * @param email
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/checkEmailRemote", method=RequestMethod.GET)
	@ResponseBody
	public Boolean checkEmailRemote(Long memberId, String email) throws Exception {
		// 首先进行非空校验
		if (email != null && !"".equals(email.trim())) {
			// 根据邮箱获取会员信息
			Member member = memberService.getMemberByEmail(email);
			if (member == null || (memberId != null && memberId == member.getMemberId())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 对前台传来的数据进行非空校验
	 * @param member
	 * @return boolean
	 */
	private boolean checkMember(Member member) {
		String nickname = member.getNickname();
		String password = member.getPassword();
		String email = member.getEmail();
		if (nickname != null && !"".equals(nickname.trim())
				&&  password != null && !"".equals(password.trim())
				&& email != null && !"".equals(email.trim())) {
			return true;
		}
		return false;
	}
	
}