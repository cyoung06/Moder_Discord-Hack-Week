package kr.kro.syeyoung.moder.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataSource {
	private static Properties proper=  new Properties();
	
	static {
		proper = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("hikari.properties");
			proper.load(fis);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	private static HikariConfig config = new HikariConfig(proper);
//	private static HikariDataSource ds = new HikariDataSource(config);
//	
	private DataSource() {}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(proper.getProperty("jdbcUrl"));
	}
}
