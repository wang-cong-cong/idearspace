package com.pinyougou.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Brand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author cong
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    @RequestMapping("/findAll.do")
    public List<Brand> findAll(){
        return brandService.findAll();
    }

    @RequestMapping("/findPage.do")
    public PageResult<Brand> findPage(int page,int size){
      return  brandService.findPage(page,size);
    }

    @RequestMapping("/add.do")
    public Result add(@RequestBody Brand brand){
        try {
            brandService.add(brand);
            return new Result(true,"增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"增加失败");
        }
    }

    @RequestMapping("/findOne.do")
    public Brand findOne(long id){
     return brandService.findOne(id);
    }

    @RequestMapping("/update.do")
    public Result update(@RequestBody Brand brand){
        try {
            brandService.update(brand);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改失败");
        }
    }

    @RequestMapping("/delete.do")
    public Result delete(long[] ids){

        try {
            brandService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }


    @RequestMapping("/search.do")
    public PageResult search(@RequestBody Brand brand,int page,int size){
      return   brandService.findPage(brand,page,size);
    }

    @RequestMapping("/selectBrandList.do")
    public List<Map> selectBrandList(){
        return brandService.selectBrandList();
    }
}
