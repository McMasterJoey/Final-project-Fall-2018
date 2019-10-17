package Gamejam;

import java.util.Collections;
import java.util.Observer;

import connectFour.ConnectFourControllerView;

import java.util.ArrayList;
import java.util.Observable;
import controller.AccountManager;
import controller.DBGameManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import model.GameJamGameInterface;
import model.SanityCheckFailedException;
import ticTacToe.TicTacToeControllerView;

/**
 * The MainScreen view. Should be the first thing that pops up when loading the
 * application Should be what is returned to after closing a game.
 * 
 * @author Joey McMaster
 * @author Wes Rodgers
 *
 */
public class GamejamMainScreen extends BorderPane implements Observer {
	private GridPane initGameselectonboxarea;
	private HBox initTopBar;
	private HBox initLoggedInBar;
	private HBox initCreateAccountMenuBar;
	private HBox initLoggedInInGameBar;
	private VBox initLeftBar;
	private Label leftBarMsg;
	private Label leftBarStats;
	private VBox initCreateAccountMenu;
	private AccountManager acctMgr;
	private DBGameManager dbGameManager;
	private TicTacToeControllerView tictactoegameview;
	private int gameInUseIndex = -1;
	private GameIconItem[] initgamelist;
	private ConnectFourControllerView connectFourGameView;
	private String loggedinusername;
	private boolean userLoggedIn = false;
	private boolean userisAdmin = false;
	private boolean DEBUG_FakeDatabase = false; // REMOVE WHEN DONE
	public GamejamMainScreen() {
		super();
		init();
	}
////////////////////////View Init Functions go here /////////////////////////////////////////////
	/**
	 * Inits the Object
	 */
	private void init() {
		// Get the references to the database connector classes
		// KEEP THESE AT TOP OR YOU WILL HAVE FUN WITH NULL POINTER EXECPTIONS!
		this.acctMgr = AccountManager.getInstance();
		this.acctMgr.addObserver(this);
		this.dbGameManager = DBGameManager.getInstance();
		
		// Set up GUI Elements
		this.initTopBar = initTopBar();
		this.initGameselectonboxarea = initGamePanel();
		this.initLeftBar = initLeftBar();
		
		// Not in user parts that can be used later
		this.initCreateAccountMenu = initCreateAccountScreen();
		this.initLoggedInBar = initLoggedInBar();
		this.initCreateAccountMenuBar = initCreateAccountMenuBar();
		this.initLoggedInInGameBar = initLoggedInInGameBar();
		
		// Set up Game Views
		this.tictactoegameview = new TicTacToeControllerView();
		this.connectFourGameView = new ConnectFourControllerView();
		
		// Set currently in user views
		this.setTop(this.initTopBar);
		this.setCenter(this.initGameselectonboxarea);
		this.setLeft(this.initLeftBar);
	}
	/**
	 * Generates the control structure that will exist the left bar.
	 * 
	 * @return A VBox that contains all the structures for the left bar.
	 */
	private VBox initLeftBar() {
		VBox retval = new VBox();
		retval.setPrefWidth(145);
		retval.setPrefHeight(578);

		leftBarMsg = new Label();
		leftBarMsg.setWrapText(true);
		leftBarStats = new Label();
		setGuestMessage();
		
		retval.getChildren().addAll(leftBarMsg, leftBarStats);
		return retval;
	}

	/**
	 * Generates the control structure that will exist in the top bar while creating
	 * an account.
	 * 
	 * @return An HBox that contains all the structure for the top bar while
	 *         creating an account.
	 */
	private HBox initCreateAccountMenuBar() {
		HBox leftbox = new HBox();
		Button mainmenu = new Button("Back To Main Menu");
		mainmenu.setPrefWidth(144);
		mainmenu.setPrefHeight(25);
		mainmenu.setOnMouseClicked((click) -> {
			doBackToMainMenuButtonClickInCreateAccountMenuBar();
		});
		// Add to Left Hbox
		leftbox.getChildren().add(mainmenu);
		leftbox.setPrefWidth(758);
		leftbox.setPrefHeight(25);
		leftbox.setAlignment(Pos.TOP_LEFT); // Set it so it aligns on the left
		return leftbox;
	}
	/**
	 * Gets the item that is surposed to be the top most part of the application
	 * This should be the clickable menus that allow the use to log in and adjust
	 * their account settings.
	 * 
	 * @return A HBox with all the options already added to it.
	 */
	private HBox initTopBar() {
		HBox retval = new HBox(); // General Box
		retval.setAlignment(Pos.TOP_RIGHT); // Set it so it aligns on the right
		HBox leftbox = new HBox();
		// Create New Account Button
		Button newacc = new Button("Create New Account");
		newacc.setPrefWidth(144);
		newacc.setPrefHeight(25);
		newacc.setOnMouseClicked((click) -> {
			createNewAccountButtonClick();
		});
		// Add to Left Hbox
		leftbox.getChildren().add(newacc);
		leftbox.setPrefWidth(758);
		leftbox.setPrefHeight(25);
		leftbox.setAlignment(Pos.TOP_LEFT); // Set it so it aligns on the left
		// Add to right hbox first to acheive correct look
		retval.getChildren().add(leftbox);

		TextField username = new TextField();
		username.setPromptText("username");
		username.setPrefWidth(280);
		username.setPrefHeight(25);
		PasswordField password = new PasswordField();
		password.setPromptText("password");
		password.setPrefWidth(165);
		password.setPrefHeight(25);
		// username.set
		Button login = new Button("Login");
		login.setPrefWidth(144);
		login.setPrefHeight(25);
		login.setTextFill(Color.RED);
		login.setTextAlignment(TextAlignment.CENTER);
		login.setOnMouseClicked((click) -> {
			loginButtonClick();
		});
		retval.getChildren().addAll(username, password, login);
		retval.setPrefWidth(600);
		retval.setPrefHeight(25);
		return retval;
	}

	/**
	 * Generates the item that will be put in the top bar of the application while a
	 * user is at the main screen and logged in.
	 * 
	 * @return An HBox with the control structures to act as the top bar of the
	 *         application
	 */
	private HBox initLoggedInBar() {
		HBox retval = new HBox(); // General Box
		retval.setAlignment(Pos.TOP_RIGHT); // Set it so it aligns on the right
		HBox leftbox = new HBox();
		// Create New Account Button
		Button logout = new Button("Logout");
		logout.setPrefWidth(144);
		logout.setPrefHeight(25);
		logout.setOnMouseClicked((click) -> {
			logoutButtonClick();
		});
		// Add to Left Hbox
		leftbox.getChildren().add(logout);
		leftbox.setPrefWidth(1100);
		leftbox.setPrefHeight(25);
		leftbox.setAlignment(Pos.TOP_LEFT); // Set it so it aligns on the left
		// Add to right hbox first to acheive correct look
		retval.getChildren().add(leftbox);

		Label loggedinusername = new Label("Test User");
		loggedinusername.setPrefWidth(400);
		Button settings = new Button();
		settings.setPrefWidth(25);
		settings.setPrefHeight(25);
		Image icon = new Image(getClass().getResourceAsStream("/usersettingsbuttonbackground.png"));
		settings.setGraphic(new ImageView(icon));
		settings.setOnMouseClicked((click) -> {
			userSettingsButtonClick();
		});
		retval.getChildren().addAll(loggedinusername, settings);
		retval.setPrefWidth(600);
		retval.setPrefHeight(25);
		return retval;
	}
	/**
	 * Generates the iteme that will be put in the top bar of the application while a user is in a game and logged in.
	 * @return An HBox with the control structures to act as the top bar of the application.
	 */
	private HBox initLoggedInInGameBar() {
		HBox retval = new HBox(); // General Box
		retval.setAlignment(Pos.TOP_RIGHT); // Set it so it aligns on the right
		HBox leftbox = new HBox();
		// Create New Account Button
		Button logout = new Button("Back To Main Menu");
		logout.setPrefWidth(144);
		logout.setPrefHeight(25);
		logout.setOnMouseClicked((click) -> {
			BackToMainMenuButtonClickLoggedIn();
		});
		// Add to Left Hbox
		leftbox.getChildren().add(logout);
		leftbox.setPrefWidth(1100);
		leftbox.setPrefHeight(25);
		leftbox.setAlignment(Pos.TOP_LEFT); // Set it so it aligns on the left
		// Add to right hbox first to acheive correct look
		retval.getChildren().add(leftbox);

		Label loggedinusername = new Label("Test User");
		loggedinusername.setPrefWidth(400);
		Button settings = new Button();
		settings.setPrefWidth(25);
		settings.setPrefHeight(25);
		Image icon = new Image(getClass().getResourceAsStream("/usersettingsbuttonbackground.png"));
		settings.setGraphic(new ImageView(icon));
		settings.setOnMouseClicked((click) -> {
			userSettingsButtonClick();
		});
		retval.getChildren().addAll(loggedinusername, settings);
		retval.setPrefWidth(600);
		retval.setPrefHeight(25);
		return retval;
	}

	/**
	 * Creates the Create Account Center Pane Screen
	 * 
	 * @return The VBox containing the elements of the screen.
	 */
	private VBox initCreateAccountScreen() {
		VBox retval = new VBox();
		Label info = new Label("Type in your Username and Password, then click the Create Account Button.");
		TextField username = new TextField();
		username.setPromptText("username");
		username.setPrefWidth(300);
		username.setPrefHeight(25);
		PasswordField password = new PasswordField();
		password.setPromptText("password");
		password.setPrefWidth(300);
		password.setPrefHeight(25);
		Button makeaccount = new Button("Create Account");
		makeaccount.setOnMouseClicked((click) -> {
			createNewAccountButtonOnFinishedClick();
		});
		retval.getChildren().addAll(info, username, password, makeaccount);
		retval.setPrefWidth(600);
		retval.setPrefHeight(600);
		return retval;
	}
	/**
	 * Gets the item that surposed to be in the center of the application. In this
	 * case its the game selector menu.
	 * 
	 * @return A grid pane of all the buttons to lead to each game.
	 */
	private GridPane initGamePanel() {
		GridPane grid = new GridPane();
		grid.getColumnConstraints().add(new ColumnConstraints(272));
		grid.getColumnConstraints().add(new ColumnConstraints(272));
		grid.getColumnConstraints().add(new ColumnConstraints(272));
		grid.getColumnConstraints().add(new ColumnConstraints(272));
		this.initgamelist = getGameList();
		for (int x = 0; x < this.initgamelist.length; x++) {
			// Sanity check
			if (this.initgamelist[x].getGameID() < 0 || this.initgamelist[x].getGameID() > 11) {
				throw new SanityCheckFailedException("When adding game icons, one of the games had id that was out of range!");
			}
			//
			GameButton gamebutton = new GameButton();
			Image icon = new Image(getClass().getResourceAsStream(this.initgamelist[x].getIconFilePath()));
			gamebutton.setGraphic(new ImageView(icon));
			gamebutton.setMetaDataString(this.initgamelist[x].getName());
			gamebutton.setOnMouseClicked((click) -> {
				GameButton but = (GameButton) click.getSource();
				gameButtonClick(but.getMetaDataString());
			});
			grid.add(gamebutton, x % 4, x / 4);
		}
		return grid;
	}
//////////////////////// Button Click Handlers go here  /////////////////////////////////////////////
	/**
	 * Handles when a user wants to exit the create account screen without creating an account.
	 */
	private void doBackToMainMenuButtonClickInCreateAccountMenuBar() {
		this.setTop(this.initTopBar);
		this.setCenter(this.initGameselectonboxarea);
	}
	/**
	 * Handles the event were the user is logged in and is in a game and wants to go back to the main menu.
	 */
	private void BackToMainMenuButtonClickLoggedIn() {
		stopAndSaveCurrentGame();
		this.setTop(this.initLoggedInBar);
		this.setCenter(this.initGameselectonboxarea);
		updateLeftPane();
	}
	/**
	 * Handles the event were the Log out button is clicked Assumes: A user is
	 * already logged in.
	 */
	private void logoutButtonClick() {
		acctMgr.logout();
		userLoggedIn = false;
		System.out.println("Logout!");
		this.setTop(this.initTopBar);
		//updateLeftPane();
	}

	/**
	 * Handles the event were the User Settings button is clicked Assumes: A user is
	 * already logged in.
	 */
	private void userSettingsButtonClick() {
		// TODO: Implement User Settings
		System.out.println("User settings!");
	}

	/**
	 * Handles the event were the Log in Button is Clicked
	 */
	private void loginButtonClick() {
		// Ever time the initTopBar is changed, update this function
		TextField username = (TextField) this.initTopBar.getChildren().get(1);
		PasswordField password = (PasswordField) this.initTopBar.getChildren().get(2);
		boolean successful = doLogin(username.getText(), password.getText());
		if (successful) {
			this.userLoggedIn = true;
			this.loggedinusername = username.getText();
			UpdateLoggedInBarsWithUserNameOfCurrentUser();
			this.setTop(this.initLoggedInBar);
		} else {
			username.setPromptText("Invalid username or password");
			username.setText("");
			password.setText("");
		}
		
		//updateLeftPane();
	}
	/**
	 * Handles the event were the Create New Account Button is Clicked
	 */
	private void createNewAccountButtonClick() {
		// Reset state of the Create Account Menu
		Label info = (Label) this.initCreateAccountMenu.getChildren().get(0);
		Button button = (Button) this.initCreateAccountMenu.getChildren().get(3);
		info.setText("Type in your Username and Password, then click the Create Account Button.");
		button.setTextFill(Color.BLACK);
		// Set it
		this.setTop(initCreateAccountMenuBar);
		this.setCenter(this.initCreateAccountMenu);
	}

	/**
	 * Handles the act of creating an account.
	 */
	private void createNewAccountButtonOnFinishedClick() {
		// Ever time I change initCreateAccountScreen, I need to update this function
		Label info = (Label) initCreateAccountMenu.getChildren().get(0);
		TextField username = (TextField) this.initCreateAccountMenu.getChildren().get(1);
		PasswordField password = (PasswordField) this.initCreateAccountMenu.getChildren().get(2);
		Button button = (Button) this.initCreateAccountMenu.getChildren().get(3);
		int status = doCreateNewAccount(username.getText(), password.getText());
		if (status == 1 || status == 0) {
			userLoggedIn = true;
			this.loggedinusername = username.getText();
			button.setTextFill(Color.BLACK);
			doGUIUpdateOnCreateAccountSuccess();
		} else if (status == 2) {
			System.out.println("Username already in use");
			// TODO: Handle username already in use
			info.setText("User name: '" + username.getText() + "' already in use. Try a diffrent one.");
			button.setTextFill(Color.RED);
		} else if (status == 3) {
			System.err.println("Other error encounted on account creation");
			// TODO: Handle other error in creation
			info.setText("Error in account creation, try again later.");
			button.setTextFill(Color.RED);
		}
	}
	/**
	 * Mapping function, maps game button clicks to their respected game and inits
	 * it.
	 * 
	 * @param name The name field of the button clicked, used to ID it.
	 */
	private void gameButtonClick(String name) {
		this.gameInUseIndex = -1;
		if (name.equals("Tic-Tac-Toe")) {
			this.gameInUseIndex = 0;
			if (userLoggedIn) {
				this.setTop(this.initLoggedInInGameBar);
			} else {
				this.setTop(this.initCreateAccountMenuBar);
			}
			this.setCenter(this.tictactoegameview);
		}
		if (name.equals("Connect-Four")) {
			if(userLoggedIn) {
				this.setTop(this.initLoggedInInGameBar);
			} else {
				this.setTop(this.initCreateAccountMenuBar);
			}
			this.setCenter(this.connectFourGameView);
			//connectFourGameView.setAlignment(Pos.CENTER);
		}
	}
/////////////////////////////// GUI Update Functions go here ///////////////////////////////////////////

	/**
	 * Updates the GUI after a new account is made succesfuly,
	 */
	private void doGUIUpdateOnCreateAccountSuccess() {
		System.out.println("Account creation successful");
		UpdateLoggedInBarsWithUserNameOfCurrentUser();
		this.setTop(this.initLoggedInBar);
		this.setCenter(this.initGameselectonboxarea);
		updateLeftPane();
	}
	/**
	 * Sets the message to the left panel.
	 * Assumes the user is a guest.
	 */
	private void setGuestMessage() {
		this.leftBarMsg.setText("\nYou are not logged in.\nIf you were logged in, you could see your stats!");
		this.leftBarStats.setText("\n\nLevel: 0\nExp: 0");
	}
	/**
	 * Updates the object when the objects it observes changes.
	 */
	@Override
	public void update(Observable o, Object obj) {
		updateLeftPane();
	}
	/**
	 * Updates left pane
	 */
	private void updateLeftPane() {
		if (this.acctMgr.isGuest()) {
			setGuestMessage();
		} else if (acctMgr.isAdmin()) {
			this.leftBarMsg.setText("Administrator Account");
			this.leftBarStats.setText("");
		} else {
			acctMgr.refreshUserStats();
			this.leftBarMsg.setText("Welcome to Gamejam, " + this.acctMgr.getCurUsername());
			this.leftBarStats.setText("\n\nLevel: " + this.acctMgr.getLevel() + "\nExp: " + this.acctMgr.getExp());

			ArrayList<String> games = new ArrayList<>(dbGameManager.getGameList().keySet());
			Collections.sort(games);

			leftBarStats.setText(leftBarStats.getText() + "\n");

			for (String game : games) {
				Integer id = dbGameManager.getGameList().get(game);
				Integer numPlayed = acctMgr.getNumGamesPlayed().get(dbGameManager.getGameList().get(game));
				leftBarStats.setText(leftBarStats.getText() + "\n" + game + ":");
				leftBarStats.setText(leftBarStats.getText() + "\nWins: " + acctMgr.getGameWins().get(id));
				leftBarStats.setText(leftBarStats.getText() + "\nLosses: " + acctMgr.getGameLosses().get(id));
				leftBarStats.setText(leftBarStats.getText() + "\nTies: " + acctMgr.getGameTies().get(id));
				leftBarStats.setText(leftBarStats.getText() + "\nIncomplete: " + acctMgr.getGameIncompletes().get(id) + "\n");
			}
		}
	}
	/**
	 * Updates all the GUI elements that use the username of the current user.
	 */
	private void UpdateLoggedInBarsWithUserNameOfCurrentUser() {
		Label lo = (Label) this.initLoggedInBar.getChildren().get(1);
		Label log = (Label) this.initLoggedInInGameBar.getChildren().get(1);
		lo.setText(this.loggedinusername);
		log.setText(this.loggedinusername);
	}
//////////////////////// Data base interaction functions go here  ////////////////////////////////////////////////////
	/**
	 * Fetches all the games that are implemented
	 */
	private GameIconItem[] getGameList() {
		if (this.DEBUG_FakeDatabase) {
			GameIconItem[] retval = new GameIconItem[2];
			retval[0] = new GameIconItem("Tic-Tac-Toe", "/tictactoeicon.png", 0);
			retval[1] = new GameIconItem("Connect-Four", "/connectFourIcon.png", 1);
			return retval;
		}
		ArrayList<GameIconItem> allgames = this.dbGameManager.fetchAllGameSetUpInfo();
		GameIconItem[] retval = new GameIconItem[allgames.size()];
		for(int x = 0; x < retval.length; x++) {
			retval[x] = allgames.get(x); // Transfer the arraylist content to the array
			retval[x].setGameID(x);      // Set the gameid of the value.
		}
		return retval;
	}
	/**
	 * Creates a new account in the system with the provided info Returns positive
	 * on success, returns negative on failure
	 * 
	 * @param username The username of the account to be created
	 * @param password The password of the account to be created
	 * @return Status code of the result.
	 */
	private int doCreateNewAccount(String username, String password) {
		System.out.println("username = '" + username + "' password = '" + password + "'");
		if (this.DEBUG_FakeDatabase) {
			return 1;
		} else {
			return this.acctMgr.createAccount(username, password);
		}
	}
	/**
	 * Carries out the login. Returns postive if it was successful Returns negative
	 * if it wasn't successful
	 * 
	 * @return The result code of the attempted login
	 */
	private boolean doLogin(String username, String password) {
		System.out.println("username = '" + username + "' password = '" + password + "'");
		if (this.DEBUG_FakeDatabase) {
			return true;
		} else {
			return this.acctMgr.login(username, password);
		}
	}
////////////////////////Other function types go here ////////////////////////////////////////////////////
	/**
	 * Stops and saves the current game so the user can go back the main menu.
	 */
	private void stopAndSaveCurrentGame() {
		GameJamGameInterface game = getLoaddedGame();
		if (game == null) {
			return;
			// Will swap out once intergrated with Wes's code.
			//throw new SanityCheckFailedException("getLoaddedGame returned null when stoping and saving current game!");
		}
		String path = "/userdata/" + acctMgr.getCurUsername();
		game.saveGame();
		game.pauseGame();
	}
	/**
	 * Returns the view of the loaded game in interface form.
	 * @return The view of the loaded game, returns null if no game is loaded.
	 */
	private GameJamGameInterface getLoaddedGame() {
		if (this.gameInUseIndex == 0) {
			return (GameJamGameInterface) this.tictactoegameview;
		} else if (this.gameInUseIndex == 1) {
			return (GameJamGameInterface) this.connectFourGameView;
		}
		return null;
	}
}
