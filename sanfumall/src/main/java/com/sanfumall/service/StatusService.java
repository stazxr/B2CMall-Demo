package com.sanfumall.service;

import com.sanfumall.common.pojo.entity.Status;

public interface StatusService {

	/**
	 * 根据状态编码获取状态对象
	 * @param statusCode
	 * @return Status
	 * @throws Exception
	 */
	public Status getStatusByStatusCode(String statusCode) throws Exception;
	
}
