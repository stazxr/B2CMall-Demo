package com.sanfumall.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Address;
import com.sanfumall.common.pojo.entity.Area;
import com.sanfumall.common.pojo.entity.Member;
import com.sanfumall.service.AddressService;
import com.sanfumall.service.AreaService;
import com.sanfumall.service.MemberService;

@Controller(value="addressController")
@RequestMapping("/address")
public class AddressController extends BaseController {
	
	@Resource(name="addressService")
	private AddressService addressService;
	@Resource(name="memberService")
	private MemberService memberService;
	@Resource(name="areaService")
	private AreaService areaService;
	
	/**
	 * 获取收货地址列表
	 * @return address_index.html
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView getIndex() throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		Long memberId = ((Member) session.getAttribute("member")).getMemberId();
		List<Address> addressList = addressService.getAddressListByMember(memberId);
		resultMap.put("addressList", addressList);
		return new ModelAndView("address/address_index", resultMap);
	}
	
	/**
	 * 加载添加收货地址页面
	 * @return address_add.html
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public ModelAndView getAddPage() throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		List<Area> firstAreaList = areaService.getFirstAreaList();
		List<Area> secondAreaList = areaService.getSubAreaList(firstAreaList.get(0).getAreaCode());
		resultMap.put("firstAreaList", firstAreaList);
		resultMap.put("secondAreaList", secondAreaList);
		return new ModelAndView("address/address_add", resultMap);
	}
	
	/**
	 * 添加收货地址
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public Boolean addAddress(Address address) throws Exception {
		if (checkAddress(address)) {
			String consigneeAddress = "";
			String[] consigneeAddressCode = request.getParameterValues("consigneeAddressCode");
			for (String areaCode : consigneeAddressCode) {
				// 获取Area对象
				Area area = areaService.getAreaByAreaCode(areaCode);
				if (area != null) {
					consigneeAddress += area.getAreaName() + "-";
				}
			}
			address.setConsigneeAddress(consigneeAddress.substring(0, consigneeAddress.length() - 1));
			// 获取当前登录对象
			Member member = memberService.getMemberById(((Member) session.getAttribute("member")).getMemberId());
			address.setMember(member);
			// 获取当前时间
			address.setCreateTime(new Date());
			// 保存收货地址信息
			return addressService.savaAddress(address);
		}
		return false;
	}
	
	/**
	 * 加载修改收货地址页面
	 * @return address_update.html
	 * @throws Exception
	 */
	@RequestMapping(value="/update/{addressId}", method=RequestMethod.GET)
	public ModelAndView getUpdatePage(@PathVariable("addressId")Long addressId) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		// 获取修改的Address对象
		Address address = addressService.getAddressById(addressId);
		resultMap.put("address", address);
		// 获取收货地址字符串（格式为"省-市-县"或"省-市"）
		String consigneeAddress = address.getConsigneeAddress();
		// 分隔字符串获取字符串数组
		String[] consigneeAddressName = consigneeAddress.split("-");
		// 第一步：获取当前地址的省名称以及一级省的列表
		String provinceName = consigneeAddressName[0];
		List<Area> firstAreaList = areaService.getFirstAreaList();
		resultMap.put("provinceName", provinceName);
		resultMap.put("firstAreaList", firstAreaList);
		// 第二步：获取二级城市的名称以及对于的城市列表
		String cityName = consigneeAddressName[1];
		List<Area> secondAreaList = areaService.getSubAreaList(areaService.getAreaByAreaName(provinceName).getAreaCode());
		resultMap.put("cityName", cityName);
		resultMap.put("secondAreaList", secondAreaList);
		// 判断地址是否还有三级区县信息
		if (consigneeAddressName.length == 3) {
			// 含有区县信息，获取区县名称以及对于的三级区域列表
			String countyName = consigneeAddressName[2];
			List<Area> thirdAreaList = areaService.getSubAreaList(areaService.getAreaByAreaName(cityName).getAreaCode());
			resultMap.put("countyName", countyName);
			resultMap.put("thirdAreaList", thirdAreaList);
		}
		return new ModelAndView("address/address_update", resultMap);
	}
	
	/**
	 * 修改收货地址
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean updateAddress(Address address) throws Exception {
		if (checkAddress(address) && address.getAddressId() != null) {
			String consigneeAddress = "";
			String[] consigneeAddressCode = request.getParameterValues("consigneeAddressCode");
			for (String areaCode : consigneeAddressCode) {
				// 获取Area对象
				Area area = areaService.getAreaByAreaCode(areaCode);
				if (area != null) {
					consigneeAddress += area.getAreaName() + "-";
				}
			}
			address.setConsigneeAddress(consigneeAddress.substring(0, consigneeAddress.length() - 1));
			// 获取原先的Address对象，回填数据
			Address origAddress = addressService.getAddressById(address.getAddressId()); 
			address.setMember(origAddress.getMember());
			address.setCreateTime(origAddress.getCreateTime());
			address.setSortOrder(origAddress.getSortOrder());
			// 获取当前时间
			address.setUpdateTime(new Date());
			// 保存收货地址信息
			return addressService.savaAddress(address);
		}
		return false;
	}
	
	/**
	 * 加载详情页面
	 * @return address_add.html
	 * @throws Exception
	 */
	@RequestMapping(value="/detail/{addressId}", method=RequestMethod.GET)
	public ModelAndView getDetailPage(@PathVariable("addressId")Long addressId) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		Address address = addressService.getAddressById(addressId);
		resultMap.put("address", address);
		return new ModelAndView("address/address_detail", resultMap);
	}
	
	/**
	 * 设置默认收货地址
	 * @param addressId
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/setDefaultAddress/{addressId}", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean setDefaultAddress(@PathVariable("addressId")Long addressId) throws Exception {
		if (addressId != null) {
			// 获取sortOrder为一的Address对象，并将其sortOrder设为2L
			List<Address> addressList = addressService.getAddressListBySortOrder(1L);
			System.out.println(addressList);
			if (addressList.size() > 0) {
				for (Address address : addressList) {
					address.setSortOrder(2L);
					try {
						addressService.savaAddress(address);
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			}
			// 根据addressId获取Address对象，然后将其sortOrder设为1L
			Address address = addressService.getAddressById(addressId);
			address.setSortOrder(1L);
			return addressService.savaAddress(address);
		}
		return false;
	}
	
	/**
	 * 删除收货地址
	 * @param addressId
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAddress/{addressId}", method=RequestMethod.DELETE)
	@ResponseBody
	public Boolean deleteAddress(@PathVariable("addressId")Long addressId) throws Exception {
		if (addressId != null) {
			// 根据addressId获取Address对象，然后将其sortOrder设为1L
			Address address = addressService.getAddressById(addressId);
			return addressService.deleteAddress(address);
		}
		return false;
	}

	/**
	 * 对前台传来的信息进行非空校验
	 * @param address
	 * @return boolean
	 */
	private boolean checkAddress(Address address) {
		String consigneeName = address.getConsigneeName();
		String consigneeCellphone = address.getConsigneeCellphone();
		String consigneeDetailAddress = address.getConsigneeDetailAddress();
		String consigneeZip = address.getConsigneeZip();
		// 非空校验
		if (consigneeName != null && !"".equals(consigneeName)
				&& consigneeCellphone != null && !"".equals(consigneeCellphone)
				&& consigneeDetailAddress != null && !"".equals(consigneeDetailAddress)
				&& consigneeZip != null && !"".equals(consigneeZip)) {
			return true;
		}
		return false;
	}
	
}