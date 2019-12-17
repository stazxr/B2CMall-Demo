package com.sanfumall.admin.service;

import com.sanfumall.common.pojo.entity.Product;
import com.sanfumall.common.pojo.vo.Page;

public interface ProductService {

	/**
	 * 根据商品名称获取商品
	 * @param productName
	 * @return Product
	 * @throws Exception
	 */
	public Product getproductByName(String productName) throws Exception;

	/**
	 * 根据商品ID获取商品
	 * @param productId
	 * @return Product
	 * @throws Exception
	 */
	public Product getproductById(Long productId) throws Exception;
	
	/**
	 * 获取商品信息列表
	 * @param page
	 * @return Page<Product>
	 * @throws Exception
	 */
	public Page<Product> getProductListByPage(Page<Product> page) throws Exception;

	/**
	 * 保存或修改商品信息
	 * @param product
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaProduct(Product product) throws Exception;

}