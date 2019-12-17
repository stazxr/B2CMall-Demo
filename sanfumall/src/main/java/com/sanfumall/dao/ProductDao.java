package com.sanfumall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Product;

public interface ProductDao extends BaseDao<Product, Long> {

	/**
	 * 根据排序字段获取商品列表
	 * @param statusCode
	 * @return List<Product>
	 * @throws Exception
	 */
	@Query("from Product p where p.status.statusCode=:statusCode order by p.sortOrder asc")
	public List<Product> findProductListByOrder(@Param("statusCode")String statusCode) throws Exception;

	/**
	 * 根据上架时间获取商品列表
	 * @param statusCode
	 * @return List<Product>
	 * @throws Exception
	 */
	@Query("from Product p where p.status.statusCode=:statusCode order by p.createTime desc")
	public List<Product> findNewProductList(@Param("statusCode")String statusCode) throws Exception;

	/**
	 * 获取类别相关的商品
	 * @param categoryId
	 * @return List<Product>
	 * @throws Exception
	 */
	@Query("from Product p where p.status.statusCode=:statusCode and p.category.categoryId=:categoryId order by p.putawayTime desc")
	public List<Product> findProductListByCategory(@Param("statusCode")String statusCode, @Param("categoryId")Long categoryId) throws Exception;

}