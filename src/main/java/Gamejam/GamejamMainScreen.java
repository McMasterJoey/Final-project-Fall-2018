package Gamejam;

import java.util.Collections;
import java.util.Observer;
import connectFour.ConnectFourControllerView;
import java.util.ArrayList;
import java.util.Observable;
import controller.AccountManager;
import controller.DBGameManager;
import controller.GameControllerView;
import controller.StatsManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.Leaderboard;
import model.SanityCheckFailedException;
import model.Score;
import ticTacToe.TicTacToeControllerView;

/**
 * The MainScreen view. Should be the first thing that pops up when loading the
 * application Should be what is returned to after closing a game.
 * 
 * @author Joey McMaster
 * @author Wes Rodgers
 * @author Nicholas Fiegel
 *
 */
public class GamejamMainScreen extends BorderPane implements Observer {
	private GridPane initGameselectonboxarea;
	private GridPane initThemeMenu;
	private HBox initTopBar;
	private HBox initLoggedInBar;
	private HBox initCreateAccountMenuBar;
	private HBox initLoggedInInGameBar;
	private VBox initUserSettingsMainMenu;
	private VBox initLeftBar;
	private Label leftBarMsg;
	private Label leftBarStats;
	private ProgressBar expBar;
	private Label expBarLabel;
	private VBox initCreateAccountMenu;
	private VBox leaderScreen;
	private TableView<Score> scoresTable;
	private ComboBox<String> leaderBoardSelection;
	private AccountManager acctMgr;
	private DBGameManager dbGameManager;
	private Leaderboard leaderboard;
	private TicTacToeControllerView tictactoegameview;
	private ConnectFourControllerView connectFourGameView;
	private String loggedinusername;
	private boolean userLoggedIn = false;
	private boolean userisAdmin = false;
	private boolean agamewasloaded = false;
	private boolean DEBUG_FakeDatabase = false; // REMOVE WHEN DONE
	private int gameInUseIndex = -1;
	private GameIconItem[] initgamelist;
	private Button[] initbuttonlist;
	private ImageView[] themeimages;
	private int[] themeSettings;
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
		leaderboard = Leaderboard.getInstance();
		
		// Init Collections
		this.initbuttonlist = new Button[70];
		this.themeimages = initThemeImages();
				
		// Set up GUI Elements
		this.initTopBar = initTopBar();
		this.initGameselectonboxarea = initGamePanel();
		this.initLeftBar = initLeftBar();
		leaderScreen = initLeaderScreen();
		
		// Not in user parts that can be used later
		this.initCreateAccountMenu = initCreateAccountScreen();
		this.initLoggedInBar = initLoggedInBar();
		this.initCreateAccountMenuBar = initCreateAccountMenuBar();
		this.initLoggedInInGameBar = initLoggedInInGameBar();
		
		// Set up Game Views
		this.tictactoegameview = new TicTacToeControllerView();
		this.connectFourGameView = new ConnectFourControllerView();
		
		// Set up Extra menus
		this.initUserSettingsMainMenu = initUserSettingsGUI();
		this.initThemeMenu = initThemeMenu();
		
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
		retval.setPadding(new Insets(5));
		this.leftBarMsg = new Label();
		this.leftBarMsg.setWrapText(true);
		this.leftBarStats = new Label();
		expBarLabel = new Label();
		expBar = new ProgressBar(0.0);
		setGuestMessage();
		
		retval.getChildren().addAll(leftBarMsg, expBarLabel, expBar, leftBarStats);
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
		// Add to button collection
		this.initbuttonlist[0] = mainmenu;
		
		// Add to Left Hbox
		leftbox.getChildren().add(mainmenu);
		leftbox.setPrefWidth(758);
		leftbox.setPrefHeight(25);
		leftbox.setAlignment(Pos.TOP_LEFT); // Set it so it aligns on the left
		return leftbox;
	}
	/**
	 * Gets the item that is supposed to be the top most part of the application
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

		// Leaderboard button
		Button viewLeaderboard = new Button("View Leaderboard");
		viewLeaderboard.setOnAction( (ae) -> viewLeaderboardClick());
		this.initbuttonlist[8] = viewLeaderboard;

		// Add to Left Hbox
		leftbox.getChildren().addAll(newacc, viewLeaderboard);
		
		leftbox.setPrefWidth(758);
		leftbox.setPrefHeight(25);
		leftbox.setAlignment(Pos.TOP_LEFT); // Set it so it aligns on the left
		// Add to right hbox first to achieve correct look
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
		// Add buttons to button collection
		this.initbuttonlist[1] = newacc;
		this.initbuttonlist[2] = login;
    
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
		// Leaderboard button
		Button viewLeaderboard = new Button("View Leaderboard");
		viewLeaderboard.setOnAction( (ae) -> viewLeaderboardClick());
		this.initbuttonlist[9] = viewLeaderboard;

		// Add to Left Hbox
		leftbox.getChildren().addAll(logout, viewLeaderboard);
		leftbox.setPrefWidth(1100);
		leftbox.setPrefHeight(25);
		leftbox.setAlignment(Pos.TOP_LEFT); // Set it so it aligns on the left
		// Add to right hbox first to achieve correct look
		retval.getChildren().add(leftbox);

		Label loggedinusername = new Label("Test User");
		loggedinusername.setFont(new Font(14));
		loggedinusername.setPrefWidth(400);
		Button settings = new Button();
		settings.setPrefWidth(25);
		settings.setPrefHeight(25);
		settings.setGraphic(themeimages[0]);
		settings.setOnMouseClicked((click) -> {
			userSettingsButtonClick();
		});
		// Add buttons to collection
		this.initbuttonlist[3] = logout;
		this.initbuttonlist[4] = settings;
				
		retval.getChildren().addAll(loggedinusername, settings);
		retval.setPrefWidth(600);
		retval.setPrefHeight(25);
		return retval;
	}
	/**
	 * Generates the item that will be put in the top bar of the application while a user is in a game and logged in.
	 * @return An HBox with the control structures to act as the top bar of the application.
	 */
	private HBox initLoggedInInGameBar() {
		HBox retval = new HBox(); // General Box
		retval.setAlignment(Pos.TOP_RIGHT); // Set it so it aligns on the right
		HBox leftbox = new HBox();
		// Back to Main Menu Button
		Button logout = new Button("Back To Main Menu");
		logout.setPrefWidth(144);
		logout.setPrefHeight(25);
		logout.setOnMouseClicked((click) -> {
			BackToMainMenuButtonClickLoggedIn();
		});
		this.initbuttonlist[10] = logout;
		// Add to Left Hbox
		leftbox.getChildren().add(logout);
		leftbox.setPrefWidth(1100);
		leftbox.setPrefHeight(25);
		leftbox.setAlignment(Pos.TOP_LEFT); // Set it so it aligns on the left
		// Add to right hbox first to achieve correct look
		retval.getChildren().add(leftbox);

		Label loggedinusername = new Label("Test User");
		loggedinusername.setFont(new Font(14));
		loggedinusername.setPrefWidth(400);
		Button settings = new Button();
		settings.setPrefWidth(25);
		settings.setPrefHeight(25);
		settings.setGraphic(this.themeimages[1]);
		settings.setOnMouseClicked((click) -> {
			userSettingsButtonClickInGame();
		});
		// Add buttons to collection
		this.initbuttonlist[5] = logout; // Back to main menu button
		this.initbuttonlist[6] = settings;
				
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
		// Add button to collection
		this.initbuttonlist[7] = makeaccount;
				
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
		int bid = 55;
		for (int x = 0; x < this.initgamelist.length; x++) {
			if (this.initgamelist[x].getGameID() < 0 || this.initgamelist[x].getGameID() > 11) {
				throw new SanityCheckFailedException("When adding game icons, one of the games had id that was out of range!");
			}
			GameButton gamebutton = new GameButton();
			Image icon = new Image(getClass().getResourceAsStream(this.initgamelist[x].getIconFilePath()));
			gamebutton.setGraphic(new ImageView(icon));
			gamebutton.setMetaDataString(this.initgamelist[x].getName());
			gamebutton.setOnMouseClicked((click) -> {
				GameButton but = (GameButton) click.getSource();
				gameButtonClick(but.getMetaDataString());
			});
			// Add button to collection
			this.initbuttonlist[bid] = gamebutton;
			bid++;
			grid.add(gamebutton, x % 4, x / 4);
		}
		return grid;
	}

	private VBox initLeaderScreen() {
		VBox screen = new VBox();
		scoresTable = new TableView<>();
		leaderBoardSelection = new ComboBox();

		// Fill the combobox to select leaderboard statistics
		leaderBoardSelection.getItems().add("All Scores");

		for (String game : dbGameManager.getGameListByName().keySet()) {
			leaderBoardSelection.getItems().add(game);
		}

		leaderBoardSelection.getSelectionModel().selectFirst();

		leaderBoardSelection.setOnAction( (ae) -> {
			handleLeaderBoardSelectionChange();
		});

		TableColumn<Score, String> username = new TableColumn<>("Username");
		username.setCellValueFactory(new PropertyValueFactory<>("username"));

		TableColumn<Score, String> gameName = new TableColumn<>("Game");
		gameName.setCellValueFactory(new PropertyValueFactory<>("gameName"));

		TableColumn<Score, Integer> score = new TableColumn<>("Score");
		score.setCellValueFactory(new PropertyValueFactory<>("score"));

		scoresTable.getColumns().addAll(username, gameName, score);
		handleLeaderBoardSelectionChange();
		screen.getChildren().addAll(leaderBoardSelection, scoresTable);

		return screen;
	}
	/**
	 * Gets the item that is the theme menu for the main GUI
	 * @return The Gridpane to put into a control structure.
	 */
	private GridPane initThemeMenu() {
		initDefaultTheme();
		GridPane retval = new GridPane();
		retval.getColumnConstraints().add(new ColumnConstraints(544));
		retval.getColumnConstraints().add(new ColumnConstraints(544));
		VBox left = new VBox();  // Used for Pre-made Themes Buttons
		VBox right = new VBox(); // Used for Custom made themes.
		Button backtosettings = new Button("Back to Settings Menu");
		backtosettings.setOnMouseClicked((click) -> {
			this.setCenter(this.initUserSettingsMainMenu);
		});
		this.initbuttonlist[12] = backtosettings;
		left.getChildren().add(backtosettings);
		
		Button theme0 = new Button("Default Theme");
		theme0.setOnMouseClicked((click) -> {
			initDefaultTheme();
			this.initbuttonlist[4].setGraphic(themeimages[0]);
			this.initbuttonlist[6].setGraphic(themeimages[1]);
			updateTheme();
		});
		this.initbuttonlist[13] = theme0;
		left.getChildren().add(theme0);
		Button theme1 = new Button("Night Theme");
		theme1.setOnMouseClicked((click) -> {
			this.themeSettings[0] = RegionColors.BLACK;   // Button Backgrounds
			this.themeSettings[1] = RegionColors.BLACK;  // Left Panel Upper Text color 
			this.themeSettings[2] = RegionColors.BLACK;   // Upper bars background
			this.themeSettings[3] = RegionColors.BLACK;   // Middle Area background / Overall background
			this.themeSettings[4] = RegionColors.BLACK;   // Left Panel background
			this.themeSettings[5] = RegionColors.WHITE;  // New account/Back/Logout button text
			this.themeSettings[6] = RegionColors.WHITE;  // Left Panel Lower text color
			this.themeSettings[7] = RegionColors.RED;    // Login button text color
			this.themeSettings[8] = RegionColors.WHITE;    // Default Settings Text color
			this.initbuttonlist[4].setGraphic(themeimages[2]);
			this.initbuttonlist[6].setGraphic(themeimages[3]);
			updateTheme();
		});
		this.initbuttonlist[14] = theme1;
		left.getChildren().add(theme1);
		Button theme2 = new Button("USA Theme");
		theme2.setOnMouseClicked((click) -> {
			this.themeSettings[0] = RegionColors.WHITE;   // Button Backgrounds
			this.themeSettings[1] = RegionColors.RED;  // Left Panel Upper Text color 
			this.themeSettings[2] = RegionColors.BLUE;   // Upper bars background
			this.themeSettings[3] = RegionColors.BLUE;   // Middle Area background / Overall background
			this.themeSettings[4] = RegionColors.BLUE;   // Left Panel background
			this.themeSettings[5] = RegionColors.RED;  // New account/Back/Logout button text
			this.themeSettings[6] = RegionColors.RED;  // Left Panel Lower text color
			this.themeSettings[7] = RegionColors.RED;    // Login button text color
			this.themeSettings[8] = RegionColors.RED;    // Default Settings Text color
			this.initbuttonlist[4].setGraphic(themeimages[0]);
			this.initbuttonlist[6].setGraphic(themeimages[1]);
			updateTheme();
		});
		this.initbuttonlist[15] = theme2;
		left.getChildren().add(theme2);
		updateTheme();
		retval.add(left, 0, 0);
		retval.add(right,1, 0);
		return retval;
	}
	/**
	 * Creates the theme array and inits it, then updates the theme.
	 */
	private void initDefaultTheme() {
		this.themeSettings = new int[9];
		this.themeSettings[0] = RegionColors.DEFAULT_BUTTON_BACKGROUND;   // Button Backgrounds
		this.themeSettings[1] = RegionColors.BLACK;  // Left Panel Upper Text color 
		this.themeSettings[2] = RegionColors.DEFAULT_BACKGROUND;   // Upper bars background
		this.themeSettings[3] = RegionColors.DEFAULT_BACKGROUND;   // Middle Area background / Overall background
		this.themeSettings[4] = RegionColors.DEFAULT_BACKGROUND;   // Left Panel background
		this.themeSettings[5] = RegionColors.BLACK;  // New account/Back/Logout button text
		this.themeSettings[6] = RegionColors.BLACK;  // Left Panel Lower text color
		this.themeSettings[7] = RegionColors.RED;    // Login button text color
		this.themeSettings[8] = RegionColors.BLACK;    // Defalt Settings Text color
	}
	/**
	 * Creates the Usersettings menu that pops up when the user clicks the gear icon.
	 * @return An VBox that holds all the control elements that make up the user settings menu.
	 */
	private VBox initUserSettingsGUI() {
		VBox pane = new VBox();
		Label info = new Label(this.loggedinusername + ": Account Settings");
		info.setFont(new Font(66));
		GridPane grid = new GridPane();
		grid.getColumnConstraints().add(new ColumnConstraints(272));
		grid.getColumnConstraints().add(new ColumnConstraints(272));
		grid.getColumnConstraints().add(new ColumnConstraints(272));
		grid.getColumnConstraints().add(new ColumnConstraints(272));
		Button thememenu = new Button();
		Image icon1 = new Image(getClass().getResourceAsStream("/usersettingsthememenubuttonbackground.png"));
		thememenu.setGraphic(new ImageView(icon1));
		thememenu.setOnMouseClicked((click) -> {
			swapToThemeMenuButtonClick();
		});
		this.initbuttonlist[11] = thememenu;
		grid.add(thememenu,0,0);
		pane.getChildren().addAll(info,grid);
		return pane;
	}
	/**
	 * Loads all images that can be used more than once in the GUI such as for themes. 
	 * @return An array of all image objects that can be used more than once in the GUI
	 */
	private ImageView[] initThemeImages() {
		ImageView[] retval = new ImageView[4];
		// Without duplicating these, the settings button graphic wouldn't display always.
		retval[0] = new ImageView(new Image(getClass().getResourceAsStream("/usersettingsbuttonbackground.png")));
		retval[1] = new ImageView(new Image(getClass().getResourceAsStream("/usersettingsbuttonbackground.png")));
		retval[2] = new ImageView(new Image(getClass().getResourceAsStream("/usersettingsbuttonbackgroundnighttheme.png")));
		retval[3] = new ImageView(new Image(getClass().getResourceAsStream("/usersettingsbuttonbackgroundnighttheme.png")));
		return retval;
	}
//////////////////////// Button Click Handlers go here  /////////////////////////////////////////////
	/**
	 * Handles the event where the user who is logged in is at the user settings menu and clicks the Theme Menu Button.
	 */
	private void swapToThemeMenuButtonClick() {
		this.setCenter(this.initThemeMenu);
	}
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
		boolean fromgame = justInGame();
		if (fromgame) {
			stopAndSaveCurrentGame();
		}
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
		Gamejam.DPrint("Logout!");
		this.setTop(this.initTopBar);
		this.setCenter(this.initGameselectonboxarea);
		//updateLeftPane();
	}

	/**
	 * Handles the event where the User Settings button is clicked Assumes: A user is
	 * already logged in.
	 */
	private void userSettingsButtonClick() {
		this.gameInUseIndex = -1;
		this.agamewasloaded = false;
		Label l = (Label) this.initUserSettingsMainMenu.getChildren().get(0);
		l.setText(this.loggedinusername + ": Account Settings");
		this.setTop(this.initLoggedInInGameBar);
		this.setCenter(this.initUserSettingsMainMenu);
	}
	/**
	 * Handles the event where the user is loggedin and ingame and clicks the User Settings button.
	 */
	private void userSettingsButtonClickInGame() {
		// Do Nothing!
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
			username.setText("");
			password.setText("");
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
			Gamejam.DPrint("Username already in use");
			info.setText("User name: '" + username.getText() + "' already in use. Try a diffrent one.");
			button.setTextFill(Color.RED);
		} else if (status == 3) {
			Gamejam.DPrint("Other error encounted on account creation");
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
			this.agamewasloaded = true;
			if (userLoggedIn) {
				loadAndResumeGame();
				this.setTop(this.initLoggedInInGameBar);
			} else {
				this.setTop(this.initCreateAccountMenuBar);
			}
			this.setCenter(this.tictactoegameview);
		}
		if (name.equals("Connect-Four")) {
			this.gameInUseIndex = 1;
			this.agamewasloaded = true;
			if(userLoggedIn) {
				loadAndResumeGame();
				this.setTop(this.initLoggedInInGameBar);
			} else {
				this.setTop(this.initCreateAccountMenuBar);
			}
			this.setCenter(this.connectFourGameView);
		}
	}
	/**
	 * 	Handles when the leaderboard button is clicked by the user.
	 */
	private void viewLeaderboardClick() {
		this.setCenter(leaderScreen);
		this.gameInUseIndex = -1;
		this.agamewasloaded = false;
		if (this.userLoggedIn) {
			this.setTop(initLoggedInInGameBar);
		} else {
			this.setTop(initCreateAccountMenuBar);
		}
	}

/////////////////////////////// GUI Update Functions go here ///////////////////////////////////////////
	/**
	 * Updates the GUI after a new account is made succesfuly,
	 */
	private void doGUIUpdateOnCreateAccountSuccess() {
		Gamejam.DPrint("Account creation successful");
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
		this.leftBarMsg.setText("\nYou are not logged in.\nIf you were logged in, you could see your stats!\n\n");
		expBar.setProgress(0.0);
		expBarLabel.setText("Exp: 0/0");
		this.leftBarStats.setText("Level: 0\nExp: 0");
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

		if (acctMgr.isGuest()) {
			setGuestMessage();
		} else if (acctMgr.isAdmin()) {
			leftBarMsg.setText("Administrator Account");
			leftBarStats.setText("");
		} else {
			leftBarMsg.setText("Welcome to Gamejam,\n" + acctMgr.getCurUsername() + "!\n\n");
			expBarLabel.setText("Account Statistics:\nExp: " + acctMgr.getExpInLevel() + "/" + acctMgr.getExpForLevel(acctMgr.getLevel() + 1));
			expBar.setProgress( (double) acctMgr.getExpInLevel() / acctMgr.getExpForLevel(acctMgr.getLevel() + 1) );
			leftBarStats.setText("Level: " + acctMgr.getLevel() + "\nTotal Exp: " + acctMgr.getTotalExp() + "\n\nGame Statistics:");

			ArrayList<String> games = new ArrayList<>(dbGameManager.getGameListByName().keySet());
			Collections.sort(games);

			for (String game : games) {
				Integer id = dbGameManager.getGameListByName().get(game);
				Integer numPlayed = acctMgr.getNumGamesPlayed().get(dbGameManager.getGameListByName().get(game));
				leftBarStats.setText(leftBarStats.getText() + "\n" + game + ":");
				leftBarStats.setText(leftBarStats.getText() + "\n High Score: " + acctMgr.getHighScore(game) + "\n");
				leftBarStats.setText(leftBarStats.getText() + "\n  Wins: " + acctMgr.getGameWins().get(id));
				leftBarStats.setText(leftBarStats.getText() + "\n  Losses: " + acctMgr.getGameLosses().get(id));
				leftBarStats.setText(leftBarStats.getText() + "\n  Ties: " + acctMgr.getGameTies().get(id));
				leftBarStats.setText(leftBarStats.getText() + "\n  Incomplete: " + acctMgr.getGameIncompletes().get(id));
				leftBarStats.setText((leftBarStats.getText() + "\n   Total: " + numPlayed));
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
		Gamejam.DPrint("username = '" + username + "' password = '" + password + "'");
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
		Gamejam.DPrint("username = '" + username + "' password = '" + password + "'");
		if (this.DEBUG_FakeDatabase) {
			return true;
		} else {
			return this.acctMgr.login(username, password);
		}
	}

	private void handleLeaderBoardSelectionChange() {
		String choice = leaderBoardSelection.getValue();

		scoresTable.getItems().clear();

		if (choice.equals("All Scores")) {
			for (Score cur : leaderboard.getAllScores()) {
				scoresTable.getItems().add(cur);
			}
		} else {
			for (Score cur : leaderboard.getTopTen(choice)) {
				scoresTable.getItems().add(cur);
			}
		}
	}

////////////////////////Other function types go here ////////////////////////////////////////////////////
	/**
	 * Updates the theme of mainGUI
	 */
	private void updateTheme() {
		
		for(int x = 0; x < this.initbuttonlist.length; x++) {
			if (this.initbuttonlist[x] == null) {
				continue;
			}
			this.initbuttonlist[x].setBackground(RegionColors.getBackgroundColor(this.themeSettings[0]));
		}
		
		this.leftBarMsg.setTextFill(RegionColors.getColor(this.themeSettings[1]));
		
		this.initTopBar.setBackground(RegionColors.getBackgroundColor(this.themeSettings[2]));
		this.initLoggedInInGameBar.setBackground(RegionColors.getBackgroundColor(this.themeSettings[2]));
		this.initLoggedInBar.setBackground(RegionColors.getBackgroundColor(this.themeSettings[2]));
		this.initCreateAccountMenuBar.setBackground(RegionColors.getBackgroundColor(this.themeSettings[2]));
		
		this.setBackground(RegionColors.getBackgroundColor(this.themeSettings[3]));
		
		this.initLeftBar.setBackground(RegionColors.getBackgroundColor(this.themeSettings[4]));
		
		this.initbuttonlist[0].setTextFill(RegionColors.getColor(this.themeSettings[5]));
		this.initbuttonlist[1].setTextFill(RegionColors.getColor(this.themeSettings[5]));
		this.initbuttonlist[3].setTextFill(RegionColors.getColor(this.themeSettings[5]));
		this.initbuttonlist[7].setTextFill(RegionColors.getColor(this.themeSettings[5]));
		this.initbuttonlist[8].setTextFill(RegionColors.getColor(this.themeSettings[5]));
		this.initbuttonlist[9].setTextFill(RegionColors.getColor(this.themeSettings[5]));
		this.initbuttonlist[10].setTextFill(RegionColors.getColor(this.themeSettings[5]));
		
		Label l0 = (Label) this.initLoggedInBar.getChildren().get(1);
		l0.setTextFill(RegionColors.getColor(this.themeSettings[5]));
		Label l1 = (Label)  initLoggedInInGameBar.getChildren().get(1);
		l1.setTextFill(RegionColors.getColor(this.themeSettings[5]));
		
		this.leftBarStats.setTextFill(RegionColors.getColor(this.themeSettings[6]));
		
		this.initbuttonlist[2].setTextFill(RegionColors.getColor(this.themeSettings[7]));
		
		Label l = (Label) this.initUserSettingsMainMenu.getChildren().get(0);
		l.setTextFill(RegionColors.getColor(this.themeSettings[8]));
		for(int x = 11; x < 16; x++) {
			this.initbuttonlist[x].setTextFill(RegionColors.getColor(this.themeSettings[8]));
		}
		
	}
	/**
	 * Stops and saves the current game so the user can go back the main menu.
	 */
	public void stopAndSaveCurrentGame() {
		// Fixes failing to exit the app when no games were loaded.
		if (!this.agamewasloaded) {
			return;
		}
		GameControllerView game = getLoaddedGame();
		if (game == null) {
			throw new SanityCheckFailedException("stopAndSaveCurrentGame: couldn't get the loaded game!");
		}
		boolean paused = game.pauseGame();
		if (this.acctMgr.isGuest()) {
			// The user is not logged in, do nothing.
			if (!paused) {
				throw new SanityCheckFailedException("stopAndSaveCurrentGame: pauseGame failed to pause!");
			}
			return;
		}
		boolean saved = game.saveGame();
		if (!saved || !paused) {
			throw new SanityCheckFailedException("stopAndSaveCurrentGame: saveGame or pauseGame failed to save and pause!");
		}
	}
	/**
	 * Loads the saved data of the current game into the gamestate.
	 * If no data exists or it fails, does nothing.
	 */
	private void loadAndResumeGame() {
		GameControllerView game = getLoaddedGame();
		if (game == null) {
			throw new SanityCheckFailedException("loadAndResumeGame: couldn't get the loaded game!");
		}
		boolean unpaused = game.unPauseGame();
		if (this.acctMgr.isGuest()) {
			// The user is not logged in, do nothing.
			if (!unpaused) {
				throw new SanityCheckFailedException("loadAndResumeGame: unPauseGame failed to unpause!");
			}
			return;
		}
		boolean loaded = game.loadSaveGame();
		if (!loaded || !unpaused) {
			throw new SanityCheckFailedException("loadAndResumeGame: loadSaveGame or unPauseGame failed to load and unpause!");
		}
	}
	/**
	 * Returns the view of the loaded game in interface form.
	 * @return The view of the loaded game, returns null if no game is loaded.
	 */
	private GameControllerView getLoaddedGame() {
		if (this.gameInUseIndex == 0) {
			return (GameControllerView) this.tictactoegameview;
		} else if (this.gameInUseIndex == 1) {
			return (GameControllerView) this.connectFourGameView;
		}
		return null;
	}
	/**
	 * Determines if a game was just played or is being played.
	 * @return True if a game is being played or a game was the last major menu used. Otherwise false.
	 */
	private boolean justInGame() {
		return this.gameInUseIndex >= 0;
	}
	/**
	 * Fetches the array of Theme Images stored within this object. Enables other objects that want to reuse some files to not have to generate duplicates.
	 * @return The array of image objects that are used more than once in this view object.
	 */
	public ImageView[] getThemeImages() {
		return this.themeimages;
	}
}
