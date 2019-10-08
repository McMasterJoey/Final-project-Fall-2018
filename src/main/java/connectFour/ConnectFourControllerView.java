package connectFour;

import java.util.Observable;
import java.util.Observer;

import controller.AccountManager;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
public class ConnectFourControllerView extends GridPane implements Observer, GameJamGameInterface {
	private ConnectFourModel gameModel;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	private AudioClip moveSound, winSound, loseSound, tieSound;
	private AccountManager accountmanager;
	private StackPane[][] placeholder;

	/**
	 * Constructor for the Connect 4 controller-view sets up the board and various
	 * listeners
	 */
	public ConnectFourControllerView() {
		initializeGame();
		setupResources();
		accountmanager = AccountManager.getInstance();

		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);
	}

	/**
	 * Initializes the game by setting up the board and creating the model if
	 * necessary.
	 */
	private void initializeGame() {
		if (gameModel == null) {
			gameModel = new ConnectFourModel();
			// TODO gameModel.setAIStrategy(AINAME);
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
		placeholder = new StackPane[7][6];
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				Rectangle rect = new Rectangle(100, 100, Color.BLUE);
				Circle circ = new Circle(43, Color.WHITE);
				circ.setStroke(Color.BLACK);
				circ.setStrokeWidth(1);
				StackPane spane = new StackPane();
				spane.getChildren().addAll(rect, circ);
				this.add(spane, col, row);
				placeholder[col][row] = spane;
			}
		}

		setupListeners();
	}

	/**
	 * sets up the listener that determines where the player is trying to make a
	 * move
	 */
	private void setupListeners() {
		this.setOnMouseClicked((click) -> {
			// this finds the offset of the top left corner of the top
			// left grid so we can accurately determine clicks no matter
			// how the game is resized without having to write a listener for
			// each of the columns.
			moveSound.play();
			double offset = this.getChildren().get(0).getLayoutX();
			int col = ((int) ((click.getX() - offset) / 100));
			if (col >= 0 && col < 7 && gameModel.available(col)) {
				// TODO change to non testing when strategy is implemented.
				gameModel.humanMove(col, true);
			}
		});
	}

	/**
	 * disables the listeners
	 */
	private void disableListeners() {
		this.setOnMouseClicked((click) -> {
		});
	}

	@Override
	/**
	 * saves the game to the given filepath
	 * 
	 * @param filepath the name of the file we want to save
	 * @return true if the save was succesful, false otherwise
	 */
	public boolean saveGame(String filepath) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	/**
	 * loads a saved game from the given filepath
	 * 
	 * @param filepath the location from which to load the game
	 * @return true if the load was succesful, false otherwise
	 */
	public boolean loadSaveGame(String filepath) {
		// TODO Auto-generated method stub
		return false;
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
	 * @return true if the new game was started succesfully, false otherwise
	 */
	public boolean newGame() {
		try {
			initializeGame();
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
		
		System.out.println("--------------");
		System.out.println(gameModel.toString());
		System.out.println("--------------");
		
		if (gameModel.tied()) {
			accountmanager.logGlobalStat(true, "Tic-Tac-Toe", 2, 1);
			tieSound.play();
		} else if (gameModel.won('R') || gameModel.won('Y')) {
			disableListeners();
			if (gameModel.won('R')) {
				accountmanager.logGlobalStat(true, "Connect-Four", 0, 1);
				winSound.play();
			} else {
				accountmanager.logGlobalStat(true, "Connect-Four", 1, 1);
				loseSound.play();
			}
		}
	}

}
