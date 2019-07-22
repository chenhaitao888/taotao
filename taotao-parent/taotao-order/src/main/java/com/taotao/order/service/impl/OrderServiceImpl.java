package com.taotao.order.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.dao.CodisManager;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import com.taotao.result.TaotaoResult;

/**
 * 订单service
 * <p>Title: OrderService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月16日上午10:06:20
 * @version:1.0
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private CodisManager codisManager;
	@Value("${ORDER_GEN_KEY}")
	private String ORDER_GEN_KEY;
	@Value("${ORDER_DETAIL_GEN_KEY}")
	private String ORDER_DETAIL_GEN_KEY;
	@Value("${ORDER_INIT_KEY}")
	private String ORDER_INIT_KEY;
	
	/**
	 * 创建订单
	 * <p>Title: orderCreate</p>
	 * <p>Description: </p>
	 * @param order
	 * @param orderItems
	 * @param orderShipping
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult orderCreate(TbOrder order, List<TbOrderItem> orderItems,
			TbOrderShipping orderShipping) {
		/**
		 * 向订单表插入记录
		 */
		if(order == null){
			return TaotaoResult.build(400, "订单信息为空");
		}
		//补全订单信息
		order.setUpdateTime(new Date());
		order.setCreateTime(new Date());
		//1.未付款,2已付款,3.未发货,4.已发货,5.交易成功,6.交易关闭
		order.setStatus(1);
		//0.未评价,1.已评价
		order.setBuyerRate(0);
		/*使用redis incr命令生成orderId*/
		if(StringUtils.isBlank(codisManager.get(ORDER_GEN_KEY))){
			codisManager.set(ORDER_GEN_KEY, ORDER_INIT_KEY);
		}
		long orderId = codisManager.incr(ORDER_GEN_KEY);
		order.setOrderId(orderId + "");
		//向表中插入数据
		orderMapper.insert(order);
		/**
		 * 向订单明细表插入记录
		 */
		/*补全订单明细表*/
		if(orderItems.size() == 0 || orderItems == null){
			return TaotaoResult.build(400, "订单明细为空");
		}
		for (TbOrderItem tbOrderItem : orderItems) {
			//订单明细id
			long id = codisManager.incr(ORDER_DETAIL_GEN_KEY);
			tbOrderItem.setId(id + "");
			tbOrderItem.setOrderId(orderId + "");
			orderItemMapper.insert(tbOrderItem);
		}
		/**
		 * 向物流表中插入记录
		 */
		if(orderShipping == null){
			TaotaoResult.build(400, "物流信息为空");
		}
		/*补全物流信息*/
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShipping.setOrderId(orderId + "");
		orderShippingMapper.insert(orderShipping);
		return TaotaoResult.ok(orderId);
	}

}
