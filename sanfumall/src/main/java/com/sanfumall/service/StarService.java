package com.sanfumall.service;

import java.util.List;

import com.sanfumall.common.pojo.entity.Star;

public interface StarService {

	/**
	 * 获取收藏列表
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public List<Star> getListByMember(Long memberId) throws Exception;

	/**
	 * 添加收藏信息
	 * @param star
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaStar(Star star) throws Exception;

	/**
	 * 根据starId获取Star对象
	 * @param starId
	 * @return Star
	 * @throws Exception
	 */
	public Star getStarById(Long starId) throws Exception;

	/**
	 * 删除收藏信息
	 * @param star
	 * @return Booean
	 * @throws Exception
	 */
	public Boolean deleteStar(Star star) throws Exception;

	/**
	 * 根据productId与memberId获取Star对象
	 * @param productId
	 * @param memberId
	 * @return Star
	 * @throws Exception
	 */
	public Star getStarByProductAndMember(Long productId, Long memberId) throws Exception;

}
