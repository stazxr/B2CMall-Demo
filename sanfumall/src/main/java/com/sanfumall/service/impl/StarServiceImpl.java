package com.sanfumall.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.common.pojo.entity.Star;
import com.sanfumall.dao.StarDao;
import com.sanfumall.service.StarService;

@Service("starService")
@Transactional
public class StarServiceImpl implements StarService {

	@Resource(name="starDao")
	private StarDao starDao;
	
	/**
	 * 获取收藏列表
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public List<Star> getListByMember(Long memberId) throws Exception {
		return starDao.findListByMember(memberId);
	}

	/**
	 * 添加收藏信息
	 * @param star
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaStar(Star star) throws Exception {
		try {
			starDao.save(star);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据starId获取Star对象
	 * @param starId
	 * @return Star
	 * @throws Exception
	 */
	public Star getStarById(Long starId) throws Exception {
		return starDao.getOne(starId);
	}

	/**
	 * 删除收藏信息
	 * @param star
	 * @return Booean
	 * @throws Exception
	 */
	public Boolean deleteStar(Star star) throws Exception {
		try {
			starDao.delete(star);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据productId与memberId获取Star对象
	 * @param productId
	 * @param memberId
	 * @return Star
	 * @throws Exception
	 */
	public Star getStarByProductAndMember(Long productId, Long memberId) throws Exception {
		return starDao.getStarByProductAndMember(productId, memberId);
	}
	
}