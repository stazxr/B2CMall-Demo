package com.sanfumall.common.util;

import java.util.Properties;

/**
 * 系统常量工具类：
 * 包含性别常量、分页常量、状态常量
 * @author SunTao
 * @since 2018-12-12
 */
public class ConstantUtil {
	private static Properties props = new Properties();	
	
	static {
		try {
			// 程序启动时，加载配置文件
			props.load(ConstantUtil.class.getClassLoader().getResourceAsStream("babymall.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 性别：男-M
	 */
	public static final String GENDER_MALE = props.getProperty("system.gender.male");
	/**
	 * 性别：女-F
	 */
	public static final String GENDER_FEMALE = props.getProperty("system.gender.female");
	
	/**
	 * 分页信息：页码-1
	 */
	public static final Integer PAGE_NUM = Integer.parseInt(props.getProperty("system.page.num"));
	/**
	 * 分页信息：每页显示的条数-10
	 */
	public static final Integer PAGE_SIZE = Integer.parseInt(props.getProperty("system.page.size"));
	
	/**
	 * 状态：是否启用
	 */
	public static final String STATUS_ISABLE = props.getProperty("system.status.isable");
	/**
	 * 状态：启用
	 */
	public static final String STATUS_ENABLE = props.getProperty("system.status.enable");
	/**
	 * 状态：禁用
	 */
	public static final String STATUS_DISABLE = props.getProperty("system.status.disable");
	
	/**
	 * 状态：商品状态
	 */
	public static final String STATUS_PRODUCT = props.getProperty("system.status.product");
	/**
	 * 状态：上架
	 */
	public static final String STATUS_PUTAWAY = props.getProperty("system.status.putaway");
	/**
	 * 状态：下架
	 */
	public static final String STATUS_SOLDOUT = props.getProperty("system.status.soldout");
	
	/**
	 * 状态：订单状态
	 */
	public static final String STATUS_ORDER = props.getProperty("system.status.order");
	/**
	 * 状态：已下单，未支付（待付款）
	 */
	public static final String STATUS_CREATE = props.getProperty("system.status.create");
	/**
	 * 状态：已支付，未发货（待发货）
	 */
	public static final String STATUS_PAYMENT = props.getProperty("system.status.payment");
	/**
	 * 状态：已发货，未收货（待收货）
	 */
	public static final String STATUS_SEND = props.getProperty("system.status.send");
	/**
	 * 状态：已收货
	 */
	public static final String STATUS_RECEIVE = props.getProperty("system.status.receive");
	/**
	 * 状态：退货
	 */
	public static final String STATUS_BACK = props.getProperty("system.status.back");
	/**
	 * 状态：退款
	 */
	public static final String STATUS_REFUND = props.getProperty("system.status.refund");

}

