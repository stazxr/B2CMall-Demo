package com.sanfumall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Grade;

public interface GradeDao extends BaseDao<Grade, Long> {

	/**
	 * 根据排序字段升序获取会员等级列表
	 * @return List<Grade>
	 * @throws Exception
	 */
	@Query("from Grade g order by g.sortOrder asc")
	public List<Grade> findAllBySort() throws Exception;

}
