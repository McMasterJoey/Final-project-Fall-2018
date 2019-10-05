package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

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
	public GameIconItem[] fetchAllGameSetUpInfo() {
		HashSet<GameIconItem> dataset = new HashSet<GameIconItem>();
		ResultSet rs = null;
		try {
			rs = DBconnection.executeQuery("SELECT name, iconpath FROM games");
			if (rs.next()) {
				GameIconItem game = new GameIconItem(rs.getString("name"), rs.getString("iconpath"), -1);
				dataset.add(game);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			System.err.println("Dataset fetch error!");
			return null;
		}
		// Take the resulting set and return an array.
		if (dataset.size() == 0) {
			System.err.println("Dataset had nothing in it!");
			return null;
		}
		GameIconItem[] retval = new GameIconItem[dataset.size()];
		Iterator<GameIconItem> iter = dataset.iterator();
		int x = 0;
		while(iter.hasNext()) {
			retval[x] = iter.next();
			x++;
		}
		return retval;
	}
}
