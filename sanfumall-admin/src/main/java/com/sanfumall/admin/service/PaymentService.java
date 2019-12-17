package com.sanfumall.admin.service;

import com.sanfumall.common.pojo.entity.Payment;
import com.sanfumall.common.pojo.vo.Page;

public interface PaymentService {

	/**
	 * 分页获取支付类型列表
	 * @param page
	 * @return Page<Payment>
	 * @throws Exception
	 */
	public Page<Payment> getPaymentListByPage(Page<Payment> page) throws Exception;

	/**
	 * 添加支付类型
	 * @param payment
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaPayment(Payment payment) throws Exception;

	/**
	 * 根据paymentId获取Payment对象
	 * @param paymentId
	 * @return Payment对象
	 * @throws Exception
	 */
	public Payment getPaymentByPaymentId(Long paymentId) throws Exception;

	/**
	 * 根据paymentName获取Payment对象
	 * @param paymentName
	 * @return Payment对象
	 * @throws Exception
	 */
	public Payment getPaymentByPaymentName(String paymentName) throws Exception;

}