package com.sanfumall.service.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.dao.StatusDao;
import com.sanfumall.service.StatusService;

@Service(value="statusService")
@Transactional
public class StatusServiceImpl implements StatusService {
	
	@Resource(name="statusDao")
	private StatusDao statusDao;
	
	/**
	 * 根据状态编码获取状态对象
	 * @param statusCode
	 * @return Status
	 * @throws Exception
	 */
	public Status getStatusByStatusCode(String statusCode) throws Exception {
		return statusDao.findByStatusCode(statusCode);
	}
	
}
