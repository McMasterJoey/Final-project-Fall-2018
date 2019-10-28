 package battleship;

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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Combined Controller and View for Battleship game. Manipulates the counterpart
 * BattleshipModel class, draws the game to canvas, sets up the listeners to deal
 * with human moves, and acts as a BorderPane that can be hosted anywhere that wants
 * to hold the game.
 * @author Wes Rodgers
 *
 */

public class BattleshipControllerView extends GameControllerView {
	private BattleshipModel gameModel;
	private GameMenu menuBar;
	private static final int HEIGHT = 450;
	private static final int WIDTH = 450;
	private boolean shipsSet = false;
	private Canvas humanBoard;
	private GraphicsContext hbgc;
	private Canvas computerBoard;
	private GraphicsContext cbgc;
	private Canvas shipSelector;
	private GraphicsContext ssgc;
	private Image carrierImage, battleshipImage, destroyerImage, submarineImage, patrolBoatImage;
	
	public BattleshipControllerView() {
		gameModel = new BattleshipModel();
		humanBoard = new Canvas(WIDTH, HEIGHT);
		hbgc = humanBoard.getGraphicsContext2D();
		computerBoard = new Canvas(WIDTH, HEIGHT);
		cbgc = computerBoard.getGraphicsContext2D();
		shipSelector = new Canvas(WIDTH/2, HEIGHT);
		ssgc = shipSelector.getGraphicsContext2D();
		
		this.setRight(humanBoard);
		this.setLeft(shipSelector);
		
		gameName = "battleship";
		menuBar = GameMenu.getMenuBar(this);
		this.setTop(menuBar);
		setupResources();
		initializeGame();
		accountmanager = AccountManager.getInstance();

		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);
	}
	
	private void initializeGame() {
		if (gameModel == null) {
			gameModel = new BattleshipModel();
			/*gameModel.setAIStrategy(new <strategy/ai name>());*/
			gameModel.addObserver(this);
		} else {
			gameModel.clearBoard();
		}
		renderBoard();
		setupListeners();
		
	}

	private void setupResources() {
		carrierImage = new Image(BattleshipControllerView.class.getResource("/carrier.png").toString());
		battleshipImage = new Image(BattleshipControllerView.class.getResource("/battleship.png").toString());
		destroyerImage = new Image(BattleshipControllerView.class.getResource("/destroyer.png").toString());
		submarineImage = new Image(BattleshipControllerView.class.getResource("/submarine.png").toString());
		patrolBoatImage = new Image(BattleshipControllerView.class.getResource("/patrolBoat.png").toString());
	}
	
	private void renderBoard() {
		
		if(!shipsSet) {	
			
		} else {
			
		}
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
				gameModel = (BattleshipModel) ois.readObject();
				ois.close();
				update(gameModel, this);
				file.delete();
			} else {
				retVal = newGame();
			}
		} catch(IOException | ClassNotFoundException e) {
			return false;
		}
		renderBoard();
		setupListeners();
		gameModel.addObserver(this);
		update(gameModel, this);
		return retVal;
	}	

	@Override
	public boolean pauseGame() {
		try {
			disableListeners();
			return true;
		}catch(Exception ex) {
			return false;
		}
	}

	private void disableListeners() {
		shipSelector.setOnMouseClicked((click) ->{});
		computerBoard.setOnMouseClicked((click) ->{});		
	}

	@Override
	public boolean unPauseGame() {
		try {
			setupListeners();
			return true;
		}catch(Exception ex) {
			return false;
		}
	}

	private void setupListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean newGame() {
		try {
			gameModel.clearBoard();
			shipsSet = false;
			renderBoard();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@Override
	protected void updateStatistics() {
		if (!(gameModel.won(true) || gameModel.won(false)) && gameModel.maxMovesRemaining() > 0) {
			accountmanager.logGlobalStat(true, "Battleship", logStatType.INCOMPLETE, 1);
			accountmanager.logGameStat("Battleship", logStatType.INCOMPLETE, 1);
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
