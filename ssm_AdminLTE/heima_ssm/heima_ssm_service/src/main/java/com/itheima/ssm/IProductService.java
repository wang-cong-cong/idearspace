package com.itheima.ssm;

import com.itheima.ssm.domain.Product;

import java.util.List;


public interface IProductService {

    public List<Product> findAll();

    public void save(Product product);
}
