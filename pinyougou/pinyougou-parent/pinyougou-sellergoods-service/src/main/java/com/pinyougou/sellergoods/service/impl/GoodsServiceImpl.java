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
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
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

		setItemList(goods);
	}


	private void setItemValus(Goods goods,TbItem item){
		//添加商品spu编号
		item.setGoodsId(goods.getGoods().getId());
		//添加商家ID
		item.setSellerId(goods.getGoods().getSellerId());
		//添加商品分类三级Id
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
	}

	//插入sku列表数据
	private void setItemList(Goods goods){
		if ("1".equals(goods.getGoods().getIsEnableSpec())){
			//实现sku商品的保存
			for (TbItem item : goods.getItemList()){
				//添加商品标题
				String title = goods.getGoods().getGoodsName();
				Map<String,Object> specMap = JSON.parseObject(item.getSpec());
				for (String key : specMap.keySet()){
					title += " "+specMap.get(key);
				}
				item.setTitle(title);

				setItemValus(goods,item);

				tbItemMapper.insert(item);
			}
		}else{
			TbItem item = new TbItem();

			item.setTitle(goods.getGoods().getGoodsName());
			item.setPrice(goods.getGoods().getPrice());
			item.setStatus("1");
			item.setIsDefault("1");
			item.setNum(99999);
			item.setSpec("{}");

			setItemValus(goods,item);

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
	public void update(Goods goods){

		//如果是重新修改的商品要修改审核状态
		goods.getGoods().setAuditStatus("0");
		//保存商品表
		goodsMapper.updateByPrimaryKey(goods.getGoods());
		//保存扩展表
		goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());

		//先删除原有得sku列中的数据
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goods.getGoods().getId());
		tbItemMapper.deleteByExample(example);

		//添加新的sku列表数据
		setItemList(goods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){

		Goods good = new Goods();

		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		good.setGoods(tbGoods);

		TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		good.setGoodsDesc(tbGoodsDesc);

        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(id);
        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
        good.setItemList(tbItems);


        return good;
    }

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			TbGoods goods = goodsMapper.selectByPrimaryKey(id);
			goods.setIsDelete("1");
			goodsMapper.updateByPrimaryKey(goods);
		}
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();

		//添加一条查询的条件就是过滤掉已经逻辑删除的商品
			criteria.andIsDeleteIsNull();
		
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

	@Override
	public void updateStatus(Long[] ids, String status) {

		for (Long id : ids) {

			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}

}
