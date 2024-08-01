package servlet.cake;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Cake;
import bean.PageBean;
import dao.CakeDao;
import dao.impl.CakeDaoImpl;

/**
 * Servlet implementation class CakeList
 */
@WebServlet("/CakeList")
public class CakeList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int MAX_LIST_SIZE = 12;
	private static final String BOOKLIST_PATH="jsp/cake/cakelist.jsp";
   

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action=request.getParameter("action");
		if(action==null) {
			action="list";
		}
		switch(action) {
		case "d":
			break;
			
		case "list"://默认全部商品列表
			cakeList(request,response);
			break;
		}
	}

	private void cakeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CakeDao bd = new CakeDaoImpl();
		int curPage = 1;
		String page = request.getParameter("page");
		if (page != null) {
			curPage = Integer.parseInt(page);
		}
		
		PageBean pb=null;
		List<Cake> cakeList=new ArrayList<Cake>();
		String catalogIdStr = request.getParameter("catalogId");//获取有没有分类id，没有就是查全部
		
		if(catalogIdStr!=null) {
			int catalogId=Integer.parseInt(catalogIdStr);
			pb = new PageBean(curPage, MAX_LIST_SIZE, bd.cakeReadCount(catalogId));
			cakeList = bd.cakeList(pb,catalogId);
			
			if(cakeList.size()>0) {
				request.setAttribute("title", cakeList.get(0).getCatalog().getCatalogName());//从返回的分类集合中第一个获取数据的分类
			}
			
		}else {
			pb = new PageBean(curPage, MAX_LIST_SIZE, bd.cakeReadCount());
			cakeList = bd.cakeList(pb);
			request.setAttribute("title", "所有商品");
		}
		
		request.setAttribute("pageBean", pb);
		request.setAttribute("cakeList",cakeList);
		
		request.getRequestDispatcher(BOOKLIST_PATH).forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
