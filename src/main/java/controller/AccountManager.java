package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;

import model.SanityCheckFailedException;

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
					isGuest = rs.getBoolean("guest");
					exp = rs.getInt("exp");
					level = rs.getInt("level");
					
					System.out.println("Login: " + curUsername + " " + isAdmin + " " + isGuest);
					
					setChanged();
					notifyObservers();
					return true;
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
		
		return false;
	}
	/**
	 * Logs a stat for the logged in user
	 * stattype: 
	 * 0 = wins
	 * 1 = losses
	 * 2 = ties
	 * 3 = incomplete
	 * 4 = time played (in seconds)
	 * @param mode Set to true if adding the value to the pre existing value, set to false if setting. 
	 * @param game The name of the game whose stats are being set
	 * @param stattype The id of the stat type (refer to the enum LogStatType)
	 * @param value The value to be added/set 
	 */
	public void logGlobalStat(boolean mode, String game, int stattype, int value) {
		// Arg Check
		if (stattype < 0 || stattype > 4 || value < 0) {
			throw new IllegalArgumentException("Invalid stattype was out of range or value was below 0.");
		}
		if (this.isGuest) {
			// Do nothing if this is a guest account.
			return;
		}
		// Insert into stat table 
		ResultSet rs = null;

		try {
			rs = conn.executeQuery("select g.gameid, a.accountid, s.statsid, s.wins, s.losses,s.ties,s.incomplete,s.timeplayed from games g join statistics s on g.gameid = s.gameid and g.name = ? join accounts a on a.accountid = s.accountid and a.username = ?", game, this.curUsername);
			if (rs.next()) {
				// Should only get 1 row.
				int gameid = rs.getInt("gameid");
				int accountid = rs.getInt("accountid");
				int statsid = rs.getInt("statsid");
				int time = (int) rs.getTime("timeplayed").getTime();
				time = 0; //
				int[] dv = new int[]{rs.getInt("wins"), rs.getInt("losses"), rs.getInt("ties"), rs.getInt("incomplete"), time};
				System.out.println(time);
				if (mode) {
					dv[stattype] += value;
				} else {
					dv[stattype] = value;
				}
				conn.execute("update statistics set wins = ?, losses = ?, ties = ?, incomplete = ?, timeplayed = ? where statsid = ?", dv[0],dv[1],dv[2],dv[3],dv[4], statsid);
				if (rs.next()) {
					throw new SanityCheckFailedException("SQL query to fetch global stats returned more than 1 row.");
				}
			} else {
				System.out.println("Possible error: Query to find the users global stats table for the inputed game was non existant.");
				// We are missing a stat, create one
				int[] dv = new int[5];
				dv[stattype] = value;
				try {
					rs = conn.executeQuery("select accountid from accounts where accounts.username = ?", this.curUsername);
					rs.next();
					int accountid = rs.getInt("accountid");
					rs = conn.executeQuery("select gameid from games where name = ?", game);
					rs.next();
					int gameid = rs.getInt("gameid");
					conn.execute("INSERT INTO statistics(accountid, gameid, wins, losses, ties, incomplete, timeplayed) VALUES(?, ?, ?, ?, ?, ?, ?)", accountid, gameid, dv[0],dv[1],dv[2],dv[3],dv[4]);
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	// logout, ie switch to the guest account
	public void logout() {
		curUsername = "guest";
		isAdmin = false;
		isGuest = true;
		exp = 0;
		level = 1;

		setChanged();
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
