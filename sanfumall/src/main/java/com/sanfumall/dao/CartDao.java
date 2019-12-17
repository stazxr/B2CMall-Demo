package com.sanfumall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Cart;

public interface CartDao extends BaseDao<Cart, Long> {

	/**
	 * 获取会员对应的购物车列表
	 * @param memberId
	 * @return List<Cart>
	 * @throws Exception
	 */
	@Query("from Cart c where c.member.memberId=:memberId order by c.createTime asc")
	public List<Cart> findCartListByMemberId(@Param("memberId")Long memberId) throws Exception;

}
