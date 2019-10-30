 package battleship;

import java.awt.Point;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;

import battleship.Ship.Direction;
import controller.AccountManager;
import controller.GameControllerView;
import controller.GameMenu;
import controller.StatsManager;
import controller.logStatType;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
		gamePane.getChildren().removeAll(gamePane.getChildren());
		gamePane.getChildren().add(humanBoard);
		drawBoard(hbgc, true);
		if(!shipsSet) {	
			int count = 0;
			for(Ship ship : gameModel.getHumanShips()) {
				Image boatImage = null;
				boatImage = getImage(ship);
				ShipView sv = new ShipView(boatImage, ship);
				int length = ship.getSize();
				sv.setFitWidth(WIDTH/10*length-4);
				sv.setFitHeight(HEIGHT/10-4);				
				makeDraggable(sv);
				gamePane.getChildren().add(sv);
				sv.setLayoutY(count*2*HEIGHT/10 + 50);
				sv.setLayoutX(WIDTH/2 - sv.getShip().getSize()*WIDTH/10/2);
				count++;
			}
			addSetButton();
		} else {
			gamePane.getChildren().add(computerBoard);
			drawBoard(cbgc, false);
		}
	}
	
	private void addSetButton() {
		Button setShips = new Button("Ready to play!");
		setShipsButtonStyle(setShips);
		setShipsButtonListener(setShips);
		BorderPane.setAlignment(setShips, Pos.CENTER);
		this.setBottom(setShips);
		
	}

	private void setShipsButtonStyle(Button setShips) {
		String buttonStyle = "-fx-padding: 8 15 15 15;" + 
				" -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;" + 
				" -fx-background-radius: 8;" + 
				" -fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%)," + 
				" #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);" + 
				" -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );" + 
				" -fx-font-weight: bold;" + 
				" -fx-font-size: 1.1em;";
		setShips.setStyle(buttonStyle);
		
		setShips.setOnMouseEntered((me) -> {
			String bStyle = "-fx-padding: 8 15 15 15;" + 
					" -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;" + 
					" -fx-background-radius: 8;" + 
					" -fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a3431a 0%, #903b19 100%)," + 
					" #9d402c, #d86e40, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);" + 
					" -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );" + 
					" -fx-font-weight: bold;" + 
					" -fx-font-size: 1.6em;";;
			setShips.setStyle(bStyle);
		});
		
		setShips.setOnMouseExited((me) -> {
			String bStyle = "-fx-padding: 8 15 15 15;" + 
					" -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;" + 
					" -fx-background-radius: 8;" + 
					" -fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%)," + 
					" #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);" + 
					" -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );" + 
					" -fx-font-weight: bold;" + 
					" -fx-font-size: 1.1em;";
			setShips.setStyle(bStyle);
		});
	}

	private void setShipsButtonListener(Button setShips) {
		setShips.setOnMouseClicked((click) -> {
			for(Node node : gamePane.getChildren()) {
				if(node instanceof ShipView) {
					if(((ShipView) node).getLayoutX() < humanBoard.getLayoutX() 
							|| ((ShipView) node).getLayoutY() < humanBoard.getLayoutY()){
						showNotPlacedMessage();
						return;
					}
				}
			}
			
			boolean set = showVerifyPositionMessage();
			if(!set) {
				return;
			}
			
			ArrayList<ShipView> toRemove = new ArrayList<ShipView>();
			for(Node node : gamePane.getChildren()) {
				if(node instanceof ShipView) {
					ShipView sv = (ShipView) node;
					Ship ship = sv.getShip();
					int row = ((int) (sv.getLayoutX() - humanBoard.getLayoutX())) / ((int) WIDTH/10);
					int col = ((int) (sv.getLayoutY() - humanBoard.getLayoutY())) / ((int) WIDTH/10);
					int endRow = row + (ship.getDirection() == Direction.HORIZONTAL ? 0 : ship.getSize());
					int endCol = col + (ship.getDirection() == Direction.HORIZONTAL ? ship.getSize() : 0);
					ship.setPosition(new Point(col, row), new Point(endCol, endRow));
					toRemove.add(sv);
				}
			}
			gamePane.getChildren().removeAll(toRemove);
			gamePane.getChildren().add(computerBoard);
			shipsSet = true;
			this.getChildren().remove(setShips);
			renderBoard();
		});
		
	}

	private boolean shipsOverlapping() {
		
		for(Node node : gamePane.getChildren()) {
			if(node instanceof ShipView) {
				ShipView sv = (ShipView) node;
				
				for(Node node2 : gamePane.getChildren()) {
					
					if(node2 instanceof ShipView) {
						ShipView sv2 = (ShipView) node2;
						
						if(sv != sv2) {
							Point rect1 = new Point((int) sv.getLayoutX(), (int) sv.getLayoutY());
							Point rect2 = new Point((int) sv2.getLayoutX(), (int) sv2.getLayoutY());
							int rect1Width = WIDTH/10 * (sv.getShip().getDirection() == 
									Direction.HORIZONTAL ? sv.getShip().getSize() : 1);
							int rect1Height = HEIGHT/10 * (sv.getShip().getDirection() ==
									Direction.HORIZONTAL ? 1 : sv.getShip().getSize());
							int rect2Width = WIDTH/10 * (sv2.getShip().getDirection() == 
									Direction.HORIZONTAL ? sv2.getShip().getSize() : 1);
							int rect2Height = HEIGHT/10 * (sv2.getShip().getDirection() ==
									Direction.HORIZONTAL ? 1 : sv2.getShip().getSize());
							
							if(rect1.x < rect2.x + rect2Width 
									&& rect1.x + rect1Width > rect2.x
									&& rect1.y < rect2.y + rect2Height
									&& rect1.y + rect1Height > rect2.y) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean showVerifyPositionMessage() {
		// TODO Auto-generated method stub
		return true;
	}

	private void adjustShipPosition(ShipView sv, Point initialLocation) {
		if(sv.getLayoutX() < humanBoard.getLayoutX() - WIDTH/10/4
				|| sv.getLayoutY() < humanBoard.getLayoutY() - HEIGHT/10/4
				|| sv.getLayoutX() > humanBoard.getLayoutX() + WIDTH
				|| sv.getLayoutY() > humanBoard.getLayoutY() + HEIGHT) {
			sv.setLayoutX(initialLocation.x);
			sv.setLayoutY(initialLocation.y);
		} else {
			int xPosOnBoard = (int) (sv.getLayoutX() - humanBoard.getLayoutX());
			int yPosOnBoard = (int) (sv.getLayoutY() - humanBoard.getLayoutY());
			int snapX = (int) (humanBoard.getLayoutX() + ((xPosOnBoard + WIDTH/10/4) / (WIDTH/10)) * (WIDTH/10));
			int snapY = (int) (humanBoard.getLayoutY() + ((yPosOnBoard + HEIGHT/10/4) / (HEIGHT/10)) * (HEIGHT/10));
			sv.setLayoutX(snapX);
			sv.setLayoutY(snapY);
			
			if(shipsOverlapping()) {
				sv.setLayoutX(initialLocation.x);
				sv.setLayoutY(initialLocation.y);
			}
		}
	}

	private void showNotPlacedMessage() {
		// TODO Auto-generated method stub
		
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
private void drawBoard(GraphicsContext pbgc, boolean human) {
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
		
		if(shipsSet && human) {
			for(Ship ship : gameModel.getHumanShips()) {
				Image shipImage = getImage(ship);
				pbgc.drawImage(shipImage, ship.getPoints()[0].getY() * WIDTH/10, 
					ship.getPoints()[0].getX() * HEIGHT/10,
					ship.getDirection() == Direction.HORIZONTAL ? ship.getSize()*WIDTH/10 - 4 : WIDTH/10 - 4,
					ship.getDirection() == Direction.HORIZONTAL ? HEIGHT/10 - 4 : ship.getSize()*HEIGHT/10 - 4);
			}
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
				shipsSet = true;
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
		computerBoard.setOnMouseClicked((click) ->{
			int clickX = (int) click.getX() / (WIDTH/10);
			int clickY = (int) click.getY() / (HEIGHT/10);
			gameModel.humanMove(clickY, clickX);
		});		
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
        final Point initialLocation = new Point();

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
            initialLocation.x = (int) sv.getLayoutX();
            initialLocation.y = (int) sv.getLayoutY();
            dragDelta.x = (int) me.getX();
            dragDelta.y = (int) me.getY();
            node.getScene().setCursor(Cursor.CLOSED_HAND);
            if(me.isSecondaryButtonDown()) {
            	flipShip(sv);
				if(shipsOverlapping()) {
					flipShip(sv);
				}
			}
        });
        node.setOnMouseReleased(me -> {
            if (!me.isPrimaryButtonDown()) {
                node.getScene().setCursor(Cursor.DEFAULT);
            }
            adjustShipPosition((ShipView) node, initialLocation);
        });
        node.setOnMouseDragged(me -> {
            node.setLayoutX(node.getLayoutX() + me.getX() - dragDelta.x);
            node.setLayoutY(node.getLayoutY() + me.getY() - dragDelta.y);
        });
    }

	private void flipShip(ShipView sv) {
		sv.getShip().setDirection(sv.getShip().getDirection() == Direction.HORIZONTAL ? 
				Direction.VERTICAL : Direction.HORIZONTAL);
		
		sv.setImage(getImage(sv.getShip()));
		
		sv.setFitWidth(sv.getShip().getDirection() == Direction.HORIZONTAL ? 
				WIDTH/10*sv.getShip().getSize()-4 : WIDTH/10-4);
		
		sv.setFitHeight(sv.getShip().getDirection() == Direction.HORIZONTAL ? 
				HEIGHT/10-4 : HEIGHT/10*sv.getShip().getSize()-4);		
	}
}
