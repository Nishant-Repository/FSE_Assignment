package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static Connection con = null;

	static {
		final String dburl = "jdbc:mysql://localhost:3306/tweet";
		final String user = "root";
		final String password = "pass@word1";
		try {
			con = DriverManager.getConnection(dburl, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		return con;
	}
}
