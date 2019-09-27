package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Establishes a connection to the jukebox436 database, and provides a getter
 * for other classes to use the connection.
 * 
 * @author Nicholas Fiegel, moving code from Rick Mercer's SongLibrary.java
 *
 */
public class DBConnection {

	// JDBC driver name and database URL
	// static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	// Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
	static final String DB_URL = "jdbc:mysql://localhost/gamejam?characterEncoding=utf8";
	// Database credentials
	static final String USER = "root";
	static final String PASS = "csc436zona";
	// The adapters connection to a MySQL data base on this computer
	private Connection conn = null;
	// Use the singleton pattern for this class
	private static DBConnection singleton = null;

	// Constructor for the class, private because of the singleton pattern
	private DBConnection() {
		// Register the JDBC driver (need an Oracle account and
		// mysql-connector...-bin.jar
		try {
			Class.forName(JDBC_DRIVER);
			
			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		}


	}
	
	// Getter for the class
	synchronized public static DBConnection getInstance() {
		
		if (singleton == null) {
			singleton = new DBConnection();
		}
		
		return singleton;
	}
	
	// Execute a query on the database given a String, using a prepared statement
	public ResultSet executeQuery(String query, Object... values) throws SQLException {
		ResultSet rs;
		PreparedStatement stmt = conn.prepareStatement(query);
		
		for (int i = 1; i <= values.length; i++) {
			stmt.setObject(i, values[i - 1]);
		}
		
		rs = stmt.executeQuery();
		
		return rs;
	}
	
	// Execute a transaction on the database given a String, used a prepared statement
	public boolean execute(String transaction, Object... values) throws SQLException {
		boolean result;
		PreparedStatement stmt = conn.prepareStatement(transaction);
		
		for (int i = 1; i <= values.length; i++) {
			stmt.setObject(i, values[i - 1]);
		}
		
		result = stmt.execute();
		
		return result;
	}
}
