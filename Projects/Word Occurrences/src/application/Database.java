package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Deals with connecting and creating database and schema for word occurrences
 * 
 * @author Jordan
 *
 */
public class Database {
	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/";
	private static final String DB_NAME = "WORD_OCCURRENCES";

	private static String DB_USER = null;
	private static String DB_PASS = null;

	/**
	 * Sets username for mysql for local computer
	 * 
	 * @param dB_USER username for mysql
	 */
	public void setDB_USER(String dB_USER) {
		DB_USER = dB_USER;
	}

	/**
	 * Sets password for mysql for local computer
	 * 
	 * @param dB_PASS password for mysql
	 */
	public void setDB_PASS(String dB_PASS) {
		DB_PASS = dB_PASS;
	}

	/**
	 * Gets connection to schema to execute SQL statements.
	 * 
	 * @return Connection to schema to use for MYSQL Statements
	 * @throws Exception
	 */
	Connection getConnection() throws Exception {

		try {
			Class.forName(JDBC_DRIVER);

			Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASS);

			return conn;
		} catch (Exception e) {
			System.out.println(e);
		}

		return null;
	}

	/**
	 * Create schema for word occurrences.
	 * 
	 * @throws Exception
	 */
	void createSchema() throws Exception {
		Connection conn = null;
		Statement stmt = null;

		try {
			Class.forName(JDBC_DRIVER);

			System.out.print("Connecting... ");
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			System.out.println("Connected.");

			stmt = conn.createStatement();
			String sql;

			System.out.print("Dropping schema... ");
			sql = "DROP SCHEMA " + DB_NAME;
			stmt.executeUpdate(sql);
			System.out.println("Dropped.");

			System.out.print("Creating schema... ");
			sql = "CREATE SCHEMA IF NOT EXISTS " + DB_NAME;
			stmt.executeUpdate(sql);
			System.out.println("Schema created.");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
			conn.close();
		}

	}

	/**
	 * Create table for storing words and counts
	 * 
	 * @throws Exception
	 */
	void createTable() throws Exception {
		Connection conn = null;
		Statement stmt = null;

		try {
			System.out.print("Connecting... ");
			conn = getConnection();
			System.out.println("Connected.");

			stmt = conn.createStatement();
			String sql;

			System.out.print("Creating table... ");
			sql = "CREATE TABLE IF NOT EXISTS WORD (id INTEGER UNSIGNED NOT NULL, word VARCHAR(255), count INTEGER, PRIMARY KEY (id))";
			stmt.executeUpdate(sql);
			System.out.println("Table created.");

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			stmt.close();
			conn.close();
		}
	}
}
