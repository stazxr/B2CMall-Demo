package com.sanfumall.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.admin.dao.PaymentDao;
import com.sanfumall.admin.dao.StatusDao;
import com.sanfumall.admin.service.PaymentService;
import com.sanfumall.common.pojo.entity.Payment;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.vo.Page;

@Service(value="paymentService")
@Transactional
public class PaymentServiceImpl implements PaymentService {
	
	@Resource(name="paymentDao")
	private PaymentDao paymentDao;
	@Resource(name="statusDao")
	private StatusDao statusDao;

	/**
	 * 分页获取支付类型列表
	 * @param page
	 * @return Page<Payment>
	 * @throws Exception
	 */
	public Page<Payment> getPaymentListByPage(Page<Payment> page) throws Exception {
		// 根据page携带的分页信息获取分页查询limit的两个参数startIndex,pageSize 
		Integer pageSize = page.getPageSize();
		Integer startIndex = (page.getPageNum() - 1) * pageSize;
		// 首先判断是否为模糊查询,如果keyword为空或空字符串，则为普通查询，否则为模糊查询
		String keyword = page.getKeyword();
		if (keyword != null && !"".equals(keyword)) {
			// 进行分页模糊查询
			// 首先，判断是否是按状态名称查询，如果是按照状态名称查询，则将状态Id赋值给keyword
			Status status = statusDao.findByStatusName(keyword);
			if (status != null) {
				keyword = Long.toString(status.getStatusId());
			}
			// 然后，进行分页模糊查询， 并将查询的结果set到page对象中
			List<Payment> paymentList = paymentDao.findpaymentListByLikePage(startIndex, pageSize, keyword);
			page.setList(paymentList);
			// 获取在不分页的情况下进行模糊查询的总数量，总页数，并set到page对象中
			Integer totalSize = paymentDao.findpaymentListByLike(keyword).size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		} else {
			// 进行普通的分页查询
			// 获取分页信息，并set到page对象中
			List<Payment> paymentList = paymentDao.findpaymentListByPage(startIndex, pageSize);
			page.setList(paymentList);
			// 获取总页数与总数量
			Integer totalSize = paymentDao.findAll().size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		}
		return page;
	}

	/**
	 * 添加支付类型
	 * @param payment
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaPayment(Payment payment) throws Exception {
		try {
			paymentDao.save(payment);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据paymentId获取Payment对象
	 * @param paymentId
	 * @return Payment对象
	 * @throws Exception
	 */
	public Payment getPaymentByPaymentId(Long paymentId) throws Exception {
		return paymentDao.getOne(paymentId);
	}

	/**
	 * 根据paymentName获取Payment对象
	 * @param paymentName
	 * @return Payment对象
	 * @throws Exception
	 */
	public Payment getPaymentByPaymentName(String paymentName) throws Exception {
		return paymentDao.findByPaymentName(paymentName);
	}

}