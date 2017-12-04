package ro.astl.paymentsws.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import ro.astl.paymentsws.model.Category;
import ro.astl.paymentsws.model.CategoryAmount;
import ro.astl.paymentsws.model.Payment;

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
	
	@Override
	public List<Payment> getAmountPerDate(LocalDate date, String author){
		List<Payment> amountsPerDay = new ArrayList<Payment>();
		String stringDate = date.format(DateTimeFormatter.ISO_DATE);
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmnt = prepareGetAmountPerDate(conn, stringDate, author);
				ResultSet rs = stmnt.executeQuery()){
			while(rs.next()) {
				Payment payment = new Payment();
				Category category = new Category();
				category.setId(rs.getInt("category_id"));
				category.setLabel(rs.getString("category"));
				payment.setAuthor(rs.getString("author"));
				payment.setAmount(rs.getFloat("amount"));
				Date dbDate = rs.getDate("date");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dbDate);
				int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
				int month = calendar.get(Calendar.MONTH) + 1;
				int year = calendar.get(Calendar.YEAR);
				payment.setDate(LocalDate.of(year, month, dayOfMonth));
				amountsPerDay.add(payment);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return amountsPerDay;
	}
	
	@Override
	public Map<String, Integer> getMonthlySummary(LocalDate date, String author) {
		Map<String, Integer> monthlySummary = new HashMap<String, Integer>();
		String stringDate = date.format(DateTimeFormatter.ISO_DATE);
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmnt = prepareGetMonthlySum(conn, stringDate, author);
				ResultSet rs = stmnt.executeQuery()){
			while(rs.next()) {
				BigDecimal monthlySum = rs.getBigDecimal("amount");
				int intMonthlySum = monthlySum.intValue();
				monthlySummary.put("monthlySum", intMonthlySum);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return monthlySummary;
	}
	
	private static PreparedStatement prepareGetAmountByCategory(Connection conn, String date, String author) throws SQLException {
		PreparedStatement stmnt = null;
		String sql = "SELECT p.category, SUM(p.amount) AS amount FROM payments p WHERE p.author=? AND p.date >=? GROUP BY p.category";
		stmnt = conn.prepareStatement(sql);
		stmnt.setString(1, author);
		stmnt.setString(2, date);
		return stmnt;
	}
	
	private static PreparedStatement prepareGetAmountPerDate(Connection conn, String date, String author) throws SQLException {
		PreparedStatement stmnt = null;
		String sql = "SELECT p.id, p.author, SUM(p.amount) AS amount, c.id AS category_id, p.category, p.date FROM payments p INNER JOIN payment_categories c on p.category = c.name WHERE p.author=? AND p.date >=?  GROUP BY p.date"; 
		stmnt = conn.prepareStatement(sql);
		stmnt.setString(1, author);
		stmnt.setString(2, date);
		return stmnt;
	}
	
	private static PreparedStatement prepareGetMonthlySum(Connection conn, String date, String author) throws SQLException {
		PreparedStatement stmnt = null;
		String sql = "SELECT p.author, SUM(p.amount) AS amount FROM payments p WHERE p.author=? AND p.date >=?";
		stmnt = conn.prepareStatement(sql);
		stmnt.setString(1, author);
		stmnt.setString(2, date);
		return stmnt;
	}

}
