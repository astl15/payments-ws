package ro.astl.paymentsws.dao;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class DBUtils {
	
	public static final DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/wltmngr");
		dataSource.setUsername("root");
		dataSource.setPassword("wltmngr");
		return dataSource;
	}

}
