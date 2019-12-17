package com.sanfumall.common.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="sys_address")
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long addressId;						// 收货地址主键		
	private Member member;						// 创建会员
	private String consigneeName;				// 收货人姓名
	private String consigneeCellphone;			// 收货人联系电话
	private String consigneeAddress;			// 收货人地址
	private String consigneeDetailAddress;		// 收货人详细地址
	private String consigneeZip;				// 邮编
	private Long sortOrder;						// 排序字段
	private Date createTime;					// 创建时间
	private Date updateTime;					// 更新时间
	
	public Address() {}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	@ManyToOne(targetEntity=Member.class, fetch=FetchType.LAZY, 
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="member_id")
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public String getConsigneeCellphone() {
		return consigneeCellphone;
	}
	public void setConsigneeCellphone(String consigneeCellphone) {
		this.consigneeCellphone = consigneeCellphone;
	}
	public String getConsigneeAddress() {
		return consigneeAddress;
	}
	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}
	public String getConsigneeDetailAddress() {
		return consigneeDetailAddress;
	}
	public void setConsigneeDetailAddress(String consigneeDetailAddress) {
		this.consigneeDetailAddress = consigneeDetailAddress;
	}
	public String getConsigneeZip() {
		return consigneeZip;
	}
	public void setConsigneeZip(String consigneeZip) {
		this.consigneeZip = consigneeZip;
	}
	public Long getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}