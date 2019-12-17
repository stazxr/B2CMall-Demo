package com.sanfumall.service;

import java.util.List;

import com.sanfumall.common.pojo.entity.Product;

public interface ProductService {

	/**
	 * 根据排序字段获取商品列表
	 * @return List<Product>
	 * @throws Exception
	 */
	public List<Product> getProductListByOrder() throws Exception;

	/**
	 * 获取新品上架商品列表
	 * @return List<Product>
	 * @throws Exception
	 */
	public List<Product> getNewProductList() throws Exception;

	/**
	 * 根据productId获取Product对象
	 * @param productId
	 * @return Product对象
	 */
	public Product getProductById(Long productId) throws Exception;
	
	/**
	 * 获取类别相关的商品
	 * @param categoryId
	 * @return List<Product>
	 * @throws Exception
	 */
	public List<Product> getProductListByCategory(Long categoryId) throws Exception;

}