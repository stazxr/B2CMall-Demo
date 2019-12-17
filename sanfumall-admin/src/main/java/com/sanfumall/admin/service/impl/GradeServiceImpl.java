package com.sanfumall.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.admin.dao.GradeDao;
import com.sanfumall.admin.service.GradeService;
import com.sanfumall.common.pojo.entity.Grade;
import com.sanfumall.common.pojo.vo.Page;

@Service(value="gradeService")
@Transactional
public class GradeServiceImpl implements GradeService {

	@Resource(name="gradeDao")
	private GradeDao gradeDao;
	
	/**
	 * 分页展示会员等级列表
	 * @param page
	 * @return Page<Grade>
	 * @throws Exception
	 */
	public Page<Grade> getGradeListByPage(Page<Grade> page) throws Exception {
		// 根据page携带的分页信息获取分页查询limit的两个参数startIndex,pageSize 
		Integer pageSize = page.getPageSize();
		Integer startIndex = (page.getPageNum() - 1) * pageSize;
		// 首先判断是否为模糊查询,如果keyword为空或空字符串，则为普通查询，否则为模糊查询
		String keyword = page.getKeyword();
		if (keyword != null && !"".equals(keyword)) {
			// 进行分页模糊查询， 并将查询的结果set到page对象中
			List<Grade> gradeList = gradeDao.findGradeListByLikePage(startIndex, pageSize, keyword);
			page.setList(gradeList);
			// 获取在不分页的情况下进行模糊查询的总数量，总页数，并set到page对象中
			Integer totalSize = gradeDao.findGradeListByLike(keyword).size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		} else {
			// 进行普通的分页查询
			// 获取分页信息，并set到page对象中
			List<Grade> gradeList = gradeDao.findGradeListByPage(startIndex, pageSize);
			page.setList(gradeList);
			// 获取总页数与总数量
			Integer totalSize = gradeDao.findAll().size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		}
		return page;
	}

	/**
	 * 保存或修改会员等级
	 * @param grade
	 * @return Booelan
	 * @throws Exception
	 */
	public Boolean savaGrade(Grade grade) throws Exception {
		try {
			gradeDao.save(grade);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据会员等级Id获取Grade对象
	 * @param gradeId
	 * @return Grade
	 * @throws Exception
	 */
	public Grade getGradeByGradeId(Long gradeId) throws Exception {
		return gradeDao.getOne(gradeId);
	}

	/**
	 * 根据会员等级名称获取会员等级信息
	 * @param gradeName
	 * @return Grade
	 * @throws Exception
	 */
	public Grade getGradeByGradeName(String gradeName) throws Exception {
		return gradeDao.findByGradeName(gradeName);
	}

}
