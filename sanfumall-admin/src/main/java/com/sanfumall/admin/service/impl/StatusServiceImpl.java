package com.sanfumall.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.admin.dao.StatusDao;
import com.sanfumall.admin.service.StatusService;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.vo.Page;


@Service("statusService")
@Transactional
public class StatusServiceImpl implements StatusService {
	
	@Resource(name="statusDao")
	private StatusDao statusDao;
	
	/**
	 * 根据状态编码获取状态对象
	 * @param statusCode
	 * @return Status
	 * @throws Exception
	 */
	public Status getStatusByStatusCode(String statusCode) throws Exception {
		return statusDao.findByStatusCode(statusCode);
	}
	
	/**
	 * 根据分页信息获取状态列表
	 * @param page 分页信息
	 * @return Page<Status>
	 * @throws Exception
	 */
	public Page<Status> getStatusListByPage(Page<Status> page) throws Exception {
		// 根据page携带的分页信息获取分页查询limit的两个参数
		Integer pageSize = page.getPageSize();
		Integer startIndex = (page.getPageNum() - 1) * pageSize;
		// 首先判断是否为模糊查询
		String keyword = page.getKeyword();
		if (keyword != null && !"".equals(keyword)) {
			// 进行分页模糊查询,注意，对keyword进行大写转换
			List<Status> statusList = statusDao.findStatusListByLikePage(startIndex, pageSize, keyword.toUpperCase());
			page.setList(statusList);
			// 获取分页信息，并回填到page对象中
			Integer totalSize = statusDao.findStatusListByLike(keyword).size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		} else {
			// 进行普通的分页查询
			List<Status> statusList = statusDao.findStatusListByPage(startIndex, pageSize);
			page.setList(statusList);
			// 获取分页信息，并回填到page对象中
			Integer totalSize = statusDao.findAll().size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		}
		return page;
	}

}