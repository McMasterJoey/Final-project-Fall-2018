package connectFour;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

import controller.AccountManager;
import controller.logStatType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.GameJamGameInterface;
import ticTacToe.TicTacToeControllerView;

/**
 * Combined Controller and View of the MVC design pattern. This class exists to
 * manipulate its counterpart ConnectFourModel object and to display that on the
 * screen as an extension of GridPane. Implements GameJameGameInterface to
 * provide methods for controlling the game in a meta sense, saving/loading, new
 * game, etc.
 * 
 * @author Wes Rodgers
 *
 */
public class ConnectFourControllerView extends BorderPane implements Observer, GameJamGameInterface {
	private ConnectFourModel gameModel;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	private AudioClip moveSound, winSound, loseSound, tieSound;
	private AccountManager accountmanager;
	private StackPane[][] placeholder;
	private String gameName = "connect-four";
	
	// Original implemention by Wes has the object extending gridpane, this that grid pane.
	// Updated version uses a border pane with original grid pane set to its center to add game speific options
	// Should look and play the exact same way as before.
	private GridPane _primarypane;
	/**
	 * Constructor for the Connect 4 controller-view sets up the board and various
	 * listeners
	 */
	public ConnectFourControllerView() {
		this.setTop(setUpMenuBar());
		initializeGame();
		setupResources();
		accountmanager = AccountManager.getInstance();

		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);
		_primarypane.setPrefHeight(HEIGHT);
		_primarypane.setPrefWidth(WIDTH);
	}

	/**
	 * Initializes the game by setting up the board and creating the model if
	 * necessary.
	 */
	private void initializeGame() {
		if (gameModel == null) {
			gameModel = new ConnectFourModel();
			gameModel.setAIStrategy(new ConnectFourHardAI());
			gameModel.addObserver(this);
		} else {
			gameModel.clearBoard();
		}
		setupBoard();

	}

	/**
	 * intializes the proper soundfx resources for this game.
	 */
	private void setupResources() {
		moveSound = new AudioClip(TicTacToeControllerView.class.getResource("/moveSound.mp3").toString());
		winSound = new AudioClip(TicTacToeControllerView.class.getResource("/winSound.mp3").toString());
		loseSound = new AudioClip(TicTacToeControllerView.class.getResource("/loseSound.mp3").toString());
		tieSound = new AudioClip(TicTacToeControllerView.class.getResource("/tieSound.mp3").toString());
	}

	/**
	 * Draws the correct squares and circles to represent an empty connect Four
	 * board to the screen
	 */
	private void setupBoard() {
		_primarypane = new GridPane();
		placeholder = new StackPane[7][6];
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				Rectangle rect = new Rectangle(100, 100, Color.BLUE);
				Circle circ = new Circle(43, Color.WHITE);
				circ.setStroke(Color.BLACK);
				circ.setStrokeWidth(1);
				StackPane spane = new StackPane();
				spane.getChildren().addAll(rect, circ);
				_primarypane.add(spane, col, row);
				placeholder[col][row] = spane;
			}
		}
		this.setCenter(_primarypane);
		setupListeners();
	}

	/**
	 * sets up the listener that determines where the player is trying to make a
	 * move
	 */
	private void setupListeners() {
		_primarypane.setOnMouseClicked((click) -> {
			// this finds the offset of the top left corner of the top
			// left grid so we can accurately determine clicks no matter
			// how the game is resized without having to write a listener for
			// each of the columns.
			moveSound.play();
			double offset = _primarypane.getChildren().get(0).getLayoutX();
			int col = ((int) ((click.getX() - offset) / 100));
			if (col >= 0 && col < 7 && gameModel.available(col)) {
				// TODO change to non testing when strategy is implemented.
				gameModel.humanMove(col, false);
			}
		});
	}

	/**
	 * disables the listeners
	 */
	private void disableListeners() {
		_primarypane.setOnMouseClicked((click) -> {
		});
	}

	@Override
	/**
	 * saves the game to the given filepath
	 * 
	 * @param filepath the name of the file we want to save
	 * @return true if the save was successful, false otherwise
	 */
	public boolean saveGame(String filepath) {
		if(accountmanager.isGuest()) {
			return false;
		}
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			String fname = accountmanager.getCurUsername() + "-" + gameName + ".dat";
			String sep = System.getProperty("file.separator");
			fos = new FileOutputStream(System.getProperty("user.dir") + sep + "save-data" + sep + fname);			
			oos = new ObjectOutputStream(fos);
			oos.writeObject(gameModel);
			oos.close();
		} catch (IOException e) {
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
	public boolean loadSaveGame(String filepath) {
		FileInputStream fis;
		ObjectInputStream ois;
		try {
			fis = new FileInputStream(filepath);
			ois = new ObjectInputStream(fis);
			gameModel = (ConnectFourModel) ois.readObject();
			ois.close();
			update(gameModel, this);
		} catch(IOException | ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	@Override
	/**
	 * pauses the game
	 * 
	 * @return true if the pause is successful, false otherwise
	 */
	public boolean pauseGame() {
		try {
			disableListeners();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@Override
	/**
	 * unpauses the game
	 * 
	 * @return true if the unpause was successful, false otherwise
	 */
	public boolean unPauseGame() {
		try {
			setupListeners();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@Override
	/**
	 * creates a new game
	 * 
	 * @return true if the new game was started successfully, false otherwise
	 */
	public boolean newGame() {
		try {
			gameModel.clearBoard();
			setupBoard();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@Override
	public void update(Observable arg0, Object arg1) {

		char[][] board = gameModel.getBoard();
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				if (board[row][col] != '_') {
					Circle circ = new Circle(43, board[row][col] == 'R' ? Color.INDIANRED : Color.YELLOW);
					placeholder[col][row].getChildren().add(circ);
				}
			}
		}
		
		//System.out.println("--------------");
		//System.out.println(gameModel.toString());
		//System.out.println("--------------");
		
		if (gameModel.tied()) {
			accountmanager.logGlobalStat(true, "Connect-Four", logStatType.TIE, 1);
			accountmanager.logGameStat("Connect-Four", logStatType.TIE, 0);
			tieSound.play();
		} else if (gameModel.won('R') || gameModel.won('Y')) {
			disableListeners();
			if (gameModel.won('R')) {
				accountmanager.logGlobalStat(true, "Connect-Four", logStatType.WIN, 1);
				accountmanager.logGameStat("Connect-Four", logStatType.WIN, 0);
				winSound.play();
			} else {
				accountmanager.logGlobalStat(true, "Connect-Four", logStatType.LOSS, 1);
				accountmanager.logGameStat("Connect-Four", logStatType.LOSS, 0);
				loseSound.play();
			}
		}
	}
	/**
	 * Sets up the Menubar for the game Tic-tac-toe
	 * @return The menu bar to be placed at the top of the game's UI.
	 */
	private MenuBar setUpMenuBar() {
		MenuBar bar = new MenuBar();
		Menu optmenu = new Menu("Connect Four Options");
		MenuItem newgame = new MenuItem("Start New Game");
		MenuItem diffhard = new MenuItem("Set diffuclty to Hard");
		MenuItem diffeasy = new MenuItem("Set diffuclty to Easy");
		newgame.setOnAction((event) -> { 
			if (!(gameModel.won('R') || gameModel.won('Y')) && gameModel.maxMovesRemaining() > 0) {
				accountmanager.logGlobalStat(true, "Connect-Four", logStatType.INCOMPLETE, 1);
				accountmanager.logGameStat("Connect-Four", logStatType.INCOMPLETE, 1);
			}
			boolean result = newGame();
			if (!result) {
				System.err.println("ERROR on trying to set new Connect 4 game!");
			}
		});
		diffhard.setOnAction((event) -> { 
			gameModel.setAIStrategy(new ConnectFourHardAI());
		});
		diffeasy.setOnAction((event) -> { 
			gameModel.setAIStrategy(new ConnectFourEasyAI());
		});
		optmenu.getItems().addAll(newgame,diffeasy,diffhard);
		bar.getMenus().addAll(optmenu);
		return bar;
	}
}
