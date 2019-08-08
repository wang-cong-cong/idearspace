package com.pinyougou.seckill.service.impl;
import java.util.Date;
import java.util.List;

import com.pinyougou.dao.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import org.apache.zookeeper.data.Id;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.dao.TbSeckillOrderMapper;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.pojo.TbSeckillOrderExample;
import com.pinyougou.pojo.TbSeckillOrderExample.Criteria;
import com.pinyougou.seckill.service.SeckillOrderService;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import util.IdWorker;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

	@Autowired
	private TbSeckillOrderMapper seckillOrderMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSeckillOrder> findAll() {
		return seckillOrderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSeckillOrder> page=   (Page<TbSeckillOrder>) seckillOrderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbSeckillOrder seckillOrder) {
		seckillOrderMapper.insert(seckillOrder);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbSeckillOrder seckillOrder){
		seckillOrderMapper.updateByPrimaryKey(seckillOrder);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbSeckillOrder findOne(Long id){
		return seckillOrderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			seckillOrderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSeckillOrder seckillOrder, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSeckillOrderExample example=new TbSeckillOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(seckillOrder!=null){			
						if(seckillOrder.getUserId()!=null && seckillOrder.getUserId().length()>0){
				criteria.andUserIdLike("%"+seckillOrder.getUserId()+"%");
			}
			if(seckillOrder.getSellerId()!=null && seckillOrder.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+seckillOrder.getSellerId()+"%");
			}
			if(seckillOrder.getStatus()!=null && seckillOrder.getStatus().length()>0){
				criteria.andStatusLike("%"+seckillOrder.getStatus()+"%");
			}
			if(seckillOrder.getReceiverAddress()!=null && seckillOrder.getReceiverAddress().length()>0){
				criteria.andReceiverAddressLike("%"+seckillOrder.getReceiverAddress()+"%");
			}
			if(seckillOrder.getReceiverMobile()!=null && seckillOrder.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+seckillOrder.getReceiverMobile()+"%");
			}
			if(seckillOrder.getReceiver()!=null && seckillOrder.getReceiver().length()>0){
				criteria.andReceiverLike("%"+seckillOrder.getReceiver()+"%");
			}
			if(seckillOrder.getTransactionId()!=null && seckillOrder.getTransactionId().length()>0){
				criteria.andTransactionIdLike("%"+seckillOrder.getTransactionId()+"%");
			}
	
		}
		
		Page<TbSeckillOrder> page= (Page<TbSeckillOrder>)seckillOrderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;

	@Autowired
	private IdWorker idWorker;

	/**
	 * 修改库存信息，存储秒杀订单
	 * @param seckillId
	 * @param userId
	 */
	@Override
	public void submitOrder(long seckillId, String userId) {
		//根据seckillId从redis中取出商品
		TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(seckillId);
		if (seckillGoods==null){
			throw new RuntimeException("商品不存在");
		}
		if (seckillGoods.getStockCount()<=0){
			throw new RuntimeException("商品已抢光");
		}
		//减少库存量
		seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
		redisTemplate.boundHashOps("seckillGoods").put(seckillId,seckillGoods);
		if (seckillGoods.getStockCount() == 0){
			//库存为0时把数据同步到数据库
			seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
			//从缓存中删除
			redisTemplate.boundHashOps("seckillGoods").delete(seckillId);
		}

		//存储订单
		TbSeckillOrder tbSeckillOrder = new TbSeckillOrder();
		long orderId = idWorker.nextId();
		tbSeckillOrder.setId(orderId);
		tbSeckillOrder.setSeckillId(seckillId);
		tbSeckillOrder.setUserId(userId);
		tbSeckillOrder.setSellerId(seckillGoods.getSellerId());
		tbSeckillOrder.setCreateTime(new Date());
		tbSeckillOrder.setStatus("0");
		tbSeckillOrder.setMoney(seckillGoods.getCostPrice());
		redisTemplate.boundHashOps("seckillOrder").put(userId,tbSeckillOrder);
	}

	@Override
	public TbSeckillOrder seachOrderFromRedisByUserId(String userId) {
		return (TbSeckillOrder) redisTemplate.boundHashOps("seckillOrder").get(userId);
	}

	@Override
	public void saveOrderFromRedisToDb(String userId, Long orderId, String transactionId) {
		TbSeckillOrder seckillOrder = (TbSeckillOrder) redisTemplate.boundHashOps("seckillOrder").get(userId);
		if (seckillOrder == null){
			throw new RuntimeException("订单不存在");
		}
		//如果与传过来的id不相符
		if (seckillOrder.getId().longValue()!= orderId.longValue()){
			throw new RuntimeException("订单不相符");
		}
		//交易流水号
		seckillOrder.setTransactionId(transactionId);
		//支付时间
		seckillOrder.setPayTime(new Date());
		seckillOrder.setStatus("1");
		//存入数据库
		seckillOrderMapper.insert(seckillOrder);
		//从缓存中删除
		redisTemplate.boundHashOps("seckillOrder").delete(userId);
	}

	@Override
	public void deleteOrderFromRedis(String userId, Long orderId) {
		//从缓存出提取订单
		TbSeckillOrder seckillOrder = (TbSeckillOrder) redisTemplate.boundHashOps("seckillOrder").get(userId);
		if (seckillOrder != null){
			//从缓存中删除订单
			redisTemplate.boundHashOps("seckillOrder").delete(userId);
			//获取缓存中的商品
			TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(seckillOrder.getSeckillId());
			//判断缓存中是否存在该商品
			if (seckillGoods != null){
				//有商品修改数量
				seckillGoods.setStockCount(seckillGoods.getStockCount()+1);
				//存入缓存
				redisTemplate.boundHashOps("seckillGoods").put(seckillOrder.getSeckillId(),seckillGoods);
			}else{
				//没有商品从数据库中查出该商品
				TbSeckillGoods tbSeckillGoods = seckillGoodsMapper.selectByPrimaryKey(seckillOrder.getSeckillId());
				//修改商品数量
				tbSeckillGoods.setStockCount(seckillGoods.getStockCount());
				//存入缓存
				redisTemplate.boundHashOps("seckillGoods").put(seckillOrder.getSeckillId(),tbSeckillGoods);
			}
		}
	}

}
