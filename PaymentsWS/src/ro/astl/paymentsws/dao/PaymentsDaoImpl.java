package ro.astl.paymentsws.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.sql.DataSource;

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
		String description = payment.getDescription();
		String category = payment.getCategory().getLabel();
		float amount = payment.getAmount();
		LocalDate date = payment.getDate();
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmnt = prepareStatement("addPayment",conn,description,amount,category,date)){
			int affectedRows = stmnt.executeUpdate();
			if(affectedRows>0) {
				isExecuted = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isExecuted;
	}
	
	private static PreparedStatement prepareStatement(String operation, Connection conn, Object... paramsSQL) throws SQLException{
		PreparedStatement stmnt = null;
		String sql = "";
		if(paramsSQL.length<1) {
			return stmnt;
		}else {
			switch(operation) {
				case "addPayment":
					sql = "INSERT INTO wltmngr.payments(description,amount,category,date)VALUES(?,?,?,?)";
					stmnt = conn.prepareStatement(sql);
					stmnt.setString(1, (String) paramsSQL[0]);
					stmnt.setFloat(2, (float) paramsSQL[1]);
					stmnt.setString(3, (String) paramsSQL[2]);
					stmnt.setObject(4, paramsSQL[3]);
					break;
			}
			return stmnt;
		}
	}
}
