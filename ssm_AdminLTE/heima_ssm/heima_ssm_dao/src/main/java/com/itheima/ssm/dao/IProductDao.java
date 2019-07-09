package com.itheima.ssm.dao;

import com.itheima.ssm.domain.Product;

import java.util.List;

public interface IProductDao {

    /**
     * 查询
     * @return
     */
    public List<Product> findAll();

    /**
     * 添加
     * @param product
     */
    public void save(Product product);
}
