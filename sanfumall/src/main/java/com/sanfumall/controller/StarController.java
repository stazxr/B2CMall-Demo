package com.sanfumall.controller;

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
import com.sanfumall.common.pojo.entity.Member;
import com.sanfumall.common.pojo.entity.Product;
import com.sanfumall.common.pojo.entity.Star;
import com.sanfumall.service.MemberService;
import com.sanfumall.service.ProductService;
import com.sanfumall.service.StarService;

@Controller(value="starController")
@RequestMapping("/star")
public class StarController extends BaseController {
	
	@Resource(name="starService")
	private StarService starService;
	@Resource(name="memberService")
	private MemberService memberService;
	@Resource(name="productService")
	private ProductService productService;
	
	/**
	 * 获取收藏列表
	 * @return star_index.html
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView getIndex() throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		Long memberId = ((Member) session.getAttribute("member")).getMemberId();
		List<Star> starList = starService.getListByMember(memberId);
		resultMap.put("starList", starList);
		return new ModelAndView("star/star_index", resultMap);
	}
	
	/**
	 * 添加收藏
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/add/{productId}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean addAddress(@PathVariable("productId")Long productId) throws Exception {
		if (productId != null) {
			// 创建star对象
			Star star = new Star();
			// 获取商品对象
			Product product = productService.getProductById(productId);
			star.setProduct(product);
			// 获取当前登录对象
			Member member = memberService.getMemberById(((Member) session.getAttribute("member")).getMemberId());
			star.setMember(member);
			// 获取当前时间
			star.setCreateTime(new Date());
			// 保存收货地址信息
			return starService.savaStar(star);
		}
		return false;
	}
	
	/**
	 * 删除收藏
	 * @param addressId
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteStar/{productId}", method=RequestMethod.DELETE)
	@ResponseBody
	public Boolean deleteStar(@PathVariable("productId")Long productId) throws Exception {
		if (productId != null) {
			// 根据productId与当前登录对象获取Star对象
			Long memberId = ((Member) session.getAttribute("member")).getMemberId();
			Star star = starService.getStarByProductAndMember(productId, memberId);
			if (star != null) {
				return starService.deleteStar(star);
			}
		}
		return false;
	}
	
}