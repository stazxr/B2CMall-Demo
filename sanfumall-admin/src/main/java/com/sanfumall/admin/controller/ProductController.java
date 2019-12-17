package com.sanfumall.admin.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.sanfumall.admin.service.AttributeService;
import com.sanfumall.admin.service.CategoryService;
import com.sanfumall.admin.service.ProductService;
import com.sanfumall.admin.service.SkuService;
import com.sanfumall.admin.service.StatusService;
import com.sanfumall.admin.service.UserService;
import com.sanfumall.admin.service.ValueService;
import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Category;
import com.sanfumall.common.pojo.entity.Product;
import com.sanfumall.common.pojo.entity.SKU;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.entity.User;
import com.sanfumall.common.pojo.entity.Value;
import com.sanfumall.common.pojo.vo.Page;
import com.sanfumall.common.util.ConstantUtil;

@Controller(value="productController")
@RequestMapping("/product")
public class ProductController extends BaseController {

	@Resource(name="categoryService")
	private CategoryService categoryService;
	@Resource(name="statusService")
	private StatusService statusService;
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="productService")
	private ProductService productService;
	@Resource(name="valueService")
	private ValueService valueService;
	@Resource(name="attributeService")
	private AttributeService attributeService;
	@Resource(name="skuService")
	private SkuService skuService;
	
	/**
	 * 分页展示商品列表
	 * @param pageNum 页码
	 * @param pageSize 每页显示的条数 
	 * @param keyword 模糊查询的参数，如果为null或空字符串,则进行普通分页查询，否则进行模糊分页查询
	 * @return product_index.html
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView getCategoryIndex(Integer pageNum, Integer pageSize, String keyword) throws Exception {
		// 首先，创建分页对象
		Page<Product> page = new Page<>(pageNum, pageSize, keyword);
		page = productService.getProductListByPage(page);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("page", page);
		return new ModelAndView("product/product_index", resultMap);
	}
	 
	/**
	 * 加载商品添加页面
	 * @return product_add.html
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public ModelAndView getAddPage() throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		List<Category> firstCategoryList = categoryService.getFirstCategoryList();
		// 遍历一级类别集合，如果该类别没有二级类别，则从集合中移除
		for (int i = 0; i < firstCategoryList.size(); i++) {
			if (firstCategoryList.get(i).getCategoryList().size() == 0) {
				firstCategoryList.remove(i);
			}
		}
		List<Category> secondCategoryList = categoryService.getSecondCategoryListByCategoryId(firstCategoryList.get(0).getCategoryId());
		resultMap.put("firstCategoryList", firstCategoryList);
		resultMap.put("secondCategoryList", secondCategoryList);
		return new ModelAndView("product/product_add", resultMap);
	}
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String removeUploader() throws Exception {
		return "product/product_add";
	}
	
	/**
	 * 进行商品添加操作
	 * @param product 商品信息
	 * @param categoryId 商品的类别
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public Boolean addProduct(Product product, Long categoryId) throws Exception {
		if (checkProduct(product)) {
			if (categoryId != null && categoryId > 0) {
				// 绑定商品类别
				Category category = categoryService.getCategoryById(categoryId);
				product.setCategory(category);
				// 获取上架状态
				Status status = statusService.getStatusByStatusCode(ConstantUtil.STATUS_PUTAWAY);
				product.setStatus(status);
				// 获取当前登录用户,记录创建者
				User createUser = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
				product.setCreateUser(createUser);
				product.setCreateTime(new Date());
				// 先保存商品基本信息，然后添加商品SKU
				productService.savaProduct(product);
				// 将每个SKU放到SKUList中，并绑定到商品中
				List<SKU> skuList = new ArrayList<SKU>();
				// 添加SKU信息，首先获取要添加的SKU信息
				String[] aryPrice = request.getParameterValues("price");
				String[] aryStore = request.getParameterValues("store");
				String[] aryPoint = request.getParameterValues("point");
				String[] aryDiscount = request.getParameterValues("discount");
				// 首先判断是否添加SKU信息
				if (aryPrice != null) {
					// 添加了SKU信息，判断SKU是否包含属性值
					String[] aryValue = request.getParameterValues("value");
					if (aryValue != null) {
						// 包含属性值（多条SKU）
						// 存放属性值ID
						List<Long> valueIdList = new ArrayList<Long>();
						// 将aryValue转化为valueIdList
						for (String valueId : aryValue) {
							valueIdList.add(Long.valueOf(valueId));
						}
						// 获取SKU的数量
						int skuLen = aryPrice.length;
						// 判断属性的数量
						int attrLen = 0;
						if (valueIdList.size() == skuLen * 2) {
							// 说明有两个属性
							attrLen = 2;
						} else {
							// 说明只有一个属性
							attrLen = 1;
						}
						// 循环获取SKU对象
						for (int i = 0; i < skuLen; i++) {
							SKU sku = new SKU();
							List<Value> valueList = new ArrayList<Value>();
							for (int j = 0, pc = 0; j < valueIdList.size(); pc++) {
								Value value = valueService.getValueById(Long.valueOf(valueIdList.get(j)));
								valueList.add(value);
								valueIdList.remove(0);
								if (pc == attrLen - 1) {
									sku.setValueList(valueList);
									break;
								}
							}
							sku.setProduct(product);
							sku.setPrice(BigDecimal.valueOf(Double.parseDouble(aryPrice[i])));
							sku.setStore(Long.valueOf(aryStore[i]));
							sku.setPoint(Long.valueOf(aryPoint[i]));
							sku.setDiscount(BigDecimal.valueOf(Double.parseDouble(aryDiscount[i])));
							skuService.savaSku(sku);
							skuList.add(sku);
						}
					} else {
						// 不包含属性值（只有一条SKU）
						SKU sku = new SKU();
						sku.setProduct(product);
						sku.setPrice(BigDecimal.valueOf(Double.parseDouble(aryPrice[0])));
						sku.setStore(Long.valueOf(aryStore[0]));
						sku.setPoint(Long.valueOf(aryPoint[0]));
						sku.setDiscount(BigDecimal.valueOf(Double.parseDouble(aryDiscount[0])));
						skuService.savaSku(sku);
						skuList.add(sku);
					}
					product.setSkuList(skuList);
					return productService.savaProduct(product);
				}
			}
		}
		return false;
	}
	
	/**
	 * 加载商品修改页面
	 * @return product_update.html
	 * @throws Exception
	 */
	@RequestMapping(value="/update", method=RequestMethod.GET)
	public ModelAndView getUpdatePage(Long productId) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		// 根据productId获取product对象
		Product product = productService.getproductById(productId);
		resultMap.put("product", product);
		return new ModelAndView("product/product_update", resultMap);
	}
	
	/**
	 * 进行商品修改操作
	 * @param product 商品信息
	 * @param categoryId 商品的类别
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean updateProduct(Product product) throws Exception {
		if (checkProduct(product)) {
			// 获取原来的商品信息
			Product origPriduct = productService.getproductById(product.getProductId());
			// 绑定未修改的信息
			product.setProductNo(origPriduct.getProductNo());
			product.setStatus(origPriduct.getStatus());
			product.setCreateUser(origPriduct.getCreateUser());
			product.setCreateTime(origPriduct.getCreateTime());
			product.setCategory(origPriduct.getCategory());
			// 获取当前登录用户,记录修改者
			User updateUser = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
			product.setUpdateUser(updateUser);
			product.setUpdateTime(new Date());
			// 将每个SKU放到SKUList中，并绑定到商品中
			List<SKU> skuList = new ArrayList<SKU>();
			// 添加SKU信息，首先获取要添加的SKU信息
			String[] arySkuId = request.getParameterValues("skuId");
			String[] aryPrice = request.getParameterValues("price");
			String[] aryStore = request.getParameterValues("store");
			String[] aryPoint = request.getParameterValues("point");
			String[] aryDiscount = request.getParameterValues("discount");
			// 首先判断是否添加SKU信息
			if (arySkuId != null) {
				// 添加了SKU信息，判断SKU是否包含属性值
				String[] aryValue = request.getParameterValues("value");
				if (aryValue != null) {
					// 包含属性值（多条SKU）
					// 存放属性值ID
					List<Long> valueIdList = new ArrayList<Long>();
					// 将aryValue转化为valueIdList
					for (String valueId : aryValue) {
						valueIdList.add(Long.valueOf(valueId));
					}
					// 获取SKU的数量
					int skuLen = arySkuId.length;
					// 判断属性的数量
					int attrLen = 0;
					if (valueIdList.size() == skuLen * 2) {
						// 说明有两个属性
						attrLen = 2;
					} else {
						// 说明只有一个属性
						attrLen = 1;
					}
					// 循环获取SKU对象
					for (int i = 0; i < skuLen; i++) {
						SKU sku = new SKU();
						List<Value> valueList = new ArrayList<Value>();
						for (int j = 0, pc = 0; j < valueIdList.size(); pc++) {
							Value value = valueService.getValueById(Long.valueOf(valueIdList.get(j)));
							valueList.add(value);
							valueIdList.remove(0);
							if (pc == attrLen - 1) {
								sku.setValueList(valueList);
								break;
							}
						}
						sku.setProduct(product);
						sku.setSkuId(Long.valueOf(arySkuId[i]));
						sku.setPrice(BigDecimal.valueOf(Double.parseDouble(aryPrice[i])));
						sku.setStore(Long.valueOf(aryStore[i]));
						sku.setPoint(Long.valueOf(aryPoint[i]));
						sku.setDiscount(BigDecimal.valueOf(Double.parseDouble(aryDiscount[i])));
						skuService.savaSku(sku);
						skuList.add(sku);
					}
				} else {
					// 不包含属性值（只有一条SKU）
					SKU sku = new SKU();
					sku.setProduct(product);
					sku.setSkuId(Long.valueOf(arySkuId[0]));
					sku.setPrice(BigDecimal.valueOf(Double.parseDouble(aryPrice[0])));
					sku.setStore(Long.valueOf(aryStore[0]));
					sku.setPoint(Long.valueOf(aryPoint[0]));
					sku.setDiscount(BigDecimal.valueOf(Double.parseDouble(aryDiscount[0])));
					skuService.savaSku(sku);
					skuList.add(sku);
				}
				product.setSkuList(skuList);
				return productService.savaProduct(product);
			}
		}
		return false;
	}

	/**
	 * 加载商品详情页面
	 * @return product_detail.html
	 * @throws Exception
	 */
	@RequestMapping(value="/detail", method=RequestMethod.GET)
	public ModelAndView getDetailPage(Long productId) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		Product product = productService.getproductById(productId);
		resultMap.put("product", product);
		return new ModelAndView("product/product_detail", resultMap);
	}
	
	/**
	 * 进行商品状态的修改
	 * @param productId
	 * @param statusCode
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/changeStatus/{productId}/{statusCode}", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean changeStatus(@PathVariable("productId")Long productId, @PathVariable("statusCode")String statusCode) throws Exception {
		if (statusCode != null && !"".equals(statusCode) && productId != null) {
			// 根据productId获取商品对象
			Product product = productService.getproductById(productId);
			// 根据状态编码获取状态对象,并保存带属性对象中
			Status status = statusService.getStatusByStatusCode(statusCode);
			product.setStatus(status);
			// 获得当前登录用户,记录修改者,修改时间
			User updateUser = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
			product.setUpdateUser(updateUser);
			product.setUpdateTime(new Date());
			// 调用业务层保存属性
			return productService.savaProduct(product);
		}
		return false;
	}
	
	/**
	 * 检测商品名称是否重复
	 * @param productId
	 * @param productName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/remoteValidateProductInfo", method=RequestMethod.GET)
	@ResponseBody
	public Boolean remoteValidateProductInfo(Long productId, String productName) throws Exception {
		if (productName != null && !"".equals(productName.trim())) {
			Product product = productService.getproductByName(productName);
			if (product == null || (productId != null && productId == product.getProductId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测商品内容的合法性
	 * @param product
	 * @return boolean
	 */
	private boolean checkProduct(Product product) throws Exception {
		String productName = product.getProductName();
		if (productName == null || "".equals(productName)) {
			Product findProduct = productService.getproductByName(productName);
			if (findProduct == null || (product.getProductId() != null && product.getProductId() == findProduct.getProductId())) {
				return true;
			}
			return false;
		}
		return true;
	}
	
}