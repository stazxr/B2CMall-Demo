package com.sanfumall.admin.service;

import java.util.List;

import com.sanfumall.common.pojo.entity.Attribute;
import com.sanfumall.common.pojo.vo.Page;

public interface AttributeService {
	
	/**
	 * 分页获取属性列表
	 * @param page 分页信息
	 * @return 带有分页列表的Page对象
	 * @throws Exception
	 */
	public Page<Attribute> getAttributeListByPage(Page<Attribute> page) throws Exception;

	/**
	 * 保存或修改Attribute对象
	 * @param attribute
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaAttribute(Attribute attribute) throws Exception;
	
	/**
	 * 根据属性ID获取属性
	 * @param attributeId
	 * @return Attribute
	 */
	public Attribute getAttributeById(Long attributeId) throws Exception;
	
	/**
	 * 根据属性名称获取属性
	 * @param attributeName
	 * @return Attribute 
	 * @throws Exception
	 */
	public Attribute getAttributeByName(String attributeName) throws Exception;

	/**
	 * 根据attributeName与categoryId获取属性(用于校验attributeName的唯一性)
	 * @param attributeName
	 * @param categoryId
	 * @return Attribute 
	 * @throws Exception
	 */
	public Attribute getAttributeByNameAndCategoryId(String attributeName, Long categoryId) throws Exception;

	/**
	 * 根据categoryId获取对应的属性集合
	 * @param categoryId
	 * @return List<Attribute>
	 * @throws Exception
	 */
	public List<Attribute> getAttributeListByCategory(Long categoryId) throws Exception;

}