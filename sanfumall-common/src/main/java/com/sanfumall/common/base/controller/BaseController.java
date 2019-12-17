package com.sanfumall.common.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 基础控制类，控制层的类通过继承该类获得HttpServletRequest与HttpSession对象
 * @author SunTao
 * @since 2018-12-12
 */
@Controller("baseController")
public class BaseController {
	@Autowired
	protected HttpServletRequest request;
	
	@Autowired
	protected HttpSession session;
}
