package com.sanfumall.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Grade;

public interface GradeDao extends BaseDao<Grade, Long> {
	
	/**
	 * 根据会员等级名称获取Grade对象
	 * @param gradeName
	 * @return Grade
	 * @throws Exception
	 */
	public Grade findByGradeName(String gradeName) throws Exception;

	/**
	 * 分页获取Grade列表
	 * @param startIndex
	 * @param pageSize
	 * @return List<Grade>
	 * @throws Exception
	 */
	@Query(value="select * from sys_grade g order by g.sort_order asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Grade> findGradeListByPage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize) throws Exception;

	/**
	 * 分页模糊查询
	 * @param startIndex
	 * @param pageSize
	 * @param keyword
	 * @return List<Grade>
	 * @throws Exception
	 */
	@Query(value="select * from sys_grade g where g.grade_name like CONCAT('%',:keyword,'%') order by g.sort_order asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Grade> findGradeListByLikePage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize, @Param("keyword")String keyword) throws Exception;

	/**
	 * 模糊查询
	 * @param keyword
	 * @return List<Grade>
	 * @throws Exception
	 */
	@Query(value="select * from sys_grade g where g.grade_name like CONCAT('%',:keyword,'%') order by g.sort_order asc", nativeQuery = true)
	public List<Grade> findGradeListByLike(@Param("keyword")String keyword) throws Exception;

}