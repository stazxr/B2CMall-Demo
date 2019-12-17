package com.sanfumall.admin.service;

import com.sanfumall.common.pojo.entity.SKU;

public interface SkuService {

	/**
	 * 保存或修改SKU
	 * @param sku
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaSku(SKU sku) throws Exception;

}
