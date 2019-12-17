package com.sanfumall.admin.service.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.admin.dao.SkuDao;
import com.sanfumall.admin.service.SkuService;
import com.sanfumall.common.pojo.entity.SKU;

@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService {

	@Resource(name="skuDao")
	private SkuDao skuDao;
	
	/**
	 * 保存或修改SKU
	 * @param sku
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaSku(SKU sku) throws Exception {
		try {
			skuDao.save(sku);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}