package com.sanfumall.common.pojo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="sys_category")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long categoryId;				// 类别ID
	private String categoryName;			// 类别名称
	private Category parent;				// 上级类别
	private Integer sortOrder;				// 排序字段
	private Status status;					// 状态
	@JsonIgnore
	private List<Category> categoryList;    // 子集合类别
	@JsonIgnore
	private List<Attribute> attributeList;  // 拥有的属性集合
	@JsonIgnore
	private User createUser;				// 创建者
	private Date createTime;				// 创建时间
	@JsonIgnore
	private User updateUser;				// 更新者
	private Date updateTime;				// 更新时间
	
	public Category() {}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	@ManyToOne(targetEntity=Category.class, fetch=FetchType.EAGER,
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="parent_id")
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	@ManyToOne(targetEntity=Status.class, fetch=FetchType.EAGER, 
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="status_id")
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	@OneToMany(targetEntity=Category.class, mappedBy="parent",fetch=FetchType.EAGER,
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	public List<Category> getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}
	@OneToMany(targetEntity=Attribute.class, mappedBy="category",fetch=FetchType.LAZY,
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	public List<Attribute> getAttributeList() {
		return attributeList;
	}
	public void setAttributeList(List<Attribute> attributeList) {
		this.attributeList = attributeList;
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

	@Override
	public String toString() {
		return "categoryId:" + categoryId + "\tcategoryName:" + categoryName + "\tparent:[" + parent + "]\tstatus:[" + status + "]";
	}
	
}