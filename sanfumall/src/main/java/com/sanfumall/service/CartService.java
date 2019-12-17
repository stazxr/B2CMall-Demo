package com.sanfumall.service;

import java.util.List;

import com.sanfumall.common.pojo.entity.Cart;

public interface CartService {

	/**
	 * 获取会员对应的购物车列表
	 * @param memberId
	 * @return List<Cart>
	 * @throws Exception
	 */
	public List<Cart> getCartListByMember(Long memberId) throws Exception;

	/**
	 * 持久化Cart对象
	 * @param cart
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaCart(Cart cart) throws Exception;

	/**
	 * 根据cartId获取购物车对象
	 * @param cartId
	 * @return	Cart对象
	 * @throws Exception
	 */
	public Cart getCartById(Long cartId) throws Exception;

	/**
	 * 删除购物车
	 * @param cart
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean deleteCart(Cart cart) throws Exception;

}
