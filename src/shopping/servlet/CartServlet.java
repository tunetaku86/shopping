package shopping.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import shopping.bean.CartBean;
import shopping.bean.ItemBean;
import shopping.dao.DAOException;
import shopping.dao.ItemDAO;

/**
 * Servlet implementation class CartServlet
 */
@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CartServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//パラメータの解析
			String action = request.getParameter("action");
			//showまたはパラメータなしの場合はカートページを表示
			if (action == null||action.length() == 0||action.equals("show")) {
				gotoPage(request, response, "/cart.jsp");
			//addはカートに追加処理
			}else if (action.equals("add")) {
				int code = Integer.parseInt(request.getParameter("item_code"));
				int quantity = Integer.parseInt(request.getParameter("quantity"));
				HttpSession session = request.getSession(true);
				CartBean cart = (CartBean)session.getAttribute("cart");
				if(cart == null) {//初めてのクライアントの場合はカートを作成する
					cart = new CartBean();
					session.setAttribute("cart", cart);
				}
				//商品コードの商品を取得する
				ItemDAO dao = new ItemDAO();
				ItemBean bean = dao.findByPrimaryKey(code);
				//カートに追加する
				cart.addCart(bean, quantity);
				gotoPage(request, response, "/cart.jsp");
				//deleteからはカートの削除処理
			}else if(action.equals("delete")){
				HttpSession session = request.getSession(false);
				if (session == null) {//セッションオブジェクトなし
					request.setAttribute("message", "セッションが切れています。もう一度トップページより操作してください。");
					gotoPage(request, response, "/errInternal.jsp");
					return;
				}
				CartBean cart = (CartBean)session.getAttribute("cart");
				if (cart == null) {
						request.setAttribute("message", "正しく操作してください");
						gotoPage(request, response, "/errInternal.jsp");
					return;
				}
				int code = Integer.parseInt(request.getParameter("item_code"));
				cart.deleteCart(code);
				gotoPage(request, response, "/cart.jsp");

			}else {//actionの値が不正
				request.setAttribute("message", "正しく操作してください。");
				gotoPage(request, response, "/errInternal.jsp");
			}

		} catch (DAOException e) {
			e.printStackTrace();
			request.setAttribute("message", "内部エラーが発生しました。");
			gotoPage(request, response, "/errInternal.jsp");
		}
	}

		private void gotoPage(HttpServletRequest request,
				HttpServletResponse response, String page)throws ServletException, IOException{
			RequestDispatcher rd = request.getRequestDispatcher(page);
			rd.forward(request, response);




	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
