package com.sanfumall.service;

import java.util.List;

import com.sanfumall.common.pojo.entity.Address;

public interface AddressService {

	/**
	 * 获取收货地址列表
	 * @param memberId
	 * @return List<Address>
	 * @throws Exception
	 */
	public List<Address> getAddressListByMember(Long memberId) throws Exception;

	/**
	 * 保存修改Address对象
	 * @param address
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaAddress(Address address) throws Exception;

	/**
	 * 根据addressId获取Address对象
	 * @param addressId
	 * @return Address
	 * @throws Exception
	 */
	public Address getAddressById(Long addressId) throws Exception;

	/**
	 * 根据排序字段获取Address列表
	 * @param sortOrder
	 * @return List<Address>
	 * @throws Exception
	 */
	public List<Address> getAddressListBySortOrder(Long sortOrder) throws Exception;

	/**
	 * 删除收货地址
	 * @param address
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean deleteAddress(Address address) throws Exception;

}