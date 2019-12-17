package com.sanfumall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Address;

public interface AddressDao extends BaseDao<Address, Long> {

	/**
	 * 获取收货地址列表
	 * @param memberId
	 * @return List<Address>
	 * @throws Exception
	 */
	@Query("from Address a where a.member.memberId=:memberId order by a.sortOrder asc")
	public List<Address> findAddressListByMember(@Param("memberId")Long memberId) throws Exception;

	/**
	 * 根据排序字段获取Address列表
	 * @param sortOrder
	 * @return List<Address>
	 * @throws Exception
	 */
	@Query("from Address a where a.sortOrder=:sortOrder")
	public List<Address> findAddressListBySoryOrder(@Param("sortOrder")Long sortOrder) throws Exception;
	
}
