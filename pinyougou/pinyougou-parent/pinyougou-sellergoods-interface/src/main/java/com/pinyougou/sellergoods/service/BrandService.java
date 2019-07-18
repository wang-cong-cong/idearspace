package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.Brand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 品牌接口
 * @author cong
 */
public interface BrandService {

    public List<Brand> findAll();

    /**
     * 品牌分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult<Brand> findPage(int pageNum,int pageSize);

    /**
     * 增加品牌
     * @param brand
     */
    public void add(Brand brand);

    /**
     * 查询指定id的品牌，用于回显数据
     * @param id
     * @return
     */
    public Brand findOne(long id);

    /**
     * 修改品牌
     * @param brand
     */
    public void update(Brand brand);


    /**
     * 批量删除
     * @param ids
     */
    public void delete(long[] ids);

    /**
     * 品牌分页并按照条件查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult<Brand> findPage(Brand brand,int pageNum,int pageSize);

    /**
     * 查询下拉框的品牌
     * @return
     */
    public List<Map>  selectBrandList();

}
