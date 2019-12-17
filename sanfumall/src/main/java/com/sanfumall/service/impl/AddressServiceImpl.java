package com.sanfumall.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.common.pojo.entity.Address;
import com.sanfumall.dao.AddressDao;
import com.sanfumall.service.AddressService;

@Service(value="addressService")
@Transactional
public class AddressServiceImpl implements AddressService {

	@Resource(name="addressDao")
	private AddressDao addressDao;
	
	/**
	 * 获取收货地址列表
	 * @param memberId
	 * @return List<Address>
	 * @throws Exception
	 */
	public List<Address> getAddressListByMember(Long memberId) throws Exception {
		return addressDao.findAddressListByMember(memberId);
	}

	/**
	 * 保存修改Address对象
	 * @param address
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaAddress(Address address) throws Exception {
		if (address.getAddressId() == null) {
			// 保存操作
			try {
				addressDao.save(address);
				address.setSortOrder(address.getAddressId());
				addressDao.save(address);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 修改操作
			try {
				addressDao.save(address);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 根据addressId获取Address对象
	 * @param addressId
	 * @return Address
	 * @throws Exception
	 */
	public Address getAddressById(Long addressId) throws Exception {
		return addressDao.getOne(addressId);
	}

	/**
	 * 根据排序字段获取Address列表
	 * @param sortOrder
	 * @return List<Address>
	 * @throws Exception
	 */
	public List<Address> getAddressListBySortOrder(Long sortOrder) throws Exception {
		return addressDao.findAddressListBySoryOrder(sortOrder);
	}

	/**
	 * 删除收货地址
	 * @param address
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean deleteAddress(Address address) throws Exception {
		try {
			addressDao.delete(address);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}