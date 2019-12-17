package com.sanfumall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.SKU;

public interface SkuDao extends BaseDao<SKU, Long> {

	/**
	 * 根据商品Id获取对应的SKU列表，并根据升序排序
	 * @param productId
	 * @return List<SKU>
	 * @throws Exception
	 */
	@Query("from SKU s where s.product.productId=:productId order by s.price asc")
	List<SKU> findSkuListByProductIdOrderPriceAsc(@Param("productId")Long productId) throws Exception;

}
