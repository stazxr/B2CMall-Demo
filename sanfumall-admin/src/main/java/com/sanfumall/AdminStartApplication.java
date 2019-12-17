package com.sanfumall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 魔晶商城启动类
 * @author SunTao
 * @since 2018-12-12
 * @version v2.0
 */
@ComponentScan
@EnableAutoConfiguration
public class AdminStartApplication {
	public static void main(String[] args) {
		SpringApplication.run(AdminStartApplication.class, args);
	}
}

