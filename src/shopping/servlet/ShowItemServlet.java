package shopping.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shopping.bean.CategoryBean;
import shopping.bean.ItemBean;
import shopping.dao.DAOException;
import shopping.dao.ItemDAO;

/**
 * Servlet implementation class ShowItemServlet
 */
@WebServlet("/ShowItemServlet")
public class ShowItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


    public ShowItemServlet() {
        super();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			//パラメータの解析
			String action = request.getParameter("action");
			//topまたはパラメータなしの場合はトップページを表示
			if (action == null || action.length() == 0 ||action.equals("top")) {
				gotopage(request, response, "/top.jsp");

			}else if(action.equals("list")) {
				int categoryCode = Integer.parseInt(request.getParameter("code"));
				ItemDAO dao = new ItemDAO();
				List<ItemBean> list = dao.findByCategory(categoryCode);
				//Listをリクエストスコープに入れてJSPにフォワードする
				request.setAttribute("items", list);
				gotopage(request, response, "/list.jsp");
			}else {
				request.setAttribute("message", "正しく操作してください。");
				gotopage(request, response, "/errInternal.jsp");
			}

		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute("message", "内部エラーが発生しました。");
			gotopage(request, response, "/errInternal.jsp");

		}
	}

	private void gotopage(HttpServletRequest request, HttpServletResponse response, String page)
								throws ServletException, IOException{
			RequestDispatcher rd = request.getRequestDispatcher(page);
			rd.forward(request, response);

		}

	public void init() throws ServletException{
		try {
			//カテゴリ一覧は最初にアプリケーションスコープへ入れる
			ItemDAO dao = new ItemDAO();
			List<CategoryBean> list = dao.findAllCategory();
			getServletContext().setAttribute("categories", list);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServletException();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
