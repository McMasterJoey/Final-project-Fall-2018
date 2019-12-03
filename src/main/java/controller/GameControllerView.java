package controller;

import java.util.Observer;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

/**
 * 
 * @author
 *
 */
public abstract class GameControllerView extends BorderPane implements Observer {
	protected AccountManager accountManager;
	protected StatsManager statsManager;
	protected String gameName;

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
	public AccountManager getAccountManager() {
		return accountManager;
	}

	/**
	 * 
	 * @param accountManager
	 */
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
	
	protected void showEndScreen() {
		Alert verifyAlert = new Alert(AlertType.INFORMATION);
		verifyAlert.setTitle("Game Over");
		verifyAlert.setHeaderText("You " + this.wonString() + "!");
		verifyAlert.setContentText("This game earned you " + this.getScore() + " points");
		verifyAlert.show();
	}
	
	protected abstract String wonString();
	
	protected abstract void updateStatistics();

	public abstract int getScore();
	
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
