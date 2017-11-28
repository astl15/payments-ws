package ro.astl.paymentsws.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ro.astl.paymentsws.model.Category;

public class PaymentsCategoriesDaoImpl implements PaymentsCategoriesDao {
	
	private final DataSource dataSource = DBUtils.getDataSource();
	private static PaymentsCategoriesDao instance = PaymentsCategoriesDaoImpl
			.getInstance();
	
	public static final PaymentsCategoriesDao getInstance() {
		if(instance==null) {
			synchronized (PaymentsCategoriesDaoImpl.class) {
				if(instance==null) {
					instance = new PaymentsCategoriesDaoImpl();
				}
			}
		}
		return instance;
	}
	
	@Override
	public List<Category> getCategories() {
		List<Category> categories = new ArrayList<Category>();
		
		try (Connection conn = (Connection) dataSource.getConnection();
				PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM wltmngr.payment_categories");
				ResultSet rs = stmnt.executeQuery()) {
					while (rs.next()) {
						Category tempCategory = new Category();
						tempCategory.setId(rs.getInt("id"));
						tempCategory.setLabel(rs.getString("name"));
						tempCategory.setLocale(rs.getString("locale"));
						categories.add(tempCategory);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
		return categories;
		}

	@Override
	public boolean addCategory(String label, String locale) {
		boolean isExecuted = false;
		try(Connection conn = dataSource.getConnection();
			PreparedStatement stmnt = preareAddCategory(conn,label,locale)){
			int affectedRows = stmnt.executeUpdate();
			if(affectedRows > 0) {
				isExecuted = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isExecuted;
	}
	
	/**to be replaced */
	private static PreparedStatement prepareStatement(String operation, Connection conn, String... paramsSQL) throws SQLException{
		PreparedStatement stmnt = null;
		String sql = "";
		if(paramsSQL.length<1) {
			return stmnt;
		}else {
			switch(operation) {
				case "addCategory":
					sql = "INSERT INTO wltmngr.payment_categories(name)VALUES(?)";
					stmnt = conn.prepareStatement(sql);
					stmnt.setString(1, paramsSQL[0]);
					break;
			}
			return stmnt;
		}
	}
	
	private static PreparedStatement preareAddCategory(Connection conn, String name, String locale) throws SQLException {
		PreparedStatement stmnt = null;
		String sql = "INSERT INTO wltmngr.payment_categories(name,locale)VALUES(?,?)";
		stmnt = conn.prepareStatement(sql);
		stmnt.setString(1, name);
		stmnt.setString(2, locale);
		return stmnt;
	}
}
