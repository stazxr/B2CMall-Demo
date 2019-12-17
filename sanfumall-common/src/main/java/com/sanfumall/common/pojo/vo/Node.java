package com.sanfumall.common.pojo.vo;

import java.io.Serializable;

/**
 * zTree节点对象，用于给zTree传递参数
 * 注意：属性与zTree节点的保持相同: id,pId,name,open
 * @author SunTao
 * @since 2018-12-18
 */
public class Node implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;				// 节点Id 
	private Long pId;				// 父节点Id,没有父节点则为0
	private String name;			// 节点名称	
	private Boolean open;			// 是否展开
	private Boolean checked;		// 是否选中
	private Boolean doCheck;		// 是否可以任意勾选

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	// 注意：不是setPId(Long pId)/getPId() 父节点会出不来
	public Long getpId() {
		return pId;
	}
	public void setpId(Long pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public Boolean getDoCheck() {
		return doCheck;
	}
	public void setDoCheck(Boolean doCheck) {
		this.doCheck = doCheck;
	}
	
}