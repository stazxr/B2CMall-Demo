package com.sanfumall.common.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 数据持久层基础接口，数据持久层的接口继承该接口获得相关的CURD方法，以及分页等查询方法
 * @author SunTao
 * @param <T> 对应对象的类型
 * @param <ID> 主键的类型
 */
@NoRepositoryBean
public interface BaseDao<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>{}
