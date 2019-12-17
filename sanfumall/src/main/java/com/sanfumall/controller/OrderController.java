package com.sanfumall.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Address;
import com.sanfumall.common.pojo.entity.Member;
import com.sanfumall.common.pojo.entity.OrderItem;
import com.sanfumall.common.pojo.entity.SKU;
import com.sanfumall.service.AddressService;
import com.sanfumall.service.MemberService;
import com.sanfumall.service.SkuService;

@Controller(value="orderController")
@RequestMapping("/order")
public class OrderController extends BaseController {
	
	@Resource(name="memberService")
	private MemberService memberService;
	@Resource(name="skuService")
	private SkuService skuService;
	@Resource(name="addressService")
	private AddressService addressService;
	
	/**
	 * 加载生成订单页面
	 * @return order_add.html
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public ModelAndView getAddPage() throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		// 绑定SKU信息与对应的数量
		String[] skuIdAry = request.getParameterValues("skuId");
		String[] countAry = request.getParameterValues("count");
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		BigDecimal pay = new BigDecimal(0);
		for (int i= 0; i < skuIdAry.length; i++) {
			// 创建订单明细对象
			OrderItem oi = new OrderItem();
			SKU sku = skuService.getSkuById(Long.valueOf(skuIdAry[i]));
			sku.setCurrentPrice(sku.getPrice().multiply(sku.getDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP));
			BigDecimal currentPrice = sku.getCurrentPrice();
			oi.setSku(sku);
			oi.setCurrentPrice(currentPrice);
			oi.setCount(Long.valueOf(countAry[i]));
			oi.setTotalPrice(currentPrice.multiply(new BigDecimal(oi.getCount())));
			orderItemList.add(oi);
			pay = pay.add(oi.getTotalPrice());
		}
		resultMap.put("orderItemList", orderItemList);
		resultMap.put("pay", pay);
		// 根据memberId获取对应的地址列表
		List<Address> addressList = addressService.getAddressListByMember(((Member) session.getAttribute("member")).getMemberId());
		resultMap.put("addressList", addressList);
		return new ModelAndView("order/order_add", resultMap);
	}
	
}