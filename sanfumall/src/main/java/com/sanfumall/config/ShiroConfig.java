package com.sanfumall.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sanfumall.common.util.security.MallFormAuthenticationFilter;
import com.sanfumall.util.ShiroDBRealm;

/**
 * shiro安全框架的配置
 * @author SunTao
 * @since 2019-1-10
 */
@Configuration
public class ShiroConfig {
	
	// 创建realm数据源（抽象意义上的dao层），负责程序和安全数据的连接器
	@Bean
	public ShiroDBRealm shiroDBRealm() {
		return new ShiroDBRealm();
	}
	
	// 配置shiro核心组件SecurityManager,realm由它来进行控制,所以需要进行注入realm
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(shiroDBRealm());
		return securityManager;
	}
	
	// 判断分发过滤请求
	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		// 创建ShiroFilterFactoryBean对象
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		// 设定SecurityManager对象
		shiroFilter.setSecurityManager(securityManager);
		// 配置shiro默认登录界面地址（前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据）
		shiroFilter.setLoginUrl("/member/login");
		// 设定成功后的跳转地址
		shiroFilter.setSuccessUrl("/");
		
		//自定义过滤规则
		Map<String, Filter> filterMap = shiroFilter.getFilters();
		//在过滤规则中重新设定组合
		filterMap.put("authc", new MallFormAuthenticationFilter());
		
		// 设置安全过滤规则，需要使用LinkedHashMap有序的map集合
		Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
		// 配置不会被拦截的链接,从上到下依次判断
		// 1、静态资源（css、js、imgs）
		filterChainDefinitionMap.put("/static/**", "anon");
		// 2、首页加载
		filterChainDefinitionMap.put("/index", "anon");
		// 3、验证码请求
		filterChainDefinitionMap.put("/code/numCode", "anon");
		// 4、会员注册页面请求
		filterChainDefinitionMap.put("/member/register", "anon");
		// 5、会员发送邮箱注册码请求
		filterChainDefinitionMap.put("/member/sendEmail", "anon");
		// 6、对验证码是否正确的校验
		filterChainDefinitionMap.put("/member/checkEmailCode/**", "anon");
		// 7、对邮箱的唯一性进行校验
		filterChainDefinitionMap.put("/member/checkEmailRemote", "anon");
		// 8、忘记密码请求
		filterChainDefinitionMap.put("/member/forgetPwd", "anon");
		// 9、忘记密码第一步：查看账号是否存在
		filterChainDefinitionMap.put("/member/checkEmailIsExist", "anon");
		// 10、忘记密码修改密码完成后，对保存密码进行放行
		filterChainDefinitionMap.put("/member/savaNewPwd", "anon");
		// 11、商品详情
		filterChainDefinitionMap.put("/product/detail", "anon");
		// 8、商品详情页获取商品价钱
		filterChainDefinitionMap.put("/product/getPriceAndStore", "anon");
		// 8、商品列表
		filterChainDefinitionMap.put("/product/index/**", "anon");

		// 配置退出过滤器
		filterChainDefinitionMap.put("/member/logout", "logout");
		
		// 其他所有的都需要进行认证
		filterChainDefinitionMap.put("/**", "authc");
		
		shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilter;
	}
	
}