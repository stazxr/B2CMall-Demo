package com.sanfumall.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Cart;
import com.sanfumall.common.pojo.entity.Category;
import com.sanfumall.common.pojo.entity.Member;
import com.sanfumall.common.pojo.entity.Product;
import com.sanfumall.common.pojo.entity.SKU;
import com.sanfumall.service.CartService;
import com.sanfumall.service.CategoryService;
import com.sanfumall.service.MemberService;
import com.sanfumall.service.ProductService;
import com.sanfumall.service.SkuService;

@Controller("indexController")
public class IndexController extends BaseController {
	
	@Resource(name="categoryService")
	private CategoryService categoryService;
	@Resource(name="productService")
	private ProductService productService;
	@Resource(name="memberService")
	private MemberService memberService;
	@Resource(name="skuService")
	private SkuService skuService;
	@Resource(name="cartService")
	private CartService cartService;
	
	/**
	 * 未登录的网站首页
	 * @return admin.html
	 * @throws Exception
	 */
	@RequestMapping(value="/index")
	public ModelAndView getAdminIndex() throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 从Session获得当前登陆的对象
		Member member = (Member) session.getAttribute("member");
		if (member != null) {
			member = memberService.getMemberById(member.getMemberId());
			resultMap.put("member", member);
			// mini购物车信息
			List<Cart> cartList = new ArrayList<Cart>();
			for (Cart cart : member.getCartList()) {
				SKU sku = cart.getSku();
				sku.setCurrentPrice(sku.getPrice().multiply(sku.getDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP));
				cart.setSku(sku);
				cartList.add(cart);
			}
			member.setCartList(cartList);
		}
		// 获取类别信息，展示类别列表
		List<Category> firstCategoryList = categoryService.findFirstCategoryList();
		resultMap.put("firstCategoryList", firstCategoryList);
		// 获取轮播图展示的商品信息以及热销产品
		List<Product> carouselProductList = new ArrayList<Product>();
		List<Product> hotProductList = new ArrayList<Product>();
		List<Product> productList = productService.getProductListByOrder();
		Product activeCarouselProduct = productList.get(0);
		for (int i = 0; i < 8; i++) {
			hotProductList.add(productList.get(i));
			if (i == 0) {
				continue;
			}
			carouselProductList.add(productList.get(i));
		}
		// 设置商品的最低价与最高价
		setPrice(hotProductList);
		resultMap.put("activeCarouselProduct", activeCarouselProduct);
		resultMap.put("carouselProductList", carouselProductList);
		resultMap.put("hotProductList", hotProductList);
		// 获取最新上架商品
		List<Product> productListByTime = productService.getNewProductList();
		List<Product> newProductList = new ArrayList<Product>();
		if (productListByTime.size() < 12) {
			for (int i = 0; i < productListByTime.size(); i++) {
				newProductList.add(productListByTime.get(i));
			}
		} else {
			for (int i = 0; i < 12; i++) {
				newProductList.add(productListByTime.get(i));
			}
		}
		// 设置商品的最低价与最高价
		setPrice(newProductList);
		resultMap.put("newProductList", newProductList);
		// 更多推荐商品
		
		// 获取搜索框提示信息
		List<Category> allCategoryList = categoryService.findAllCategoryList();
		StringBuffer autoCompleteword = new StringBuffer();
		for (int i = 0; i < productList.size(); i++) {
			autoCompleteword = autoCompleteword.append(productList.get(i).getProductName() + ",");
		}
		for (int i = productList.size(), j = 0; i < productList.size() + allCategoryList.size(); i++, j++) {
			autoCompleteword = autoCompleteword.append(allCategoryList.get(j).getCategoryName() + ",");
		}
		resultMap.put("autoCompleteword", autoCompleteword.toString());
		return new ModelAndView("index", resultMap);
	}
	
	/**
	 * 会员登录验证成功后加载商城首页
	 * @return admin.html
	 * @throws Exception
	 */
	@RequestMapping(value="/")
	public ModelAndView getIndex() throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 从Session获得当前登陆的对象
		Member member = (Member) session.getAttribute("member");
		resultMap.put("member", member);
		// mini购物车信息
		List<Cart> cartList = new ArrayList<Cart>();
		for (Cart cart : member.getCartList()) {
			SKU sku = cart.getSku();
			sku.setCurrentPrice(sku.getPrice().multiply(sku.getDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP));
			cart.setSku(sku);
			cartList.add(cart);
		}
		member.setCartList(cartList);
		// 获取类别信息，展示类别列表
		List<Category> firstCategoryList = categoryService.findFirstCategoryList();
		resultMap.put("firstCategoryList", firstCategoryList);
		// 获取轮播图展示的商品信息以及热销产品
		List<Product> carouselProductList = new ArrayList<Product>();
		List<Product> hotProductList = new ArrayList<Product>();
		List<Product> productList = productService.getProductListByOrder();
		Product activeCarouselProduct = productList.get(0);
		for (int i = 0; i < 8; i++) {
			hotProductList.add(productList.get(i));
			if (i == 0) {
				continue;
			}
			carouselProductList.add(productList.get(i));
		}
		// 设置商品的最低价与最高价
		setPrice(hotProductList);
		resultMap.put("activeCarouselProduct", activeCarouselProduct);
		resultMap.put("carouselProductList", carouselProductList);
		resultMap.put("hotProductList", hotProductList);
		// 获取最新上架商品
		List<Product> productListByTime = productService.getNewProductList();
		List<Product> newProductList = new ArrayList<Product>();
		if (productListByTime.size() < 12) {
			for (int i = 0; i < productListByTime.size(); i++) {
				newProductList.add(productListByTime.get(i));
			}
		} else {
			for (int i = 0; i < 12; i++) {
				newProductList.add(productListByTime.get(i));
			}
		}
		// 设置商品的最低价与最高价
		setPrice(newProductList);
		resultMap.put("newProductList", newProductList);
		// 更多推荐商品
		
		// 获取搜索框提示信息
		List<Category> allCategoryList = categoryService.findAllCategoryList();
		StringBuffer autoCompleteword = new StringBuffer();
		for (int i = 0; i < productList.size(); i++) {
			autoCompleteword = autoCompleteword.append(productList.get(i).getProductName() + ",");
		}
		for (int i = productList.size(), j = 0; i < productList.size() + allCategoryList.size(); i++, j++) {
			autoCompleteword = autoCompleteword.append(allCategoryList.get(j).getCategoryName() + ",");
		}
		resultMap.put("autoCompleteword", autoCompleteword.toString());
		return new ModelAndView("index", resultMap);
	}
	
	/**
	 * 设置商品的最低价与最高价
	 * @param hotProductList
	 * @throws Exception 
	 */
	private void setPrice(List<Product> productList) throws Exception {
		for (int i = 0; i < productList.size(); i++) {
			Product product = productList.get(i);
			List<SKU> skuList = skuService.getSkuListByProductIdOrderPriceAsc(product.getProductId());
			BigDecimal[] currentPrice = new BigDecimal[skuList.size()];
			for (int j = 0; j < skuList.size(); j++) {
				SKU sku = skuList.get(j);
				sku.setCurrentPrice(sku.getPrice().multiply(sku.getDiscount()));
				currentPrice[j] = sku.getCurrentPrice();
			}
			Arrays.sort(currentPrice);
			product.setMinPrice(currentPrice[0].setScale(2, BigDecimal.ROUND_HALF_UP));
			product.setMaxPrice(currentPrice[currentPrice.length - 1].setScale(2, BigDecimal.ROUND_HALF_UP));
		}
	}
	
}