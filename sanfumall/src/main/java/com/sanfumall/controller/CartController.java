package com.sanfumall.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Cart;
import com.sanfumall.common.pojo.entity.Member;
import com.sanfumall.common.pojo.entity.SKU;
import com.sanfumall.service.CartService;
import com.sanfumall.service.MemberService;
import com.sanfumall.service.SkuService;

@Controller(value="cartController")
@RequestMapping("/cart")
public class CartController extends BaseController {
	
	@Resource(name="cartService")
	private CartService cartService;
	@Resource(name="memberService")
	private MemberService memberService;
	@Resource(name="skuService")
	private SkuService skuService;
	
	/**
	 * 获取购物车列表
	 * @return cart_index.html
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView getRoleIndex(Long memberId) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		List<Cart> cartList = cartService.getCartListByMember(memberId);
		for (Cart cart : cartList) {
			SKU sku = cart.getSku();
			sku.setCurrentPrice(sku.getPrice().multiply(sku.getDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		resultMap.put("cartList", cartList);
		return new ModelAndView("cart/cart_index", resultMap);
	}
	
	/**
	 * 添加购物车
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/add/{skuId}/{count}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean addCart(@PathVariable("skuId")Long skuId, @PathVariable("count")Long count) throws Exception {
		if (skuId != null && count != null && count > 0) {
			// 创建star对象
			Cart cart = new Cart();
			// 获取SKU对象
			SKU sku = skuService.getSkuById(skuId);
			cart.setSku(sku);
			// 获取当前登录对象
			Member member = memberService.getMemberById(((Member) session.getAttribute("member")).getMemberId());
			cart.setMember(member);
			// 获取当前时间
			cart.setCreateTime(new Date());
			// setSKU的数量
			cart.setCount(count);
			// 保存收货地址信息
			return cartService.savaCart(cart);
		}
		return false;
	}
	
	/**
	 * 删除购物车项
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
	@ResponseBody
	public Boolean deleteCart(Long cartId) throws Exception {
		if (cartId != null) {
			Cart cart = cartService.getCartById(cartId);
			return cartService.deleteCart(cart);
		}
		return false;
	}
	
}