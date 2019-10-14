package controller;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import ticTacToe.EasyAI;
import ticTacToe.IntermediateAI;

public class GameMenu extends MenuBar{
	private Menu optmenu;
	private MenuItem newgame;
	private MenuItem diffinter;
	private MenuItem diffeasy;
	private MenuItem saveGame;
	public Menu getOptmenu() {
		return optmenu;
	}

	public void setOptmenu(Menu optmenu) {
		this.optmenu = optmenu;
	}

	public MenuItem getNewgame() {
		return newgame;
	}

	public void setNewgame(MenuItem newgame) {
		this.newgame = newgame;
	}

	public MenuItem getDiffinter() {
		return diffinter;
	}

	public void setDiffinter(MenuItem diffinter) {
		this.diffinter = diffinter;
	}

	public MenuItem getDiffeasy() {
		return diffeasy;
	}

	public void setDiffeasy(MenuItem diffeasy) {
		this.diffeasy = diffeasy;
	}

	public MenuItem getSaveGame() {
		return saveGame;
	}

	public void setSaveGame(MenuItem saveGame) {
		this.saveGame = saveGame;
	}

	public MenuItem getLoadGame() {
		return loadGame;
	}

	public void setLoadGame(MenuItem loadGame) {
		this.loadGame = loadGame;
	}

	private MenuItem loadGame;
	
	private GameMenu(GameControllerView controller) {
		optmenu = new Menu(controller.getGameName() + " Options");
		newgame = new MenuItem("Start New Game");
		diffinter = new MenuItem("Set diffuclty to Intermediate");
		diffeasy = new MenuItem("Set diffuclty to Easy");
		saveGame = new MenuItem("Save Game");
		loadGame = new MenuItem("Load Game");
		newgame.setOnAction((event) -> {
			controller.updateStatistics();
			boolean result = controller.newGame();
			if (!result) {
				System.err.println("ERROR on trying to set new tic-tac-toe game!");
			}
		});
		saveGame.setOnAction((event) -> {
			controller.saveGame();
		});
		loadGame.setOnAction((event) -> {
			controller.loadSaveGame();
		});
		
		optmenu.getItems().addAll(newgame,diffeasy,diffinter,saveGame,loadGame);
		this.getMenus().addAll(optmenu);
	}
	
	public static GameMenu getMenuBar(GameControllerView controller) {
		return new GameMenu(controller);		
	}
}
