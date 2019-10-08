package ticTacToe;

import java.util.Observable;
import java.util.Observer;

import controller.AccountManager;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import model.GameJamGameInterface;

/**
 * Provides a GUI view and the listeners required for players to make Tic Tac
 * Toe moves with mouse clicks.
 * 
 * @author Wes Rodgers
 *
 */
public class TicTacToeControllerView extends GridPane implements Observer, GameJamGameInterface {

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
	private AccountManager accountmanager;

	public TicTacToeControllerView() {
		initializeGame();
		setupResources();
		accountmanager = AccountManager.getInstance();

		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);
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

		this.setAlignment(Pos.CENTER);
		topLeft = new BorderPane();
		topLeft.setStyle("-fx-border-color : white black black white; -fx-border-width : 2");
		topLeft.setPrefSize(200, 200);
		this.add(topLeft, 0, 0);
		placeholder[0][0] = topLeft;

		midLeft = new BorderPane();
		midLeft.setStyle("-fx-border-color : black black black white; -fx-border-width : 2");
		midLeft.setPrefSize(200, 200);
		this.add(midLeft, 0, 1);
		placeholder[0][1] = midLeft;

		botLeft = new BorderPane();
		botLeft.setStyle("-fx-border-color : black black white white; -fx-border-width : 2");
		botLeft.setPrefSize(200, 200);
		this.add(botLeft, 0, 2);
		placeholder[0][2] = botLeft;

		topCenter = new BorderPane();
		topCenter.setStyle("-fx-border-color : white black black black; -fx-border-width : 2");
		topCenter.setPrefSize(200, 200);
		this.add(topCenter, 1, 0);
		placeholder[1][0] = topCenter;

		midCenter = new BorderPane();
		midCenter.setStyle("-fx-border-color : black black black black; -fx-border-width : 2");
		midCenter.setPrefSize(200, 200);
		this.add(midCenter, 1, 1);
		placeholder[1][1] = midCenter;

		botCenter = new BorderPane();
		botCenter.setStyle("-fx-border-color : black black white black; -fx-border-width : 2");
		botCenter.setPrefSize(200, 200);
		this.add(botCenter, 1, 2);
		placeholder[1][2] = botCenter;

		topRight = new BorderPane();
		topRight.setStyle("-fx-border-color : white white black black; -fx-border-width : 2");
		topRight.setPrefSize(200, 200);
		this.add(topRight, 2, 0);
		placeholder[2][0] = topRight;

		midRight = new BorderPane();
		midRight.setStyle("-fx-border-color : black white black black; -fx-border-width : 2");
		midRight.setPrefSize(200, 200);
		this.add(midRight, 2, 1);
		placeholder[2][1] = midRight;

		botRight = new BorderPane();
		botRight.setStyle("-fx-border-color : black white white black; -fx-border-width : 2");
		botRight.setPrefSize(200, 200);
		this.add(botRight, 2, 2);
		placeholder[2][2] = botRight;

		setupListeners();

	}

	/**
	 * sets up the click listener for the board. Finds which spot the user clicked
	 * on and attempts to make a move at that spot.
	 */
	private void setupListeners() {
		topLeft.setOnMouseClicked((click) -> {
			gameModel.humanMove(0, 0, false);
		});

		midLeft.setOnMouseClicked((click) -> {
			gameModel.humanMove(1, 0, false);
		});

		botLeft.setOnMouseClicked((click) -> {
			gameModel.humanMove(2, 0, false);
		});

		topCenter.setOnMouseClicked((click) -> {
			gameModel.humanMove(0, 1, false);
		});

		midCenter.setOnMouseClicked((click) -> {
			gameModel.humanMove(1, 1, false);
		});

		botCenter.setOnMouseClicked((click) -> {
			gameModel.humanMove(2, 1, false);
		});

		topRight.setOnMouseClicked((click) -> {
			gameModel.humanMove(0, 2, false);
		});

		midRight.setOnMouseClicked((click) -> {
			gameModel.humanMove(1, 2, false);
		});

		botRight.setOnMouseClicked((click) -> {
			gameModel.humanMove(2, 2, false);
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
		moveSound.play();
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
					this.getChildren().remove(placeholder[j][i]);
					this.add(box, j, i);
				}
			}
		}
		System.out.println(gameModel.toString());
		if (gameModel.tied()) {
			accountmanager.logGlobalStat(true, "Tic-Tac-Toe", 2, 1);
			tieSound.play();
		} else if (gameModel.won('X') || gameModel.won('O')) {
			if (gameModel.won('X')) {
				accountmanager.logGlobalStat(true, "Tic-Tac-Toe", 0, 1);
				winSound.play();
			} else {
				accountmanager.logGlobalStat(true, "Tic-Tac-Toe", 1, 1);
				loseSound.play();
			}

			/*
			 * *****Old code to figure out a stroke for the winning row/col/diagonal****
			 * String winningDirection = gameModel.getWinningDirection(); Point[]
			 * winningSquares = gameModel.getWinningSquares(winningDirection);
			 * gc.setStroke(Color.BLUE);
			 * 
			 * switch (winningDirection) { case "horizontal": gc.strokeLine(0,
			 * winningSquares[0].x*200+100, 600, winningSquares[2].x*200+100); break; case
			 * "vertical": gc.strokeLine(winningSquares[0].y*200+100, 0,
			 * winningSquares[2].y*200+100, 600); break; case "diagonal": if
			 * (winningSquares[0].x == 0) { gc.strokeLine(0, 0, 600, 600); } else {
			 * gc.strokeLine(0, 600, 600, 0); } break; } gc.setStroke(Color.BLACK);
			 */
		}
	}

	@Override
	public boolean loadSaveGame(String filepath) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveGame(String filepath) {
		// TODO Auto-generated method stub
		return false;
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
			initializeGame();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

}