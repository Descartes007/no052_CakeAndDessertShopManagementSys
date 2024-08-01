package servlet.admin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import  bean.Cake;
import  bean.Catalog;
import  bean.PageBean;
import  bean.UpLoadImg;
import  dao.CakeDao;
import  dao.CatalogDao;
import  dao.UpLoadImgDao;
import  dao.impl.CakeDaoImpl;
import  dao.impl.CatalogDaoImpl;
import  dao.impl.UpLoadImgDaoImpl;
import  util.RanUtil;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class CakeManageServlet
 */
@WebServlet("/jsp/admin/CakeManageServlet")
public class CakeManageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String BOOKLIST_PATH = "cakeManage/cakeList.jsp";// 商品列表页面地址
	private static final String BOOKADD_PATH = "cakeManage/cakeAdd.jsp";// 商品增加页面地址
	private static final String BOOKEDIT_PATH = "cakeManage/cakeEdit.jsp";// 商品修改页面地址
	private static final String BOOKDETAIL_PATH = "cakeManage/cakeDetail.jsp";// 商品详情页面地址
	private static final String BOOKIMGDIR_PATH="images/cake/cakeimg/";//商品图片保存文件夹相对路径

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		switch (action) {
		case "list":
			cakeList(request, response);
			break;
		case "detail":
			cakeDetail(request, response);
			break;
		case "addReq":
			cakeAddReq(request, response);
			break;
		case "add":
			cakeAdd(request, response);
			break;
		case "edit":
			cakeEdit(request, response);
			break;
		case "update":
			cakeUpdate(request,response);
			break;
		case "find":
			cakeFind(request, response);
			break;
		case "updateImg":
			updateImg(request,response);
			break;
		case "del":
			cakeDel(request,response);
			break;
		case "batDel":
			cakeBatDel(request,response);
			break;
		case "seach":
			seachCake(request,response);
		}
	}
	private void seachCake(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		int curPage = 1;
		String page = request.getParameter("page");
		if (page != null) {
			curPage = Integer.parseInt(page);
		}
		int maxSize = Integer.parseInt(request.getServletContext().getInitParameter("maxPageSize"));
		String cakename = request.getParameter("cakename");
		CakeDao bd = new CakeDaoImpl();
		PageBean pb = null;
		if(cakename != null && cakename != "") {
			pb = new PageBean(curPage, maxSize, bd.cakeReadCount(cakename));
			request.setAttribute("cakeList", bd.cakeList(pb,cakename));
		}else {
			pb = new PageBean(curPage, maxSize, bd.cakeReadCount());
			request.setAttribute("cakeList", bd.cakeList(pb));
		}
		
		request.setAttribute("pageBean", pb);
		request.getRequestDispatcher(BOOKLIST_PATH).forward(request, response);
	}

	//商品批量删除
	private void cakeBatDel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ids=request.getParameter("ids");
		CakeDao bd=new CakeDaoImpl();
		UpLoadImgDao uid=new UpLoadImgDaoImpl();
		File contextPath=new File(request.getServletContext().getRealPath("/"));
		
		String imgIds=bd.findimgIdByIds(ids);//批量查询图片的id并组成一组字符串
		
		List<UpLoadImg> list = uid.findImgByIds(imgIds);
		if(bd.cakeBatDelById(ids)) {
			request.setAttribute("cakeMessage", "商品已批量删除");
			if(uid.imgBatDelById(imgIds)) {
				for(UpLoadImg uli:list) {
					//批量删除本地文件
					File f=new File(contextPath,uli.getImgSrc());
					if(f.exists()) {
						f.delete();
					}
				}
			}
		}else {
			request.setAttribute("cakeMessage", "商品批量删除失败");
		}
		//用户删除成功失败都跳转到用户列表页面
		cakeList(request, response);//通过servlet中listUser跳到用户列表
	}

	//商品删除
	private void cakeDel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id=Integer.parseInt(request.getParameter("id"));
		File contextPath=new File(request.getServletContext().getRealPath("/"));
		CakeDao bd=new CakeDaoImpl();
		UpLoadImgDao uid=new UpLoadImgDaoImpl();
		Cake cake=bd.findCakeById(id);
		//这里先删除数据库商品信息，再删除商品图片及本地硬盘图片信息
		if(bd.cakeDelById(id)) {
			request.setAttribute("cakeMessage", "商品已删除");
			if(uid.imgDelById(cake.getImgId())) {
				//删除本地文件
				File f=new File(contextPath,cake.getUpLoadImg().getImgSrc());
				if(f.exists()) {
					f.delete();
				}
			}
		}else {
			request.setAttribute("cakeMessage", "商品删除失败");
		}
		
		//用户删除成功失败都跳转到用户列表页面
		cakeList(request, response);//通过servlet中listUser跳到用户列表
		
	}

	//商品更新
	private void cakeUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CakeDao cakeDao=new CakeDaoImpl();
		Cake cake=new Cake();
		cake.setCakeId(Integer.parseInt(request.getParameter("cakeId")));
		cake.setCatalogId(Integer.parseInt(request.getParameter("catalog")));
		cake.setPrice(Double.parseDouble(request.getParameter("price")));
		cake.setDescription(request.getParameter("description"));
		
		if(cakeDao.cakeUpdate(cake)) {
			request.setAttribute("cakeMessage", "修改成功");
			cakeList(request, response);
		}else {
			request.setAttribute("cakeMessage", "图片失败");
			request.setAttribute("cakeInfo", cakeDao.findCakeById(cake.getCakeId()));
			request.getRequestDispatcher(BOOKEDIT_PATH).forward(request, response);
		}
	}

	// 更新商品图片
	private void updateImg(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		int cakeId = Integer.parseInt(request.getParameter("id"));
		boolean flag = false;
		String imgSrc = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		String imgName = null;
		String contentType = null;

		CakeDao cakeDao = new CakeDaoImpl();
		UpLoadImgDao upImgDao = new UpLoadImgDaoImpl();
		
		File contextPath=new File(request.getServletContext().getRealPath("/"));
		File dirPath = new File( contextPath,BOOKIMGDIR_PATH);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		DiskFileItemFactory dfif = new DiskFileItemFactory();
		ServletFileUpload servletFileUpload = new ServletFileUpload(dfif);
		List<FileItem> parseRequest = null;
		try {
			parseRequest = servletFileUpload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		Iterator<FileItem> iterator = parseRequest.iterator();
		while (iterator.hasNext()) {
			FileItem fileItem = iterator.next();
			if (!fileItem.isFormField()) {

				inputStream = fileItem.getInputStream();
				contentType = fileItem.getContentType();
				if ("image/jpeg".equals(contentType)) {
					imgName = RanUtil.getUUID() + ".jpg";
					flag = true;
				}
				if ("image/png".equals(contentType)) {
					imgName = RanUtil.getUUID() + ".png";
					flag = true;
				}

			}

		}
		if (flag) {
			imgSrc = BOOKIMGDIR_PATH + imgName;
			outputStream = new FileOutputStream(new File(contextPath,imgSrc));
			IOUtils.copy(inputStream, outputStream);
			outputStream.close();
			inputStream.close();
			//根据商品id去查询图片信息
			Cake cake = cakeDao.findCakeById(cakeId);
			UpLoadImg upImg = cake.getUpLoadImg();
			// 删除旧图片文件如果存在
			File oldImg = new File(contextPath,cake.getUpLoadImg().getImgSrc());
			if (oldImg.exists()) {
				oldImg.delete();
			}
			upImg.setImgName(imgName);
			upImg.setImgSrc(imgSrc);
			upImg.setImgType(contentType);
			
			
			if (upImgDao.imgUpdate(upImg)) {
				request.setAttribute("cakeMessage", "图片修改成功");
			} else {
				request.setAttribute("cakeMessage", "图片修改失败");
			}
		} else {
			request.setAttribute("cakeMessage", "图片修改失败");
		}
		cakeEdit(request,response);
	}

	// 获取商品分类信息
	private void cakeAddReq(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CatalogDao cd = new CatalogDaoImpl();
		request.setAttribute("catalog", cd.getCatalog());
		request.getRequestDispatcher(BOOKADD_PATH).forward(request, response);

	}

	// 商品增加
	private void cakeAdd(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean flag = false;
		
		Map<String, String> map = new HashMap<>();
		InputStream inputStream = null;
		OutputStream outputStream = null;
		File dirPath = new File(request.getServletContext().getRealPath("/") + BOOKIMGDIR_PATH);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		DiskFileItemFactory dfif = new DiskFileItemFactory();
		ServletFileUpload servletFileUpload = new ServletFileUpload(dfif);
		// 解决乱码
		servletFileUpload.setHeaderEncoding("ISO8859_1");

		List<FileItem> parseRequest = null;
		try {
			parseRequest = servletFileUpload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}

		Iterator<FileItem> iterator = parseRequest.iterator();

		while (iterator.hasNext()) {
			FileItem fileItem = iterator.next();
			// 判断是否是表单的普通字段true为普通表单字段，false为上传文件内容
			if (fileItem.isFormField()) {
				String name = new String(fileItem.getFieldName().getBytes("ISO8859_1"), "utf-8");
				String value = new String(fileItem.getString().getBytes("ISO8859_1"), "utf-8");
				map.put(name, value);
			} else {
				String imgName = null;

				String contentType = fileItem.getContentType();

				if ("image/jpeg".equals(contentType)) {
					imgName = RanUtil.getUUID() + ".jpg";
					flag = true;
				}
				if ("image/png".equals(contentType)) {
					imgName = RanUtil.getUUID() + ".png";
					flag = true;
				}
				if (flag) {
					inputStream = fileItem.getInputStream();
					File file = new File(dirPath, imgName);
					outputStream = new FileOutputStream(file);
					// 保存img信息到map集合中，后面传入对象使用
					map.put("imgName", imgName);
					map.put("imgSrc", BOOKIMGDIR_PATH + imgName);
					map.put("imgType", contentType);
				}

			}
		}
		// 如果上传的内容小于3个必填项或者图片没有或类型不正确返回
		if (map.size() < 3 || !flag) {
			request.setAttribute("cakeMessage", "商品添加失败");
			cakeAddReq(request, response);
		} else {
			// 验证通过才可以保存图片流到本地
			IOUtils.copy(inputStream, outputStream);
			outputStream.close();
			inputStream.close();

			// 把map集合中存储的表单数据提取出来转换为cake对象
			// 这里要求商品增加的字段要和数据库字段一致，不然map集合转对象会出错
			Cake cake = new Cake();
			cake.setCakeName(map.get("cakeName"));
			cake.setPrice(Double.parseDouble(map.get("price")));
			cake.setDescription(map.get("desc"));
			
			// 商品分类信息
			Catalog catalog = cake.getCatalog();
			catalog.setCatalogId(Integer.parseInt(map.get("catalog")));
			// 图片信息
			UpLoadImg upLoadImg = cake.getUpLoadImg();
			upLoadImg.setImgName(map.get("imgName"));
			upLoadImg.setImgSrc(map.get("imgSrc"));
			upLoadImg.setImgType(map.get("imgType"));

			// 增加商品先增加商品图片,商品图片增加成功了在添加商品信息
			UpLoadImgDao uid = new UpLoadImgDaoImpl();
			if (uid.imgAdd(cake.getUpLoadImg())) {
				// 获取商品图片添加后的id
				Integer imgId = uid.findIdByImgName(upLoadImg.getImgName());
				upLoadImg.setImgId(imgId);

				CakeDao bd = new CakeDaoImpl();
				if (bd.cakeAdd(cake)) {
					request.setAttribute("cakeMessage", "商品添加成功");
					cakeList(request, response);
				} else {
					request.setAttribute("cakeMessage", "商品添加失败");
					cakeAddReq(request, response);
				}
			} else {
				// 图片添加失败就判定商品添加失败
				request.setAttribute("cakeMessage", "商品添加失败");
				cakeAddReq(request, response);
			}

		}
	}

	// 获取商品列表
	private void cakeList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int curPage = 1;
		String page = request.getParameter("page");
		if (page != null) {
			curPage = Integer.parseInt(page);
		}
		int maxSize = Integer.parseInt(request.getServletContext().getInitParameter("maxPageSize"));
		CakeDao bd = new CakeDaoImpl();
		PageBean pb = new PageBean(curPage, maxSize, bd.cakeReadCount());
		
		request.setAttribute("pageBean", pb);
		request.setAttribute("cakeList", bd.cakeList(pb));
		request.getRequestDispatcher(BOOKLIST_PATH).forward(request, response);
	}

	// 商品详情页
	private void cakeDetail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		CakeDao bd = new CakeDaoImpl();
		request.setAttribute("cakeInfo", bd.findCakeById(Integer.parseInt(id)));
		request.getRequestDispatcher(BOOKDETAIL_PATH).forward(request, response);

	}

	// 接收商品修改请求
	private void cakeEdit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int cakeId = Integer.parseInt(request.getParameter("id"));
		CakeDao cakeDao = new CakeDaoImpl();
		CatalogDao catalogDao = new CatalogDaoImpl();
		request.setAttribute("catalog", catalogDao.getCatalog());//获取商品分类信息
		request.setAttribute("cakeInfo", cakeDao.findCakeById(cakeId));//获取商品信息byId
		request.getRequestDispatcher(BOOKEDIT_PATH).forward(request, response);
	}

	// ajax查找商品是否存在
	private void cakeFind(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String cakeName = request.getParameter("param");

		CakeDao bd = new CakeDaoImpl();
		// 这里实例化json对象需要导入6个jar包（
		// commons-lang-2.4.jar
		// ,commons-collections-3.2.1.jar,commons-beanutils-1.8.3.jar
		// json-lib-2.4-jdk15.jar ,ezmorph-1.0.6.jar ,commons-logging-1.1.3.jar）
		JSONObject json = new JSONObject();
		if (bd.findCakeByCakeName(cakeName)) {
			json.put("info", "该商品已存在");
			json.put("status", "n");
		} else {
			json.put("info", "输入正确");
			json.put("status", "y");
		}
		response.getWriter().write(json.toString());

	}

}
