package com.sanfumall.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.common.pojo.entity.Product;
import com.sanfumall.common.util.ConstantUtil;
import com.sanfumall.dao.ProductDao;
import com.sanfumall.service.ProductService;

@Service(value="productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Resource(name="productDao")
	private ProductDao productDao;
	
	/**
	 * 根据排序字段获取商品列表
	 * @return List<Product>
	 * @throws Exception
	 */
	public List<Product> getProductListByOrder() throws Exception {
		return productDao.findProductListByOrder(ConstantUtil.STATUS_PUTAWAY);
	}

	/**
	 * 获取新品上架商品列表
	 * @return List<Product>
	 * @throws Exception
	 */
	public List<Product> getNewProductList() throws Exception {
		return productDao.findNewProductList(ConstantUtil.STATUS_PUTAWAY);
	}

	/**
	 * 根据productId获取Product对象
	 * @param productId
	 * @return Product对象
	 */
	public Product getProductById(Long productId) throws Exception {
		return productDao.getOne(productId);
	}

	/**
	 * 获取类别相关的商品
	 * @param categoryId
	 * @return List<Product>
	 * @throws Exception
	 */
	public List<Product> getProductListByCategory(Long categoryId) throws Exception {
		return productDao.findProductListByCategory(ConstantUtil.STATUS_PUTAWAY, categoryId);
	}

}