package com.sanfumall.admin.controller;

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

import com.sanfumall.admin.service.AttributeService;
import com.sanfumall.admin.service.CategoryService;
import com.sanfumall.admin.service.StatusService;
import com.sanfumall.admin.service.UserService;
import com.sanfumall.admin.service.ValueService;
import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Attribute;
import com.sanfumall.common.pojo.entity.Category;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.entity.User;
import com.sanfumall.common.pojo.entity.Value;
import com.sanfumall.common.pojo.vo.Page;
import com.sanfumall.common.util.ConstantUtil;

@Controller(value="valueController")
@RequestMapping("/value")
public class ValueController extends BaseController {
	
	@Resource(name="valueService")
	private ValueService valueService;
	@Resource(name="attributeService")
	private AttributeService attributeService;
	@Resource(name="categoryService")
	private CategoryService categoryService;
	@Resource(name="statusService")
	private StatusService statusService;
	@Resource(name="userService")
	private UserService userService;
	
	/**
	 * 分页展示属性值列表
	 * @param pageNum 页码
	 * @param pageSize 每页显示的条数
	 * @param keyword 模糊查询的参数，如果为null或空字符串,则进行普通分页查询，否则进行模糊分页查询
	 * @return "value/value_index" 进入属性值信息列表，且分页展示属性值信息
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView getAttributeIndex(Integer pageNum, Integer pageSize, String keyword) throws Exception {
		// 首先，创建分页对象
		Page<Value> page = new Page<>(pageNum, pageSize, keyword);
		page = valueService.getValueListByPage(page);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("page", page);
		return new ModelAndView("value/value_index", resultMap);
	}
	
	/**
	 * 加载属性值添加页面
	 * @return value_add.html
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public ModelAndView getAddPage() throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		List<Category> firstCategoryList = categoryService.getFirstCategoryList();
		// 遍历一级类别集合，如果该类别没有二级类别，则从集合中移除
		for (int i = 0; i < firstCategoryList.size(); i++) {
			if (firstCategoryList.get(i).getCategoryList().size() == 0) {
				firstCategoryList.remove(i);
				continue;
			}
			// 获取每个一级类别对应的二级类别
			List<Category> secondCategoryList = categoryService.getSecondCategoryListByCategoryId(firstCategoryList.get(i).getCategoryId());
			// 遍历二级类别集合，如果该类别没有属性，则从集合中移除
			for (int j = 0; j < secondCategoryList.size(); j++) {
				if (secondCategoryList.get(j).getAttributeList().size() == 0) {
					firstCategoryList.remove(i);
				}
			}
		}
		List<Category> secondCategoryList = categoryService.getSecondCategoryListByCategoryId(firstCategoryList.get(0).getCategoryId());
		List<Attribute> attributeList = attributeService.getAttributeListByCategory(secondCategoryList.get(0).getCategoryId());
		resultMap.put("firstCategoryList", firstCategoryList);
		resultMap.put("secondCategoryList", secondCategoryList);
		resultMap.put("attributeList", attributeList);
		return new ModelAndView("value/value_add", resultMap);
	}
	
	/**
	 * 进行属性值添加操作
	 * @param value 添加的属性值信息
	 * @param attributeId 属性值对应的属性Id
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public Boolean addValue(Value value, Long attributeId) throws Exception {
		if (value.getValueName() != null && !"".equals(value.getValueName().trim())) {
			if (attributeId != null && attributeId > 0) {
				Attribute attribute = attributeService.getAttributeById(attributeId);
				value.setAttribute(attribute);
				// 获取启用状态
				Status status = statusService.getStatusByStatusCode(ConstantUtil.STATUS_ENABLE);
				value.setStatus(status);
				// 获取当前登录用户
				User createUser = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
				value.setCreateUser(createUser);
				value.setCreateTime(new Date());
				return valueService.savaValue(value);
			}
		}
		return false;
	}
	
	/**
	 * 加载属性值修改页面
	 * @param valueId
	 * @return value_update.html
	 * @throws Exception
	 */
	@RequestMapping(value="/update/{valueId}", method=RequestMethod.GET)
	public ModelAndView getUpdatePage(@PathVariable("valueId")Long valueId) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		// 获取修改的属性值的原信息
		Value value = valueService.getValueById(valueId);
		resultMap.put("value", value);
		List<Category> firstCategoryList = categoryService.getFirstCategoryList();
		// 遍历一级类别集合，如果该类别没有二级类别，则从集合中移除
		for (int i = 0; i < firstCategoryList.size(); i++) {
			if (firstCategoryList.get(i).getCategoryList().size() == 0) {
				firstCategoryList.remove(i);
				continue;
			}
			// 获取每个一级类别对应的二级类别
			List<Category> secondCategoryList = categoryService.getSecondCategoryListByCategoryId(firstCategoryList.get(i).getCategoryId());
			// 遍历二级类别集合，如果该类别没有属性，则从集合中移除
			for (int j = 0; j < secondCategoryList.size(); j++) {
				if (secondCategoryList.get(j).getAttributeList().size() == 0) {
					firstCategoryList.remove(i);
				}
			}
		}
		List<Category> secondCategoryList = categoryService.getSecondCategoryListByCategoryId(value.getAttribute().getCategory().getParent().getCategoryId());
		List<Attribute> attributeList = attributeService.getAttributeListByCategory(value.getAttribute().getCategory().getCategoryId());
		resultMap.put("firstCategoryList", firstCategoryList);
		resultMap.put("secondCategoryList", secondCategoryList);
		resultMap.put("attributeList", attributeList);
		return new ModelAndView("value/value_update", resultMap);
	}
	
	/**
	 * 进行属性值修改
	 * @param valueId
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean updateCategory(Value value, Long attributeId) throws Exception {
		if (attributeId != null && attributeId > 0) {
			// 获取属性信息，并set回属性值对象中
			Attribute attribute = attributeService.getAttributeById(attributeId);
			value.setAttribute(attribute);
			// 根据valueId获取原有的Value对象
			Value origValue = valueService.getValueById(value.getValueId());
			// 回填属性值原有状态
			value.setStatus(origValue.getStatus());
			// 回填创建用户
			value.setCreateUser(origValue.getCreateUser());
			value.setCreateTime(origValue.getCreateTime());
			// 修改value的时候，应该注意SKU信息
			value.setSkuList(origValue.getSkuList());
			// 获得当前登录用户，记录修改者与修改时间
			User updateUser = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
			value.setUpdateUser(updateUser);
			value.setUpdateTime(new Date());
			return valueService.savaValue(value);
		}
		return false;
	}
	
	/**
	 * 进行属性值状态的修改
	 * @param valueId
	 * @param statusCode
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/changeStatus/{valueId}/{statusCode}", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean changeStatus(@PathVariable("valueId")Long valueId, @PathVariable("statusCode")String statusCode) throws Exception {
		if (statusCode != null && !"".equals(statusCode) && valueId != null) {
			// 根据ID获取属性值对象
			Value value = valueService.getValueById(valueId);
			// 根据状态编码获取状态对象,并保存带属性值对象中
			Status status = statusService.getStatusByStatusCode(statusCode);
			value.setStatus(status);
			// 获得当前登录用户,记录修改者,修改时间
			User updateUser = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
			value.setUpdateUser(updateUser);
			value.setUpdateTime(new Date());
			// 调用业务层保存属性值
			return valueService.savaValue(value);
		}
		return false;
	}
	
	/**
	 * 检验属性值名称是否重复
	 * @param valueId 存在id,为修改时的校验，否则为添加时的校验
	 * @param valueName
	 * @param attributeId 
	 * @throws Exception
	 */
	@RequestMapping(value="/remoteValidateValueInfo", method=RequestMethod.GET)
	@ResponseBody
	public Boolean remoteValidateCategoryInfo(Long valueId, String valueName, Long attributeId) throws Exception {
		if (valueName != null && !"".equals(valueName.trim()) && attributeId != null) {
			Value value = valueService.getValueByNameAndAttributeId(valueName, attributeId);
			if (value == null || (valueId != null && valueId == value.getValueId())) {
				return true;
			}
		}
		return false;
	}
	
}