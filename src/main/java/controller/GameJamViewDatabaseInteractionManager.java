package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import Gamejam.GameIconItem;
/**
 * Interacts with the database on behalf of the Main GUI, Does any interaction that dosn't directly involve account management.
 * @author Joey McMaster
 *
 */
public class GameJamViewDatabaseInteractionManager {
	private static GameJamViewDatabaseInteractionManager _singleton = null;
	private DBConnection DBconnection;
	
	private GameJamViewDatabaseInteractionManager() {
		DBconnection = DBConnection.getInstance();
	}

	/**
	 * Makes the class a singleton
	 * @return The only instance of the class.
	 */
	synchronized public static GameJamViewDatabaseInteractionManager getInstance() {
		if (_singleton == null) {
			_singleton = new GameJamViewDatabaseInteractionManager();
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
				GameIconItem game = new GameIconItem(rs.getString("name"), rs.getString("iconpath"), rs.getInt("gameid"));
				retval.add(game);
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
}
