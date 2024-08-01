package dao;

import java.util.List;

import bean.Order;
import bean.PageBean;
import bean.User;

public interface OrderDao {
	
	//增加一个订单记录
	boolean orderAdd(Order order);
	//查找订单编号通过订单号
	int findOrderIdByOrderNum(String orderNum);
	//统计总订单数
	long orderReadCount();
	//根据订单号统计总订单数
	long orderReadCount(String ordernum);
	//统计总订单数(by userId)
	long orderReadCount(int userId);
	//统计总订单数(by orderStatus)
	long orderReadCountByStatus(int status);
	//统计总订单数(by orderStatus and ordernum)
	long orderReadCountByStatus(int status,String ordernum);
	//获得订单列表（分页）,条件用户id
	List<Order> orderList(PageBean pageBean);
	//获得订单列表（分页）,条件订单号
	List<Order> orderList(PageBean pageBean,String ordernum);
	//获得订单列表（分页）,条件用户id
	List<Order> orderList(PageBean pageBean,int userId);
	//获得订单列表（分页）,条件orderStatus
	List<Order> orderListByStatus(PageBean pb, int status);
	//获得订单列表（分页）,条件orderStatus  ordernum
	List<Order> orderListByStatus(PageBean pb, int status,String ordernum);
	//查找订单编号通过订单号
	Order findOrderByOrderId(int orderId);
	
	//更改订单状态
	boolean orderStatus(int orderId,int status);
	
	boolean deleteOrder(int orderid);
	boolean deleteOrderItem(int orderid);

	
	
}
