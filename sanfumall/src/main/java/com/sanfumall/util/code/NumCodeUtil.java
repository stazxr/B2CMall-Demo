package com.sanfumall.util.code;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

/**
 * 读取kaptcha.properties配置文件，生成DefaultKaptcha的配置信息
 * @author SunTao
 * @since 2018-12-13
 */
@Component("numCodeUtil")
public class NumCodeUtil {
	
	private static Properties props = new Properties();
	
	@Bean
	public DefaultKaptcha defaultKaptcha() throws Exception {
		// 创建DefaultKaptcha对象
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		// 读取配置文件
		try {
			props.load(NumCodeUtil.class.getClassLoader().getResourceAsStream("kaptcha.properties"));
		}catch (Exception e) {
			e.printStackTrace();
		}
		// 将Properties文件设到DefaultKaptcha对象中
		defaultKaptcha.setConfig(new Config(props));
		return defaultKaptcha;
	}
	
}