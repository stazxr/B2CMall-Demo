package com.sanfumall.admin.controller;

import java.util.ArrayList;
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

@Controller(value="attributeController")
@RequestMapping("/attribute")
public class AttributeController extends BaseController {
	
	@Resource(name="attributeService")
	private AttributeService attributeService;
	@Resource(name="categoryService")
	private CategoryService categoryService;
	@Resource(name="statusService")
	private StatusService statusService;
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="valueService")
	private ValueService valueService;
	
	/**
	 * 分页展示属性列表
	 * @param pageNum 页码
	 * @param pageSize 每页显示的条数
	 * @param keyword 模糊查询的参数，如果为null或空字符串,则进行普通分页查询，否则进行模糊分页查询
	 * @return attribute_index.html
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView getAttributeIndex(Integer pageNum, Integer pageSize, String keyword) throws Exception {
		// 首先，创建分页对象
		Page<Attribute> page = new Page<>(pageNum, pageSize, keyword);
		page = attributeService.getAttributeListByPage(page);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("page", page);
		return new ModelAndView("attribute/attribute_index", resultMap);
	}
	
	/**
	 * 加载属性添加页面
	 * @return attribute_add.html
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public ModelAndView getAddPage() throws Exception {
		List<Category> firstCategoryList = categoryService.getFirstCategoryList();
		// 遍历一级类别集合，如果该类别没有二级类别，则从集合中移除
		for (int i = 0; i < firstCategoryList.size(); i++) {
			if (firstCategoryList.get(i).getCategoryList().size() == 0) {
				firstCategoryList.remove(i);
			}
		}
		return new ModelAndView("attribute/attribute_add", "firstCategoryList", firstCategoryList);
	}
	
	/**
	 * 进行属性添加操作
	 * @param attribute 添加的属性信息
	 * @param categoryId 属性对应的类别
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public Boolean addAttribute(Attribute attribute, Long categoryId) throws Exception {
		if (attribute.getAttributeName() != null && !"".equals(attribute.getAttributeName().trim())) {
			if (categoryId != null && categoryId > 0) {
				Category category = categoryService.getCategoryById(categoryId);
				attribute.setCategory(category);
				// 获取启用状态
				Status status = statusService.getStatusByStatusCode(ConstantUtil.STATUS_ENABLE);
				attribute.setStatus(status);
				// 获取当前登录用户,记录创建者
				User createUser = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
				attribute.setCreateUser(createUser);
				attribute.setCreateTime(new Date());
				return attributeService.savaAttribute(attribute);
			}
		}
		return false;
	}
	
	/**
	 * 加载属性修改页面
	 * @param attributeId
	 * @return attribute_update.html
	 * @throws Exception
	 */
	@RequestMapping(value="/update/{attributeId}", method=RequestMethod.GET)
	public ModelAndView getUpdatePage(@PathVariable("attributeId")Long attributeId) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		// 获取修改的属性的原信息
		Attribute attribute = attributeService.getAttributeById(attributeId);
		// 获取所有的一级类别
		List<Category> firstCategoryList = categoryService.getFirstCategoryList();
		// 遍历一级类别集合，如果该类别没有二级类别，则从集合中移除
		for (int i = 0; i < firstCategoryList.size(); i++) {
			if (firstCategoryList.get(i).getCategoryList().size() == 0) {
				firstCategoryList.remove(i);
			}
		}
		// 获取属性对应的一级类别的子类别集合
		List<Category> secondCategoryList = categoryService.getSecondCategoryListByCategoryId(attribute.getCategory().getParent().getCategoryId());
		resultMap.put("attribute", attribute);
		resultMap.put("firstCategoryList", firstCategoryList);
		resultMap.put("secondCategoryList", secondCategoryList);
		return new ModelAndView("attribute/attribute_update", resultMap);
	}
	
	/**
	 * 进行属性修改操作
	 * @param attribute
	 * @param categoryId
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean updateCategory(Attribute attribute, Long categoryId) throws Exception {
		if (attribute.getAttributeName() != null && !"".equals(attribute.getAttributeName().trim())) {
			if (categoryId != null && categoryId > 0) {
				// 获取类别信息，并set回attribute中
				Category category = categoryService.getCategoryById(categoryId);
				attribute.setCategory(category);
				// 根据attributeId获取原有的属性信息
				Attribute origAttribute = attributeService.getAttributeById(attribute.getAttributeId());
				// 回填属性原有状态
				attribute.setStatus(origAttribute.getStatus());
				// 回填创建者与创建时间
				attribute.setCreateUser(origAttribute.getCreateUser());
				attribute.setCreateTime(origAttribute.getCreateTime());
				// 获得当前登录用户，记录修改者与修改时间
				User updateUser = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
				attribute.setUpdateUser(updateUser);
				attribute.setUpdateTime(new Date());
				return attributeService.savaAttribute(attribute);
			}
		}
		return false;
	}
	
	/**
	 * 加载属性详情页面
	 * @param attributeId
	 * @return attribute_detail.html
	 * @throws Exception
	 */
	@RequestMapping(value="/detail/{attributeId}", method=RequestMethod.GET)
	public ModelAndView getDetailPage(@PathVariable("attributeId")Long attributeId) throws Exception {
		// 根据attributeId获取要展示的属性信息
		Attribute attribute = attributeService.getAttributeById(attributeId);
		return new ModelAndView("attribute/attribute_detail", "attribute", attribute);
	}
	
	/**
	 * 进行属性状态的修改
	 * @param attributeId
	 * @param statusCode
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/changeStatus/{attributeId}/{statusCode}", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean changeStatus(@PathVariable("attributeId")Long attributeId, @PathVariable("statusCode")String statusCode) throws Exception {
		if (statusCode != null && !"".equals(statusCode) && attributeId != null) {
			// 根据attributeId获取属性对象
			Attribute attribute = attributeService.getAttributeById(attributeId);
			// 根据状态编码获取状态对象,并保存带属性对象中
			Status status = statusService.getStatusByStatusCode(statusCode);
			attribute.setStatus(status);
			// 获得当前登录用户,记录修改者,修改时间
			User updateUser = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
			attribute.setUpdateUser(updateUser);
			attribute.setUpdateTime(new Date());
			// 调用业务层保存属性
			return attributeService.savaAttribute(attribute);
		}
		return false;
	}
	
	/**
	 * 根据categoryId获取属性集合
	 * @param categoryId
	 * @return List<Attribute>
	 * @throws Exception
	 */
	@RequestMapping(value="/getAttributeListByCategory/{categoryId}", method=RequestMethod.GET)
	@ResponseBody
	public List<Attribute> getAttributeListByCategory(@PathVariable("categoryId")Long categoryId) throws Exception {
		List<Attribute> attributeList = attributeService.getAttributeListByCategory(categoryId);
		return attributeList;
	} 
	
	/**
	 * 根据categoryId获取其对应的所有属性集合以及属性对应的属性值集合
	 * @param attributeId
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@RequestMapping(value="/getAttributeAndValueList/{categoryId}", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAttributeAndValueList(@PathVariable("categoryId")Long categoryId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取二级类别下的所有启用的属性，并绑定Map
		List<Attribute> attributeList = attributeService.getAttributeListByCategory(categoryId);
		// 创建一个List集合，存放所有的属性对应的属性值集合
		List<Object> valueLists = new ArrayList<Object>();
		// 如果该二级类别下存在属性，则遍历所有属性，分别获取他们对应的属性值集合，并添加到valueLists中
		if (attributeList != null && attributeList.size() > 0) {
			for (int i = 0; i < attributeList.size(); i++) {
				List<Value> valueList = valueService.getValueListByAttributeId(attributeList.get(i).getAttributeId(), ConstantUtil.STATUS_ENABLE);
				if (valueList.size() == 0) {
					attributeList.remove(i);
				} else {
					valueLists.add(valueList);
				}
			}
		}
		resultMap.put("attributeList", attributeList);
		resultMap.put("valueLists", valueLists);
		return resultMap;
	}
	
	/**
	 * 在添加和修改的过程中，检验属性名称是否重复
	 * @param attributeId 添加过程，该项为null
	 * @param attributeName 待校验的属性名称
	 * @param categoryId 属性对应的类别Id
	 * @throws Exception
	 */
	@RequestMapping(value="/remoteValidateAttributeInfo", method=RequestMethod.GET)
	@ResponseBody
	public Boolean remoteValidateCategoryInfo(Long attributeId, String attributeName, Long categoryId) throws Exception {
		if (attributeName != null && !"".equals(attributeName.trim()) && categoryId != null && categoryId != 0) {
			// 根据属性名称与类别Id取数据库中查找属性，如果没有，则返回true, 如果有，但是查出来的属性的attributeId与前台传过来的相等，也返回true
			Attribute attribute = attributeService.getAttributeByNameAndCategoryId(attributeName, categoryId);
			if (attribute == null || (attributeId != null && attributeId == attribute.getAttributeId())) {
				return true;
			}
		}
		return false;
	}
	
}