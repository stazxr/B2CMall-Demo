package com.sanfumall.admin.service;

import java.util.List;

import com.sanfumall.common.pojo.entity.Value;
import com.sanfumall.common.pojo.vo.Page;

public interface ValueService {

	/**
	 * 获取属性值列表
	 * @param page
	 * @return Page<Value>
	 * @throws Exception
	 */
	public Page<Value> getValueListByPage(Page<Value> page) throws Exception;

	/**
	 * 对属性值进行保存或修改操作
	 * @param value
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaValue(Value value) throws Exception;
	
	/**
	 * 根据valueName与attributeId获取Value对象（用于判断属性值名称的唯一性）
	 * @param valueName
	 * @param attributeId
	 * @return Value
	 */
	public Value getValueByNameAndAttributeId(String valueName, Long attributeId) throws Exception;

	/**
	 * 根据属性值Id获取属性值
	 * @param valueId
	 * @return
	 * @throws Exception
	 */
	public Value getValueById(Long valueId) throws Exception;

	/**
	 * 根据attributeId获取其对应的属性值集合
	 * @param attributeId
	 * @param statusCode 
	 * @return List<Value>
	 * @throws Exception
	 */
	public List<Value> getValueListByAttributeId(Long attributeId, String statusCode) throws Exception;

}