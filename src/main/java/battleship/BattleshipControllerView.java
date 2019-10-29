 package battleship;

import java.awt.Point;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;

import battleship.Ship.Direction;
import controller.AccountManager;
import controller.GameControllerView;
import controller.GameMenu;
import controller.StatsManager;
import controller.logStatType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

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
	private static final int HEIGHT = 525;
	private static final int WIDTH = 525;
	private boolean shipsSet = false;
	private Canvas humanBoard;
	private GraphicsContext hbgc;
	private Canvas computerBoard;
	private GraphicsContext cbgc;
	private Image carrierImage, battleshipImage, destroyerImage, submarineImage, patrolBoatImage, 
		carrierVerticalImage, battleshipVerticalImage, destroyerVerticalImage, 
		submarineVerticalImage, patrolBoatVerticalImage;
	private AnchorPane gamePane;
	
	public BattleshipControllerView() {
		gameModel = new BattleshipModel();
		humanBoard = new Canvas(WIDTH, HEIGHT);
		hbgc = humanBoard.getGraphicsContext2D();
		computerBoard = new Canvas(WIDTH, HEIGHT);
		cbgc = computerBoard.getGraphicsContext2D();
		
		gamePane = new AnchorPane();
		gamePane.setMinWidth(WIDTH*2 + 50);
		this.setCenter(gamePane);
		AnchorPane.setTopAnchor(humanBoard, 50.0);
		AnchorPane.setRightAnchor(humanBoard, 25.0);
		AnchorPane.setTopAnchor(computerBoard, 50.0);
		gamePane.getChildren().add(humanBoard);
		
		gameName = "battleship";
		menuBar = GameMenu.getMenuBar(this);
		this.setTop(menuBar);
		setupResources();
		initializeGame();
		accountManager = AccountManager.getInstance();
		statsManager = StatsManager.getInstance();

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
		carrierVerticalImage = new Image(BattleshipControllerView.class.getResource("/carrierVertical.png").toString());
		battleshipVerticalImage = new Image(BattleshipControllerView.class.getResource("/battleshipVertical.png").toString());
		destroyerVerticalImage = new Image(BattleshipControllerView.class.getResource("/destroyerVertical.png").toString());
		submarineVerticalImage = new Image(BattleshipControllerView.class.getResource("/submarineVertical.png").toString());
		patrolBoatVerticalImage = new Image(BattleshipControllerView.class.getResource("/patrolBoatVertical.png").toString());
	}
	
	private void renderBoard() {
		drawBoard(hbgc);
		if(!shipsSet) {	
			for(Ship ship : gameModel.getHumanShips()) {
				Image boatImage = null;
				boatImage = getImage(ship);
				ShipView sv = new ShipView(boatImage, ship);
				int length = ship.getSize();
				sv.setFitWidth(WIDTH/10*length);
				sv.setFitHeight(HEIGHT/10);				
				makeDraggable(sv);
				((Pane) this.getCenter()).getChildren().add(sv);
			}
		} else {
			drawBoard(cbgc);
		}
	}
	
	private Image getImage(Ship ship) {
		Image boatImage = null;
		Direction dir = ship.getDirection();
		switch(ship.getName()) {
		case "carrier":
			boatImage = dir == Direction.HORIZONTAL ? carrierImage : carrierVerticalImage;
			break;
		case "battleship":
			boatImage = dir == Direction.HORIZONTAL ? battleshipImage : battleshipVerticalImage;
			break;
		case "destroyer":
			boatImage = dir == Direction.HORIZONTAL ? destroyerImage : destroyerVerticalImage;
			break;
		case "submarine":
			boatImage = dir == Direction.HORIZONTAL ? submarineImage : submarineVerticalImage;
			break;
		case "patrolBoat":
			boatImage = dir == Direction.HORIZONTAL ? patrolBoatImage : patrolBoatVerticalImage;
			break;
		default:
			break;
	}
	return boatImage;
}
private void drawBoard(GraphicsContext pbgc) {
		pbgc.setFill(Color.LIGHTSTEELBLUE);
		pbgc.fillRect(0,0,WIDTH,HEIGHT);
		pbgc.setStroke(Color.BLACK);
		pbgc.strokeLine(0, 0, 0, HEIGHT);
		pbgc.strokeLine(0, 0, WIDTH, 0);
		pbgc.strokeLine(WIDTH, HEIGHT, 0, HEIGHT);
		pbgc.strokeLine(WIDTH, HEIGHT, WIDTH, 0);
		for(int i = 0; i < 10; i++) {
			pbgc.strokeLine(i*(WIDTH/10) , 0, i*(WIDTH/10) , HEIGHT);
		}
		for(int i = 0; i < 10; i++) {
			pbgc.strokeLine(0, i*(HEIGHT/10), WIDTH, i*(HEIGHT/10));
		}
		
		//TODO draw ships to board
	}

	@Override
	/**
	 * saves the game to the given filepath
	 * 
	 * @param filepath the name of the file we want to save
	 * @return true if the save was successful, false otherwise
	 */
	public boolean saveGame() {
		if(accountManager.isGuest()) {
			return false;
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
			String fname = accountManager.getCurUsername() + "-" + gameName + ".dat";
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
			//accountManager.logGlobalStat(true, "Battleship", logStatType.INCOMPLETE, 1);
			statsManager.logGameStat("Battleship", logStatType.INCOMPLETE, 1, getScore());
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	//lifted almost wholesale from a stackoverflow answer from user jewelsea
	//https://stackoverflow.com/questions/17312734/how-to-make-a-draggable-node-in-javafx-2-0
	//just altered a little to look better here.
	private void makeDraggable(Node node) {
        final Point dragDelta = new Point();

        node.setOnMouseEntered(me -> {
            if (!me.isPrimaryButtonDown()) {
                node.getScene().setCursor(Cursor.OPEN_HAND);
            }
        });
        node.setOnMouseExited(me -> {
            if (!me.isPrimaryButtonDown()) {
                node.getScene().setCursor(Cursor.DEFAULT);
            }
        });
        node.setOnMousePressed(me -> {
        	ShipView sv = (ShipView) node;
            if (me.isPrimaryButtonDown()) {
                node.getScene().setCursor(Cursor.DEFAULT);
            }
            dragDelta.x = (int) me.getX();
            System.out.println("dragDelta x = " + dragDelta.x);
            dragDelta.y = (int) me.getY();
            System.out.println("dragDelta y = " + dragDelta.y);
            node.getScene().setCursor(Cursor.CLOSED_HAND);
            if(me.isSecondaryButtonDown()) {
				sv.getShip().setDirection(sv.getShip().getDirection() == Direction.HORIZONTAL ? 
						Direction.VERTICAL : Direction.HORIZONTAL);
				sv.setImage(getImage(sv.getShip()));
				sv.setFitWidth(sv.getShip().getDirection() == Direction.HORIZONTAL ? 
						WIDTH/10*sv.getShip().getSize() : WIDTH/10);
				sv.setFitHeight(sv.getShip().getDirection() == Direction.HORIZONTAL ? 
						HEIGHT/10 : HEIGHT/10*sv.getShip().getSize());	
			}
        });
        node.setOnMouseReleased(me -> {
            if (!me.isPrimaryButtonDown()) {
                node.getScene().setCursor(Cursor.DEFAULT);
            }
            System.out.println("Node x" + node.getLayoutX());
            System.out.println("Node y" + node.getLayoutY());
        });
        node.setOnMouseDragged(me -> {
            node.setLayoutX(node.getLayoutX() + me.getX() - dragDelta.x);
            node.setLayoutY(node.getLayoutY() + me.getY() - dragDelta.y);
            System.out.println("mouse x" + me.getX());
            System.out.println("mouse y" + me.getY());
        });
    }
}
