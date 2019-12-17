package com.sanfumall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 魔晶商城启动类
 * @author SunTao
 * @since 2019-1-10
 * @version v1.0
 */
@ComponentScan
@EnableAutoConfiguration
public class StartApplication {
	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
	}
}

