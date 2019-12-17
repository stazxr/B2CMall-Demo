package com.sanfumall.common.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="sys_sku")
public class SKU implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long skuId;					// SKUID
	private Product product;			// 所属的商品
	private Long store;					// 库存
	private BigDecimal price;			// 价格
	private BigDecimal discount;		// 折扣
	private Long point;					// 积分
	private BigDecimal currentPrice;    // 折后价
	@JsonIgnore
	private List<Value> valueList;		// 对应的属性值集合
	
	public SKU() {}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	@ManyToOne(targetEntity=Product.class, fetch=FetchType.EAGER, 
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="product_id")
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Long getStore() {
		return store;
	}
	public void setStore(Long store) {
		this.store = store;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public Long getPoint() {
		return point;
	}
	public void setPoint(Long point) {
		this.point = point;
	}
	@Transient
	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}
	@ManyToMany(targetEntity=Value.class, fetch=FetchType.LAZY,
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="sys_value_sku",
			joinColumns= {@JoinColumn(name="sku_id")},
			inverseJoinColumns= {@JoinColumn(name="value_id")})
	public List<Value> getValueList() {
		return valueList;
	}
	public void setValueList(List<Value> valueList) {
		this.valueList = valueList;
	}
	
}