package com.wust5.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Access to the testtable records shared by the data servlets.
 */
public final class StudentRecords {

	public static final String TABLE = "testtable";

	private static final String SELECT_ALL = "SELECT * FROM " + TABLE + " ORDER BY StuNo";

	private StudentRecords() {
	}

	public static void createTable() throws Exception {
		Connection conn = null;
		Statement s = null;
		try {
			DBUtil.loadDriver();
			conn = DBUtil.getConnection();
			s = conn.createStatement();
			s.execute("drop table " + TABLE);
			s.execute("create table " + TABLE
					+ "(place varchar(40), StuNo varchar(20) ,Psw varchar(20),Checkid char)");
		} finally {
			DBUtil.closeQuietly(s);
			DBUtil.closeQuietly(conn);
		}
	}

	public static boolean insert(String place, String stuNo, String checkid) {
		Connection conn = null;
		Statement s = null;
		try {
			conn = DBUtil.getConnection();
			s = conn.createStatement();
			s.execute("insert into " + TABLE + " values('" + place + "','" + stuNo + "','" + stuNo + "','"
					+ checkid + "')");
			conn.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DBUtil.closeQuietly(s);
			DBUtil.closeQuietly(conn);
		}
	}

	public static int count() {
		Connection conn = null;
		Statement s = null;
		String total = "";
		try {
			conn = DBUtil.getConnection();
			s = conn.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from " + TABLE);
			while (rs.next()) {
				total = rs.getString(1);
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeQuietly(s);
			DBUtil.closeQuietly(conn);
		}
		return Numbers.parseDigits(total);
	}

	/**
	 * Every record carrying the given checkid.
	 */
	public static JSONArray findAllByCheckid(String checkid) {
		JSONArray data = new JSONArray();
		Connection conn = null;
		Statement s = null;
		try {
			conn = DBUtil.getConnection();
			s = conn.createStatement();
			ResultSet rs = s.executeQuery(SELECT_ALL);
			while (rs.next()) {
				JSONObject record = toJson(rs, checkid);
				if (null != record) {
					data.put(record);
				}
			}
			rs.close();
			s.close();
			conn.commit();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeQuietly(s);
			DBUtil.closeQuietly(conn);
		}
		return data;
	}

	/**
	 * At most <code>length</code> records carrying the given checkid, read from the
	 * <code>start</code>th iteration onwards. Scanning stops at the first record
	 * with another checkid.
	 */
	public static JSONArray findPageByCheckid(String checkid, int start, int length) {
		JSONArray data = new JSONArray();
		Connection conn = null;
		Statement s = null;
		try {
			conn = DBUtil.getConnection();
			s = conn.createStatement();
			ResultSet rs = s.executeQuery(SELECT_ALL);
			for (int i = start; i < start + length; i++) {
				if (!rs.next()) {
					break;
				}
				JSONObject record = toJson(rs, checkid);
				if (null == record) {
					break;
				}
				data.put(record);
			}
			rs.close();
			s.close();
			conn.commit();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeQuietly(s);
			DBUtil.closeQuietly(conn);
		}
		return data;
	}

	private static JSONObject toJson(ResultSet rs, String checkid) throws SQLException {
		if (!rs.getString("checkid").contentEquals(checkid)) {
			return null;
		}
		JSONObject record = new JSONObject();
		record.put("place", rs.getString("place"));
		record.put("num", rs.getString("StuNo"));
		record.put("psw", rs.getString("Psw"));
		return record;
	}
}
