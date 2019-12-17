package com.sanfumall.common.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="sys_grade")
public class Grade implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long gradeId;			// 会员等级主键	
	private String gradeName;		// 会员等级名称
	private Long minPoint;			// 最小积分
	private Long maxPoint;			// 最大积分
	private BigDecimal discount;	// 折扣
	private Long sortOrder;			// 排序字段
	@JsonIgnore
	private User createUser;		// 创建者
	private Date createTime;		// 创建时间
	@JsonIgnore
	private User updateUser;		// 更新者
	private Date updateTime;		// 更新时间
	
	public Grade() {}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getGradeId() {
		return gradeId;
	}
	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public Long getMinPoint() {
		return minPoint;
	}
	public void setMinPoint(Long minPoint) {
		this.minPoint = minPoint;
	}
	public Long getMaxPoint() {
		return maxPoint;
	}
	public void setMaxPoint(Long maxPoint) {
		this.maxPoint = maxPoint;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public Long getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}
	@ManyToOne(targetEntity=User.class, fetch=FetchType.LAZY, 
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="create_user")
	public User getCreateUser() {
		return createUser;
	}
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@ManyToOne(targetEntity=User.class, fetch=FetchType.LAZY, 
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="update_user")
	public User getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(User updateUser) {
		this.updateUser = updateUser;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}