package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import  bean.OrderItem;
import  dao.OrderItemDao;
import  util.DbUtil;


public class OrderItemDaoImpl implements OrderItemDao {

	@Override
	public boolean orderAdd(OrderItem orderItem) {
		String sql="insert into s_orderItem(cakeId,orderId,quantity) values(?,?,?)";
		
		int i= DbUtil.excuteUpdate(sql,orderItem.getCakeId(),orderItem.getOrderId(),orderItem.getQuantity());
		
		return i>0?true:false;
	}

	@Override
	public List<OrderItem> findItemByOrderId(int orderId) {
		List<OrderItem> lo=new ArrayList<>();
		String sql="select * from s_orderItem where orderId=?";
		List<Map<String, Object>> query = DbUtil.executeQuery(sql, orderId);
		if(query.size()>0) {
			for(Map<String,Object> map:query) {
				OrderItem oItem=new OrderItem(map);
				lo.add(oItem);
			}
			
		}
		return lo;
	}

}
