package kr.kro.syeyoung.moder.database;

import java.sql.Connection;
import java.sql.SQLException;

import kr.kro.syeyoung.moder.Main;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource {
	private static HikariConfig config = new HikariConfig(Main.getInstance().getConfig());
	private static HikariDataSource ds;
	
	private DataSource() {}
	
	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}
