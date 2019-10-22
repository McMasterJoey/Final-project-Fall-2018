package controller;

import java.util.Observer;

import javafx.scene.layout.BorderPane;
import model.GameModel;

/**
 * 
 * @author
 *
 */
public abstract class GameControllerView extends BorderPane implements Observer {
	protected AccountManager accountmanager;
	protected String gameName;
	protected GameModel gameModel;

	/**
	 * 
	 * @return
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * 
	 * @param gameName
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * 
	 * @return
	 */
	public AccountManager getAccountmanager() {
		return accountmanager;
	}

	/**
	 * 
	 * @param accountmanager
	 */
	public void setAccountmanager(AccountManager accountmanager) {
		this.accountmanager = accountmanager;
	}

	/**
	 * 
	 * @return
	 */
	public GameModel getGameModel() {
		return gameModel;
	}

	/**
	 * 
	 * @param gameModel
	 */
	public void setGameModel(GameModel gameModel) {
		this.gameModel = gameModel;
	}

	protected abstract void updateStatistics();

	// Takes a path, loads the save game linked
	// Sets the state of that save game to current state of game.
	public abstract boolean loadSaveGame();

	// Takes a path, saves the current game state to linked path.
	public abstract boolean saveGame();

	// When applicable, pauses the game.
	public abstract boolean pauseGame();

	// When applicable, unpauses the game if it was paused.
	public abstract boolean unPauseGame();

	// Starts a new game, overrides current save game.
	public abstract boolean newGame();
	// All methods return true or false
	// Returning true indicates the action was a success
	// Returning false indicates the action failed or is unimplemented.
	
	//public abstract int getScore();
}
