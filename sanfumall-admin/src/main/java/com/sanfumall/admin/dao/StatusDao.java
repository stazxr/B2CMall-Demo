package com.sanfumall.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Status;

public interface StatusDao extends BaseDao<Status, Long> {
	
	/**
	 * 根据statusCode获取Status对象
	 * @param statusCode
	 * @return Status
	 * @throws Exception
	 */
	public Status findByStatusCode(String statusCode) throws Exception;
	
	/**
	 * 根据statusName获取Status对象
	 * @param statusName
	 * @return Status
	 * @throws Exception
	 */
	public Status findByStatusName(String statusName) throws Exception;
	
	/**
	 * 分页获取状态列表
	 * @param startIndex 开始的索引
	 * @param pageSize 每页显示的条数
	 * @return List<Status>
	 * @throws Exception
	 */
	@Query(value="select * from sys_status s order by s.sort_order asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Status> findStatusListByPage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize) throws Exception;
	
	/**
	 * 分页模糊查询
	 * 注意：参数顺序   where --> order --> limit
	 * @param startIndex 分页开始的下标
	 * @param pageSize 每页显示的条数
	 * @param keyword 模糊查询的参数(status_name,status_code,status_id)
	 * @return List<Status>
	 */
	@Query(value="select * from sys_status s where s.status_name like CONCAT('%',:keyword,'%') or s.status_id like CONCAT('%',:keyword,'%') or s.status_code like CONCAT('%',:keyword,'%') order by s.sort_order asc limit :startIndex, :pageSize", nativeQuery = true)
	public List<Status> findStatusListByLikePage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize, @Param("keyword")String keyword) throws Exception;
	
	/**
	 * 不分页的情况下进行模糊查询
	 * @param keyword
	 * @return List<Status>
	 * @throws Exception
	 */
	@Query(value="select * from sys_status s where s.status_name like CONCAT('%',:keyword,'%') or s.status_id like CONCAT('%',:keyword,'%') or s.status_code like CONCAT('%',:keyword,'%') order by s.sort_order asc", nativeQuery = true)
	public List<Status> findStatusListByLike(@Param("keyword")String keyword) throws Exception;
	
}