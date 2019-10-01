package Gamejam;

import controller.AccountManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import view.TicTacToeControllerView;

/**
 * The MainScreen view. Should be the first thing that pops up when loading the
 * application Should be what is returned to after closing a game.
 * 
 * @author Joey McMaster
 *
 */
public class GamejamMainScreen extends BorderPane {
	private GridPane initGameselectonboxarea;
	private HBox initTopBar;
	private HBox initLoggedInBar;
	private HBox initCreateAccountMenuBar;
	private HBox initLoggedInInGameBar;
	private VBox initLeftBar;
	private VBox initCreateAccountMenu;
	private AccountManager acctMgr;
	private String loggedinusername;
	private boolean userLoggedIn = false;
	
	private boolean DEBUG_FakeDatabase = true; // REMOVE WHEN DONE
	public GamejamMainScreen() {
		super();
		init();
	}

	/**
	 * Inits the Object
	 */
	private void init() {
		this.initTopBar = initTopBar();
		this.setTop(this.initTopBar);
		this.initGameselectonboxarea = initGamePanel();
		this.setCenter(this.initGameselectonboxarea);
		this.initLeftBar = initLeftBar();
		this.setLeft(this.initLeftBar);
		// Not in user parts that can be used later
		this.initCreateAccountMenu = initCreateAccountScreen();
		this.initLoggedInBar = initLoggedInBar();
		this.initCreateAccountMenuBar = initCreateAccountMenuBar();
		this.initLoggedInInGameBar = initLoggedInInGameBar();
		// Get the reference to the AccountManager
		this.acctMgr = AccountManager.getInstance();
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

		Label loginmsg = new Label(
				"You are not logged in. \nLog in to see your \ngame stats and access \nyour game saves!");
		Label stats = new Label("\n\n\n\n\n\nPlace holder stats msg");
		retval.getChildren().addAll(loginmsg, stats);
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
	 * Handles when a user wants to exit the create account screen without creating an account.
	 */
	private void doBackToMainMenuButtonClickInCreateAccountMenuBar() {
		this.setTop(this.initTopBar);
		this.setCenter(this.initGameselectonboxarea);
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

		TextField username = new TextField("Username");
		username.setPrefWidth(280);
		username.setPrefHeight(25);
		TextField password = new TextField("Password");
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
	 * Updates all the GUI elements that use the username of the current user.
	 */
	private void UpdateLoggedInBarsWithUserNameOfCurrentUser() {
		Label lo = (Label) this.initLoggedInBar.getChildren().get(1);
		Label log = (Label) this.initLoggedInInGameBar.getChildren().get(1);
		lo.setText(this.loggedinusername);
		log.setText(this.loggedinusername);
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
	 * Handles the event were the user is logged in and is in a game and wants to go back to the main menu.
	 */
	private void BackToMainMenuButtonClickLoggedIn() {
		stopAndSaveCurrentGame();
		this.setTop(this.initLoggedInBar);
		this.setCenter(this.initGameselectonboxarea);
		
	}
	/**
	 * Handles the event were the Log out button is clicked Assumes: A user is
	 * already logged in.
	 */
	private void logoutButtonClick() {
		// TODO: Implement Logout
		userLoggedIn = false;
		System.out.println("Logout!");
		this.setTop(this.initTopBar);
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
		TextField password = (TextField) this.initTopBar.getChildren().get(2);
		boolean successful = doLogin(username.getText(), password.getText());
		if (successful) {
			// TODO: Update the Left with statistics
			System.out.println("Login successful\n");
			userLoggedIn = true;
			this.loggedinusername = username.getText();
			UpdateLoggedInBarsWithUserNameOfCurrentUser();
			this.setTop(this.initLoggedInBar);
		} else {
			// TODO: Handle unsuccessful login
			System.out.println("Invalid username or password\n");
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
	 * Creates the Create Account Center Pane Screen
	 * 
	 * @return The VBox containing the elements of the screen.
	 */
	private VBox initCreateAccountScreen() {
		VBox retval = new VBox();
		Label info = new Label("Type in your Username and Password, then click the Create Account Button.");
		TextField username = new TextField("Username");
		username.setPrefWidth(300);
		username.setPrefHeight(25);
		TextField password = new TextField("Password");
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
	 * Handles the act of creating an account.
	 */
	private void createNewAccountButtonOnFinishedClick() {
		// Ever time I change initCreateAccountScreen, I need to update this function
		Label info = (Label) initCreateAccountMenu.getChildren().get(0);
		TextField username = (TextField) this.initCreateAccountMenu.getChildren().get(1);
		TextField password = (TextField) this.initCreateAccountMenu.getChildren().get(2);
		Button button = (Button) this.initCreateAccountMenu.getChildren().get(3);
		int status = doCreateNewAccount(username.getText(), password.getText());
		// TODO: Change the UI to reflect the status returned.

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
	 * Updates the GUI after a new account is made succesfuly,
	 */
	private void doGUIUpdateOnCreateAccountSuccess() {
		System.out.println("Account creation successful");
		UpdateLoggedInBarsWithUserNameOfCurrentUser();
		this.setTop(this.initLoggedInBar);
		this.setCenter(this.initGameselectonboxarea);
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
	 * Gets the item that surposed to be in the center of the application. In this
	 * case its the game selector menu.
	 * 
	 * @return A grid pane of all the buttons to lead to each game.
	 */
	private GridPane initGamePanel() {
		GridPane grid = new GridPane();
		grid.getColumnConstraints().add(new ColumnConstraints(260));
		grid.getColumnConstraints().add(new ColumnConstraints(260));
		grid.getColumnConstraints().add(new ColumnConstraints(260));
		grid.getColumnConstraints().add(new ColumnConstraints(260));
		GameIconItem[] gamelist = getGameList();
		for (int x = 0; x < gamelist.length; x++) {
			Button gamebutton = new Button();
			Image icon = new Image(getClass().getResourceAsStream(gamelist[x].getIconFilePath()));
			gamebutton.setGraphic(new ImageView(icon));
			gamebutton.setText(gamelist[x].getName());
			gamebutton.setOnMouseClicked((click) -> {
				Button but = (Button) click.getSource();
				gameButtonClick(but.getText());
			});
			grid.add(gamebutton, x % 4, x / 4);
		}
		return grid;
	}
	/**
	 * Stops and saves the current game so the user can go back the main menu.
	 */
	private void stopAndSaveCurrentGame() {
		// TODO: Implement Me!
	}
	/**
	 * Mapping function, maps game button clicks to their respected game and inits
	 * it.
	 * 
	 * @param name The name field of the button clicked, used to ID it.
	 */
	private void gameButtonClick(String name) {
		if (name.equals("Tic-tac-toe")) {
			if (userLoggedIn) {
				this.setTop(this.initLoggedInInGameBar);
			} else {
				this.setTop(initCreateAccountMenuBar);
			}
			init_tictactoe();
		}
	}

	/**
	 * Inits tictactoe and sets its accordingly
	 */
	private void init_tictactoe() {
		this.setCenter(new TicTacToeControllerView());
	}

	/**
	 * Fetches all the games that are implemented
	 */
	private GameIconItem[] getGameList() {
		GameIconItem[] retval = new GameIconItem[1];
		retval[0] = new GameIconItem("Tic-tac-toe", "/tictactoeicon.png", 0);
		return retval;
	}
	// REMOVE WHEN DONE
	// Used for debugging
	private void DEBUG_PretendImLoggedIn() {
		userLoggedIn = true;
		loggedinusername = "Nothingbutbread is Awesome!";
	}
}
