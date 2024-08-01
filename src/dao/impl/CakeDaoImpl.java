package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import  bean.Cake;
import  bean.Catalog;
import  bean.PageBean;
import  bean.UpLoadImg;
import  dao.CakeDao;
import  util.DateUtil;
import  util.DbUtil;

public class CakeDaoImpl implements CakeDao {

	@Override
	public List<Cake> cakeList(PageBean pageBean) {
		List<Cake> list = new ArrayList<>();

		String sql = "select * from view_cake limit ?,?";
		// 查询的分页结果集
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql, (pageBean.getCurPage() - 1) * pageBean.getMaxSize(),
				pageBean.getMaxSize());

		// 把查询的cake结果由List<Map<String, Object>>转换为List<Cake>
		if (lm.size() > 0) {
			for (Map<String, Object> map : lm) {
				Cake cake = new Cake(map);
				list.add(cake);
			}
		}

		return list;
	}

	@Override
	public long cakeReadCount() {
		String sql = "select count(*) as count from s_cake";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql);
		return lm.size() > 0 ? (long) lm.get(0).get("count") : 0;
	}

	@Override
	public boolean cakeAdd(Cake cake) {
		String sql = "insert into s_cake(cakeName,catalogId,price,description,imgId,addTime) values(?,?,?,?,?,?)";

		int i = DbUtil.excuteUpdate(sql, cake.getCakeName(), cake.getCatalog().getCatalogId(), 
				cake.getPrice(), cake.getDescription(), cake.getUpLoadImg().getImgId(),
				DateUtil.getTimestamp());

		return i > 0 ? true : false;
	}

	@Override
	public Cake findCakeById(int cakeId) {
		String sql = "select * from view_cake where cakeId=?";
		Cake cake = null;
		List<Map<String, Object>> list = DbUtil.executeQuery(sql, cakeId);
		if (list.size() > 0) {
			cake = new Cake(list.get(0));
		}
		return cake;
	}

	/**
	 * 
	 */
	@Override
	public boolean findCakeByCakeName(String cakeName) {
		String sql = "select * from s_cake where cakeName=?";
		List<Map<String, Object>> list = DbUtil.executeQuery(sql, cakeName);
		return list.size() > 0 ? true : false;
	}

	/**
	 * 更新图书信息
	 */
	@Override
	public boolean cakeUpdate(Cake cake) {
		String sql = "update s_cake set catalogId=?,price=?,description=? where cakeId=?";
		int i = DbUtil.excuteUpdate(sql, cake.getCatalogId(),  cake.getPrice(),
				cake.getDescription(), cake.getCakeId());
		return i > 0 ? true : false;
	}

	/**
	 * 图书删除
	 */
	@Override
	public boolean cakeDelById(int cakeId) {
		String sql = "delete from s_cake where cakeId=?";
		int i = DbUtil.excuteUpdate(sql, cakeId);
		return i > 0 ? true : false;
	}

	/**
	 * 批量查询
	 */
	@Override
	public String findimgIdByIds(String ids) {
		String imgIds = "";
		String sql = "select imgId from s_cake where cakeId in(" + ids + ")";
		List<Map<String, Object>> list = DbUtil.executeQuery(sql);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (i != list.size() - 1) {
					imgIds += list.get(i).get("imgId") + ",";
				} else {
					imgIds += list.get(i).get("imgId");
				}
			}
		}
		return imgIds;
	}

	// 批量删除
	@Override
	public boolean cakeBatDelById(String ids) {
		String sql = "delete from s_cake where cakeId in(" + ids + ")";
		int i = DbUtil.excuteUpdate(sql);
		return i > 0 ? true : false;
	}

	// 随机查询一定数量的书
	@Override
	public List<Cake> cakeList(int num) {
		List<Cake> list = new ArrayList<>();
		String sql = "select * from view_cake order by rand() LIMIT ?";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql, num);
		// 把查询的cake结果由List<Map<String, Object>>转换为List<Cake>
		if (lm.size() > 0) {
			for (Map<String, Object> map : lm) {
				Cake cake = new Cake(map);
				list.add(cake);
			}
		}
		return list;
	}

	/**
	 * 查询指定数量新书
	 */
	@Override
	public List<Cake> newCakes(int num) {
		List<Cake> list = new ArrayList<>();
		String sql = "SELECT * FROM view_cake ORDER BY addTime desc limit 0,?";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql, num);
		// 把查询的cake结果由List<Map<String, Object>>转换为List<Cake>
		if (lm.size() > 0) {
			for (Map<String, Object> map : lm) {
				Cake cake = new Cake(map);
				list.add(cake);
			}
		}
		return list;
	}

	/**
	 * 按分类id统计图书数量
	 */
	@Override
	public long cakeReadCount(int catalogId) {
		String sql = "select count(*) as count from s_cake where catalogId=?";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql, catalogId);
		return lm.size() > 0 ? (long) lm.get(0).get("count") : 0;
	}

	/**
	 * 按分类id获取图书列表
	 */
	@Override
	public List<Cake> cakeList(PageBean pageBean, int catalogId) {
		List<Cake> list = new ArrayList<>();

		String sql = "select * from view_cake where catalogId=? limit ?,?";
		// 查询的分页结果集
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql, catalogId,
				(pageBean.getCurPage() - 1) * pageBean.getMaxSize(), pageBean.getMaxSize());

		// 把查询的cake结果由List<Map<String, Object>>转换为List<Cake>
		if (lm.size() > 0) {
			for (Map<String, Object> map : lm) {
				Cake cake = new Cake(map);
				list.add(cake);
			}
		}
		return list;
	}
	
	/**
	 * 按分类id获取图书列表
	 */
	@Override
	public List<Cake> cakeList(PageBean pageBean, String cakename) {
		List<Cake> list = new ArrayList<>();

		String sql = "select * from view_cake where cakeName like '%"+cakename+"%' limit ?,?";
		// 查询的分页结果集
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql,
				(pageBean.getCurPage() - 1) * pageBean.getMaxSize(), pageBean.getMaxSize());

		// 把查询的cake结果由List<Map<String, Object>>转换为List<Cake>
		if (lm.size() > 0) {
			for (Map<String, Object> map : lm) {
				Cake cake = new Cake(map);
				list.add(cake);
			}
		}
		return list;
	}


	@Override
	public long cakeReadCount(String cakename) {
		String sql = "select count(*) as count from s_cake where cakeName like '%"+cakename+"%'";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql);
		return lm.size() > 0 ? (long) lm.get(0).get("count") : 0;
	}

}
