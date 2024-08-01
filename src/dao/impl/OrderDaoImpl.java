package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import  bean.Order;
import  bean.PageBean;
import  bean.User;
import  dao.OrderDao;
import  util.DbUtil;

public class OrderDaoImpl implements OrderDao {
	/**
	 * 向s_order插入一条订单记录
	 */
	@Override
	public boolean orderAdd(Order order) {
		String sql="insert into s_order(orderNum,userId,orderDate,orderStatus,money) values(?,?,?,?,?)";
		
		int i= DbUtil.excuteUpdate(sql,order.getOrderNum(),order.getUserId(),order.getOrderDate(),order.getOrderStatus(),order.getMoney());
		
		return i>0?true:false;	
	}
	/**
	 * by订单号查询订单编号
	 */
	@Override
	public int findOrderIdByOrderNum(String orderNum) {
		int orderId=0;
		String sql="select orderId from s_order where orderNum=?";
		List<Map<String, Object>> query = DbUtil.executeQuery(sql, orderNum);
		if(query.size()>0) {
			orderId=(int) query.get(0).get("orderId");
		}
		
		return orderId;
	}
	@Override
	public long orderReadCount(int userId) {
		String sql = "select count(*) as count from s_order where userId=?";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql,userId);
		return lm.size() > 0 ? (long) lm.get(0).get("count") : 0;
	}
	@Override
	public List<Order> orderList(PageBean pageBean,int userId) {
		List<Order> lo=new ArrayList<>();
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		
		String sql="select * from s_order where userId=? limit ?,?";
		
		list=DbUtil.executeQuery(sql,userId,(pageBean.getCurPage()-1)*pageBean.getMaxSize(),pageBean.getMaxSize());
		
		if(list.size()>0) {
			for(Map<String,Object> map:list) {
				Order order=new Order(map);
				lo.add(order);
			}
		}
		
		return lo;
	}
	
	@Override
	public List<Order> orderList(PageBean pageBean,String ordernum) {
		List<Order> lo=new ArrayList<>();
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		
		String sql="select * from s_order where orderNum like '%"+ordernum+"%' limit ?,?";
		
		list=DbUtil.executeQuery(sql,(pageBean.getCurPage()-1)*pageBean.getMaxSize(),pageBean.getMaxSize());
		
		if(list.size()>0) {
			for(Map<String,Object> map:list) {
				Order order=new Order(map);
				lo.add(order);
			}
		}
		
		return lo;
	}
	
	@Override
	public long orderReadCount() {
		String sql = "select count(*) as count from s_order";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql);
		return lm.size() > 0 ? (long) lm.get(0).get("count") : 0;
	}
	
	@Override
	public List<Order> orderList(PageBean pageBean) {
		List<Order> lo=new ArrayList<>();
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		
		String sql="select * from s_order limit ?,?";
		
		list=DbUtil.executeQuery(sql,(pageBean.getCurPage()-1)*pageBean.getMaxSize(),pageBean.getMaxSize());
		
		if(list.size()>0) {
			for(Map<String,Object> map:list) {
				Order order=new Order(map);
				lo.add(order);
			}
		}
		
		return lo;
	}
	
	@Override
	public Order findOrderByOrderId(int orderId) {
		Order order=null;
		String sql="select * from s_order where orderId=?";
		List<Map<String, Object>> query = DbUtil.executeQuery(sql, orderId);
		if(query.size()>0) {
			order=new Order(query.get(0));
		}
		
		return order;
	}
	
	@Override
	public long orderReadCountByStatus(int status) {
		String sql = "select count(*) as count from s_order where orderStatus=?";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql,status);
		return lm.size() > 0 ? (long) lm.get(0).get("count") : 0;
	}
	@Override
	public long orderReadCountByStatus(int status,String ordernum) {
		String sql = "select count(*) as count from s_order where orderStatus=? and orderNum like '%"+ordernum+"%'";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql,status);
		return lm.size() > 0 ? (long) lm.get(0).get("count") : 0;
	}
	
	@Override
	public List<Order> orderListByStatus(PageBean pageBean, int status) {
		List<Order> lo=new ArrayList<>();
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		
		String sql="select * from s_order where orderStatus=? limit ?,?";
		
		list=DbUtil.executeQuery(sql,status,(pageBean.getCurPage()-1)*pageBean.getMaxSize(),pageBean.getMaxSize());
		
		if(list.size()>0) {
			for(Map<String,Object> map:list) {
				Order order=new Order(map);
				lo.add(order);
			}
		}
		return lo;
	}
	
	@Override
	public List<Order> orderListByStatus(PageBean pageBean, int status, String ordernum) {
		List<Order> lo=new ArrayList<>();
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		
		String sql="select * from s_order where orderStatus=? and ordernum like '%"+ordernum+"%' limit ?,?";
		
		list=DbUtil.executeQuery(sql,status,(pageBean.getCurPage()-1)*pageBean.getMaxSize(),pageBean.getMaxSize());
		
		if(list.size()>0) {
			for(Map<String,Object> map:list) {
				Order order=new Order(map);
				lo.add(order);
			}
		}
		return lo;
	}
	
	@Override
	public boolean orderStatus(int orderId,int status) {
		String sql="update s_order set orderStatus=? where orderId=?";
		int i = DbUtil.excuteUpdate(sql, status,orderId);
		return i>0?true:false;	
	}
	@Override
	public long orderReadCount(String ordernum) {
		String sql = "select count(*) as count from s_order where orderNum like '%"+ordernum+"%'";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql);
		return lm.size() > 0 ? (long) lm.get(0).get("count") : 0;
	}
	@Override
	public boolean deleteOrder(int orderid) {
		String sql = "delete from s_order where orderId=?";
		int i = DbUtil.excuteUpdate(sql, orderid);
		return i > 0 ? true : false;
	}
	
	@Override
	public boolean deleteOrderItem(int orderid) {
		String sql = "delete from s_orderitem where orderId=?";
		int i = DbUtil.excuteUpdate(sql, orderid);
		return i > 0 ? true : false;
	}
	
}
