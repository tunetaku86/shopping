package shopping.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import shopping.bean.CategoryBean;
import shopping.bean.ItemBean;

public class ItemDAO {
	private Connection con;

	public ItemDAO() throws DAOException{
		getConnection();
	}

	//findAllCategory 全件取得
	public List<CategoryBean> findAllCategory() throws DAOException{

		if (con == null) {
			getConnection();
		}

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//SQL文の作成
			String sql = "SELECT * FROM category ORDER BY code";
			//PreparedStatementオブジェクトの取得
			st = con.prepareStatement(sql);
			//SQLの実行
			rs = st.executeQuery();
			//結果の取得
			List<CategoryBean> list = new ArrayList<CategoryBean>();
				while (rs.next()) {
					int code = rs.getInt("code");
					String name = rs.getString("name");
					CategoryBean bean = new CategoryBean(code,name);
					list.add(bean);
				}
				//カテゴリー一覧をListとして返す
				return list;


		}catch(Exception e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました");
		}finally {
				try {
					if(rs != null) rs.close();
					if(st != null) st.close();
					close();
					} catch (Exception e) {
						throw new DAOException("リソースの解放に失敗しました");
			}
		}
	}
	//findByCategory
	public List<ItemBean> findByCategory(int categoryCode) throws DAOException{

		if (con == null)
			getConnection();

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			//SQL文の作成
			String sql = "SELECT * FROM item WHERE category_code = ? ORDER BY code";
			//PreparedStatementオブジェクトの取得
			st = con.prepareStatement(sql);
			//カテゴリの設定
			st.setInt(1, categoryCode);
			//SQLの実行
			rs = st.executeQuery();
			//結果の取得
			List<ItemBean> list = new ArrayList<ItemBean>();
			while (rs.next()) {
				int code = rs.getInt("code");
				String name = rs.getString("name");
				int price = rs.getInt("price");
				ItemBean bean = new ItemBean(code, name, price);
				list.add(bean);
			}
			return list;

		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました。");
		}finally {
			try {
				//リソースの解放
				if(rs != null)rs.close();
				if (st != null)st.close();
				close();

			} catch (Exception e) {
				throw new DAOException("リソースの解放に失敗しました");
			}
		}
	}

	public ItemBean findByPrimaryKey(int key) throws DAOException{
		if(con == null)
			getConnection();

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//SQL文の作成
			String sql = "SELECT * FROM item WHERE code = ?";
			//PreparedStatementオブジェクトの取得
			st = con.prepareStatement(sql);
			//カテゴリの設定
			st.setInt(1, key);
			//SQL文の実行
			rs = st.executeQuery();
			//結果の取得および表示
			if(rs.next()) {
				int code = rs.getInt("code");
				String name = rs.getString("name");
				int price = rs.getInt("price");
				ItemBean bean = new ItemBean(code, name, price);
				return bean;
			}else {
				//主キーに該当するレコードなし
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました");
		}finally {
			try {
			//リソースの解放
				if(rs != null)rs.close();
				if(st != null)st.close();
				close();

			} catch (Exception e) {
				throw new DAOException("リソースの解放に失敗しました");
			}
		}
	}


	private void getConnection() throws DAOException{
		try {
			//JDBCドライバの登録
			Class.forName("org.postgresql.Driver");
			//URL,ユーザー名,パスワードの設定
			String url = "jdbc:postgresql:sample";
			String user = "tune";
			String pass = "";
			//データベースへの接続
			con = DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("接続に失敗しました");
		}
	}

	private void close() throws SQLException{
		if (con != null) {
			con.close();
			con = null;
		}
	}
}
