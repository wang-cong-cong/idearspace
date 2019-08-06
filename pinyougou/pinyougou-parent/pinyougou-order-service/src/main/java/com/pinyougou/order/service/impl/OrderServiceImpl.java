package com.pinyougou.order.service.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pinyougou.dao.TbOrderItemMapper;
import com.pinyougou.dao.TbPayLogMapper;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.dao.TbOrderMapper;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderExample;
import com.pinyougou.pojo.TbOrderExample.Criteria;
import com.pinyougou.order.service.OrderService;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;

	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private TbPayLogMapper tbPayLogMapper;

	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	@Transactional
	public void add(TbOrder order) {
		//从redis中获取购物车列表
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());

		//创建集合用于存放订单
		List<String> orderList = new ArrayList<>();
		//总金额
		double total_money = 0;
		//遍历购物车列表添加到新订单
		for (Cart cart : cartList) {
			TbOrder order1 = new TbOrder();
			long orderId = idWorker.nextId();
			order1.setOrderId(orderId);
			order1.setUserId(order.getUserId());//订单id
			order1.setPaymentType(order.getPaymentType());//支付类型
			order1.setStatus("1");//状态为未付款
			order1.setCreateTime(new Date());//订单创建时间
			order1.setUpdateTime(new Date());//更新时间
			order1.setReceiverAreaName(order.getReceiverAreaName());//地址
			order1.setReceiverMobile(order.getReceiverMobile());//手机号
			order1.setReceiver(order.getReceiver());//收货人
			order1.setSourceType(order.getSourceType());//订单来源
			order1.setSellerId(order.getSellerId());//商家ID
			double money = 0;
			for (TbOrderItem tbOrderItem : cart.getOrderItemList()) {
				tbOrderItem.setOrderId(orderId);
				tbOrderItem.setId(idWorker.nextId());
				tbOrderItem.setSellerId(cart.getSellerId());
				money+=tbOrderItem.getTotalFee().doubleValue();

				tbOrderItemMapper.insert(tbOrderItem);
			}
			order1.setPayment(new BigDecimal(money));
			orderMapper.insert(order1);

			orderList.add(String.valueOf(orderId));
			total_money += money;
		}

		//添加支付日志
		if ("1".equals(order.getPaymentType())){
			TbPayLog paylog = new TbPayLog();
			//支付订单号
			paylog.setOutTradeNo(String.valueOf(idWorker.nextId()));
			//创建时间
			paylog.setCreateTime(new Date());
			//商户ID
			paylog.setUserId(order.getUserId());
			//订单ID串
			paylog.setOrderList(orderList.toString().replace("[","").replace("]",""));
			//交易金额
			paylog.setTotalFee((long)(total_money*100));
			//交易状态
			paylog.setTradeState("0");
			//支付方式,微信
			paylog.setPayType("1");

			tbPayLogMapper.insert(paylog);

			//将日志存入redis中
			redisTemplate.boundHashOps("paylog").put(order.getUserId(),paylog);
		}

		//清除购物车信息
		redisTemplate.boundHashOps("cartList").delete(order.getUserId());
	}

	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 从缓存中提取日志
	 * @param userId
	 * @return
	 */
	@Override
	public TbPayLog searchPaylogFromRedis(String userId) {
		return (TbPayLog) redisTemplate.boundHashOps("paylog").get(userId);
	}

	@Override
	public void updateOrderStatus(String out_trade_no, String transcation_id) {
		//修改日志表中的数据
		TbPayLog tbPayLog = tbPayLogMapper.selectByPrimaryKey(out_trade_no);
		//支付时间
		tbPayLog.setPayTime(new Date());
		//微信订单流水号
		tbPayLog.setTransactionId(transcation_id);
		//交易成功
		tbPayLog.setTradeState("1");
		//修改数据
		tbPayLogMapper.updateByPrimaryKey(tbPayLog);
		//修改订单表中的数据
		String orderList = tbPayLog.getOrderList();
		String[] orderString = orderList.split(",");
		for (String orderId : orderString) {
			TbOrder tbOrder = orderMapper.selectByPrimaryKey(Long.valueOf(orderId));
			//已付款的状态，1为未支付2为已支付
			tbOrder.setStatus("2");
			//支付时间
			tbOrder.setPaymentTime(new Date());
			//修改订单表
			orderMapper.updateByPrimaryKey(tbOrder);
		}
		//清除缓存中的paylog
		redisTemplate.boundHashOps("paylog").delete(tbPayLog.getUserId());
	}

}
