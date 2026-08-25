package testsupport;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Boots the embedded Derby database used by the servlets inside the build
 * directory and gives tests control over the contents of "testtable".
 */
public final class DerbyTestSupport {

	public static final String URL = "jdbc:derby:wust5DB;create=true";

	private static final String FOUR_COLUMN_SCHEMA =
			"create table testtable(place varchar(40), StuNo varchar(20), Psw varchar(20), Checkid char)";

	private static final String THREE_COLUMN_SCHEMA =
			"create table testtable(place varchar(40), StuNo varchar(20), Psw varchar(20))";

	private DerbyTestSupport() {
	}

	public static void bootDatabase() {
		File home = new File("target/derby-test");
		home.mkdirs();
		System.setProperty("derby.system.home", home.getAbsolutePath());
		System.setProperty("derby.stream.error.file",
				new File(home, "derby.log").getAbsolutePath());
	}

	public static Connection connect() throws SQLException {
		bootDatabase();
		return DriverManager.getConnection(URL);
	}

	public static void createFourColumnTable() throws SQLException {
		recreate(FOUR_COLUMN_SCHEMA);
	}

	public static void createThreeColumnTable() throws SQLException {
		recreate(THREE_COLUMN_SCHEMA);
	}

	public static void dropTable() throws SQLException {
		try (Connection conn = connect(); Statement s = conn.createStatement()) {
			execIgnoringMissingTable(s, "drop table testtable");
		}
	}

	public static void insert(String place, String stuNo, String psw, String checkid) throws SQLException {
		try (Connection conn = connect(); Statement s = conn.createStatement()) {
			s.execute("insert into testtable values('" + place + "','" + stuNo + "','" + psw + "','" + checkid + "')");
		}
	}

	public static int countRows() throws SQLException {
		try (Connection conn = connect(); Statement s = conn.createStatement()) {
			java.sql.ResultSet rs = s.executeQuery("select count(*) from testtable");
			rs.next();
			return rs.getInt(1);
		}
	}

	private static void recreate(String ddl) throws SQLException {
		try (Connection conn = connect(); Statement s = conn.createStatement()) {
			execIgnoringMissingTable(s, "drop table testtable");
			s.execute(ddl);
		}
	}

	private static void execIgnoringMissingTable(Statement s, String sql) throws SQLException {
		try {
			s.execute(sql);
		} catch (SQLException e) {
			// 42Y55: table does not exist, nothing to drop
			if (!"42Y55".equals(e.getSQLState())) {
				throw e;
			}
		}
	}
}
