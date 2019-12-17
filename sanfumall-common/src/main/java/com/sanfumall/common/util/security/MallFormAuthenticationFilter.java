package com.sanfumall.common.util.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.util.StringUtils;

import com.sanfumall.common.util.security.MallToken;

/**
 * 在FormAuthenticationFilter的基础上扩展了认证验证码的功能
 * @author SunTao
 * @since 2018-12-13
 */
public class MallFormAuthenticationFilter extends FormAuthenticationFilter {
	
	/**
	 * 点击登录后，shiro会拦截登录请求，进入executeLogin进行登录认证
	 */
	@Override
	protected boolean executeLogin(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		// 将ServletRequest和ServletResponse进行类型转换
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		// 封装NumCodeToken对象
		MallToken mallToken = createmallToken(request);
		// 获取用户输入的验证码
		String numCode = mallToken.getNumCode();
		// 获取session中绑定的正确的验证码
		String successNumCode = (String) request.getSession().getAttribute("successNumCode");
		try {
			// 校验验证码,并判断验证码是否正确,默认认证成功,如果认证失败,则抛出异常,进入onLoginFailure方法，重定向到登录界面
			validateNumCode(successNumCode, numCode);
			// 说明验证码认证成功,获取Shiro主体对象Subject
			Subject subject = getSubject(request, response);
			// 经过一系列的跳转会进入ShiroDBRealm的doGetAuthenticationInfo(AuthenticationToken token)方法中，进行其他登录信息的认证
			subject.login(mallToken);
			// 认证成功后，进入onLoginSuccess()方法，跳转到successUrl所指向的地址（没设置的话，默认为"/"）
			// 注意：原始的onLoginSuccess()会覆盖掉我们在配置文件中设置的successUrl，所以需要对其进行重写
			return onLoginSuccess(mallToken, subject, request, response);
		} catch (UnknownAccountException e) {
			// 认证失败，抛出错误登录信息，重定向到登录界面
			e.printStackTrace();
			return onLoginFailure(mallToken, new AuthenticationException(e.getMessage()), request, response);
		}
	}
	
	/**
	 * 创建mallTaken对象，将用户登录的信息封装在taken中
	 * @param request 用户发出的登录请求信息
	 * @return mallToken对象
	 */
	private MallToken createmallToken(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String numCode = request.getParameter("numCode");
		return new MallToken(username, password, isRememberMe(request), getHost(request), numCode);
	}
	
	/**
	 * 对于验证码的校验
	 * @param successNumCode DefaultKaptcha生成的验证码
	 * @param numCode 用户输入的验证码
	 * @return 如果校验成功，返回true,校验失败，则抛出异常
	 * @throws 
	 */
	private boolean validateNumCode(String successNumCode, String numCode) {
		if (successNumCode != null && !"".equals(successNumCode)) {
			if (numCode != null && !"".equals(numCode.trim())) {
				if (successNumCode.equals(numCode.trim())) {
					return true;
				} else {
					throw new UnknownAccountException("numCode is not correct！");
				}
			} else {
				throw new UnknownAccountException("numCode must not be null!"); 
			}
		} else {
			throw new UnknownAccountException("successNumCode must not be null!");
		}
	}
	
	/**
	 * 因为发现设置的successUrl没生效，所以追踪源码发现如果SavedRequest对象不为null,则它会覆盖掉我们设置
	 * 的successUrl，所以我们要重写onLoginSuccess方法，在它覆盖掉我们设置的successUrl之前，去除掉
	 * SavedRequest对象,SavedRequest对象的获取方式为：
	 * savedRequest = (SavedRequest) session.getAttribute(SAVED_REQUEST_KEY);
	 * public static final String SAVED_REQUEST_KEY = "shiroSavedRequest";
	 * 解决方案：从session对象中移出shiroSavedRequest
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		if (!StringUtils.isEmpty(getSuccessUrl())) {
			// getSession(false)：如果当前session为null,则返回null,而不是创建一个新的session
			Session session = subject.getSession(false);
			if (session != null) {
				session.removeAttribute("shiroSavedRequest");
			}
		}
		return super.onLoginSuccess(token, subject, request, response);
	}
	
}