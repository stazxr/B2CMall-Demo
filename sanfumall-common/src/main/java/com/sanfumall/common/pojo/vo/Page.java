package com.sanfumall.common.pojo.vo;

import java.io.Serializable;
import java.util.List;

import com.sanfumall.common.util.ConstantUtil;


/**
 * 分页工具类
 * 包含pageNum（页码）、pageSize（每页显示的条数）、keyword（模糊查询关键词）、totalPage（查询总页数）、totalSize（查询总数量）、list（分页结果）
 * @author SunTao
 * @param <E> 需要进行分页的类型
 * @since 2018-12-12
 */
public class Page<E> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer pageNum;
	private Integer pageSize;
	private String keyword;
	private Integer totalPage;
	private Integer totalSize;
	private List<E> list;
	
	public Page() {}
	
	/**
	 * 用于普通分页
	 * @param pageNum
	 * @param pageSize
	 */
	public Page(Integer pageNum, Integer pageSize) {
		init(pageNum, pageSize);
	}
	
	/**
	 * 用于带有模糊查询的分页
	 * @param pageNum
	 * @param pageSize
	 * @param keyword
	 */
	public Page(Integer pageNum, Integer pageSize, String keyword) {
		init(pageNum, pageSize);
		this.keyword = keyword;
	}
	
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public Integer getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}
	public List<E> getList() {
		return list;
	}
	public void setList(List<E> list) {
		this.list = list;
	}

	/**
	 * 初始化分页基本信息（校验分页参数的合理性）
	 * @param pageNum
	 * @param pageSize
	 */
	private void init(Integer pageNum, Integer pageSize) {
		// 校验分页参数是否合法
		if (pageNum == null || pageNum < 1) {
			this.pageNum = ConstantUtil.PAGE_NUM;
		} else {
			this.pageNum = pageNum;
		}
		if (pageSize == null || pageSize < 1) {
			this.pageSize = ConstantUtil.PAGE_SIZE;
		} else {
			this.pageSize = pageSize;
		}
	}
	
}
