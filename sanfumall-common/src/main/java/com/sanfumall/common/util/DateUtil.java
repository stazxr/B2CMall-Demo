package com.sanfumall.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	/**
	 * 将一个字符串转换为日期
	 * @param string
	 * @return
	 */
	public static Date parseStringToDate(String string) {
		//设定SimpleDateFormat对象格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//转化字符串为Date类型
		try {
			return dateFormat.parse(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将一个日期转换为字符串
	 * @param date
	 * @return
	 */
	public static String parseDateToString(Date date) {
		//设定SimpleDateFormat对象格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		//转化字符串为Date类型
		try {
			return dateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据身份证号截取出生日期
	 * @param idCard
	 * @return
	 */
	public static Date parseIdCardToBirthday(String idCard) {
		//从身份证中截取出生日期部分
		StringBuffer sb = new StringBuffer();
		sb.append(idCard.substring(6, 10)).append("-")
			.append(idCard.substring(10, 12)).append("-")
			.append(idCard.substring(12, 14));
		return DateUtil.parseStringToDate(sb.toString());		
	}
	
	public static void main(String[] args) {
		System.out.println(parseDateToString(parseIdCardToBirthday("142621199504103555")));
	}
	
}
