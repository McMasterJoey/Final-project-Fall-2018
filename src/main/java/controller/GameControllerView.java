package controller;


import java.util.Observer;

import javafx.scene.layout.BorderPane;
import model.GameJamGameInterface;

public abstract class GameControllerView extends BorderPane implements Observer, GameJamGameInterface{
	protected AccountManager accountmanager;
	protected String gameName;
	
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

	protected abstract void updateStatistics();
}
