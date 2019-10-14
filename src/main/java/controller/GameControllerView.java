package controller;


import java.util.Observer;

import javafx.scene.layout.BorderPane;
import model.GameJamGameInterface;
import model.GameModel;

public abstract class GameControllerView extends BorderPane implements Observer, GameJamGameInterface{
	protected AccountManager accountmanager;
	protected String gameName;
	protected GameModel gameModel;
	
	public String getGameName() {
		return gameName;
	}
	
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	public AccountManager getAccountmanager() {
		return accountmanager;
	}
	
	public void setAccountmanager(AccountManager accountmanager) {
		this.accountmanager = accountmanager;
	}

	public GameModel getGameModel() {
		return gameModel;
	}

	public void setGameModel(GameModel gameModel) {
		this.gameModel = gameModel;
	}

	protected abstract void updateStatistics();
}
