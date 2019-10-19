package connectFour;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;

import controller.AccountManager;
import controller.GameControllerView;
import controller.GameMenu;
import controller.logStatType;

import javafx.geometry.Pos;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

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
public class ConnectFourControllerView extends GameControllerView {
	private ConnectFourModel gameModel;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	private AudioClip moveSound, winSound, loseSound, tieSound;
	private StackPane[][] placeholder;
	
	// Original implemention by Wes has the object extending gridpane, this that grid pane.
	// Updated version uses a border pane with original grid pane set to its center to add game speific options
	// Should look and play the exact same way as before.
	private GridPane _primarypane;
	private GameMenu menuBar;
	// Size of 5: 0 = Overall color, 1 = Blank Circle color, 2 = Circle outline color 
	//            3 = Player Piece color, 4 = AI Piece color 
	private Color[] themesettings; // Used to
	/**
	 * Constructor for the Connect 4 controller-view sets up the board and various
	 * listeners
	 */
	public ConnectFourControllerView() {
		themesettings = new Color[5];
		themesettings[0] = Color.BLUE;
		themesettings[1] = Color.WHITE;
		themesettings[2] = Color.BLACK;
		themesettings[3] = Color.INDIANRED;
		themesettings[4] = Color.YELLOW;
		
		gameName = "connect-four";
		menuBar = GameMenu.getMenuBar(this);
		this.setTop(menuBar);
		initializeGame();
		setupResources();
		accountmanager = AccountManager.getInstance();

		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);
		_primarypane.setPrefHeight(HEIGHT);
		_primarypane.setPrefWidth(WIDTH);
		addCustomUIOptions();
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
				Rectangle rect = new Rectangle(100, 100, themesettings[0]);
				Circle circ = new Circle(43, themesettings[1]);
				circ.setStroke(themesettings[2]);
				circ.setStrokeWidth(1);
				StackPane spane = new StackPane();
				spane.getChildren().addAll(rect, circ);
				_primarypane.add(spane, col, row);
				placeholder[col][row] = spane;
			}
		}
		this.setCenter(_primarypane);
		_primarypane.setAlignment(Pos.CENTER);
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
		
		menuBar.getDiffinter().setOnAction((event) -> { 
			gameModel.setAIStrategy(new ConnectFourHardAI());
		});
		menuBar.getDiffeasy().setOnAction((event) -> { 
			gameModel.setAIStrategy(new ConnectFourEasyAI());
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
	public boolean saveGame() {
		if(accountmanager.isGuest()) {
			return false;
		}
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			String fname = accountmanager.getCurUsername() + "-" + gameName + ".dat";
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
	public boolean loadSaveGame() {
		boolean retVal = true;
		try {
			String fname = accountmanager.getCurUsername() + "-" + gameName + ".dat";
			String sep = System.getProperty("file.separator");
			String filepath = System.getProperty("user.dir") + sep + "save-data" + sep + fname;
			File file = new File(filepath);
			if(file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				gameModel = (ConnectFourModel) ois.readObject();
				ois.close();
				update(gameModel, this);
				file.delete();
			} else {
				retVal = newGame();
			}
		} catch(IOException | ClassNotFoundException e) {
			return false;
		}
		setupBoard();
		setupListeners();
		gameModel.addObserver(this);
		update(gameModel, this);
		return retVal;
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
					Circle circ = new Circle(43, board[row][col] == 'R' ? themesettings[3] : themesettings[4]);
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
	@Override
	protected void updateStatistics() {
		if (!(gameModel.won('R') || gameModel.won('Y')) && gameModel.maxMovesRemaining() > 0) {
			accountmanager.logGlobalStat(true, "Connect-Four", logStatType.INCOMPLETE, 1);
			accountmanager.logGameStat("Connect-Four", logStatType.INCOMPLETE, 1);
		}
	}
	/**
	 * Redraws the game GUI. Ment to be used after changing the theme.
	 */
	private void reDrawGameUI() {
		placeholder = new StackPane[7][6];
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				Rectangle rect = new Rectangle(100, 100, themesettings[0]);
				Circle circ = new Circle(43, themesettings[1]);
				circ.setStroke(themesettings[2]);
				circ.setStrokeWidth(1);
				StackPane spane = new StackPane();
				spane.getChildren().addAll(rect, circ);
				_primarypane.add(spane, col, row);
				placeholder[col][row] = spane;
			}
		}
		
		char[][] board = gameModel.getBoard();
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				if (board[row][col] != '_') {
					Circle circ = new Circle(43, board[row][col] == 'R' ? themesettings[3] : themesettings[4]);
					placeholder[col][row].getChildren().add(circ);
				}
			}
		}
	}
	/**
	 * 
	 */
	private void addCustomUIOptions() {
		Menu thememenu = new Menu("Theme Menu");
		MenuItem opt1 = new MenuItem("Set Default Theme");
		opt1.setOnAction((event) -> {
			themesettings[0] = Color.BLUE;
			themesettings[1] = Color.WHITE;
			themesettings[2] = Color.BLACK;
			themesettings[3] = Color.INDIANRED;
			themesettings[4] = Color.YELLOW;
			reDrawGameUI();
		});
		MenuItem opt2 = new MenuItem("Set Alternate Theme");
		opt2.setOnAction((event) -> {
			themesettings[0] = Color.DARKRED;
			themesettings[1] = Color.GREEN;
			themesettings[2] = Color.RED;
			themesettings[3] = Color.BLACK;
			themesettings[4] = Color.WHITE;
			reDrawGameUI();
		});
		MenuItem opt3 = new MenuItem("Set America Theme");
		opt3.setOnAction((event) -> {
			themesettings[0] = Color.AQUA;
			themesettings[1] = Color.WHITE;
			themesettings[2] = Color.GREY;
			themesettings[3] = Color.RED;
			themesettings[4] = Color.BLUE;
			reDrawGameUI();
		});
		MenuItem opt4 = new MenuItem("Set MLG PRO Theme");
		opt4.setOnAction((event) -> {
			themesettings[0] = Color.BLACK;
			themesettings[1] = Color.GREY;
			themesettings[2] = Color.WHITE;
			themesettings[3] = Color.GREEN;
			themesettings[4] = Color.RED;
			reDrawGameUI();
		});
		MenuItem opt5 = new MenuItem("Set Greyscale Theme");
		opt5.setOnAction((event) -> {
			themesettings[0] = Color.WHITE;
			themesettings[1] = Color.WHITE;
			themesettings[2] = Color.WHITE;
			themesettings[3] = Color.BLACK;
			themesettings[4] = Color.GREY;
			reDrawGameUI();
		});
		thememenu.getItems().addAll(opt1,opt2,opt3,opt4,opt5);
		menuBar.getMenus().add(thememenu);
	}
}
