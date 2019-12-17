package com.sanfumall.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.common.pojo.entity.Cart;
import com.sanfumall.dao.CartDao;
import com.sanfumall.service.CartService;

@Service(value="cartService")
@Transactional
public class CartServiceImpl implements CartService {

	@Resource(name="cartDao")
	private CartDao cartDao;
	
	/**
	 * 获取会员对应的购物车列表
	 * @param memberId
	 * @return List<Cart>
	 * @throws Exception
	 */
	public List<Cart> getCartListByMember(Long memberId) throws Exception {
		return cartDao.findCartListByMemberId(memberId);
	}

	/**
	 * 持久化Cart对象
	 * @param cart
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaCart(Cart cart) throws Exception {
		try {
			cartDao.save(cart);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据cartId获取购物车对象
	 * @param cartId
	 * @return	Cart对象
	 * @throws Exception
	 */
	public Cart getCartById(Long cartId) throws Exception {
		return cartDao.getOne(cartId);
	}

	/**
	 * 删除购物车
	 * @param cart
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean deleteCart(Cart cart) throws Exception {
		try {
			cartDao.delete(cart);
			System.out.println(cart.getCartId());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}