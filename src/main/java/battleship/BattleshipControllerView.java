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
import java.util.Optional;

import battleship.Ship.Direction;
import controller.AccountManager;
import controller.GameControllerView;
import controller.GameMenu;
import controller.LogStatType;
import controller.StatsManager;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

/**
 * Combined Controller and View for Battleship game. Manipulates the counterpart
 * BattleshipModel class, draws the game to canvas, sets up the listeners to
 * deal with human moves, and acts as a BorderPane that can be hosted anywhere
 * that wants to hold the game.
 * 
  * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */

public class BattleshipControllerView extends GameControllerView {
	private static final int HEIGHT = 525;
	private static final int WIDTH = 525;
	private BattleshipModel gameModel;
	private GameMenu menuBar;
	private boolean shipsSet = false;
	private boolean gameOver = false;
	private Canvas humanBoard, computerBoard;
	private GraphicsContext hbgc, cbgc;
	private Image carrierImage, battleshipImage, destroyerImage, submarineImage, patrolBoatImage, carrierVerticalImage,
			battleshipVerticalImage, destroyerVerticalImage, submarineVerticalImage, patrolBoatVerticalImage;
	private AudioClip winSound, loseSound;
	private AnchorPane gamePane;

	/**
	 * Constructor for BattleshipControllerView. Initializes all of the fields
	 * including creating a new BattleshipModel, sets up the view.
	 */
	public BattleshipControllerView() {
		humanBoard = new Canvas(WIDTH, HEIGHT);
		computerBoard = new Canvas(WIDTH, HEIGHT);
		hbgc = humanBoard.getGraphicsContext2D();
		cbgc = computerBoard.getGraphicsContext2D();

		// adjustments to how the game is displayed
		gamePane = new AnchorPane();
		gamePane.setMinWidth(WIDTH * 2 + 50);
		this.setCenter(gamePane);
		AnchorPane.setTopAnchor(humanBoard, 50.0);
		AnchorPane.setRightAnchor(humanBoard, 25.0);
		AnchorPane.setTopAnchor(computerBoard, 50.0);
		gamePane.getChildren().add(humanBoard);

		gameName = "battleship";
		menuBar = GameMenu.getMenuBar(this);
		menuBar.getDiffeasy().setOnAction((event) ->{
			gameModel.setAIStrategy(new BattleshipEasyAI());
		});
		menuBar.getDiffinter().setOnAction((event) ->{
			gameModel.setAIStrategy(new BattleshipIntermediateAI());
		});
		this.setTop(menuBar);

		accountManager = AccountManager.getInstance();
		statsManager = StatsManager.getInstance();

		setupResources();
		initializeGame();
	}

	/**
	 * Initializes the game by either creating a new model or clearing the old one.
	 * Then renders the board and sets up the listeners.
	 */
	private void initializeGame() {

		if (gameModel == null) {
			gameModel = new BattleshipModel();
			gameModel.setAIStrategy(new BattleshipIntermediateAI());
			gameModel.addObserver(this);
		} else {
			gameModel.clearBoard();
			gameOver = false;
			setupListeners();
		}

		renderBoard();
		setupListeners();
	}

	/**
	 * Initializes all of the various image resources we need to play Battleship
	 */
	private void setupResources() {
		carrierImage = new Image(BattleshipControllerView.class.getResource("/carrier.png").toString());
		battleshipImage = new Image(BattleshipControllerView.class.getResource("/battleship.png").toString());
		destroyerImage = new Image(BattleshipControllerView.class.getResource("/destroyer.png").toString());
		submarineImage = new Image(BattleshipControllerView.class.getResource("/submarine.png").toString());
		patrolBoatImage = new Image(BattleshipControllerView.class.getResource("/patrolBoat.png").toString());
		carrierVerticalImage = new Image(BattleshipControllerView.class.getResource("/carrierVertical.png").toString());
		battleshipVerticalImage = new Image(
				BattleshipControllerView.class.getResource("/battleshipVertical.png").toString());
		destroyerVerticalImage = new Image(
				BattleshipControllerView.class.getResource("/destroyerVertical.png").toString());
		submarineVerticalImage = new Image(
				BattleshipControllerView.class.getResource("/submarineVertical.png").toString());
		patrolBoatVerticalImage = new Image(
				BattleshipControllerView.class.getResource("/patrolBoatVertical.png").toString());
		winSound = new AudioClip(BattleshipControllerView.class.getResource("/winSound.mp3").toString());
		loseSound = new AudioClip(BattleshipControllerView.class.getResource("/loseSound.mp3").toString());
	}

	/**
	 * Renders the board as it currently needs to be
	 */
	private void renderBoard() {

		// first, clear the board then add and draw the human side as it is always
		// displayed
		gamePane.getChildren().removeAll(gamePane.getChildren());
		gamePane.getChildren().add(humanBoard);
		drawBoard(hbgc, true);

		// if the ships haven't been set yet, create ShipViews for each gamepiece
		// so the user can set their board with them
		if (!shipsSet) {
			int count = 0;

			for (Ship ship : gameModel.getHumanShips()) {
				ShipView sv = new ShipView(getImage(ship), ship);
				sv.setFitWidth(WIDTH / 10 * ship.getSize() - 4);
				sv.setFitHeight(HEIGHT / 10 - 4);
				makeDraggable(sv);
				gamePane.getChildren().add(sv);

				// this is just code tweaking placement so the ships are center aligned
				// on the left half of the screen
				sv.setLayoutY(count * 2 * HEIGHT / 10 + 50);
				sv.setLayoutX(WIDTH / 2 - sv.getShip().getSize() * WIDTH / 10 / 2);
				count++;
			}
			addSetButton();
		} else {
			// otherwise, add the computer board and draw it
			gamePane.getChildren().add(computerBoard);
			drawBoard(cbgc, false);
		}
	}

	/**
	 * creates a button that allows user to set their ships and adds it to this view
	 */
	private void addSetButton() {
		Button setShips = new Button("Ready to play!");
		setShipsButtonStyle(setShips);
		setShipsButtonListener(setShips);
		BorderPane.setAlignment(setShips, Pos.CENTER);
		this.setBottom(setShips);

	}

	/**
	 * sets up the style for the setShips Button
	 * 
	 * @param setShips the Button for which we want to set the style
	 */
	private void setShipsButtonStyle(Button setShips) {

		// css styling for the setShips button
		String buttonStyle = "-fx-padding: 8 15 15 15;" + " -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;"
				+ " -fx-background-radius: 8;"
				+ " -fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%),"
				+ " #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);"
				+ " -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );" + " -fx-font-weight: bold;"
				+ " -fx-font-size: 1.1em;";
		setShips.setStyle(buttonStyle);

		// when we hover we want the button to expand a little bit
		setShips.setOnMouseEntered((me) -> {
			String bStyle = "-fx-padding: 8 15 15 15;" + " -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;"
					+ " -fx-background-radius: 8;"
					+ " -fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a3431a 0%, #903b19 100%),"
					+ " #9d402c, #d86e40, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);"
					+ " -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );" + " -fx-font-weight: bold;"
					+ " -fx-font-size: 1.6em;";
			;
			setShips.setStyle(bStyle);
		});

		// and when we exit we want it to look normal again
		setShips.setOnMouseExited((me) -> {
			setShips.setStyle(buttonStyle);
		});
	}

	/**
	 * Sets up the listener for clicks for the setShips Button
	 * 
	 * @param setShips the Button for which we want to set the setShips listener.
	 */
	private void setShipsButtonListener(Button setShips) {

		setShips.setOnMouseClicked((click) -> {

			// iterate through the ShipViews, if any aren't on the board give the user a
			// message
			// to place all their ships
			for (Node node : gamePane.getChildren()) {
				if (node instanceof ShipView) {
					if (((ShipView) node).getLayoutX() < humanBoard.getLayoutX()
							|| ((ShipView) node).getLayoutY() < humanBoard.getLayoutY()) {
						showNotPlacedMessage();
						return;
					}
				}
			}

			// show an alert that verifies that the user wants to use this ship
			// configuration. If not,
			// break out of the listener.
			boolean set = showVerifyPositionMessage();
			if (!set) {
				return;
			}

			// iterate through once more, setting all ships and removing their ShipView
			ArrayList<ShipView> toRemove = new ArrayList<ShipView>();
			for (Node node : gamePane.getChildren()) {
				if (node instanceof ShipView) {
					ShipView sv = (ShipView) node;
					Ship ship = sv.getShip();

					// just math to figure out where on the board the ship currently is
					int row = ((int) (sv.getLayoutY() - humanBoard.getLayoutY())) / ((int) HEIGHT / 10);
					int col = ((int) (sv.getLayoutX() - humanBoard.getLayoutX())) / ((int) WIDTH / 10);

					// in determining the end location, we subtract 1 if we add any to compensate
					// for the fact
					// that the first location already has one unit of the ship's length in it.
					int endRow = row + (ship.getDirection() == Direction.HORIZONTAL ? 0 : ship.getSize() - 1);
					int endCol = col + (ship.getDirection() == Direction.HORIZONTAL ? ship.getSize() - 1 : 0);
					ship.setPosition(new Point(col, row), new Point(endCol, endRow));
					toRemove.add(sv);
				}
			}

			// remove the now unnecessary ShipViews, add the computerBoard to view
			// let the controller know that the ships have been set, remove the setShips
			// button
			// and redraw the game space.
			gamePane.getChildren().removeAll(toRemove);
			gamePane.getChildren().add(computerBoard);
			shipsSet = true;
			this.getChildren().remove(setShips);
			renderBoard();
		});

	}

	/**
	 * Returns true if any ships are overlapping currently, false otherwise
	 * 
	 * @return true if any ships are currently overlapping, false otherwise
	 */
	private boolean shipsOverlapping() {

		// iterate through the ships
		for (Node node : gamePane.getChildren()) {
			if (node instanceof ShipView) {
				ShipView sv = (ShipView) node;

				// iterate through the ships again
				for (Node node2 : gamePane.getChildren()) {
					if (node2 instanceof ShipView) {
						ShipView sv2 = (ShipView) node2;

						// check every ship against every other ship that isn't itself
						if (sv != sv2) {

							// determine ship width and height based on the size of the ship, the length
							// in board units, and the size of a board unit
							Point rect1 = new Point((int) sv.getLayoutX(), (int) sv.getLayoutY());
							Point rect2 = new Point((int) sv2.getLayoutX(), (int) sv2.getLayoutY());
							int rect1Width = WIDTH / 10
									* (sv.getShip().getDirection() == Direction.HORIZONTAL ? sv.getShip().getSize()
											: 1);
							int rect1Height = HEIGHT / 10 * (sv.getShip().getDirection() == Direction.HORIZONTAL ? 1
									: sv.getShip().getSize());
							int rect2Width = WIDTH / 10
									* (sv2.getShip().getDirection() == Direction.HORIZONTAL ? sv2.getShip().getSize()
											: 1);
							int rect2Height = HEIGHT / 10 * (sv2.getShip().getDirection() == Direction.HORIZONTAL ? 1
									: sv2.getShip().getSize());

							// this is the actual collision check, it doesn't actually need to be
							// checking on a pixel by pixel basis but it doesn't hurt anything and I already
							// had code written to this effect elsewhere. HOORAY, RECYCLING!
							if (rect1.x < rect2.x + rect2Width && rect1.x + rect1Width > rect2.x
									&& rect1.y < rect2.y + rect2Height && rect1.y + rect1Height > rect2.y) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Creates an alert to verify that the user wants to place their ships at the
	 * current configuration
	 * 
	 * @return true if the user clicks OK, false otherwise
	 */
	private boolean showVerifyPositionMessage() {
		Alert verifyAlert = new Alert(AlertType.CONFIRMATION);
		verifyAlert.setTitle("Confirm ship placement");
		verifyAlert.setHeaderText("Are you sure you want to use this ship configuration?");
		Optional<ButtonType> result = verifyAlert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			return true;
		}
		return false;
	}

	/**
	 * Snaps the ShipView to the board's grid if it fits constraints such as being
	 * on the board and not overlapping other ships, returns it to its location
	 * before being dragged otherwise.
	 * 
	 * @param sv              the ShipView object we want to snap to the grid
	 * @param initialLocation the ShipView's location before being dragged
	 */
	private void adjustShipPosition(ShipView sv, Point initialLocation) {

		// check that the ships top left corner has been placed on the board, reset to
		// initial location if not
		if (sv.getLayoutX() < humanBoard.getLayoutX() - WIDTH / 10 / 3
				|| sv.getLayoutY() < humanBoard.getLayoutY() - HEIGHT / 10 / 3
				|| sv.getLayoutX() > humanBoard.getLayoutX() + WIDTH
				|| sv.getLayoutY() > humanBoard.getLayoutY() + HEIGHT) {

			sv.setLayoutX(initialLocation.x);
			sv.setLayoutY(initialLocation.y);

		} else {

			// find where the ShipView is relative to the Canvas
			int xPosOnBoard = (int) (sv.getLayoutX() - humanBoard.getLayoutX());
			int yPosOnBoard = (int) (sv.getLayoutY() - humanBoard.getLayoutY());

			// exploit double and integer interaction to find the location the ShipView
			// *should* be placed
			// adding 1/3 of the total board size allows us to guess at intended location
			// for sloppy placements
			// then dividing by 52 gives us the actual unit on board. Multiplying again by
			// 52.5, the proper unit
			// size, will let us know the actual placement
			double snapX = humanBoard.getLayoutX() + (((xPosOnBoard + (int) (52.5 / 3)) / 52) * 52.5);
			double snapY = humanBoard.getLayoutY() + (((yPosOnBoard + (int) (52.5 / 3)) / 52) * 52.5);
			sv.setLayoutX(snapX);
			sv.setLayoutY(snapY);

			// if the ships are overlapping or off the board after
			if (shipsOverlapping() || shipsOffBoard()) {
				sv.setLayoutX(initialLocation.x);
				sv.setLayoutY(initialLocation.y);
			}
		}
	}

	/**
	 * Checks to see if the ships are all on the board. Returns true if any is off
	 * board, false otherwise
	 * 
	 * @return true if any ships are off board currently, false otherwise
	 */
	private boolean shipsOffBoard() {

		// iterate through the shipviews
		for (Node node : gamePane.getChildren()) {
			if (node instanceof ShipView && ((ShipView) node).getLayoutX() + WIDTH / 10 / 3 > humanBoard.getLayoutX()) {
				ShipView sv = (ShipView) node;

				// beginning location has already been checked and always will have been, so we
				// just adjust to find the location of the *last* part of the ship.
				int shipX = (int) (sv.getLayoutX() - humanBoard.getLayoutX()) / (WIDTH / 10)
						+ (sv.getShip().getDirection() == Direction.HORIZONTAL ? sv.getShip().getSize() - 1 : 0);
				int shipY = (int) (sv.getLayoutY() - humanBoard.getLayoutY()) / (HEIGHT / 10)
						+ (sv.getShip().getDirection() == Direction.HORIZONTAL ? 0 : sv.getShip().getSize() - 1);
				if (shipX >= 10 || shipY >= 10) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Creates and shows an alert to let the user know that they haven't yet placed
	 * all of their ships
	 */
	private void showNotPlacedMessage() {
		Alert verifyAlert = new Alert(AlertType.INFORMATION);
		verifyAlert.setContentText(
				accountManager.getCurUsername() + ", you need to place all of your ships before you try to set them.");
		verifyAlert.setTitle("Hol' up,");
		verifyAlert.setHeaderText("Wait a minute...");
		verifyAlert.showAndWait();
	}

	/**
	 * Finds the appropriate image resource for a given ship and returns it
	 * 
	 * @param ship
	 * @return
	 */
	private Image getImage(Ship ship) {
		Image boatImage = null;
		Direction dir = ship.getDirection();

		// we return an image for the ship based on its name and its current direction
		switch (ship.getName()) {
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

	/**
	 * draws to the passed in graphics context based on board situations
	 * 
	 * @param pbgc  the graphics context we are drawing to
	 * @param human true if we are using the human board, false otherwise
	 */
	private void drawBoard(GraphicsContext pbgc, boolean human) {

		// fill in the board and the grid
		pbgc.setFill(Color.LIGHTSTEELBLUE);
		pbgc.fillRect(0, 0, WIDTH, HEIGHT);
		pbgc.setStroke(Color.BLACK);
		pbgc.strokeLine(0, 0, 0, HEIGHT);
		pbgc.strokeLine(0, 0, WIDTH, 0);
		pbgc.strokeLine(WIDTH, HEIGHT, 0, HEIGHT);
		pbgc.strokeLine(WIDTH, HEIGHT, WIDTH, 0);
		for (int i = 0; i < 10; i++) {
			pbgc.strokeLine(i * ((double) WIDTH / (double) 10), 0, i * ((double) WIDTH / (double) 10), (double) HEIGHT);
		}
		for (int i = 0; i < 10; i++) {
			pbgc.strokeLine(0, i * ((double) HEIGHT / (double) 10), (double) WIDTH,
					i * ((double) HEIGHT / (double) 10));
		}

		// if the ships have been set and we're on the human board, draw human ships to
		// their positions
		if ((shipsSet && human) || (shipsSet && gameOver)) {
			for (Ship ship : human ? gameModel.getHumanShips() : gameModel.getComputerShips()) {
				Image shipImage = getImage(ship);
				pbgc.drawImage(shipImage,
						(double) ship.getPoints()[0].getX() * (double) WIDTH / (double) 10 + (double) 2,
						(double) ship.getPoints()[0].getY() * (double) HEIGHT / (double) 10 + (double) 2,
						ship.getDirection() == Direction.HORIZONTAL ? ship.getSize() * WIDTH / 10 - 4 : WIDTH / 10 - 4,
						ship.getDirection() == Direction.HORIZONTAL ? HEIGHT / 10 - 4
								: ship.getSize() * HEIGHT / 10 - 4);
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
		if (accountManager.isGuest()) {
			return false;
		}

		// Don't save if the game was completed
		if (!gameModel.isStillRunning()) {
			newGame();
		}

		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			String fname = accountManager.getCurUsername() + "-" + gameName + ".dat";
			String sep = System.getProperty("file.separator");
			String filepath = System.getProperty("user.dir") + sep + "save-data";
			if (!new File(filepath).exists()) {
				new File(filepath).mkdir();
			}
			filepath += sep + fname;
			fos = new FileOutputStream(filepath);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(gameModel);
			oos.close();
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
		boolean retVal = true;
		try {
			String fname = accountManager.getCurUsername() + "-" + gameName + ".dat";
			String sep = System.getProperty("file.separator");
			String filepath = System.getProperty("user.dir") + sep + "save-data" + sep + fname;
			File file = new File(filepath);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				gameModel = (BattleshipModel) ois.readObject();
				ois.close();
				update(gameModel, this);
				file.delete();
				shipsSet = gameModel.getHumanShips().get(0).isSet();
			} else {
				retVal = newGame();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		renderBoard();
		setupListeners();
		gameModel.addObserver(this);
		update(gameModel, this);
		return retVal;
	}

	/**
	 * Pauses the game by disabling listeners
	 */
	@Override
	public boolean pauseGame() {
		try {
			disableListeners();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * disables listeners
	 */
	private void disableListeners() {
		computerBoard.setOnMouseClicked((click) -> {
		});
	}

	/**
	 * unpauses the game by reenabling the listeners
	 */
	@Override
	public boolean unPauseGame() {
		try {
			setupListeners();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Sets up the listener for user moves
	 */
	private void setupListeners() {

		// just finds where on the board the user clicked and attempts to make a
		// move at that location
		computerBoard.setOnMouseClicked((click) -> {
			int clickX = (int) click.getX() / (WIDTH / 10);
			int clickY = (int) click.getY() / (HEIGHT / 10);
			if (clickX >= 10 || clickX < 0 || clickY >= 10 || clickY < 0) {
				return;
			}
			gameModel.humanMove(clickY, clickX);
		});
	}

	/**
	 * creates a new game
	 */
	@Override
	public boolean newGame() {
		try {
			gameModel.clearBoard();
			gameModel.getBattleshipAI().setBoard(gameModel.getComputerShips());
			shipsSet = false;
			gameOver = false;
			setupListeners();
			renderBoard();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * updates the user statistics
	 */
	@Override
	protected void updateStatistics() {
		if (!(gameModel.won(true) || gameModel.won(false)) && gameModel.maxMovesRemaining() > 0) {
			statsManager.logGameStat("Battleship", LogStatType.INCOMPLETE, 1, getScore());
		}
	}

	/**
	 * after a move has been made, updates the board view with the new info
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if (!gameModel.isStillRunning()) {
			gameOver = true;
			disableListeners();

			if (gameModel.won(true)) {
				winSound.play();
				statsManager.logGameStat("Battleship",  LogStatType.WIN, 1, getScore());
			} else {
				loseSound.play();
				statsManager.logGameStat("Battleship",  LogStatType.LOSS, 1, getScore());
			}
			showEndScreen();
		}
		if (shipsSet) {
			hbgc.clearRect(0, 0, WIDTH, HEIGHT);
			cbgc.clearRect(0, 0, WIDTH, HEIGHT);
			renderBoard();
			updateBoard(true);
			updateBoard(false);
		}
	}

	/**
	 * draws moves that have been made to a given board
	 * 
	 * @param b
	 */
	private void updateBoard(boolean b) {
		GraphicsContext board = b ? hbgc : cbgc;

		// iterate through the board
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {

				// if the spot isn't available, a move has been made there
				if (!gameModel.available(row, col, b)) {

					// if a ship is on that position, color is red. Color is white if not.
					board.setFill(gameModel.isShip(row, col, b) ? Color.RED : Color.WHITE);
					board.fillOval(col * WIDTH / 10 + WIDTH / 10 / 4, row * HEIGHT / 10 + HEIGHT / 10 / 4,
							WIDTH / 10 / 2, HEIGHT / 10 / 2);
				}
			}
		}

	}

	/**
	 * Returns the score for the game.
	 */
	@Override
	public int getScore() {
		if (gameModel.isStillRunning()) {
			return 0;
		}
		int baseScore;
		double difficultyModifier = gameModel.getBattleshipAI().getStrategy() instanceof BattleshipEasyAI ? 1.0 : 1.5;
		int temp = gameModel.maxMovesRemaining();
		int winModifier = 1 * (gameModel.won(true) ? 1 : -1);
		temp = temp * winModifier + 83;
		baseScore = temp * 15 + 10;
		return (int) (baseScore * difficultyModifier);
	}

	// lifted almost wholesale from a stackoverflow answer from user jewelsea
	// https://stackoverflow.com/questions/17312734/how-to-make-a-draggable-node-in-javafx-2-0
	// just altered a little to look better here.
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
			if (me.isSecondaryButtonDown()) {
				flipShip(sv);
				if (shipsOverlapping() || shipsOffBoard()) {
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

	/**
	 * Flips the ship from horizontal or vertical to the other direction and updates
	 * its image accordingly
	 * 
	 * @param sv the ship view we want to flip
	 */
	private void flipShip(ShipView sv) {

		// flip the direction
		sv.getShip().setDirection(
				sv.getShip().getDirection() == Direction.HORIZONTAL ? Direction.VERTICAL : Direction.HORIZONTAL);

		// get the new image
		sv.setImage(getImage(sv.getShip()));

		// set the width
		sv.setFitWidth(sv.getShip().getDirection() == Direction.HORIZONTAL ? WIDTH / 10 * sv.getShip().getSize() - 4
				: WIDTH / 10 - 4);

		// set the height
		sv.setFitHeight(sv.getShip().getDirection() == Direction.HORIZONTAL ? HEIGHT / 10 - 4
				: HEIGHT / 10 * sv.getShip().getSize() - 4);
	}

	private class ShipView extends ImageView {
		private Ship ship;

		public ShipView(Image img, Ship ship) {
			super(img);
			this.ship = ship;
		}

		public Ship getShip() {
			return ship;
		}
	}

	@Override
	protected String wonString() {
		return gameModel.won(true) ? "won" : "lost";
	}
}
