package com.pinyougou.sellergoods.service.impl;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.dao.*;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;

	@Autowired
	private TbItemMapper tbItemMapper;

	@Autowired
	private TbItemCatMapper tbItemCatMapper;

	@Autowired
	private TbSellerMapper tbSellerMapper;

	@Autowired
	private BrandMapper brandMapper;


	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		//设置申请状态
		goods.getGoods().setAuditStatus("0");
		goodsMapper.insert(goods.getGoods());

		//获取goods的id设置给gooddesc中的goodsid
		goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
		//添加扩展信息
		goodsDescMapper.insert(goods.getGoodsDesc());


		//实现sku商品的保存
		for (TbItem item : goods.getItemList()){
			//添加商品标题
			String title = goods.getGoods().getGoodsName();
			Map<String,Object> specMap = JSON.parseObject(item.getSpec());
			for (String key : specMap.keySet()){
				title += ""+specMap.get(key);
			}
			item.setTitle(title);
			//添加商品spu编号
			item.setGoodsId(goods.getGoods().getId());
			//添加商家ID
			item.setSellerId(goods.getGoods().getSellerId());
			//添加商品分类Id
			item.setCategoryid(goods.getGoods().getCategory3Id());
			//添加创建时间
			item.setCreateTime(new Date());
			//添加修改时间
			item.setUpdateTime(new Date());

			//添加品牌名称
			Brand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
			item.setBrand(brand.getName());

			//添加分类名称
			TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
			item.setCategory(tbItemCat.getName());

			//添加商家名称
			TbSeller tbSeller = tbSellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
			item.setSeller(tbSeller.getNickName());

			//获取图片地址
			List<Map> mapList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
			if (mapList.size()>0){
				//获取集合中第一个元素中的url
				String url = (String) mapList.get(0).get("url");
				item.setImage(url);
			}
			tbItemMapper.insert(item);
		}
	}



	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}
	
	/**
	 * 修改
	 */
	@Override
	public void update(TbGoods goods){
		goodsMapper.updateByPrimaryKey(goods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbGoods findOne(Long id){
		return goodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			goodsMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		
		if(goods!=null){			
						if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
