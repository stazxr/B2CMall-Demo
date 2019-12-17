package com.sanfumall.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Product;

public interface ProductDao extends BaseDao<Product, Long> {

	/**
	 * 根据productName获取Product对象
	 * @param productName
	 * @return Product
	 * @throws Exception
	 */
	public Product findByProductName(String Product) throws Exception;
	
	/**
	 * 分页获取商品列表
	 * @param startIndex
	 * @param pageSize
	 * @return List<Product>
	 * @throws Exception
	 */
	@Query(value="select * from sys_product p order by p.sort_order asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Product> findProductListByPage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize) throws Exception;

	/**
	 * 模糊分页查询
	 * @param startIndex
	 * @param pageSize
	 * @param keyword
	 * @return List<Product>
	 * @throws Exception
	 */
	@Query(value="select * from sys_product p where p.product_name like CONCAT('%',:keyword,'%') or p.status_id=:keyword or p.category_id=:keyword order by p.sort_order asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Product> findProductListByLikePage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize, @Param("keyword")String keyword) throws Exception;

	/**
	 * 模糊查询
	 * @param keyword
	 * @return List<Product> 
	 * @throws Exception
	 */
	@Query(value="select * from sys_product p where p.product_name like CONCAT('%',:keyword,'%') or p.status_id=:keyword or p.category_id=:keyword order by p.sort_order asc", nativeQuery = true)
	public List<Product> findProductListByLike(@Param("keyword")String keyword) throws Exception;

}