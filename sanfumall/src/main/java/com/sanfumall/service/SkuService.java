package com.sanfumall.service;

import java.util.List;

import com.sanfumall.common.pojo.entity.SKU;

public interface SkuService {

	/**
	 * 根据商品Id获取对应的SKU列表，并根据升序排序
	 * @param productId
	 * @return List<SKU>
	 * @throws Exception
	 */
	public List<SKU> getSkuListByProductIdOrderPriceAsc(Long productId) throws Exception;

	/**
	 * 根据skuId获取SKU对象
	 * @param skuId
	 * @return SKU对象
	 * @throws Exception
	 */
	public SKU getSkuById(Long skuId) throws Exception;

}
