package com.sanfumall.common.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="sys_product")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long productId;			// 商品ID
	private String productNo;		// 商品编号
	private Category category;		// 商品类型
	private String productName;		// 商品名称
	private String title;			// 标题
	private String detail;			// 详细介绍
	private String mainImg;			// 大图片
	private String subImg;			// 小图片
	private Integer sortOrder;		// 排序字段
	private Status status;			// 状态
	private Date putawayTime;		// 上架时间
	private Date soldoutTime;		// 下架时间
	@JsonIgnore
	private List<SKU> skuList;      // 商品对应的SKU集合
	@JsonIgnore
	private User createUser;		// 创建者
	private Date createTime;		// 创建时间
	@JsonIgnore
	private User updateUser;		// 更新者
	private Date updateTime;		// 更新时间
	
	private BigDecimal minPrice;	// 最低价
	private BigDecimal maxPrice;	// 最高价
	
	public Product() {}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getMainImg() {
		return mainImg;
	}
	public void setMainImg(String mainImg) {
		this.mainImg = mainImg;
	}
	public String getSubImg() {
		return subImg;
	}
	public void setSubImg(String subImg) {
		this.subImg = subImg;
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
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPutawayTime() {
		return putawayTime;
	}
	public void setPutawayTime(Date putawayTime) {
		this.putawayTime = putawayTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getSoldoutTime() {
		return soldoutTime;
	}
	public void setSoldoutTime(Date soldoutTime) {
		this.soldoutTime = soldoutTime;
	}
	@OneToMany(targetEntity=SKU.class, fetch=FetchType.LAZY,
			mappedBy="product",
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	public List<SKU> getSkuList() {
		return skuList;
	}
	public void setSkuList(List<SKU> skuList) {
		this.skuList = skuList;
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
	@Transient
	public BigDecimal getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}
	@Transient
	public BigDecimal getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

}