package com.sanfumall.common.pojo.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 状态
 * @author SunTao
 */
@Entity
@Table(name="sys_status")
public class Status implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long statusId;			// 状态Id
	private Status parent;			// 上级状态:多对一
	private String statusName;		// 状态名称
	private String statusCode;		// 状态编码
	private Long sortOrder;			// 排序字段
	
	public Status() {}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	@ManyToOne(targetEntity=Status.class, fetch=FetchType.EAGER,
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="parent_id")
	public Status getParent() {
		return parent;
	}
	public void setParent(Status parent) {
		this.parent = parent;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public Long getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	@Override
	public String toString() {
		String msg = "statusId:" + statusId + "\tstatusName:" + statusName + "\tparent:[" + parent + "]";
		return msg;
	}
	
}