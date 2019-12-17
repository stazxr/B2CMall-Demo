package com.sanfumall.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.common.pojo.entity.Grade;
import com.sanfumall.dao.GradeDao;
import com.sanfumall.service.GradeService;

@Service(value="gradeService")
@Transactional
public class GradeServiceImpl implements GradeService {

	@Resource(name="gradeDao")
	private GradeDao gradeDao;
	
	/**
	 * 获取所有的会员等级列表
	 * @return List<Grade>
	 * @throws Exception
	 */
	public List<Grade> getAllGrade() throws Exception {
		return gradeDao.findAllBySort();
	}
	
}