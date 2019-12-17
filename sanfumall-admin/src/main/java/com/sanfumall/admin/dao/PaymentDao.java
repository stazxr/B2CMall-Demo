package com.sanfumall.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Payment;

public interface PaymentDao extends BaseDao<Payment, Long> {

	/**
	 * 普通的分页查询
	 * @param startIndex 开始的索引
	 * @param pageSize 每页显示的条数
	 * @return List<Payment>
	 * @throws Exception
	 */
	@Query(value="select * from sys_payment p order by p.payment_id asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Payment> findpaymentListByPage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize) throws Exception;

	/**
	 * 分页模糊查询
	 * 注意：参数顺序   where --> order --> limit
	 * @param startIndex 分页开始的下标
	 * @param pageSize 每页显示的条数
	 * @param keyword 模糊查询的参数(payment_name,,status_id)
	 * @return List<Payment>
	 */
	@Query(value="select * from sys_payment p where p.payment_name like CONCAT('%',:keyword,'%') or p.status_id=:keyword order by p.payment_id asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Payment> findpaymentListByLikePage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize, @Param("keyword")String keyword) throws Exception;
	
	@Query(value="select * from sys_payment p where p.payment_name like CONCAT('%',:keyword,'%') or p.status_id=:keyword order by p.payment_id asc", nativeQuery = true)
	public List<Payment> findpaymentListByLike(@Param("keyword")String keyword) throws Exception;

	/**
	 * 根据paymentName获取Payment对象
	 * @param paymentName
	 * @return Payment对象
	 * @throws Exception
	 */
	public Payment findByPaymentName(String paymentName) throws Exception;

}