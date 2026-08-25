package com.wust5.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DBUtil {

	public static final String DB_URL = "jdbc:derby:wust5DB;create=true";

	private static final String DRIVER_CLASS = "org.apache.derby.jdbc.EmbeddedDriver";

	private DBUtil() {
	}

	public static void loadDriver() throws Exception {
		Class.forName(DRIVER_CLASS).newInstance();
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL);
	}

	/**
	 * Loads the embedded driver and opens the database, creating it when missing.
	 */
	public static boolean openDatabase() {
		Connection conn = null;
		Statement s = null;
		try {
			loadDriver();
			conn = getConnection();
			s = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeQuietly(s);
			closeQuietly(conn);
		}
		return true;
	}

	public static void closeQuietly(ResultSet rs) {
		if (null != rs) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeQuietly(Statement s) {
		if (null != s) {
			try {
				s.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeQuietly(Connection conn) {
		if (null != conn) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
