package com.sanfumall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sanfumall.common.base.dao.BaseDao;
import com.sanfumall.common.pojo.entity.Star;

public interface StarDao extends BaseDao<Star, Long> {
	
	/**
	 * 获取收藏列表
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	@Query("from Star s where s.member.memberId=:memberId")
	public List<Star> findListByMember(@Param("memberId")Long memberId) throws Exception;

	/**
	 * 根据productId与memberId获取Star对象
	 * @param productId
	 * @param memberId
	 * @return Star
	 * @throws Exception
	 */
	@Query("from Star s where s.product.productId=:productId and s.member.memberId=:memberId")
	public Star getStarByProductAndMember(@Param("productId")Long productId, @Param("memberId")Long memberId) throws Exception;

}
