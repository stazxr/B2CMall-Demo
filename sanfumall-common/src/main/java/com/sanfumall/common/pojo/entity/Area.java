package com.sanfumall.common.pojo.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sys_area")
public class Area implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long areaId;			// 区域Id
	private String areaCode;		// 区域行政编码
	private String areaName;		// 名称
	private String parentCode;		// 所属区域的行政编码
	
	public Area() {}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	@Override
	public String toString() {
		String msg = "areaCode:" + areaCode + "\tareaName:" + areaName + "\tparentCode" + parentCode;
		return msg;
	}

}