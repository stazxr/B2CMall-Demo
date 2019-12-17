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

import com.sanfumall.admin.service.CategoryService;
import com.sanfumall.admin.service.StatusService;
import com.sanfumall.admin.service.UserService;
import com.sanfumall.common.base.controller.BaseController;
import com.sanfumall.common.pojo.entity.Category;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.entity.User;
import com.sanfumall.common.pojo.vo.Page;
import com.sanfumall.common.util.ConstantUtil;

@Controller(value="categoryController")
@RequestMapping("/category")
public class CategoryController extends BaseController {
	
	@Resource(name="categoryService")
	private CategoryService categoryService;
	@Resource(name="statusService")
	private StatusService statusService;
	@Resource(name="userService")
	private UserService userService;
	
	/**
	 * 分页展示类别列表
	 * @param pageNum 页码
	 * @param pageSize 每页显示的条数
	 * @param keyword 模糊查询的参数，如果为null或空字符串,则进行普通分页查询，否则进行模糊分页查询
	 * @return category_index.html
	 * @throws Exception
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView getCategoryIndex(Integer pageNum, Integer pageSize, String keyword) throws Exception {
		// 首先，创建分页对象
		Page<Category> page = new Page<>(pageNum, pageSize, keyword);
		page = categoryService.getCategoryListByPage(page);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("page", page);
		return new ModelAndView("category/category_index", resultMap);
	}
	
	/**
	 * 加载类别添加页面
	 * @return category_add.html
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public ModelAndView getAddPage() throws Exception {
		List<Category> firstCategoryList = categoryService.getFirstCategoryList();
		return new ModelAndView("category/category_add", "firstCategoryList", firstCategoryList);
	}
	
	/**
	 * 进行类别添加操作
	 * @param category 添加的类别信息
	 * @param level 添加的类别等级
	 * @param parentId 上级类别ID
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public Boolean addCategory(Category category, String level, Long parentId) throws Exception {
		if ("1".equals(level)) {
			// 说明添加的是一级类别，parentId置为null
			parentId = null;
		} else {
			// 说明添加的是二级类别，校验parentId是否存在，不存在，返回false,存在，则setParent对象
			if (parentId != null) {
				Category parent = categoryService.getCategoryById(parentId);
				category.setParent(parent);
			} else {
				return false;
			}
		}
		// 获取启用状态
		Status status = statusService.getStatusByStatusCode(ConstantUtil.STATUS_ENABLE);
		category.setStatus(status);
		// 获取当前登录用户
		User createUser = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
		category.setCreateUser(createUser);
		category.setCreateTime(new Date());
		return categoryService.savaCategory(category);
	}
	
	/**
	 * 加载类别修改页面
	 * @param categoryId
	 * @return category_update.html
	 * @throws Exception
	 */
	@RequestMapping(value="/update/{categoryId}", method=RequestMethod.GET)
	public ModelAndView getUpdatePage(@PathVariable("categoryId")Long categoryId) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		// 获取原有的类别信息
		Category category = categoryService.getCategoryById(categoryId);
		// 获取所有启用的一级类别
		List<Category> firstCategoryList = categoryService.getFirstCategoryList();
		resultMap.put("category", category);
		resultMap.put("firstCategoryList", firstCategoryList);
		return new ModelAndView("category/category_update", resultMap);
	}
	
	/**
	 * 进行类别修改
	 * @param category 修改的类别信息
	 * @param level 修改的类别等级
	 * @param parentId 上级类别ID
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean updateCategory(Category category, String level, Long parentId) throws Exception {
		if ("1".equals(level)) {
			// 说明添加的是一级类别，parentId置为null
			parentId = null;
		} else {
			if (parentId != null) {
				Category parent = categoryService.getCategoryById(parentId);
				category.setParent(parent);
			} else {
				return false;
			}
		}
		// 根据categoryId获取原有的类别信息
		Category origCategory = categoryService.getCategoryById(category.getCategoryId());
		// 回填类别原有状态
		category.setStatus(origCategory.getStatus());
		// 回填创建者，创建时间
		category.setCreateUser(origCategory.getCreateUser());
		category.setCreateTime(origCategory.getCreateTime());
		// 获得当前登录用户，记录修改者与修改时间
		User createUser = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
		category.setUpdateUser(createUser);
		category.setUpdateTime(new Date());
		return categoryService.savaCategory(category);
	}
	
	/**
	 * 加载类别详情页面
	 * @param categoryId
	 * @return category_detail.html
	 * @throws Exception
	 */
	@RequestMapping(value="/detail/{categoryId}", method=RequestMethod.GET)
	public ModelAndView getDetailPage(@PathVariable("categoryId")Long categoryId) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		// 获取要展示的类别信息
		Category category = categoryService.getCategoryById(categoryId);
		// 如果是加载一级类别的详情，则获取其对应的二级类别
		if (category.getParent() == null) {
			List<Category> secondCategoryList = categoryService.getSecondCategoryListByCategoryId(category.getCategoryId());
			resultMap.put("secondCategoryList", secondCategoryList);
		}
		resultMap.put("category", category);
		return new ModelAndView("category/category_detail", resultMap);
	}
	
	/**
	 * 进行类别状态的修改
	 * @param categoryId
	 * @param statusCode
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/changeStatus/{categoryId}/{statusCode}", method=RequestMethod.PUT)
	@ResponseBody
	public Boolean changeStatus(@PathVariable("categoryId")Long categoryId, @PathVariable("statusCode")String statusCode) throws Exception {
		if (statusCode != null && !"".equals(statusCode) && categoryId != null) {
			// 根据categoryId获取Category对象
			Category category = categoryService.getCategoryById(categoryId);
			// 根据状态编码获取状态对象,并保存带类别对象中
			Status status = statusService.getStatusByStatusCode(statusCode);
			category.setStatus(status);
			// 获得当前登录用户,记录修改者,修改时间
			User updateUser = userService.getUserByUserId(((User) session.getAttribute("user")).getUserId());
			category.setUpdateUser(updateUser);
			category.setUpdateTime(new Date());
			// 调用业务层保存类别
			return categoryService.savaCategory(category);
		}
		return false;
	}
	
	/**
	 * 根据categoryId获取其对应的二级类别集合
	 * @param categoryId
	 * @return List<Category>
	 * @throws Exception
	 */
	@RequestMapping(value="/getSecondCategoryById/{categoryId}", method=RequestMethod.GET)
	@ResponseBody
	public List<Category> getSecondCategoryById(@PathVariable("categoryId")Long categoryId) throws Exception {
		List<Category> secondCategoryList = categoryService.getSecondCategoryListByCategoryId(categoryId);
		return secondCategoryList;
	}
	
	/**
	 * 在添加及修改商品的过程中，检验类别名称是否重复
	 * @param categoryName 待校验的类别名称
	 * @param categoryId 存在id,为修改时的校验，否则为添加时的校验
	 * @return Boolean
	 * @throws Exception
	 */
	@RequestMapping(value="/remoteValidateCategoryInfo", method=RequestMethod.GET)
	@ResponseBody
	public Boolean remoteValidateCategoryInfo(Long categoryId, String categoryName) throws Exception {
		if (categoryName != null && !"".equals(categoryName.trim())) {
			// 根据类别名称获取类别信息
			Category category = categoryService.getCategoryByName(categoryName);
			if (category == null || (categoryId != null && categoryId == category.getCategoryId())) {
				return true;
			}
		}
		return false;
	}
	
}