package com.sanfumall.common.util;


/**
 * 编码生成工具类
 * @author SunTao
 * @since 2018-12-19
 */
public class NoGenerateUtil {
	
	// 用户编号前缀
	private static final String USER_NO_PREFIX = "SF";
	
	/**
	 * 根据管理员Id生成管理员编码
	 * @param userId
	 * @return userNo
	 */
	public static String getUserNoByUserId(Long userId) {
		// 将管理员ID转换为字符串
		String id = String.valueOf(userId);
		while (id.length() < 6) {
			id = "0" + id;
		}
		return USER_NO_PREFIX + id;
	}
	
}
