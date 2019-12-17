package com.sanfumall.admin.service;

import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.vo.Page;

public interface StatusService {
	
	/**
	 * 根据状态编码获取状态对象
	 * @param statusCode
	 * @return Status
	 * @throws Exception
	 */
	public Status getStatusByStatusCode(String statusCode) throws Exception;
	
	/**
	 * 根据分页信息获取状态列表
	 * @param page 分页信息
	 * @param keyword 模糊查询参数，如果为null,则进行普通的查询，否则，进行模糊查询 
	 * @return Page<Status>
	 * @throws Exception
	 */
	public Page<Status> getStatusListByPage(Page<Status> page) throws Exception;

}