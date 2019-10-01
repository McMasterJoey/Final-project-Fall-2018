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
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import view.TicTacToeControllerView;
/**
 * The MainScreen view. 
 * Should be the first thing that pops up when loading the application
 * Should be what is returned to after closing a game.
 * @author Joey McMaster
 *
 */
public class GamejamMainScreen extends BorderPane {
	private GridPane initGameselectonboxarea;
	private HBox initTopBar;
	private VBox initLeftBar;
	private VBox initCreateAccountMenu;
	private AccountManager acctMgr;
	
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
		acctMgr = AccountManager.getInstance();
	}
	
	private VBox initLeftBar() {
		VBox retval = new VBox();
		retval.setPrefWidth(145);
		retval.setPrefHeight(578);
		
		Label loginmsg = new Label("You are not logged in. \nLog in to see your \ngame stats and access \nyour game saves!");
		Label stats = new Label("\n\n\n\n\n\nPlace holder stats msg");
		retval.getChildren().addAll(loginmsg,stats);
		return retval;
	}
	
	/**
	 * Gets the item that is surposed to be the top most part of the application
	 * This should be the clickable menus that allow the use to log in and adjust their account settings.
	 * @return A Menubar with all the options already added to it.
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
		//username.set
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
	 * Handles the event were the Log in Button is Clicked
	 */
	private void loginButtonClick() {
		// Ever time I change initTopBar is changed, update this function
		TextField username = (TextField) initTopBar.getChildren().get(1);
		TextField password = (TextField) initTopBar.getChildren().get(2);
		boolean successful = doLogin(username.getText(), password.getText());
		if (successful) {
			// TODO: Update the Left with statistics
			System.out.println("Login successful\n");
		} else {
			// TODO: Handle unsuccessful login
			System.out.println("Invalid username or password\n");
		}
	}
	
	/**
	 * Carries out the login.
	 * Returns postive if it was successful
	 * Returns negative if it wasn't successful
	 * @return The result code of the attempted login
	 */
	private boolean doLogin(String username, String password) {
		System.out.println("username = '" + username + "' password = '" + password + "'");
		
		return acctMgr.login(username, password);
	}
	
	/**
	 * Handles the event were the Create New Account Button is Clicked
	 */
	private void createNewAccountButtonClick() {
		this.setCenter(this.initCreateAccountMenu);
	}
	
	/**
	 * Creates the Create Account Center Pane Screen
	 * @return The VBox containing the elements of the screen.
	 */
	private VBox initCreateAccountScreen() {
		VBox retval = new VBox();
		Label info = new Label("Type in your Username and Password, then click the Create Account Button");
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
		retval.getChildren().addAll(info,username,password,makeaccount);
		retval.setPrefWidth(600);
		retval.setPrefHeight(600);
		return retval;
	}
	
	/**
	 * Handles the act of creating an account.
	 */
	private void createNewAccountButtonOnFinishedClick() {
		// Ever time I change initCreateAccountScreen, I need to update this function
		//Label info = (Label) initCreateAccountMenu.getChildren().get(0);
		TextField username = (TextField) initCreateAccountMenu.getChildren().get(1);
		TextField password = (TextField) initCreateAccountMenu.getChildren().get(2);
		int status = doCreateNewAccount(username.getText(), password.getText());
		// TODO: Change the UI to reflect the status returned.
		
		// Temporary, change back to normal starting GUI
		if (status == 0) {
			this.setCenter(this.initGameselectonboxarea);
		} else if (status == 1) {
			// TODO: Handle successful account creation
			System.out.println("Account creation successful");
		} else if (status == 2) {
			// TODO: Handle username already in use
			System.out.println("Username already in use");
		} else if (status == 3) {
			// TODO: Handle other error in creation
			System.err.println("Other error encounted on account creation");
		}
	}
	
	/**
	 * Creates a new account in the system with the provided info
	 * Returns positive on success, returns negative on failure
	 * @param username The username of the account to be created
	 * @param password The password of the account to be created
	 * @return Status code of the result.
	 */
	private int doCreateNewAccount(String username, String password) {
		System.out.println("username = '" + username + "' password = '" + password + "'");
		
		return acctMgr.createAccount(username, password);
	}
	
	/**
	 * Gets the item that surposed to be in the center of the application.
	 * In this case its the game selector menu.
	 * @return A grid pane of all the buttons to lead to each game.
	 */
	private GridPane initGamePanel() {
		GridPane grid = new GridPane();
		grid.getColumnConstraints().add(new ColumnConstraints(260));
		grid.getColumnConstraints().add(new ColumnConstraints(260));
		grid.getColumnConstraints().add(new ColumnConstraints(260));
		grid.getColumnConstraints().add(new ColumnConstraints(260));
		GameIconItem[] gamelist = getGameList();
		for(int x = 0; x < gamelist.length; x++) {
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
	 * Mapping function, maps game button clicks to their respected game and inits it.
	 * @param name The name feild of the button clicked, used to ID it.
	 */
	private void gameButtonClick(String name) {
		if (name.equals("Tic-tac-toe")) {
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
		retval[0]  = new GameIconItem("Tic-tac-toe","/tictactoeicon.png",0);
		return retval;
	}
}
