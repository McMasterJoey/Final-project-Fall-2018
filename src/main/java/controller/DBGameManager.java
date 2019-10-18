package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import Gamejam.GameIconItem;
/**
 * Interacts with the database on behalf of the Main GUI, Does any interaction that dosn't directly involve account management.
 * @author Joey McMaster
 *
 */
public class DBGameManager
{
	private static DBGameManager _singleton = null;
	private DBConnection DBconnection;
	private HashMap<String, Integer> gameListByName; // Maps game name to gameid
	private HashMap<Integer, String> gameListByID; // Maps gameid to game name
	
	private DBGameManager() {
		DBconnection = DBConnection.getInstance();
		gameListByName = new HashMap<>();
		gameListByID = new HashMap<>();
	}

	/**
	 * Makes the class a singleton
	 * @return The only instance of the class.
	 */
	synchronized public static DBGameManager getInstance() {
		if (_singleton == null) {
			_singleton = new DBGameManager();
		}
		return _singleton;
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
			rs = DBconnection.executeQuery("SELECT name, iconpath, gameid FROM games");
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

	public HashMap<String, Integer> getGameListByName() {
		return gameListByName;
	}

	public HashMap<Integer, String> getGameListByID() {
		return gameListByID;
	}
}
