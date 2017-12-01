package ro.astl.paymentsws.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import ro.astl.paymentsws.model.CategoryAmount;

public class PaymentsAmountsDaoImpl implements PaymentsAmountsDao{
	
	private static final DataSource dataSource = DBUtils.getDataSource();
	private static PaymentsAmountsDao instance = PaymentsAmountsDaoImpl.getInstance();
	
	public static final PaymentsAmountsDao getInstance() {
		if(instance == null) {
			synchronized (PaymentsAmountsDaoImpl.class) {
				if(instance==null) {
					instance = new PaymentsAmountsDaoImpl();
				}
				
			}
		}
		return instance;
	}
	
	@Override
	public List<CategoryAmount> getAmountByCategory(LocalDate date, String author) {
		List<CategoryAmount> amountsPerCategory = new ArrayList<CategoryAmount>();
		String stringDate = date.format(DateTimeFormatter.ISO_DATE);
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmnt = prepareGetAmountByCategory(conn, stringDate, author);
				ResultSet rs = stmnt.executeQuery()){
			while(rs.next()) {
				CategoryAmount categoryAmount = new CategoryAmount();
				categoryAmount.setCategory(rs.getString("category"));
				categoryAmount.setAmount(rs.getFloat("amount"));
				amountsPerCategory.add(categoryAmount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return amountsPerCategory;
	}
	
	private static PreparedStatement prepareGetAmountByCategory(Connection conn, String date, String author) throws SQLException {
		PreparedStatement stmnt = null;
		String sql = "SELECT p.category, SUM(p.amount) AS amount FROM payments p WHERE p.author=? AND p.date >=? GROUP BY p.category";
		stmnt = conn.prepareStatement(sql);
		stmnt.setString(1, author);
		stmnt.setString(2, date);
		return stmnt;
	}

}
