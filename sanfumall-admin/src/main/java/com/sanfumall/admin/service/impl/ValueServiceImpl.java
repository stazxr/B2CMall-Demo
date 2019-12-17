package com.sanfumall.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.admin.dao.AttributeDao;
import com.sanfumall.admin.dao.StatusDao;
import com.sanfumall.admin.dao.ValueDao;
import com.sanfumall.admin.service.ValueService;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.entity.Value;
import com.sanfumall.common.pojo.vo.Page;


@Service(value="valueService")
@Transactional
public class ValueServiceImpl implements ValueService {

	@Resource(name="attributeDao")
	private AttributeDao attributeDao;
	@Resource(name="statusDao")
	private StatusDao statusDao;
	@Resource(name="valueDao")
	private ValueDao valueDao;
	
	/**
	 * 分页获取属性值列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Page<Value> getValueListByPage(Page<Value> page) throws Exception {
		// 根据page携带的分页信息获取分页查询limit的两个参数
		Integer pageSize = page.getPageSize();
		Integer startIndex = (page.getPageNum() - 1) * pageSize;
		// 首先判断是否为模糊查询
		String keyword = page.getKeyword();
		if (keyword != null && !"".equals(keyword)) {
			// 进行分页模糊查询
			// 判断是否是按状态名称查询
			Status status = statusDao.findByStatusName(keyword);
			if (status != null) {
				keyword = Long.toString(status.getStatusId());
			}
			// 进行模糊分页查询，并将查询结果set到page对象中
			List<Value> valueList = valueDao.findValueListByLikePage(startIndex, pageSize, keyword.toUpperCase());
			page.setList(valueList);
			// 获取在不分页的情况下进行模糊查询的总数量，并回填到page对象中
			Integer totalSize = valueDao.findValueListByLike(keyword).size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		} else {
			// 进行普通的分页查询
			List<Value> valueList = valueDao.findValueListByPage(startIndex, pageSize);
			page.setList(valueList);
			// 获取分页信息，并回填到page对象中
			Integer totalSize = valueDao.findAll().size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		}
		return page;
	}

	/**
	 * 根据属性值名称与属性Id获取属性值
	 */
	public Value getValueByNameAndAttributeId(String valueName, Long attributeId) throws Exception {
		return valueDao.findValueByNameAndAttributeId(valueName, attributeId);
	}

	/**
	 * 对属性值进行保存或修改操作
	 * @param value
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaValue(Value value) throws Exception {
		try {
			valueDao.save(value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据valueId获取属性值
	 * @param valueId
	 * @return Value
	 * @throws Exception
	 */
	public Value getValueById(Long valueId) throws Exception {
		return valueDao.getOne(valueId);
	}

	/**
	 * 根据属性Id获取属性值集合
	 * @param attributeId
	 * @param statusCode ENABLE
	 * @return List<Value>
	 * @throws Exception
	 */
	public List<Value> getValueListByAttributeId(Long attributeId, String statusCode) throws Exception {
		return valueDao.findValueListByAttributeId(attributeId, statusCode);
	}

}