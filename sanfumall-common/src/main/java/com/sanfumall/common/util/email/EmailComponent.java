package com.sanfumall.common.util.email;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 发送邮箱验证码
 * @author SunTao
 * @since 2018-12-20
 */
@Component("emailComponent")
public class EmailComponent {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public String sendSimpleMail(String email) throws Exception {
		// 获取验证码
		String emailCode = getEmailCode();
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("stazxr@qq.com");
			message.setTo(email);
			message.setSubject("SanFu");
			String text = "【SanFu商城】您的验证码为" + emailCode + "。";
	        message.setText(text);
	        mailSender.send(message);
	        return emailCode;
		} catch (Exception e) {
			e.printStackTrace();
			return "邮箱程序出错";
		}
	}
	
	/**
	 * 生成邮箱的验证码
	 * @return 邮箱验证码
	 */
	private String getEmailCode() {
		Random random = new Random();
		int min = 100000;
		int max = 999999;
		int num = random.nextInt(max - min) + min;
		return String.valueOf(num);
	}
	
}
