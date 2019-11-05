package Gamejam;

import java.util.Collections;
import java.util.Observer;
import battleship.BattleshipControllerView;
import connectFour.ConnectFourControllerView;
import java.util.ArrayList;
import java.util.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import connectFour.ConnectFourControllerView;
import controller.AccountManager;
import controller.DBGameManager;
import controller.GameControllerView;
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
	private ScrollPane initLeftBar;
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
	private BattleshipControllerView battleshipGameView;
	private int gameInUseIndex = -1;
	private GameIconItem[] initgamelist;
	private GamejamMainScreenTheme initthemes;
	private int initthemeinuseid = 0;
	public GamejamMainScreen() {
		super();
		init();
	}
////////////////////////View Init Functions go here /////////////////////////////////////////////
	/**
	 * Inits the Object
	 */
	private void init() {
		// Init themes
		this.initthemes = new GamejamMainScreenTheme();
		this.initthemes.addRegion(200, this, "Default Center Area", new ThemeRegionProp(ThemeRegionProp.GRIDPANE));
		
		// Get the references to the database connector classes
		// KEEP THESE AT TOP OR YOU WILL HAVE FUN WITH NULL POINTER EXECPTIONS!
		this.acctMgr = AccountManager.getInstance();
		this.acctMgr.addObserver(this);
		this.dbGameManager = DBGameManager.getInstance();
		this.leaderboard = Leaderboard.getInstance();
				
		// Set up GUI Elements
		this.initTopBar = initTopBar();
		this.initGameselectonboxarea = initGamePanel();
		this.initLeftBar = initLeftPane();
		this.leaderScreen = initLeaderScreen();
		
		// Not in user parts that can be used later
		this.initCreateAccountMenu = initCreateAccountScreen();
		this.initLoggedInBar = initLoggedInBar();
		this.initCreateAccountMenuBar = initCreateAccountMenuBar();
		this.initLoggedInInGameBar = initLoggedInInGameBar();
		
		// Set up Game Views
		this.tictactoegameview = new TicTacToeControllerView();
		this.connectFourGameView = new ConnectFourControllerView();
		this.initthemes.addRegion(350, this.tictactoegameview,"Tic-tac-toe general background", new ThemeRegionProp(ThemeRegionProp.BORDERPANE));
		this.initthemes.addRegion(351, this.connectFourGameView,"Connect-4 general background", new ThemeRegionProp(ThemeRegionProp.BORDERPANE));
		this.battleshipGameView = new BattleshipControllerView();
		
		// Set up Extra menus
		this.initUserSettingsMainMenu = initUserSettingsGUI();
		this.initThemeMenu = initThemeMenu();
		
		// Set currently inuse views
		this.setTop(this.initTopBar);
		this.setCenter(this.initGameselectonboxarea);
		this.setLeft(this.initLeftBar);
		
		this.initthemes.doneAddingRegions();
	}
	/**
	 * Generates the control structure that will exist the left bar.
	 * 
	 * @return A VBox that contains all the structures for the left bar.
	 */
	private ScrollPane initLeftPane() {
		VBox leftPane = new VBox();
		ScrollPane leftPaneScroll = new ScrollPane(leftPane);
		leftPane.setPrefWidth(145);
		leftPane.setPrefHeight(578);
		leftPane.setPadding(new Insets(5));
		this.leftBarMsg = new Label();
		this.leftBarMsg.setWrapText(true);
		this.leftBarStats = new Label();
		expBarLabel = new Label();
		expBar = new ProgressBar(0.0);
		leftPane.setPrefHeight(750);
		leftPane.setPrefWidth(180);
		leftPaneScroll.setFitToWidth(true);
		setGuestMessage();
		// Add to region list
		this.initthemes.addRegion(100, leftPane, "Left VBox", new ThemeRegionProp(ThemeRegionProp.VBOX));
		this.initthemes.addRegion(101, this.leftBarMsg, "Left Top Message label", new ThemeRegionProp(ThemeRegionProp.LABEL));
		this.initthemes.addRegion(102, this.leftBarStats, "Left Stats label", new ThemeRegionProp(ThemeRegionProp.LABEL));
		
		leftPane.getChildren().addAll(this.leftBarMsg, expBarLabel, expBar, this.leftBarStats);
		return leftPaneScroll;
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
		this.initthemes.addRegion(10, mainmenu, "Main Menu Button While Creating An Account", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT));
		this.initthemes.addRegion(11, leftbox, "Create Account HBox", new ThemeRegionProp(ThemeRegionProp.HBOX));
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
    
		retval.getChildren().addAll(username, password, login);
		retval.setPrefWidth(600);
		retval.setPrefHeight(25);
		
		this.initthemes.addRegion(12, retval, "Top bar background while not logged in", new ThemeRegionProp(ThemeRegionProp.HBOX));
		this.initthemes.addRegion(13, leftbox, "Top bar left background while not logged in", new ThemeRegionProp(ThemeRegionProp.HBOX));
		this.initthemes.addRegion(14, newacc, "Top bar Create Account Button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT));
		this.initthemes.addRegion(15, viewLeaderboard, "Top bar Create Account Button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT));
		this.initthemes.addRegion(16, username, "Top bar user name input area", new ThemeRegionProp(ThemeRegionProp.TEXTINPUT));
		this.initthemes.addRegion(17, password, "Top bar pass word input area", new ThemeRegionProp(ThemeRegionProp.TEXTINPUT));
		this.initthemes.addRegion(18, login, "Top bar login Button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT));
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
		settings.setGraphic(this.initthemes.getThemeImages(0,0));
		settings.setOnMouseClicked((click) -> {
			userSettingsButtonClick();
		});
				
		retval.getChildren().addAll(loggedinusername, settings);
		retval.setPrefWidth(600);
		retval.setPrefHeight(25);
		
		this.initthemes.addRegion(19, retval, "Top bar logged in background", new ThemeRegionProp(ThemeRegionProp.HBOX));
		this.initthemes.addRegion(20, leftbox, "Top bar logged in left side background", new ThemeRegionProp(ThemeRegionProp.HBOX));
		this.initthemes.addRegion(21, logout, "Top bar logged in logout button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT));
		this.initthemes.addRegion(22, viewLeaderboard, "Top bar logged in leaderboard button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT));
		this.initthemes.addRegion(23, loggedinusername, "Top bar logged in username label", new ThemeRegionProp(ThemeRegionProp.LABEL));
		ThemeRegionProp pp = new ThemeRegionProp(ThemeRegionProp.BUTTON);
		pp.setIsContainsThemeableMainGUIImages(true);
		this.initthemes.addRegion(24, settings, "Top bar logged in user settings button", pp);
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
		settings.setGraphic(this.initthemes.getThemeImages(0,1));
		settings.setOnMouseClicked((click) -> {
			userSettingsButtonClickInGame();
		});
				
		retval.getChildren().addAll(loggedinusername, settings);
		retval.setPrefWidth(600);
		retval.setPrefHeight(25);
		
		this.initthemes.addRegion(25, retval, "Top bar logged in and in game background", new ThemeRegionProp(ThemeRegionProp.HBOX));
		this.initthemes.addRegion(26, leftbox, "Top bar logged in and in game left side background", new ThemeRegionProp(ThemeRegionProp.HBOX));
		this.initthemes.addRegion(27, logout, "Top bar logged in and in game back to main menu button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT));
		this.initthemes.addRegion(28, loggedinusername, "Top bar logged in and in game username label", new ThemeRegionProp(ThemeRegionProp.LABEL));
		ThemeRegionProp pp = new ThemeRegionProp(ThemeRegionProp.BUTTON);
		pp.setIsContainsThemeableMainGUIImages(true);
		this.initthemes.addRegion(29, settings, "Top bar logged in and in game settings button", pp);
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
		
		this.initthemes.addRegion(201, retval, "Create Account Screen background", new ThemeRegionProp(ThemeRegionProp.VBOX));
		this.initthemes.addRegion(202, info, "Create Account info label", new ThemeRegionProp(ThemeRegionProp.LABEL));
		this.initthemes.addRegion(203, username, "Create Account Screen username input", new ThemeRegionProp(ThemeRegionProp.TEXTINPUT));
		this.initthemes.addRegion(204, password, "Create Account Screen password input", new ThemeRegionProp(ThemeRegionProp.TEXTINPUT));
		this.initthemes.addRegion(205, makeaccount, "Create Account Screen Create Account Button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT));
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
		int bid = 0;
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
			this.initthemes.addRegion(300 + bid, gamebutton, "Game: " + this.initgamelist[x].getName() + " button", new ThemeRegionProp(ThemeRegionProp.BUTTON));
			bid++;
			grid.add(gamebutton, x % 4, x / 4);
		}
		return grid;
	}

	private VBox initLeaderScreen() {
		VBox screen = new VBox();
		this.scoresTable = new TableView<>();
		this.leaderBoardSelection = new ComboBox();

		// Fill the combobox to select leaderboard statistics
		this.leaderBoardSelection.getItems().add("All Scores");

		for (String game : this.dbGameManager.getGameListByName().keySet()) {
			this.leaderBoardSelection.getItems().add(game);
		}

		this.leaderBoardSelection.getSelectionModel().selectFirst();

		this.leaderBoardSelection.setOnAction( (ae) -> {
			handleLeaderBoardSelectionChange();
		});

		TableColumn<Score, String> username = new TableColumn<>("Username");
		username.setCellValueFactory(new PropertyValueFactory<>("username"));

		TableColumn<Score, String> gameName = new TableColumn<>("Game");
		gameName.setCellValueFactory(new PropertyValueFactory<>("gameName"));

		TableColumn<Score, Integer> score = new TableColumn<>("Score");
		score.setCellValueFactory(new PropertyValueFactory<>("score"));

		this.scoresTable.getColumns().addAll(username, gameName, score);
		handleLeaderBoardSelectionChange();
		screen.getChildren().addAll(leaderBoardSelection, scoresTable);
		this.initthemes.addRegion(206, screen, "Leaderboard overall background", new ThemeRegionProp(ThemeRegionProp.VBOX));
		this.initthemes.addRegion(207, this.scoresTable, "Leaderboard scores table", new ThemeRegionProp(ThemeRegionProp.TABLEVIEW));
		this.initthemes.addRegion(208, this.leaderBoardSelection, "Leaderboard selection combo box", new ThemeRegionProp(ThemeRegionProp.COMBOBOX));
		return screen;
	}
	/**
	 * Gets the item that is the theme menu for the main GUI
	 * @return The Gridpane to put into a control structure.
	 */
	private GridPane initThemeMenu() {
		GridPane retval = new GridPane();
		retval.getColumnConstraints().add(new ColumnConstraints(220));
		retval.getColumnConstraints().add(new ColumnConstraints(800));
		VBox left = new VBox();  // Used for Pre-made Themes Buttons
		VBox right = new VBox(); // Used for Custom made themes.
		Button backtosettings = new Button();
		backtosettings.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/themeBackButtonMenuIcon.png"))));
		backtosettings.setOnMouseClicked((click) -> {
			this.setCenter(this.initUserSettingsMainMenu);
		});
		left.getChildren().add(backtosettings);
		
		Button theme0 = new Button();
		theme0.setGraphic(this.initthemes.getThemeIcon(0));
		theme0.setOnMouseClicked((click) -> {
			this.initthemes.updateTheme(0);
		});
		left.getChildren().add(theme0);
		/*
		Button theme1 = new Button();
		theme1.setGraphic(this.initthemes.getThemeIcon(0));
		theme1.setOnMouseClicked((click) -> {
			this.initthemes.updateTheme(1);
		});
		left.getChildren().add(theme1);
		Button theme2 = new Button();
		theme2.setGraphic(this.initthemes.getThemeIcon(0));
		theme2.setOnMouseClicked((click) -> {
			this.initthemes.updateTheme(2);
		});
		left.getChildren().add(theme2);

		Button theme3 = new Button();
		theme3.setGraphic(this.initthemes.getThemeIcon(0));
		theme3.setOnMouseClicked((click) -> {
			this.initthemes.updateTheme(3);
		});
		left.getChildren().add(theme3);
		*/
		retval.add(left, 0, 0);
		// Custom Theme Setter
		right.getChildren().add(this.initthemes.getThemeCreator());
		retval.add(right,1, 0);
		this.initthemes.addRegion(220, retval, "Theme Menu overall background", new ThemeRegionProp(ThemeRegionProp.GRIDPANE));
		this.initthemes.addRegion(221, left, "Theme Menu left side overall background", new ThemeRegionProp(ThemeRegionProp.VBOX));
		this.initthemes.addRegion(222, right, "Theme Menu right side overall background", new ThemeRegionProp(ThemeRegionProp.VBOX));
		this.initthemes.addRegion(223, backtosettings, "Theme Menu back to user settings menu button", new ThemeRegionProp(ThemeRegionProp.BUTTON));
		this.initthemes.addRegion(224, theme0, "Theme Menu Default Theme button", new ThemeRegionProp(ThemeRegionProp.BUTTON));
		//this.initthemes.addRegion(225, theme1, "Theme Menu Night Theme button", new ThemeRegionProp(ThemeRegionProp.BUTTON));
		//this.initthemes.addRegion(226, theme2, "Theme Menu USA Theme button", new ThemeRegionProp(ThemeRegionProp.BUTTON));
		//this.initthemes.addRegion(227, theme3, "Theme Menu Experimental Theme button", new ThemeRegionProp(ThemeRegionProp.BUTTON));
		return retval;
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
		grid.add(thememenu,0,0);
		pane.getChildren().addAll(info,grid);
		this.initthemes.addRegion(209, pane, "User Settings main screen background", new ThemeRegionProp(ThemeRegionProp.VBOX));
		this.initthemes.addRegion(210, info, "User Settings main label", new ThemeRegionProp(ThemeRegionProp.LABEL));
		this.initthemes.addRegion(211, grid, "User Settings grided background", new ThemeRegionProp(ThemeRegionProp.GRIDPANE));
		this.initthemes.addRegion(212, thememenu, "User Settings Theme Menu Button", new ThemeRegionProp(ThemeRegionProp.BUTTON));
		return pane;
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
		if (name.equals("Battleship")) {
			if(userLoggedIn) {
				this.setTop(this.initLoggedInInGameBar);
			} else {
				this.setTop(this.initCreateAccountMenuBar);
			}
			this.setCenter(this.battleshipGameView);
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
		this.leftBarMsg.setText("\nYou are not logged in.\nIf you were logged in,\nyou could see your stats!\n\n");
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
				leftBarStats.setText(leftBarStats.getText() + "\n High Score: " + acctMgr.getHighScore(game));
				leftBarStats.setText(leftBarStats.getText() + "\n  Wins: " + acctMgr.getGameWins().get(id));
				leftBarStats.setText(leftBarStats.getText() + "\n  Losses: " + acctMgr.getGameLosses().get(id));
				leftBarStats.setText(leftBarStats.getText() + "\n  Ties: " + acctMgr.getGameTies().get(id));
				leftBarStats.setText(leftBarStats.getText() + "\n  Incomplete: " + acctMgr.getGameIncompletes().get(id));
				leftBarStats.setText((leftBarStats.getText() + "\n   Total: " + numPlayed) + "\n");
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
			retval[2] = new GameIconItem("Battleship", "/battleshipIcon.png", 2);
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
}
