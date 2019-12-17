package com.sanfumall.admin.service;

import com.sanfumall.common.pojo.entity.Grade;
import com.sanfumall.common.pojo.vo.Page;

public interface GradeService {

	/**
	 * 分页展示会员等级列表
	 * @param page
	 * @return Page<Grade>
	 * @throws Exception
	 */
	public Page<Grade> getGradeListByPage(Page<Grade> page) throws Exception;

	/**
	 * 保存或修改会员等级
	 * @param grade
	 * @return Booelan
	 * @throws Exception
	 */
	public Boolean savaGrade(Grade grade) throws Exception;

	/**
	 * 根据会员等级Id获取Grade对象
	 * @param gradeId
	 * @return Grade
	 * @throws Exception
	 */
	public Grade getGradeByGradeId(Long gradeId) throws Exception;

	/**
	 * 根据会员等级名称获取会员等级信息
	 * @param gradeName
	 * @return Grade
	 * @throws Exception
	 */
	public Grade getGradeByGradeName(String gradeName) throws Exception;
	
}