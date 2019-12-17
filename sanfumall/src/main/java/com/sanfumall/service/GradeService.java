package com.sanfumall.service;

import java.util.List;

import com.sanfumall.common.pojo.entity.Grade;

public interface GradeService {

	/**
	 * 获取所有的会员等级列表
	 * @return List<Grade>
	 * @throws Exception
	 */
	public List<Grade> getAllGrade() throws Exception;

}
