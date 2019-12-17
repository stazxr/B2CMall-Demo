package com.sanfumall.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.admin.dao.AttributeDao;
import com.sanfumall.admin.dao.CategoryDao;
import com.sanfumall.admin.dao.StatusDao;
import com.sanfumall.admin.service.AttributeService;
import com.sanfumall.common.pojo.entity.Attribute;
import com.sanfumall.common.pojo.entity.Category;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.vo.Page;
import com.sanfumall.common.util.ConstantUtil;

@Service("attributeService")
@Transactional
public class AttributeServiceImpl implements AttributeService {

	@Resource(name="attributeDao")
	private AttributeDao attributeDao;
	@Resource(name="statusDao")
	private StatusDao statusDao;
	@Resource(name="categoryDao")
	private CategoryDao categoryDao;
	
	/**
	 * 分页获取属性列表
	 * @param page 分页信息
	 * @return Page<Attribute>
	 * @throws Exception
	 */
	public Page<Attribute> getAttributeListByPage(Page<Attribute> page) throws Exception {
		// 根据page携带的分页信息获取分页查询limit的两个参数
		Integer pageSize = page.getPageSize();
		Integer startIndex = (page.getPageNum() - 1) * pageSize;
		// 获取分页列表，首先判断是否为模糊查询
		String keyword = page.getKeyword();
		if (keyword != null && !"".equals(keyword)) {
			// 进行分页模糊查询
			// 判断是否是按状态名称查询
			Status status = statusDao.findByStatusName(keyword);
			if (status != null) {
				keyword = Long.toString(status.getStatusId());
			}
			// 判断是否是按照类别名称查询
			Category category = categoryDao.findByCategoryName(keyword.trim());
			if (category != null) {
				keyword = Long.toString(category.getCategoryId());
			}
			// 进行模糊分页查询，并将查询结果set到page对象中
			List<Attribute> attributeList = attributeDao.findAttributeListByLikePage(startIndex, pageSize, keyword.toUpperCase());
			page.setList(attributeList);
			// 获取在不分页的情况下进行模糊查询的总数量，并回填到page对象中
			Integer totalSize = attributeDao.findAttributeListByLike(keyword).size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		} else {
			// 进行普通的分页查询，获取分页结果，并将查询结果set到page对象中
			List<Attribute> attributeList = attributeDao.findAttributeListByPage(startIndex, pageSize);
			page.setList(attributeList);
			// 获取分页信息，并回填到page对象中
			Integer totalSize = attributeDao.findAll().size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		}
		return page;
	}
	
	/**
	 * 保存或修改属性
	 * @param attribute
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaAttribute(Attribute attribute) throws Exception {
		try {
			attributeDao.save(attribute);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据attributeName获取Attribute对象
	 * @param attributeName
	 * @return Attribute 
	 * @throws Exception
	 */
	public Attribute getAttributeByName(String attributeName) throws Exception {
		return attributeDao.findByAttributeName(attributeName);
	}

	/**
	 * 根据attributeName与categoryId获取Attribute对象
	 * @param attributeName
	 * @param categoryId
	 * @return Attribute 
	 * @throws Exception
	 */
	public Attribute getAttributeByNameAndCategoryId(String attributeName, Long categoryId) throws Exception {
		return attributeDao.findByAttributeNameAndCategoryId(attributeName, categoryId);
	}

	/**
	 * 根据attributeId获取Attribute对象
	 * @param attributeId
	 * @return Attribute
	 */
	public Attribute getAttributeById(Long attributeId) {
		return attributeDao.getOne(attributeId);
	}

	/**
	 * 根据Attribute对象获取属性集合
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	public List<Attribute> getAttributeListByCategory(Long categoryId) throws Exception {
		return attributeDao.findAttributeListByCategory(categoryId, ConstantUtil.STATUS_ENABLE);
	}
	 
}