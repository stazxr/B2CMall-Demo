package com.sanfumall.admin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanfumall.admin.service.PaymentService;
import com.sanfumall.admin.service.StatusService;
import com.sanfumall.admin.service.UserService;
import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Payment;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.entity.User;
import com.sanfumall.common.pojo.vo.Page;
import com.sanfumall.common.util.ConstantUtil;

@Controller("paymentController")
@RequestMapping("/payment")
public class PaymentController extends BaseController {
	
	@Resource(name="paymentService")
	private PaymentService paymentService;
	@Resource(name="statusService")
	private StatusService statusService;
	@Resource(name="userService")
	private UserService userService;
	
	/**
	 * 分页展示支付类型列表
	 * @param pageNum 页码
	 * @param pageSize 每页显示的条数
	 * @param keyword 模糊查询的参数，如果为null,则进行普通分页查询，否则进行模糊分页查询
	 * @return payment_index.html
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView getPaymentIndex(Integer pageNum, Integer pageSize, String keyword) throws Exception {
		// 首先，创建分页对象
		Page<Payment> page = new Page<>(pageNum, pageSize, keyword);
		page = paymentService.getPaymentListByPage(page);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("page", page);
		return new ModelAndView("payment/payment_index", resultMap);
	}
	
	/**
	 * 加载支付类型添加页面
	 * @return payment_add.html
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String getAddPage() throws Exception {
		return "payment/payment_add";
	}
	// 加载页面时，uploadify.js会发出一个截取的请求重新加载页面？
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String getAddIcon() throws Exception {
		return "payment/payment_add";
	}
	
	/**
	 * 添加支付类型
	 * @param payment
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public Boolean addPayment(Payment payment) throws Exception {
		// 校验Payment对象是否合法（前端校验有可能失效）
		if (checkPayment(payment)) {
			// 校验成功，获取当前登录用户，记录创建者
			User user = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
			payment.setCreateUser(user);
			payment.setCreateTime(new Date());
			// 获取启用状态对象，并保存在payment中
			payment.setStatus(statusService.getStatusByStatusCode(ConstantUtil.STATUS_ENABLE));
			// 调用业务层保存角色
			return paymentService.savaPayment(payment);
		}
		return false;
	}

	/**
	 * 加载详情页面
	 * @param paymentId
	 * @return payment_detail.html
	 * @throws Exception
	 */
	@RequestMapping(value="/detail/{paymentId}", method=RequestMethod.GET)
	public ModelAndView getDetailPage(@PathVariable("paymentId")Long paymentId) throws Exception {
		Payment payment = paymentService.getPaymentByPaymentId(paymentId);
		return new ModelAndView("payment/payment_detail", "payment", payment);
	}
	
	/**
	 * 加载修改页面
	 * @param paymentId
	 * @return payment_update.html
	 * @throws Exception
	 */
	@RequestMapping(value="/update", method=RequestMethod.GET)
	public ModelAndView getUpdatePage(Long paymentId) throws Exception {
		Payment payment = paymentService.getPaymentByPaymentId(paymentId);
		return new ModelAndView("payment/payment_update", "payment", payment);
	}
	
	/**
	 * 修改Payment对象
	 * @param payment
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean updatePayment(Payment payment) throws Exception {
		// 校验Payment对象是否合法（前端校验有可能失效）
		if (checkPayment(payment)) {
			// 根据paymentId获得数据库中原有信息，并将未修改的信息进行回填
			Payment origPayment = paymentService.getPaymentByPaymentId(payment.getPaymentId());
			// 将排序字段,状态,创建人,创建时间回填
			payment.setStatus(origPayment.getStatus());
			payment.setCreateUser(origPayment.getCreateUser());
			payment.setCreateTime(origPayment.getCreateTime());
			// 记录修改者，修改时间
			User user = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
			payment.setUpdateUser(user);
			payment.setUpdateTime(new Date());
			// 调用业务层保存Payment对象
			return paymentService.savaPayment(payment);
			}
		return false;
	}
	
	/**
	 * 修改状态
	 * @param paymentId 修改角色的ID
	 * @param statusCode 欲修改的状态编码
	 * @return boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/changeStatus/{paymentId}/{statusCode}", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean changeStatus(@PathVariable("paymentId")Long paymentId, @PathVariable("statusCode")String statusCode) throws Exception {
		if (statusCode != null && !"".equals(statusCode) && paymentId != null) {
			// 根据paymentId获取Payment对象
			Payment payment = paymentService.getPaymentByPaymentId(paymentId);
			// 根据状态编码获取状态对象,并保存带Payment对象中
			Status status = statusService.getStatusByStatusCode(statusCode);
			payment.setStatus(status);
			// 调用业务层保存Payment
			return paymentService.savaPayment(payment);
		}
		return false;
	}
	
	/**
	 * 在添加或者修改过程中对支付类型名称进行唯一性校验
	 * @param paymentName 
	 * @param paymentId 如果是修改，则存在ID
	 * @return Boolean 如果违反了唯一性规则，则返回false,否则,返回true
	 * @throws Exception
	 */
	@RequestMapping(value="/remoteValidatePaymentInfo", method=RequestMethod.GET)
	@ResponseBody
	public Boolean remoteValidatePaymentInfo(Long paymentId, String paymentName) throws Exception {
		// 非空校验
		if (paymentName != null && !"".equals(paymentName.trim())) {
			// 根据paymentName获取Payment对象
			Payment payment = paymentService.getPaymentByPaymentName(paymentName);
			if (payment == null || (paymentId != null && payment.getPaymentId() == paymentId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 校验Payment对象
	 * @param payment
	 * @return Boolean
	 */
	private boolean checkPayment(Payment payment) {
		String paymentName = payment.getPaymentName();
		if (paymentName != null && !"".equals(paymentName.trim())) {
			return true;
		}
		return false;
	}
	
}