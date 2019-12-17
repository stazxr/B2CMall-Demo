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
@Table(name="sys_cart")
public class Cart implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long cartId;						// 购物车主键		
	private Member member;						// 创建会员
	private SKU sku;							// 商品SKU
	private Long count;							// 商品SKU的数量
	private Date createTime;					// 创建时间
	private Date updateTime;					// 更新时间
	
	public Cart() {}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getCartId() {
		return cartId;
	}
	public void setCartId(Long cartId) {
		this.cartId = cartId;
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
	@ManyToOne(targetEntity=SKU.class, fetch=FetchType.LAZY, 
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="sku_id")
	public SKU getSku() {
		return sku;
	}
	public void setSku(SKU sku) {
		this.sku = sku;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
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