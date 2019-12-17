package com.sanfumall.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sanfumall.admin.dao.CategoryDao;
import com.sanfumall.admin.dao.ProductDao;
import com.sanfumall.admin.dao.StatusDao;
import com.sanfumall.admin.service.ProductService;
import com.sanfumall.common.pojo.entity.Category;
import com.sanfumall.common.pojo.entity.Product;
import com.sanfumall.common.pojo.entity.Status;
import com.sanfumall.common.pojo.vo.Page;

@Service(value="productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Resource(name="statusDao")
	private StatusDao statusDao;
	@Resource(name="productDao")
	private ProductDao productDao;
	@Resource(name="categoryDao")
	private CategoryDao categoryDao;	
	
	/**
	 * 根据商品名称获取商品
	 * @param productName
	 * @return
	 * @throws Exception
	 */
	public Product getproductByName(String productName) throws Exception {
		return productDao.findByProductName(productName);
	}

	/**
	 * 根据商品ID获取商品
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	public Product getproductById(Long productId) throws Exception {
		return productDao.getOne(productId);
	}
	
	/**
	 * 分页获取商品列表
	 * @param page 分页信息
	 * @return Page<Product>
	 * @throws Exception
	 */
	public Page<Product> getProductListByPage(Page<Product> page) throws Exception {
		// 根据page携带的分页信息获取分页查询limit的两个参数
		Integer pageSize = page.getPageSize();
		Integer startIndex = (page.getPageNum() - 1) * pageSize;
		// 首先判断是否为模糊查询
		String keyword = page.getKeyword();
		if (keyword != null && !"".equals(keyword)) {
			// 进行分页模糊查询
			// 判断是否是按状态名称查询
			Status status = statusDao.findByStatusName(keyword);
			if (status != null) {
				keyword = Long.toString(status.getStatusId());
			}
			// 判断是否是按类别名称查询
			Category category = categoryDao.findByCategoryName(keyword);
			if (category != null) {
				keyword = Long.toString(category.getCategoryId());
			}
			// 进行模糊分页查询，并将查询结果set到page对象中
			List<Product> productList = productDao.findProductListByLikePage(startIndex, pageSize, keyword.toUpperCase());
			page.setList(productList);
			// 获取在不分页的情况下进行模糊查询的总数量，并回填到page对象中
			Integer totalSize = productDao.findProductListByLike(keyword).size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		} else {
			// 进行普通的分页查询，并将查询结果set到page对象中
			List<Product> productList = productDao.findProductListByPage(startIndex, pageSize);
			page.setList(productList);
			// 获取其他分页信息，并回填到page对象中
			Integer totalSize = productDao.findAll().size();
			Integer totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);
			page.setTotalSize(totalSize);
			page.setTotalPage(totalPage);
		}
		return page;
	}

	/**
	 * 保存会修改商品
	 * @param product
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean savaProduct(Product product) throws Exception {
		if (product.getProductId() == null) {
			// 是保存操作，先保存商品，在根据Id绑定商品编号与排序字段
			try {
				product = productDao.save(product);
				String productNo = "SF" + System.currentTimeMillis();
				product.setProductNo(productNo);
				product.setSortOrder(Integer.valueOf(product.getProductId().toString()));
				productDao.save(product);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 修改操作
			try {
				productDao.save(product);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}