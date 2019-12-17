package com.sanfumall.dao;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Status;

public interface StatusDao extends BaseDao<Status, Long> {
	
	/**
	 * 根据statusCode获取Status对象
	 * @param statusCode
	 * @return Status
	 * @throws Exception
	 */
	public Status findByStatusCode(String statusCode) throws Exception;
	
}
