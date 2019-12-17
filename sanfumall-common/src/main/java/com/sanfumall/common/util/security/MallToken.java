package com.sanfumall.common.util.security;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 在UsernamePasswordToken上扩展了获取登录时验证码的功能
 * @author SunTao
 * @since 2018-12-13
 */
public class MallToken extends UsernamePasswordToken {
	private static final long serialVersionUID = 1L;
	
	private String numCode;			// 登录时的验证码
	
	public MallToken() {}
	public MallToken(String username, String password, boolean remember,
			String host, String numCode) {
		// 调用父构造器初始化UsernamePasswordToken原始的四个参数
		super(username, password, remember, host);
		// 初始化验证码参数
		this.numCode = numCode;
	}
	
	public String getNumCode() {
		return numCode;
	}
	public void setNumCode(String numCode) {
		this.numCode = numCode;
	}
}
