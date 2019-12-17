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
@Table(name="sys_member")
public class Member implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long memberId;					// 会员主键	
	private String memberName;				// 会员名称
	private String nickname;				// 昵称
	private String password;				// 登录密码
	private Grade grade;					// 会员等级
	private Long point;						// 积分
	private String cellphone;				// 联系电话
	private String email;					// 电子邮箱
	private String imgUrl;					// 头像
	private Date createTime;				// 注册时间
	private Status status;					// 会员状态
	@JsonIgnore
	private List<Cart> cartList; 			// 购物车列表
		
	public Member() {}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@ManyToOne(targetEntity=Grade.class, fetch=FetchType.LAZY, 
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="grade_id")
	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	public Long getPoint() {
		return point;
	}
	public void setPoint(Long point) {
		this.point = point;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	@OneToMany(targetEntity=Cart.class, fetch=FetchType.LAZY, mappedBy="member",
			cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	public List<Cart> getCartList() {
		return cartList;
	}
	public void setCartList(List<Cart> cartList) {
		this.cartList = cartList;
	}
	
}