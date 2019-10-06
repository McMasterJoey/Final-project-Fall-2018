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

public class ConnectFourControllerView extends GridPane implements Observer, GameJamGameInterface{
	private ConnectFourModel gameModel;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	private AudioClip moveSound;
	private AudioClip winSound;
	private AudioClip loseSound;
	private AudioClip tieSound;
	private AccountManager accountmanager;
	private StackPane[][] placeholder;
	
	/**
	 * Constructor for the Connect 4 controller-view
	 * sets up the board and various listeners
	 */
	public ConnectFourControllerView() {
		initializeGame();
		setupResources();
		accountmanager = AccountManager.getInstance();

		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);
	}
	
	private void initializeGame() {
		if (gameModel == null) {
			gameModel = new ConnectFourModel();
			//TODO gameModel.setAIStrategy(AINAME);
			gameModel.addObserver(this);
		} else {
			gameModel.clearBoard();
		}
		setupBoard();
		
	}

	private void setupResources() {
		// TODO Find clips, init them here
		
	}

	private void setupBoard() {
		placeholder = new StackPane[7][6];
		for(int row=0; row < 6; row++) {
			for(int col=0; col < 7; col++) {
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

	private void setupListeners() {
		this.setOnMouseClicked((click)->{			
			double offset = this.getChildren().get(0).getLayoutX();
			int col = ((int) ((click.getX() - offset) / 100));
			if(col >= 0 && col < 7 && gameModel.available(col)) {
				gameModel.humanMove(col, true);
			}
		});
	}
	
	private void disableListeners() {
		// TODO for(listener) listener.setOnMouseClicked(()->{});
	}
	
	@Override
	public boolean saveGame(String filepath) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean loadSaveGame(String filepath) {
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

	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println("--------------");
		System.out.println(gameModel.toString());
		System.out.println("--------------");
		
		char[][] board = gameModel.getBoard();
		for(int row = 0; row < 6; row++) {
			for(int col = 0; col < 7; col++) {
				if(board[row][col] != '_') {
					Circle circ = new Circle(43, board[row][col] == 'R' ? Color.INDIANRED : Color.YELLOW);
					placeholder[col][row].getChildren().add(circ);
				}
			}
		}
	}

}
