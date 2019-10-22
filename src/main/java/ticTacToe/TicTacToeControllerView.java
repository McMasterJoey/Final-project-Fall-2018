package ticTacToe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;

import Gamejam.Gamejam;
import controller.*;
import Gamejam.RegionColors;
import connectFour.ConnectFourModel;
import controller.AccountManager;
import controller.GameControllerView;
import controller.GameMenu;
import controller.logStatType;

import javafx.geometry.Pos; 
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;

/**
 * Provides a GUI view and the listeners required for players to make Tic Tac
 * Toe moves with mouse clicks.
 * 
 * @author Wes Rodgers, Joey McMaster
 *
 */
public class TicTacToeControllerView extends GameControllerView {

	private TicTacToeModel gameModel;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	private Image xImage, yImage;
	private BorderPane topLeft, midLeft, botLeft, topCenter, midCenter, botCenter, topRight, midRight, botRight;
	private AudioClip moveSound;
	private AudioClip winSound;
	private AudioClip loseSound;
	private AudioClip tieSound;
	private BorderPane[][] placeholder;
	// Original implemention by Wes has the object extending gridpane, this that grid pane.
	// Updated version uses a border pane with original grid pane set to its center to add game speific options
	// Should look and play the exact same way as before.
	private GridPane _primarypane;
	GameMenu menuBar;
	
	public TicTacToeControllerView() {
		gameName = "tic-tac-toe";
		menuBar = GameMenu.getMenuBar(this);
		this.setTop(menuBar);		
		_primarypane = new GridPane();
		initializeGame();
		setupResources();		
		accountManager = AccountManager.getInstance();
		statsManager = StatsManager.getInstance();
		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);
		_primarypane.setPrefWidth(WIDTH);
		_primarypane.setPrefHeight(HEIGHT);
		_primarypane.setBackground(RegionColors.BDEFAULTBACKGROUND);
		
	}

	/**
	 * initializes this object by setting various fields and setting up the
	 * listeners.
	 */
	private void initializeGame() {
		if (gameModel == null) {
			gameModel = new TicTacToeModel();
			gameModel.setAIStrategy(new IntermediateAI());
			gameModel.addObserver(this);
		} else {
			gameModel.clearBoard();
		}
		setupBoard();
	}

	/**
	 * gets the resources for this game
	 */
	private void setupResources() {
		xImage = new Image(TicTacToeControllerView.class.getResource("/letterX.png").toString());
		yImage = new Image(TicTacToeControllerView.class.getResource("/letterO.png").toString());
		moveSound = new AudioClip(TicTacToeControllerView.class.getResource("/moveSound.mp3").toString());
		winSound = new AudioClip(TicTacToeControllerView.class.getResource("/winSound.mp3").toString());
		loseSound = new AudioClip(TicTacToeControllerView.class.getResource("/loseSound.mp3").toString());
		tieSound = new AudioClip(TicTacToeControllerView.class.getResource("/tieSound.mp3").toString());
	}

	/**
	 * gets the graphics context we are drawing to and draws the board and current
	 * moves to it.
	 */
	private void setupBoard() {
		placeholder = new BorderPane[3][3];

		// we had to change this entirely when we swapped away from canvas.
		// I tried to do all of this in a DRY manner but looping through made it
		// difficult
		// to properly set up the listeners, and since it was a last second fix I didn't
		// have time to figure out a more elegant method than this.

		_primarypane.setAlignment(Pos.CENTER);
		topLeft = new BorderPane();
		topLeft.setStyle("-fx-border-color : white black black white; -fx-border-width : 2");
		topLeft.setPrefSize(200, 200);
		_primarypane.add(topLeft, 0, 0);
		placeholder[0][0] = topLeft;

		midLeft = new BorderPane();
		midLeft.setStyle("-fx-border-color : black black black white; -fx-border-width : 2");
		midLeft.setPrefSize(200, 200);
		_primarypane.add(midLeft, 0, 1);
		placeholder[0][1] = midLeft;

		botLeft = new BorderPane();
		botLeft.setStyle("-fx-border-color : black black white white; -fx-border-width : 2");
		botLeft.setPrefSize(200, 200);
		_primarypane.add(botLeft, 0, 2);
		placeholder[0][2] = botLeft;

		topCenter = new BorderPane();
		topCenter.setStyle("-fx-border-color : white black black black; -fx-border-width : 2");
		topCenter.setPrefSize(200, 200);
		_primarypane.add(topCenter, 1, 0);
		placeholder[1][0] = topCenter;

		midCenter = new BorderPane();
		midCenter.setStyle("-fx-border-color : black black black black; -fx-border-width : 2");
		midCenter.setPrefSize(200, 200);
		_primarypane.add(midCenter, 1, 1);
		placeholder[1][1] = midCenter;

		botCenter = new BorderPane();
		botCenter.setStyle("-fx-border-color : black black white black; -fx-border-width : 2");
		botCenter.setPrefSize(200, 200);
		_primarypane.add(botCenter, 1, 2);
		placeholder[1][2] = botCenter;

		topRight = new BorderPane();
		topRight.setStyle("-fx-border-color : white white black black; -fx-border-width : 2");
		topRight.setPrefSize(200, 200);
		_primarypane.add(topRight, 2, 0);
		placeholder[2][0] = topRight;

		midRight = new BorderPane();
		midRight.setStyle("-fx-border-color : black white black black; -fx-border-width : 2");
		midRight.setPrefSize(200, 200);
		_primarypane.add(midRight, 2, 1);
		placeholder[2][1] = midRight;

		botRight = new BorderPane();
		botRight.setStyle("-fx-border-color : black white white black; -fx-border-width : 2");
		botRight.setPrefSize(200, 200);
		_primarypane.add(botRight, 2, 2);
		placeholder[2][2] = botRight;

		
		this.setCenter(this._primarypane);
		setupListeners();

	}

	/**
	 * sets up the click listener for the board. Finds which spot the user clicked
	 * on and attempts to make a move at that spot.
	 */
	private void setupListeners() {
		topLeft.setOnMouseClicked((click) -> {
			gameModel.humanMove(0, 0, false);
			moveSound.play();
		});

		midLeft.setOnMouseClicked((click) -> {
			gameModel.humanMove(1, 0, false);
			moveSound.play();
		});

		botLeft.setOnMouseClicked((click) -> {
			gameModel.humanMove(2, 0, false);
			moveSound.play();
		});

		topCenter.setOnMouseClicked((click) -> {
			gameModel.humanMove(0, 1, false);
			moveSound.play();
		});

		midCenter.setOnMouseClicked((click) -> {
			Gamejam.DPrint("what what");
			gameModel.humanMove(1, 1, false);
			Gamejam.DPrint(gameModel.toString());
			moveSound.play();
		});

		botCenter.setOnMouseClicked((click) -> {
			gameModel.humanMove(2, 1, false);
			moveSound.play();
		});

		topRight.setOnMouseClicked((click) -> {
			gameModel.humanMove(0, 2, false);
			moveSound.play();
		});

		midRight.setOnMouseClicked((click) -> {
			gameModel.humanMove(1, 2, false);
			moveSound.play();
		});

		botRight.setOnMouseClicked((click) -> {
			gameModel.humanMove(2, 2, false);
			moveSound.play();
		});
		
		menuBar.getDiffinter().setOnAction((event) -> { 
			gameModel.setAIStrategy(new IntermediateAI());
		});
		menuBar.getDiffeasy().setOnAction((event) -> { 
			gameModel.setAIStrategy(new EasyAI());
		});
	}

	/**
	 * this just clears all listeners from the board
	 */
	private void disableListeners() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				placeholder[i][j].setOnMouseClicked((click) -> {
				});
			}
		}
	}

	/**
	 * When the board changes and notifies this observer, clear the graphics context
	 * and redraw it.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		char[][] board = gameModel.getBoard();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] != '_') {
					Image image = board[i][j] == 'X' ? xImage : yImage;
					ImageView imageView = new ImageView(image);
					imageView.setFitHeight(196);
					imageView.setFitWidth(196);
					HBox box = new HBox();
					box.getChildren().add(imageView);
					box.setStyle(placeholder[j][i].getStyle());
					_primarypane.getChildren().remove(placeholder[j][i]);
					_primarypane.add(box, j, i);
				}
			}
		}
		
		Gamejam.DPrint("----------");
		Gamejam.DPrint(gameModel.toString());
		Gamejam.DPrint("----------");
		
		if (gameModel.tied()) {
			//accountManager.logGlobalStat(true, "Tic-Tac-Toe", logStatType.TIE, 1);
			statsManager.logGameStat("Tic-Tac-Toe", logStatType.TIE, 0);
			tieSound.play();
		} else if (gameModel.won('X') || gameModel.won('O')) {
			if (gameModel.won('X')) {
				//accountManager.logGlobalStat(true, "Tic-Tac-Toe", logStatType.WIN, 1);
				statsManager.logGameStat("Tic-Tac-Toe",  logStatType.WIN, 1);
				winSound.play();
			} else {
				//accountManager.logGlobalStat(true, "Tic-Tac-Toe", logStatType.LOSS, 1);
				statsManager.logGameStat("Tic-Tac-Toe", logStatType.LOSS, 1);
				loseSound.play();
			}
		}
		Gamejam.DPrint("current directory = " + System.getProperty("user.dir"));
	}

	@Override
	public boolean saveGame() {
		Gamejam.DPrint(accountmanager.getCurUsername());
		if(accountmanager.isGuest()) {
			return false;
		}
		// Don't save if the game was completed
		if(!gameModel.isStillRunning()) {
			gameModel.clearBoard();
		}
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			String fname = accountManager.getCurUsername() + "-" + gameName + ".dat";
			String sep = System.getProperty("file.separator");
			String filepath = System.getProperty("user.dir") + sep + "save-data";
			if(!new File(filepath).exists()) {
				new File(filepath).mkdir();
			}
			filepath += sep + fname;
			fos = new FileOutputStream(filepath);			
			oos = new ObjectOutputStream(fos);
			oos.writeObject(gameModel);
			oos.close();
			Gamejam.DPrint("Saved the game");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;		
	}
	
	@Override
	/**
	 * loads a saved game from the given filepath
	 * 
	 * @param filepath the location from which to load the game
	 * @return true if the load was successful, false otherwise
	 */
	public boolean loadSaveGame() {
		FileInputStream fis;
		ObjectInputStream ois;
		boolean retVal = true;
		try {
			String fname = accountManager.getCurUsername() + "-" + gameName + ".dat";
			String sep = System.getProperty("file.separator");
			String filepath = System.getProperty("user.dir") + sep + "save-data" + sep + fname;
			File file = new File(filepath);
			if(file.exists()) {
				fis = new FileInputStream(file);
				ois = new ObjectInputStream(fis);
				gameModel = (TicTacToeModel) ois.readObject();
				ois.close();
				update(gameModel, this);
				file.delete();
			} else {
				retVal = newGame();
			}
		} catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		clearView();
		setupBoard();
		setupListeners();
		gameModel.addObserver(this);
		update(gameModel, this);
		return true;	
	}

	@Override
	public boolean pauseGame() {
		try {
			disableListeners();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@Override
	public boolean unPauseGame() {
		try {
			setupListeners();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@Override
	public boolean newGame() {
		try {
			gameModel.clearBoard();
			clearView();
			setupBoard();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	private void clearView() {		
		ArrayList<Object> al = new ArrayList<Object>();
		for(Object o : _primarypane.getChildren()) {
			al.add(o);
		}
		_primarypane.getChildren().removeAll(al);		
	}

	@Override
	protected void updateStatistics() {
		if (!(gameModel.won('X') || gameModel.won('O')) && gameModel.maxMovesRemaining() > 0) {
			//accountManager.logGlobalStat(true, "Tic-Tac-Toe", logStatType.INCOMPLETE, 1);
			statsManager.logGameStat("Tic-Tac-Toe", logStatType.INCOMPLETE, 1);
		}
	}
}