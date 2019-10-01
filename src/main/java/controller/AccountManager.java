package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;

public class AccountManager extends Observable {

	private String curUsername;
	private boolean isAdmin;
	private boolean isGuest;
	private int exp, level;
	private DBConnection conn;
	// Use the singleton pattern for this class
	private static AccountManager singleton = null;
	
	// Constructor for the class, private because of the singleton pattern
	private AccountManager() {
		curUsername = "guest";
		isAdmin = false;
		isGuest = true;
		exp = 0;
		level = 1;
		conn = DBConnection.getInstance();
	}
	
	// Getter for the class
	synchronized public static AccountManager getInstance() {
		
		if (singleton == null) {
			singleton = new AccountManager();
		}
		
		return singleton;
	}
	
	// Attempt to login a user
	public boolean login(String username, String password) {
		ResultSet rs = null;

		try {
			rs = conn.executeQuery("SELECT * FROM accounts WHERE username = ?", username);
			
			if (rs.next()) {
				
				if (password.equals(rs.getString("password"))) {
					curUsername = username;
					isAdmin = rs.getBoolean("admin");
					isGuest = false;
					exp = rs.getInt("exp");
					level = rs.getInt("level");
					
					this.setChanged();
					notifyObservers();
					return true;
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
		
		return false;
	}
	
	// logout, ie switch to the guest account
	public void logout() {
		curUsername = "guest";
		isAdmin = false;
		isGuest = true;
		exp = 0;
		level = 1;

		this.setChanged();
		notifyObservers();
	}
	
	// Attempt to create an account.
	// Return codes:
	// 1: Success
	// 2: Username already in use
	// 3: Other unknown error
	public int createAccount(String username, String password) {
		
		try {
			conn.execute("INSERT INTO accounts(username, password) VALUES(?, ?)", username, password);
		} catch (SQLException se) {
			if (se.getErrorCode() == 1062) { // 1062 indicates username is already in the db
				return 2;
			} else {
				return 3;
			}
		}
		
		return 1;
	}
	
	// Attempt to delete an account
	// Return codes:
	// 1: Success
	// 2: No such user 
	//
	public int deleteAccount(String username) {

		try {
			conn.execute("DELETE from accounts where username = '"+ username + "'");
		} catch (SQLException se) {
			
		}

		return 1;
	}
	
	//---
	// Getters for account information
	
	public String getCurUsername() {
		return curUsername;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
	
	public boolean isGuest() {
		return isGuest;
	}
	
	public int getExp() {
		return exp;
	}
	
	public int getLevel() {
		return level;
	}
	
	// End getters for account information
	// ---
}
