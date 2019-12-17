package com.sanfumall.util.code;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.sanfumall.common.base.controller.BaseController;

/**
 * 获取图片验证码
 * @author SunTao
 * @since 2018-12-13
 */
@Controller("CodeController")
@RequestMapping("/code")
public class CodeController extends BaseController {
	@Autowired
	private DefaultKaptcha defaultKaptcha;
	
	@RequestMapping(value="/numCode", method=RequestMethod.GET)
	public void getNumCode(HttpServletResponse response) throws Exception {
		// 创建字节数组用于存放图片信息
		byte[] numCodeImgByte = null;
		// 获得二进制输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 通过DefaultKaptcha获得随机验证码
			String successNumCode = defaultKaptcha.createText();
			// 将生成的验证码存放在session中
			session.setAttribute("successNumCode", successNumCode);
			// 使用生成的验证码字符串，完成图片的生成
			BufferedImage bi = defaultKaptcha.createImage(successNumCode);
			// 将图片写入到流中
			ImageIO.write(bi, "jpg", baos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 使用HttpServletResponse将图片写入到浏览器中
		numCodeImgByte = baos.toByteArray();
		
		// 通过response设定响应请求类型
		// no-store用于防止重要的信息被无意的发布。在请求消息中发送将使得请求和响应消息都不使用缓存。
		response.setHeader("Cache-Control","no-store");
		// no-cache指示请求或响应消息不能缓存
		response.setHeader("Pragma","no-cache");
		/* expires是response的一个属性,它可以设置页面在浏览器的缓存里保存的时间 ,超过设定的时间后就过期 。过期后再次
		 * 浏览该页面就需要重新请求服务器发送页面数据，如果在规定的时间内再次访问次页面 就不需从服务器传送 直接从缓存中读取。
		 * */
		response.setDateHeader("Expires", 0);
		// servlet接受request请求后接受图片形式的响应
		response.setContentType("image/jpeg");
		
		//通过response获得输出流
		ServletOutputStream sos = response.getOutputStream();
		sos.write(numCodeImgByte);
		sos.close();
	} 

}