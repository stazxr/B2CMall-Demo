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
@Table(name="sys_attribute")
public class Attribute implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long attributeId;		// 属性ID
	private String attributeName;	// 属性名称
	private Category category;		// 所属类别
	private Status status;			// 状态
	@JsonIgnore
	private List<Value> valueList;  // 属性对应的属性值集合
	@JsonIgnore
	private User createUser;		// 创建者
	private Date createTime;		// 创建时间
	@JsonIgnore
	private User updateUser;		// 更新者
	private Date updateTime;		// 更新时间
	
	public Attribute() {}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	@ManyToOne(targetEntity=Category.class, fetch=FetchType.EAGER, 
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="category_id")
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	@ManyToOne(targetEntity=Status.class, fetch=FetchType.LAZY, 
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="status_id")
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	@OneToMany(targetEntity=Value.class,fetch=FetchType.LAZY,mappedBy="attribute",
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	public List<Value> getValueList() {
		return valueList;
	}
	public void setValueList(List<Value> valueList) {
		this.valueList = valueList;
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
		return "attributeId:" + attributeId + "\tattributeName:" + attributeName + "\tcategory:[" + category + "]\tstatus:[" + status + "]";
	}
	
}