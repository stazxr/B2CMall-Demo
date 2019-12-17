package com.sanfumall.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Attribute;
import com.sanfumall.common.pojo.entity.Category;
import com.sanfumall.common.pojo.entity.Member;
import com.sanfumall.common.pojo.entity.Product;
import com.sanfumall.common.pojo.entity.SKU;
import com.sanfumall.common.pojo.entity.Star;
import com.sanfumall.common.pojo.entity.Value;
import com.sanfumall.service.CategoryService;
import com.sanfumall.service.ProductService;
import com.sanfumall.service.SkuService;
import com.sanfumall.service.StarService;

@Controller(value="productController")
@RequestMapping("/product")
public class ProductController extends BaseController {
	
	@Resource(name="productService")
	private ProductService productService;
	@Resource(name="skuService")
	private SkuService skuService;
	@Resource(name="starService")
	private StarService starService;
	@Resource(name="categoryService")
	private CategoryService categoryService;
	
	@RequestMapping(value="/detail", method=RequestMethod.GET)
	public ModelAndView getDetailPage(Long productId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 根据productId获取product对象
		Product product = productService.getProductById(productId);
		List<SKU> skuList = product.getSkuList();
		BigDecimal[] currentPrice = new BigDecimal[skuList.size()];
		for (int i = 0; i < skuList.size(); i++) {
			SKU sku = skuList.get(i);
			sku.setCurrentPrice(sku.getPrice().multiply(sku.getDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP));
			currentPrice[i] = sku.getCurrentPrice();
		}
		Arrays.sort(currentPrice);
		product.setMinPrice(currentPrice[0]);
		product.setMaxPrice(currentPrice[currentPrice.length - 1]);
		// 绑定商品信息
		resultMap.put("product", product);
		// 获取与sku有关的属性
		List<Attribute> attributeList = new ArrayList<Attribute>();
		for (int i = 0; i < skuList.get(0).getValueList().size(); i++) {
			attributeList.add(skuList.get(0).getValueList().get(i).getAttribute());
		}
		// 获取所有SKU对应的属性值集合
		Set<Value> allSkuValueList = new LinkedHashSet<Value>();
		for (int i = 0; i < skuList.size(); i++) {
			List<Value> valueList = skuList.get(i).getValueList();
			for (int j = 0; j < valueList.size(); j++) {
				allSkuValueList.add(valueList.get(j));
			}
		}
		// 创建一个Map集合用于存放对应的“属性-属性值集合”信息
		Map<String, List<Value>> attrValue = new HashMap<String, List<Value>>();
		// 循环遍历其下的属性值，获取sku拥有的属性值
		for (int i = 0; i < attributeList.size(); i++) {
			// 该属性下对应的所有的属性值集合
			List<Value> allValueList = attributeList.get(i).getValueList();
			// 创建一个集合存放SKU拥有的集合
			List<Value> valueList = new ArrayList<Value>();
			for (int j = 0; j < allValueList.size(); j++) {
				if (allSkuValueList.contains(allValueList.get(j))) {
					valueList.add(allValueList.get(j));
				}
			}
			attrValue.put(attributeList.get(i).getAttributeName(), valueList);
		}
		resultMap.put("attrValue", attrValue);
		// 热销推荐
		List<Product> allAboutProduct = productService.getProductListByCategory(product.getCategory().getCategoryId());
		List<Product> aboutProductList = new ArrayList<Product>();
		if (allAboutProduct.size() < 5) {
			for (int i = 0; i < allAboutProduct.size(); i++) {
				aboutProductList.add(allAboutProduct.get(i));
			}
		} else {
			for (int i = 0; i < 5; i++) {
				aboutProductList.add(allAboutProduct.get(i));
			}
		}
		resultMap.put("aboutProductList", aboutProductList);
		// 商品详情(正则学习！！！)
		String detail = product.getDetail();
		Pattern pattern = Pattern.compile("src=\"");
		Matcher matcher = pattern.matcher(detail);
		detail = matcher.replaceAll("src=\"http://localhost:8080");
		resultMap.put("detail", detail);
		// 判断该商品是否被收藏
		Boolean isStar = false;
		// 判断是否是登录状态
		Member member = (Member) session.getAttribute("member");
		if (member != null) {
			List<Star> starList = starService.getListByMember(member.getMemberId());
			for (Star star : starList) {
				if (star.getProduct().getProductId().equals(product.getProductId())) {
					isStar = true;
					break;
				}
			}
		}
		resultMap.put("isStar", isStar);
		return new ModelAndView("product/product_detail", resultMap);
	}
	
	/**
	 * 动态获取每个SKU对应的属性
	 * @param valueId1
	 * @param valueId2
	 * @param productId
	 * @return List<String>
	 * @throws Exception
	 */
	@RequestMapping(value="/getPriceAndStore", method=RequestMethod.GET)
	@ResponseBody
	public List<String> getSkuDetail(Long valueId1, Long valueId2, Long productId) throws Exception {
		List<SKU> skuList = productService.getProductById(productId).getSkuList();
		if (valueId1 != null && valueId2 == null) {
			for (SKU sku : skuList) {
				List<Value> valueList = sku.getValueList();
				Long[] array = new Long[valueList.size()];
				for (int i = 0; i < valueList.size(); i++) {
					array[i] = valueList.get(i).getValueId();
				}
				for (int i = 0; i < array.length; i++) {
					if (valueId1.equals(array[i])) {
						List<String> price = new ArrayList<String>();
						String msg = "" + sku.getPrice() + 		// 原价
								"," + sku.getPrice().multiply(sku.getDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP) + 	// 折后价
								"," + sku.getStore() + 		// 库存
								"," + sku.getSkuId();		// skuId
						price.add(msg);
						return price;
					}
				}
			}
		} else if (valueId1 != null && valueId2 != null){
			for (SKU sku : skuList) {
				List<Value> valueList = sku.getValueList();
				Long[] array = new Long[valueList.size()];
				for (int i = 0; i < valueList.size(); i++) {
					array[i] = valueList.get(i).getValueId();
				}
				for (int i = 0; i < array.length; i++) {
					if (valueId1.equals(array[i])) {
						for (int j = 0; j < array.length; j++) {
							if (valueId2.equals(array[j])) {
								List<String> price = new ArrayList<String>();
								String msg = "" + sku.getPrice() + 			// 原价
										"," + sku.getPrice().multiply(sku.getDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP) +  // 折后价
										"," + sku.getStore() + 		// 库存
										"," + sku.getSkuId();		// skuId
								price.add(msg);
								return price;
							}
						}
					}
				}
			}
		} else {
			if (skuList.size() == 1) {
				SKU sku = skuList.get(0);
				List<String> price = new ArrayList<String>();
				String msg = "" + sku.getPrice() + 			// 原价
						"," + sku.getPrice().multiply(sku.getDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP) +  // 折后价
						"," + sku.getStore() + 		// 库存
						"," + sku.getSkuId();		// skuId
				price.add(msg);
				return price;
			}
		}
		return null;
	}
	
	/**
	 * 根据类别信息获取商品列表
	 * @param categoryId
	 * @return List<Product>
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	@ResponseBody
	public List<Product> getProductListByCategory(Long categoryId) throws Exception {
		// 创建一个集合用于存放商品列表
		List<Product> productList = new ArrayList<>();
		// 根据类别categoryId获取Category对象
		Category category = categoryService.getCategoryById(categoryId);
		if (category.getParent() == null) {
			// 说明是一级类别,获取其对应的所有的二级类别
			List<Category> categoryList = categoryService.getCategoryListByParentId(categoryId);
			// 循环遍历获取每个二级类别对应的商品列表
			for (int i = 0; i < categoryList.size(); i++) {
				List<Product> subProductList = productService.getProductListByCategory(categoryList.get(i).getCategoryId());
				if (subProductList.size() > 0) {
					for (Product subProduct : subProductList) {
						productList.add(subProduct);
					}
				}
			}
 		} else {
			// 说明是二级菜单，获取改菜单对应的所有的商品
			productList = productService.getProductListByCategory(categoryId);
		}
		return productList;
	}
	
}