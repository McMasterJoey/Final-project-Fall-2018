package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import Gamejam.GameIconItem;
import model.Achievement;

/**
 * Interacts with the database on behalf of the Main GUI, Does any interaction that doesn't directly involve account management.
 * @author Joey McMaster
 * @author Nicholas Fiegel
 *
 */
public class DBGameManager
{
	private static DBGameManager singleton = null;
	private DBConnection conn;
	private HashMap<String, Integer> gameListByName; // Maps game name to gameid
	private HashMap<Integer, String> gameListByID; // Maps gameid to game name
	private ArrayList<Achievement> achievements; // List of all the achievements in the DB
	
	private DBGameManager() {
		conn = DBConnection.getInstance();
		gameListByName = new HashMap<>();
		gameListByID = new HashMap<>();
		fetchAllGameSetUpInfo();
		fillAchievements();
	}

	/**
	 * Makes the class a singleton
	 * @return The only instance of the class.
	 */
	synchronized public static DBGameManager getInstance() {
		if (singleton == null) {
			singleton = new DBGameManager();
		}
		return singleton;
	}
	/**
	 * Gets the relevant information from the games table.
	 * to be used to construct the game icons in the GUI
	 * @return An array containing all the games that are found within the database. They are not ordered.
	 */
	public ArrayList<GameIconItem> fetchAllGameSetUpInfo() {
		ArrayList<GameIconItem> retval = new ArrayList<GameIconItem>();
		ResultSet rs = null;
		try {
			rs = conn.executeQuery("SELECT name, iconpath, gameid FROM games");
			while (rs.next()) {
				String name = rs.getString("name");
				Integer id = rs.getInt("gameid");
				GameIconItem game = new GameIconItem(name, rs.getString("iconpath"), id);
				retval.add(game);
				gameListByName.put(name, id);
				gameListByID.put(id, name);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			System.err.println("Dataset fetch error!");
			return null;
		}
		// Verify the result is not empty
		if (retval.size() == 0) {
			System.err.println("Dataset had nothing in it!");
			return null;
		}
		// Sort it to keep consistent order.
		Collections.sort(retval);
		return retval;
	}

	private void fillAchievements() {
		achievements = new ArrayList<>();
		ResultSet rs = null;

		try {
			rs = conn.executeQuery("SELECT * FROM achievements");

			while (rs.next()) {
				int achieveID = rs.getInt("achieveid");
				int gameID = rs.getInt("gameid");
				String description = rs.getString("description");
				String condition = rs.getString("condition");
				int amount = rs.getInt("amount");
				int exp = rs.getInt("exp");
				String iconPath = rs.getString("iconpath");
				achievements.add(new Achievement(achieveID, gameID, description, condition, amount, exp, iconPath));
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	// ---
	// Begin Getters

	public HashMap<String, Integer> getGameListByName() {
		return gameListByName;
	}

	public HashMap<Integer, String> getGameListByID() {
		return gameListByID;
	}

	public ArrayList<Achievement> getAchievements() {
		return achievements;
	}

	public String getAchievementIconPath(int achieveID) {

		for (Achievement cur : achievements) {

			if (cur.getAchieveID() == achieveID) {
				return cur.getIconPath();
			}
		}

		return null;
	}

	// ---
	// End Getters
}
