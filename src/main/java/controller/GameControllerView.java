package controller;


import java.util.Observer;

import javafx.scene.layout.BorderPane;
import model.GameJamGameInterface;
import model.GameModel;
/**
 * 
 * @author
 *
 */
public abstract class GameControllerView extends BorderPane implements Observer, GameJamGameInterface{
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
}
