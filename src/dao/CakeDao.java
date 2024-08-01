package dao;

import java.util.List;

import bean.Cake;
import bean.Catalog;
import bean.PageBean;

import net.sf.json.JSONObject;

public interface CakeDao {
	// 获取商品总数
	long cakeReadCount();
	
	//按书名获取商品总数
	long cakeReadCount(String cakename);
	
	public List<Cake> cakeList(PageBean pageBean, String cakename);

	// 获取商品分页列表(视图)
	List<Cake> cakeList(PageBean pageBean);

	// 按分类获取商品数量
	long cakeReadCount(int catalogId);

	// 按分类获取商品分页列表(视图)
	List<Cake> cakeList(PageBean pageBean, int catalogId);

	// 增加商品
	boolean cakeAdd(Cake cake);

	// 根据商品id查找商品信息(视图)
	Cake findCakeById(int cakeId);

	// 根据商品名称查找商品是否存在
	boolean findCakeByCakeName(String cakeName);

	// 更新商品信息
	boolean cakeUpdate(Cake cake);

	// 根据id删除商品
	boolean cakeDelById(int cakeId);

	// 根据id串查询图片id串
	String findimgIdByIds(String ids);

	// 批量删除商品
	boolean cakeBatDelById(String ids);

	// 随机获取指定数量书
	List<Cake> cakeList(int num);

	// 获取指定数量新添加的商品
	List<Cake> newCakes(int num);

}
