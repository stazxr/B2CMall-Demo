package com.sanfumall.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Member;

public interface MemberDao extends BaseDao<Member, Long> {

	/**
	 * 普通的分页查询
	 * @param startIndex 开始的索引
	 * @param pageSize 每页显示的条数
	 * @return List<Member>
	 * @throws Exception
	 */
	@Query(value="select * from sys_member m order by m.create_time asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Member> findMemberListByPage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize) throws Exception;

	/**
	 * 分页模糊查询
	 * 注意：参数顺序   where --> order --> limit
	 * @param startIndex 分页开始的下标
	 * @param pageSize 每页显示的条数
	 * @param keyword 模糊查询的参数(nickname, cellphone ,email, status_id)
	 * @return List<Member>
	 */
	@Query(value="select * from sys_member m where m.nickname like CONCAT('%',:keyword,'%') or m.cellphone=:keyword  or m.email=:keyword or m.status_id=:keyword order by m.create_time asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Member> findMemberListByLikePage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize, @Param("keyword")String keyword) throws Exception;

	/**
	 * 模糊查询
	 * @param keyword
	 * @return List<Member>
	 * @throws Exception
	 */
	@Query(value="select * from sys_member m where m.nickname like CONCAT('%',:keyword,'%') or m.cellphone=:keyword  or m.email=:keyword or m.status_id=:keyword order by m.create_time asc", nativeQuery = true)
	public List<Member> findMemberListByLike(@Param("keyword")String keyword) throws Exception;

}