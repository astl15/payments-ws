package ro.astl.paymentsws.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import ro.astl.paymentsws.model.Category;
import ro.astl.paymentsws.model.CategoryAmount;
import ro.astl.paymentsws.model.Payment;

public class PaymentsDaoImpl implements PaymentsDao {
	
	private static final DataSource dataSource = DBUtils.getDataSource();
	private static PaymentsDao instance = PaymentsDaoImpl.getInstance();

	public static final PaymentsDao getInstance() {
		if(instance == null) {
			synchronized (PaymentsDaoImpl.class) {
				if(instance == null) {
					instance = new PaymentsDaoImpl();
				}
			}
		}
		return instance;
		
	}

	@Override
	public boolean addPayment(Payment payment) {
		boolean isExecuted = false;
		String author = payment.getAuthor();
		String description = payment.getDescription();
		String category = payment.getCategory().getLabel();
		float amount = payment.getAmount();
		Date date = Date.valueOf(payment.getDate());
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmnt = prepareAddPayment(conn, author, description, amount, category, date)){
			int affectedRows = stmnt.executeUpdate();
			if(affectedRows>0) {
				isExecuted = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isExecuted;
	}
	
	@Override
	public List<Payment> getLastPaymentsByAuthor(String author) {
		List<Payment> payments = new ArrayList<Payment>();
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmnt = prepareGetLastPaymentsByAuthor(conn, author);
				ResultSet rs = stmnt.executeQuery()){
			while(rs.next()) {
				Payment tempPayment = new Payment();
				Category category = new Category();
				category.setId(rs.getInt("category_id"));
				category.setLabel(rs.getString("category"));
				tempPayment.setId(rs.getInt("id"));
				tempPayment.setDescription(rs.getString("description"));
				tempPayment.setAmount(rs.getFloat("amount"));
				tempPayment.setAuthor(rs.getString("author"));
				tempPayment.setCategory(category);
				Date dbDate = rs.getDate("date");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dbDate);
				int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
				int month = calendar.get(Calendar.MONTH) + 1;
				int year = calendar.get(Calendar.YEAR);
				tempPayment.setDate(LocalDate.of(year, month, dayOfMonth));
				payments.add(tempPayment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return payments;
	}
	
	@Override
	public List<Payment> getPaymentsByDateAndAuthor(LocalDate date, String author) {
		List<Payment> payments = new ArrayList<Payment>();
		String stringDate = date.format(DateTimeFormatter.ISO_DATE);
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmnt = prepareGetPaymentsByDateAndAuthor(conn, stringDate, author);
				ResultSet rs = stmnt.executeQuery()){
			while(rs.next()) {
				Payment tempPayment = new Payment();
				Category category = new Category();
				category.setId(rs.getInt("category_id"));
				category.setLabel(rs.getString("category"));
				tempPayment.setId(rs.getInt("id"));
				tempPayment.setDescription(rs.getString("description"));
				tempPayment.setAmount(rs.getFloat("amount"));
				tempPayment.setAuthor(rs.getString("author"));
				tempPayment.setCategory(category);
				Date dbDate = rs.getDate("date");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dbDate);
				int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
				int month = calendar.get(Calendar.MONTH) + 1;
				int year = calendar.get(Calendar.YEAR);
				tempPayment.setDate(LocalDate.of(year, month, dayOfMonth));
				payments.add(tempPayment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return payments;
	}
	
	/*private static PreparedStatement prepareStatement(String operation, Connection conn, Object... paramsSQL) throws SQLException{
		PreparedStatement stmnt = null;
		String sql = "";
		if(paramsSQL.length<1) {
			return stmnt;
		}else {
			switch(operation) {
				case "addPayment":
					sql = "INSERT INTO wltmngr.payments(author,description,amount,category,date)VALUES(?,?,?,?,?)";
					stmnt = conn.prepareStatement(sql);
					stmnt.setString(1, (String) paramsSQL[0]);
					stmnt.setString(2, (String) paramsSQL[1]);
					stmnt.setFloat(3, (float) paramsSQL[2]);
					stmnt.setString(4, (String) paramsSQL[3]);
					stmnt.setObject(5, paramsSQL[4]);
					break;
				case "getLastPaymentsByAuthor":
					sql = "SELECT p.id,p.author,p.description,p.amount,c.id AS category_id,p.category,p.date FROM payments p INNER JOIN payment_categories c ON p.category = c.name WHERE p.author = ? ORDER BY p.date DESC,p.id DESC LIMIT 10";
					stmnt = conn.prepareStatement(sql);
					stmnt.setString(1, (String) paramsSQL[0]);
					break;
			}
			return stmnt;
		}
	}*/
	
	private static PreparedStatement prepareAddPayment(Connection conn, String author, 
			String description, float amount, String category, Date date) throws SQLException {
		PreparedStatement stmnt = null;
		String sql = "INSERT INTO wltmngr.payments(author,description,amount,category,date)VALUES(?,?,?,?,?)";
		stmnt = conn.prepareStatement(sql);
		stmnt.setString(1, author);
		stmnt.setString(2, description);
		stmnt.setFloat(3, amount);
		stmnt.setString(4, category);
		stmnt.setObject(5, date);
		return stmnt;
	}
	
	private static PreparedStatement prepareGetLastPaymentsByAuthor(Connection conn, String author) throws SQLException {
		PreparedStatement stmnt = null;
		String sql = "SELECT p.id,p.author,p.description,p.amount,c.id AS category_id,p.category,p.date FROM payments p INNER JOIN payment_categories c ON p.category = c.name WHERE p.author = ? ORDER BY p.date DESC,p.id DESC LIMIT 10";
		stmnt = conn.prepareStatement(sql);
		stmnt.setString(1, author);
		return stmnt;
	}
	
	private static PreparedStatement prepareGetPaymentsByDateAndAuthor(Connection conn, String date, String author) throws SQLException {
		PreparedStatement stmnt = null;
		String sql = "SELECT p.id, p.author, p.description, p.amount,c.id AS category_id, p.category, p.date FROM payments p INNER JOIN payment_categories c ON p.category = c.name WHERE p.author=? AND p.date >=?";
		stmnt = conn.prepareStatement(sql);
		stmnt.setString(1, author);
		stmnt.setString(2, date);
		return stmnt;
	}
}
