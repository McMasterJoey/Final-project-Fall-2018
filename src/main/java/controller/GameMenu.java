package controller;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import ticTacToe.EasyAI;
import ticTacToe.IntermediateAI;

public class GameMenu extends MenuBar{
	
	private GameMenu(GameControllerView controller) {
		Menu optmenu = new Menu(controller.getGameName() + " Options");
		MenuItem newgame = new MenuItem("Start New Game");
		MenuItem diffinter = new MenuItem("Set diffuclty to Intermediate");
		MenuItem diffeasy = new MenuItem("Set diffuclty to Easy");
		MenuItem saveGame = new MenuItem("Save Game");
		MenuItem loadGame = new MenuItem("Load Game");
		newgame.setOnAction((event) -> {
			controller.updateStatistics();
			boolean result = controller.newGame();
			if (!result) {
				System.err.println("ERROR on trying to set new tic-tac-toe game!");
			}
		});
		diffinter.setOnAction((event) -> { 
			controller.getGameModel().setAIStrategy(new IntermediateAI());
		});
		diffeasy.setOnAction((event) -> { 
			controller.getGameModel().setAIStrategy(new EasyAI());
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
