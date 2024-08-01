package servlet.cake;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Cake;
import bean.Catalog;
import dao.CakeDao;
import dao.CatalogDao;
import dao.impl.CakeDaoImpl;
import dao.impl.CatalogDaoImpl;

import net.sf.json.JSONObject;

/**
 * 
 * Servlet implementation class Index
 */
@WebServlet("/ShopIndex")
public class ShopIndex extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/json"); 
		JSONObject json = new JSONObject();
		
		CakeDao bd=new CakeDaoImpl();
		List<Cake> recCakes = bd.cakeList(4);
		json.put("recCakes", recCakes);
		List<Cake> newCakes = bd.newCakes(4);
		json.put("newCakes", newCakes);
		
		PrintWriter pw = response.getWriter();
		pw.print(json);
		pw.flush();
		
		
	}

}
