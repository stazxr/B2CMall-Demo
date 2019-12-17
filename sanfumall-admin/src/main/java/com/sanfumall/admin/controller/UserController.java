package com.sanfumall.admin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.sanfumall.admin.service.RoleService;
import com.sanfumall.admin.service.StatusService;
import com.sanfumall.admin.service.UserService;
import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Role;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.entity.User;
import com.sanfumall.common.pojo.vo.Page;
import com.sanfumall.common.util.ConstantUtil;
import com.sanfumall.common.util.DateUtil;
import com.sanfumall.common.util.email.EmailComponent;
import com.sanfumall.common.util.security.MD5Util;

@Controller("userController")
@RequestMapping("/user")
public class UserController extends BaseController {
	
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="roleService")
	private RoleService roleService;
	@Resource(name="statusService")
	private StatusService statusService;
	
	@Resource(name="emailComponent")
	private EmailComponent emailComponent;
	
	/*****************************************************************/
	/****************************管理员登录操作*****************************/  
	/*****************************************************************/
	
	/**
	 * 加载登录界面
	 * @return user_login.html
	 * @throws Exception
	 */
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String getLoginPage() throws Exception {
		return "user/user_login";
	}
	
	/**
	 * shiro认证失败，重新跳转到登录界面
	 * @return "/user/logout",重定向到退出过滤器
	 * @throws Exception
	 */
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ModelAndView loginError() throws Exception {
		RedirectView view = new RedirectView(request.getContextPath() + "/user/logout");
		return new ModelAndView(view);
	}
	
	/******************************************************************/
	/******************************管理员CURD操作**************************/  
	/******************************************************************/
	
	/**
	 * 加载管理员列表页面
	 * @param pageNum 页码
	 * @param pageSize 每页显示的数量
	 * @param keyword 模糊查询关键词，如果为null,则进行普通的查询，否则进行模糊查询
	 * @return user_index.html
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView getUserIndex(Integer pageNum, Integer pageSize, String keyword) throws Exception {
		// 创建分页对象
		Page<User> page = new Page<>(pageNum, pageSize, keyword);
		page = userService.getUserListByPage(page);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("page", page);
		return new ModelAndView("user/user_index", resultMap);
	}
	
	/**
	 * 加载用户添加页面
	 * @return user_add.html
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public ModelAndView getAddPage() throws Exception {
		// 获取所有的角色信息
		List<Role> roleList = roleService.getAllRole();
		return new ModelAndView("user/user_add", "roleList", roleList);
	}
	
	/**
	 * 保存User对象
	 * @param user
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public Boolean addRole(User user, Long roleId) throws Exception {
		// 校验roleId是否为空，防止前端出错
		if (roleId != null && roleId > 0) {
			// 校验roleId是否存在
			Role role = roleService.getRoleByRoleId(roleId);
			if (role != null) {
				// 校验User对象参数是否合法（前端校验有可能失效）
				if (checkUser(user)) {
					// 将Role对象保存到User对象中
					user.setRole(role);
					// 将密码进行MD5加密，并重新保存带User对象中
					user.setPassword(MD5Util.encrypt(user.getPassword().trim()));
					// 将登录名去除首位空格，并重新保存到User对象中
					user.setLoginName(user.getLoginName().trim());
					// 获取用户的默认头像，并保存到User对象中
					user.setImgUrl("/static/images/systemImg/other/user08.png");
					// 校验成功，获取当前登录用户，记录创建者
					User createUser = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
					user.setCreateUser(createUser);
					user.setCreateTime(new Date());
					// 获取启用状态对象，并保存在管理员中
					Status status = statusService.getStatusByStatusCode(ConstantUtil.STATUS_ENABLE);
					user.setStatus(status);
					// 调用业务层保存角色
					return userService.savaUser(user);
				}
			}
		}
		return false;
	}
	
	/**
	 * 加载修改页面
	 * @param userId 
	 * @return user_update.html
	 * @throws Exception
	 */
	@RequestMapping(value="/update/{userId}", method=RequestMethod.GET)
	public ModelAndView getUpdatePage(@PathVariable("userId")Long userId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		User user = userService.getUserByUserId(userId);
		List<Role> roleList = roleService.getAllRole();
		resultMap.put("user", user);
		resultMap.put("roleList", roleList);
		return new ModelAndView("user/user_update", resultMap);
	}
	
	/**
	 * 修改用户
	 * @param user
	 * @param roleId
	 * @return 修改成功，返回true,否则返回false
	 * @throws Exception
	 */
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean updateRole(User user, Long roleId) throws Exception {
		// 校验roleId是否为null
		if (roleId != null && roleId > 0) {
			// 校验roleId是否存在
			Role role = roleService.getRoleByRoleId(roleId);
			// 校验Role对象是否合法（前端校验有可能失效）
			if (role != null) {
				if (checkUser(user)) {
					// 根据userId获得数据库中原有的用户信息，并将未修改的信息进行回填
					User origUser = userService.getUserByUserId(user.getUserId());
					// 将身份证号,出生日期,状态,创建人,创建时间回填
					user.setIdCard(origUser.getIdCard());
					user.setBirthday(user.getBirthday());
					user.setStatus(origUser.getStatus());
					user.setCreateUser(origUser.getCreateUser());
					user.setCreateTime(origUser.getCreateTime());
					// 根据角色ID，获取角色对象，并保存到user对象中
					user.setRole(roleService.getRoleByRoleId(roleId));
					/// 记录修改者，修改时间,在service层进行，这里保存自己会进入死循环
					// 调用业务层保存角色
					return userService.savaUser(user);
				}
			}
		}
		return false;
	}
	
	/**
	 * 加载详情页面
	 * @param userId
	 * @return user_detail.html
	 * @throws Exception
	 */
	@RequestMapping(value="/detail/{userId}", method=RequestMethod.GET)
	public ModelAndView getDetailPage(@PathVariable("userId")Long userId) throws Exception {
		User user = userService.getUserByUserId(userId);
		// 对出生日期进行格式化。单独向前台传值
		String birthday;
		if (user.getBirthday() != null) {
			birthday = DateUtil.parseDateToString(user.getBirthday());
		} else {
			birthday = "";
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("user", user);
		resultMap.put("birthday", birthday);
		return new ModelAndView("user/user_detail", resultMap);
	}
	
	/**
	 * 修改用户状态
	 * @param userId
	 * @param statusCode
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/changeStatus/{userId}/{statusCode}", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean changeStatus(@PathVariable("userId")Long userId, @PathVariable("statusCode")String statusCode) throws Exception {
		if (statusCode != null && !"".equals(statusCode) && userId != null) {
			// 根据ID获取管理员对象
			User user = userService.getUserByUserId(userId);
			// 根据状态编码获取状态对象,并保存带角色对象中
			Status status = statusService.getStatusByStatusCode(statusCode);
			user.setStatus(status);
			// 调用业务层保存角色
			return userService.savaUser(user);
		}
		return false;
	}
	
	/*****************************************************************/
	/****************************管理员个人信息相关方法************************/  
	/*****************************************************************/
	
	/**
	 * 加载个人信息页面
	 * @param userId
	 * @return user_self.html
	 * @throws Exception
	 */
	@RequestMapping(value="/self", method=RequestMethod.GET)
	public ModelAndView getUserSelfPage() throws Exception {
		// 获取当前登录的User对象
		User user = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
		// 格式化日期
		String birthday;
		if (user.getBirthday() != null) {
			birthday = DateUtil.parseDateToString(user.getBirthday());
		} else {
			birthday = "";
		}
		// 绑定参数
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("user", user);
		resultMap.put("birthday", birthday);
		return new ModelAndView("user/user_self", resultMap);
	}
	
	/**
	 * 加载修改个人信息页面
	 * @param userId
	 * @return user_updateself.html
	 * @throws Exception
	 */
	@RequestMapping(value="/updateself", method=RequestMethod.GET)
	public ModelAndView getUpdateSelfPage() throws Exception {
		// 获得管理员个人信息
		User user = userService.getUserByUserId(((User)(session.getAttribute("user"))).getUserId());
		// 获得角色列表
		List<Role> roleList = roleService.getAllRole();
		// 创建Map对象，绑定user与roleList
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("user", user);
		resultMap.put("roleList", roleList);
		return new ModelAndView("user/user_updateself", resultMap);
	}
	
	/**
	 * 进行个人信息修改
	 * @param user
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/updateself", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean updateSelf(User user) throws Exception {
		User origUser = userService.getUserByUserId(user.getUserId());
		user.setImgUrl(origUser.getImgUrl());
		user.setStatus(origUser.getStatus());
		user.setCreateUser(origUser.getCreateUser());
		user.setCreateTime(origUser.getCreateTime());
		user.setRole(origUser.getRole());
		Date birthday = DateUtil.parseIdCardToBirthday(user.getIdCard());
		user.setBirthday(birthday);
		return userService.savaUser(user);
	}
	
	/**
	 * 加载修改头像页面
	 * @param userId
	 * @return user_changeImg.html
	 * @throws Exception
	 */
	@RequestMapping(value="/changeImg", method=RequestMethod.GET)
	public ModelAndView changeImg(Long userId) throws Exception {
		// 判断是否是在个人信息中进行修改，如果userId不为null,则是在个人信息中进行修改，否则，需要获取登录用户对象
		User user = null;
		if (userId != null) {
			user = userService.getUserByUserId(userId);
		} else {
			user = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
		}
		return new ModelAndView("user/user_changeImg", "user", user);
	}
	// 进入修改头像页面时，uploadify.js会发出一个截取的请求重新加载页面？
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView changeImgInfo(User user) throws Exception {
		return new ModelAndView("user/user_changeImg", "user", user);
	}
	
	/**
	 * 保存头像
	 * @param userId
	 * @param imgUrl
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/changeImg", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean savaImgUrl(Long userId, String imgUrl) throws Exception {
		if (userId != null && imgUrl != null && !"".equals(imgUrl)) {
			// 获取用户对象
			User user = userService.getUserByUserId(userId);
			user.setImgUrl(imgUrl);
			return userService.savaUser(user);
		}
		return false;
	}
	
	/******************************************************************/
	/*********************管理员忘记密码、修改密码相关方法**************************/  
	/******************************************************************/
	
	/**
	 * 加载修改密码页面
	 * @param userId
	 * @return user_changepwd.html
	 * @throws Exception
	 */
	@RequestMapping(value="/changePwd", method=RequestMethod.GET)
	public ModelAndView getChangePwdPage(Long userId) throws Exception {
		// 判断是否是在个人信息中进行修改，如果userId不为null,则是在个人信息中进行修改，否则，需要获取登录用户对象
		User user = null;
		if (userId != null) {
			user = userService.getUserByUserId(userId);
		} else {
			user = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
		}
		return new ModelAndView("user/user_changepwd", "user", user);
	}
	
	/**
	 * 加载忘记密码页面
	 * @param userId
	 * @return user/user_forgetPwd.html
	 * @throws Exception
	 */
	@RequestMapping(value="/forgetPwd", method=RequestMethod.GET)
	public ModelAndView getForgetPwdPage() throws Exception {
		return new ModelAndView("user/user_forgetPwd");
	}

	/**
	 * 在忘记密码的第一步：填写登录名的过程中，校验登录名是否存在，
	 * 如果存在，则根据User对象获取email，校验email是否合格，
	 * 然后根据User对象获取userId，session绑定userId，并返回email;
	 * 如果不存在，返回false。
	 * @param loginName
	 * @return String 返回"null", 代表登录名不存在；返回"0",邮箱不存在。或者格式有问题，邮箱格式正确，返回email
	 * @throws Exception
	 */
	@RequestMapping(value="/checkLoginNameWhenForgetPwd", method=RequestMethod.GET)
	@ResponseBody
	public String checkLoginNameWhenForgetPwd(String loginName) throws Exception {
		// 首先进行非空校验
		if (loginName != null && !"".equals(loginName.trim())) {
			// 根据登录名获取User对象，如果User对象不为null,则进行后续操作，否则，返回false
			User user = userService.getUserByLoginName(loginName);
			if (user != null) {
				// 获取user的邮箱，校验邮箱的合法性
				String email = user.getEmail();
				// 判断邮箱是否为空
				if (email != null && !"".equals(email)) {
					// 判断邮箱格式是否正确
					String regex = "^[0-9A-Za-z][\\.-_0-9A-Za-z]*@[0-9A-Za-z]+(\\.[0-9A-Za-z]+)+$";
					Pattern pattern = Pattern.compile(regex);
					if (pattern.matcher(email).matches()) {
						// 绑定userId，为后续的保存密码提供User对象
						session.setAttribute("userId", user.getUserId());
						return email;
					}
				}
				return "0";
			}
		}
		return "null";
	}
	
	/**
	 * 在用户输入了正确的验证码后，进行发送邮箱操作
	 * @param email
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/SendEmail", method=RequestMethod.GET)
	@ResponseBody
	public Boolean SendEmail(String email) throws Exception {
		// 首先进行非空校验
		if (email != null && !"".equals(email.trim())) {
			// 邮箱校验通过，发送邮件
			String successEmailCode = emailComponent.sendSimpleMail(email);
			// 绑定正确的验证码，方便第二步的验证码验证
			session.setAttribute("successEmailCode", successEmailCode);
			return true;
		}
		return false;
	}
	
	/**
	 * 在忘记密码的第二步：填写验证码的过程中，校验用户输入的邮箱验证码是否正确
	 * @param emailCode 用户填写的验证码
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/checkEmailCode", method=RequestMethod.GET)
	@ResponseBody
	public Boolean checkEmailCode(String emailCode) throws Exception {
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
	 * 在忘记密码的第三步以及修改密码的过程中，进行新密码的保存
	 * @param userId 如果是修改密码，则userId不为空，如果是忘记密码，则userId为空
	 * @param password 新密码
	 * @return Booelan
	 * @throws Exception
	 */
	@RequestMapping(value="/savaNewPwd", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean savaNewPwd(Long userId, String password) throws Exception {
		// 判断是忘记密码的保存操作，还是修改密码的保存操作
		if (userId == null) {
			// 忘记密码的保存操作，获取session中绑定的用户Id，赋值给userId变量
			userId = (Long) session.getAttribute("userId");
			// 从session中移除userId属性
			session.removeAttribute("userId");
		}
		// 再次进行非空校验，进行保存操作
		if (userId != null && password != null && !"".equals(password.trim())) {
			// 根据userId获取User对象
			User user = userService.getUserByUserId(userId);
			// 对新密码进行加密，并保存到user对象中
			user.setPassword(MD5Util.encrypt(password));
			return userService.savaUser(user);
		}
		return false;
	}
	
	/**
	 * 在修改密码的过程中，检测用户输入的旧密码是否正确
	 * 根据userId与输入的密码检测用户输入的密码是否正确
	 * @param userId
	 * @param password
	 * @return Booelan
	 * @throws Exception
	 */
	@RequestMapping(value="/checkPassword", method=RequestMethod.GET)
	@ResponseBody
	public Boolean checkPassword(Long userId, String password) throws Exception {
		// 校验信息是否为空
		if (userId != null && userId > 0 && password != null && !"".equals(password.trim())) {
			// 根据ID获取用户，然后获取原来的密码
			String origPassword = userService.getUserByUserId(userId).getPassword();
			// 对前台传来的密码进行MD5加密，然后与原先的密码进行比较
			if (MD5Util.encrypt(password).equals(origPassword)) {
				return true;
			}
		}
		return false;
	}

	/******************************************************************/
	/*****************************校验相关方法******************************/  
	/******************************************************************/
	
	/**
	 * 在添加或者修改过程中对登录名进行唯一性校验
	 * @param loginName 登录名
	 * @param userId 如果是修改，则存在ID
	 * @return Boolean 如果违反了唯一性规则，则返回false,否则,返回true
	 * @throws Exception
	 */
	@RequestMapping(value="/remoteValidateloginName", method=RequestMethod.GET)
	@ResponseBody
	public Boolean checkLoginName(String loginName, Long userId) throws Exception {
		// 非空校验
		if (loginName != null && !"".equals(loginName.trim())) {
			// 根据登录名查询，获得用户
			User user = userService.getUserByLoginName(loginName);
			if (user == null || (userId != null && userId == user.getUserId())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 在保存或修改时校验用户信息的合法性
	 * @param user
	 * @return 校验成功返回true, 否则返回false
	 * @throws Exception 
	 */
	private boolean checkUser(User user) throws Exception {
		// 获取管理员姓名,登录名,密码,联系电话,电子邮箱
		String username = user.getUsername();
		String loginName = user.getLoginName();
		String password = user.getPassword();
		String cellphone = user.getCellphone();
		String email = user.getEmail();
		// 首先，进行非空校验
		if (username != null && !"".equals(username.trim()) &&
				loginName != null && !"".equals(loginName.trim()) &&
				password != null && !"".equals(password.trim()) &&
				cellphone != null && !"".equals(cellphone.trim()) &&
				email != null && !"".equals(email.trim())){
			// 对登录名进行唯一性校验
			User checkedUser = userService.getUserByLoginName(loginName);
			if (checkedUser == null || (user.getUserId() != null && user.getUserId() == checkedUser.getUserId())) {
				return true;
			}
		}
		return false;
	}

}