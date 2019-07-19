package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.dao.BrandMapper;
import com.pinyougou.pojo.Brand;
import com.pinyougou.pojo.BrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;

/**
 * @author cong
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;


    @Override
    public List<Brand> findAll(){
        return brandMapper.selectByExample(null);
    }

    @Override
    public PageResult<Brand> findPage(int pageNum, int pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<Brand> brands = brandMapper.selectByExample(null);
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        long total = pageInfo.getTotal();
        List<Brand> list = pageInfo.getList();
        return new PageResult<Brand>(total,list);
    }

    @Override
    public void add(Brand brand) {
        brandMapper.insert(brand);
    }

    @Override
    public Brand findOne(long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    @Override
    public void delete(long[] ids) {
        for (long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public PageResult<Brand> findPage(Brand brand, int pageNum, int pageSize) {

        BrandExample example = new BrandExample();
        BrandExample.Criteria criteria = example.createCriteria();

        if (brand!=null){
            if (brand.getName()!=null&&brand.getName().length()>0){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if (brand.getFirstChar()!=null&&brand.getFirstChar().length()>0){
                criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");
            }
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Brand> brands = brandMapper.selectByExample(example);
         PageInfo<Brand> pageInfo = new PageInfo<>(brands);

        long total = pageInfo.getTotal();
        List<Brand> list = pageInfo.getList();

        return new PageResult<Brand>(total,list);
    }

    @Override
    public List<Map> selectBrandList() {
        return brandMapper.selectBrandList();
    }


}
