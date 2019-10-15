package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Observable;

import model.SanityCheckFailedException;

public class AccountManager extends Observable {

	private String curUsername;
	private boolean isAdmin;
	private boolean isGuest;
	private int exp, level;
	private DBConnection conn;
	private int accountID;
	private HashMap<Integer, Integer> userStatsIDs; // maps gameid (key) to statsid (value)
	private HashMap<String, Integer> databaseGameID;
	
	// Use the singleton pattern for this class
	private static AccountManager singleton = null;
	
	// Constructor for the class, private because of the singleton pattern
	private AccountManager() {
		curUsername = "guest";
		isAdmin = false;
		isGuest = true;
		exp = 0;
		level = 1;
		accountID = -1;
		userStatsIDs = null;
		conn = DBConnection.getInstance();
		this.databaseGameID = new HashMap<String, Integer>();
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
					this.curUsername = username;
					this.isAdmin = rs.getBoolean("admin");
					this.isGuest = rs.getBoolean("guest");
					this.exp = rs.getInt("exp");
					this.level = rs.getInt("level");
					this.accountID = rs.getInt("accountid");
					fillUserStatsIDs();
					
					System.out.println("Login: " + curUsername + " " + isAdmin + " " + isGuest);
					
					setChanged();
					notifyObservers();
					// Fetch userId from database, used to reduce the frequency of queries to database.
//					rs = conn.executeQuery("select accountid from accounts where accounts.username = ?", this.curUsername);
//					rs.next();
//					this.dataBaseUserId = rs.getInt("accountid");
					return true;
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
		
		return false;
	}
	/**
	 * Logs a game played for the given user, Alternate function
	 * @param game The name of the game that was played
	 * @param stattype The type of stat being recorded see logStatType. 
	 * @param time The amount of time passed during this run of the game.
	 */
	public void logGameStat(String game, int stattype, int time) {
		if (stattype == 0) {
			this.logGameStat(game,true,false,false,false,time);
		} else if (stattype == 1) {
			this.logGameStat(game,false,true,false,false,time);
		} else if (stattype == 2) {
			this.logGameStat(game,false,false,true,false,time);
		} else if (stattype == 3) {
			this.logGameStat(game,false,false,false,true,time);
		} else {
			throw new IllegalArgumentException("Invalid stattype was out of range or value was below 0.");
		}
	}
	/**
	 * Logs a game played for the given user
	 * 
	 * @param game The name of the game that was played
	 * @param win Whether the game was won
	 * @param loss Whether the game was lost
	 * @param tie Whether the game was a tie
	 * @param incomplete Whether the game is incomplete
	 * @param time The amount of time that elapsed the game
	 */
	public void logGameStat(String game, boolean win, boolean loss, boolean tie, boolean incomplete, int time) {
		if (this.isGuest) {
			// Do nothing if this is a guest account.
			return;
		}
		try {
			System.out.println("\nFetching game from a string!");
			int gameid = getGameIdFromString(game);
			System.out.println(gameid);
			String transaction = "INSERT INTO gamelog(statsid, win, loss, tie, incomplete, timeplayed, score) VALUES(?, ?, ?, ?, ?, ?, ?)";
			conn.execute(transaction, userStatsIDs.get(gameid), win, loss, tie, incomplete, time);
			
		} catch (SQLException se) {
			se.printStackTrace();
		}
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
				int statsid = rs.getInt("statsid");
				// Need to fix ...
				int time = (int) rs.getTime("timeplayed").getTime();
				time = 0; // Set to 0 as of now so we only have valid values.
				
				int[] dv = new int[]{rs.getInt("wins"), rs.getInt("losses"), rs.getInt("ties"), rs.getInt("incomplete"), time};
				System.out.println(time);
				if (mode) {
					dv[stattype] += value;
				} else {
					dv[stattype] = value;
				}
				//System.out.println(rs.getInt("losses"));
				conn.execute("update statistics set wins = ?, losses = ?, ties = ?, incomplete = ?, timeplayed = ? where statsid = ?", dv[0],dv[1],dv[2],dv[3],dv[4], statsid);
				if (rs.next()) {
					throw new SanityCheckFailedException("SQL query to fetch global stats returned more than 1 row.");
				}
			} else {
				// We are missing a stat, create one
				int[] dv = new int[5];
				dv[stattype] = value;
				try {
					int gameid = getGameIdFromString(game);
					conn.execute("INSERT INTO statistics(accountid, gameid, wins, losses, ties, incomplete, timeplayed) VALUES(?, ?, ?, ?, ?, ?, ?)", this.accountID, gameid, dv[0],dv[1],dv[2],dv[3],dv[4]);
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
		accountID = -1;
		userStatsIDs = null;

		setChanged();
		notifyObservers();
	}
	
	// Attempt to create an account.
	// Return codes:
	// 1: Success
	// 2: Username already in use
	// 3: Other unknown error
	public int createAccount(String username, String password) {
		ResultSet rs = null;
		try {
			conn.execute("INSERT INTO accounts(username, password) VALUES(?, ?)", username, password);
			// Fetch userId from database, used to reduce the frequency of queries to database.
			rs = conn.executeQuery("select accountid from accounts where accounts.username = ?", username);
			rs.next();
			curUsername = username;
			isAdmin = false;
			isGuest = false;
			exp = 0;
			level = 1;
			accountID = rs.getInt("accountid");
			createStatisticsEntries();
			fillUserStatsIDs();
			
		} catch (SQLException se) {
			if (se.getErrorCode() == 1062) { // 1062 indicates username is already in the db
				return 2;
			} else {
				return 3;
			}
		}
		
		return 1;
	}

	private void createStatisticsEntries() {
		ResultSet rs = null;

		try
		{
			rs = conn.executeQuery("SELECT * FROM statistics WHERE statistics.accountid = ?", accountID);

			if (rs.next()) { // An entry already exists for this user
				return;
			} else {
				rs = conn.executeQuery("SELECT gameid FROM games");

				while (rs.next()) {
					int gameID = rs.getInt("gameid");
					conn.execute("INSERT INTO statistics(accountid, gameid) VALUES(?, ?)", accountID, gameID);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void fillUserStatsIDs() {
		ResultSet rs = null;
		userStatsIDs = new HashMap<>();

		try {
			System.out.println("fillUserStatsIDs: accountID = " + accountID);
			rs = conn.executeQuery("SELECT statsid, gameid FROM statistics WHERE statistics.accountid = ?", accountID);

			while (rs.next()) {
				int gameID = rs.getInt("gameid");
				int statsID = rs.getInt("statsid");
				userStatsIDs.put(gameID, statsID);
			}

			userStatsIDs.forEach((key, value) -> System.out.println("gameID: " + key + "  statsID: " + value));
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

	public int getAccountID() {
		return accountID;
	}

	public HashMap<Integer, Integer> getUserStatsIDs() {
		return userStatsIDs;
	}
	// End getters for account information
	// ---
	/**
	 * Returns the gameid in the data base for the given game name
	 * @param game The string that is the name of the game
	 * @return The gameid in the database of the game. 
	 */
	private int getGameIdFromString(String game) {
		if (this.databaseGameID.containsKey(game)) {
			return this.databaseGameID.get(game);
		}
		// Cache miss, fetch from database.
		ResultSet rs = null;
		int gameid = -1;
		try {
			rs = conn.executeQuery("select gameid from games where name = ?", game);
			rs.next();
			gameid = rs.getInt("gameid");
			if (gameid < 0) {
				throw new SanityCheckFailedException("getGameIdFromString fetched a gameid of " + gameid + " from the query!");
			}
			// Add it to Hashmap so we can quickly fetch it.
			this.databaseGameID.put(game,gameid);
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SanityCheckFailedException("getGameIdFromString threw an SQL execption when adding a game to the cache.");
		}
		if (gameid < 0) {
			throw new SanityCheckFailedException("getGameIdFromString tired to return gameid = " + gameid);
		}
		return gameid;
	}
}
