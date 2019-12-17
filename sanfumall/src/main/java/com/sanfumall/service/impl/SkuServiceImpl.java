package com.sanfumall.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.common.pojo.entity.SKU;
import com.sanfumall.dao.SkuDao;
import com.sanfumall.service.SkuService;

@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService {

	@Resource(name="skuDao")
	private SkuDao skuDao;
	
	/**
	 * 根据商品Id获取对应的SKU列表，并根据升序排序
	 * @param productId
	 * @return List<SKU>
	 * @throws Exception
	 */
	public List<SKU> getSkuListByProductIdOrderPriceAsc(Long productId) throws Exception {
		return skuDao.findSkuListByProductIdOrderPriceAsc(productId);
	}

	/**
	 * 根据skuId获取SKU对象
	 * @param skuId
	 * @return SKU对象
	 * @throws Exception
	 */
	public SKU getSkuById(Long skuId) throws Exception {
		return skuDao.getOne(skuId);
	}

}