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
@Table(name="sys_star")
public class Star implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long starId;						// 收藏主键
	private Member member;						// 创建会员
	private Product product;					// 收藏商品信息
	private Date createTime;					// 创建时间
	
	public Star() {}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getStarId() {
		return starId;
	}
	public void setStarId(Long starId) {
		this.starId = starId;
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
	@ManyToOne(targetEntity=Product.class, fetch=FetchType.LAZY, 
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="product_id")
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}