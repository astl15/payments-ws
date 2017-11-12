package ro.astl.paymentsws.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import ro.astl.paymentsws.model.Category;
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
				PreparedStatement stmnt = prepareStatement("addPayment",conn,author,description,amount,category,date)){
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
				PreparedStatement stmnt = prepareStatement("getLastPaymentsByAuthor",conn,author);
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
	
	private static PreparedStatement prepareStatement(String operation, Connection conn, Object... paramsSQL) throws SQLException{
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
					sql = "SELECT p.id,p.author,p.description,p.amount,c.id AS category_id,p.category,p.date FROM payments p INNER JOIN payment_categories c ON p.category = c.name WHERE p.author = ? ORDER BY p.date,p.id DESC LIMIT 10";
					stmnt = conn.prepareStatement(sql);
					stmnt.setString(1, (String) paramsSQL[0]);
					break;
			}
			return stmnt;
		}
	}
}
