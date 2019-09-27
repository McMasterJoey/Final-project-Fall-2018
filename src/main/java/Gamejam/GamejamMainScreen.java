package Gamejam;

import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import view.TicTacToeControllerView;
/**
 * The MainScreen view. 
 * Should be the first thing that pops up when loading the application
 * Should be what is returned to after closing a game.
 * @author Joey McMaster
 *
 */
public class GamejamMainScreen extends BorderPane {
	private GridPane gameselectonboxarea;
	public GamejamMainScreen() {
		super();
		init();
	}
	/**
	 * Inits the Object
	 */
	private void init() {
		this.setTop(_getTop());
		gameselectonboxarea = _getCenter();
		this.setCenter(gameselectonboxarea);
	}
	/**
	 * Gets the item that is surposed to be the top most part of the application
	 * This should be the clickable menus that allow the use to log in and adjust their account settings.
	 * @return A Menubar with all the options already added to it.
	 */
	private MenuBar _getTop() {
		MenuBar bar = new MenuBar();
		Menu accountmenu =  new Menu("Accounts");
		MenuItem loginopt = new MenuItem("Log in");
		MenuItem logoutopt = new MenuItem("Log out");
		MenuItem createaccountopt = new MenuItem("Create Account");
		
		accountmenu.getItems().addAll(loginopt,logoutopt,createaccountopt);
		Menu optionmenu = new Menu("Options");
		bar.getMenus().addAll(accountmenu,optionmenu);
		return bar;
	}
	/**
	 * Gets the item that surposed to be in the center of the application.
	 * In this case its the game selector menu.
	 * @return A grid pane of all the buttons to lead to each game.
	 */
	private GridPane _getCenter() {
		GridPane grid = new GridPane();
		grid.getColumnConstraints().add(new ColumnConstraints(260));
		grid.getColumnConstraints().add(new ColumnConstraints(260));
		grid.getColumnConstraints().add(new ColumnConstraints(260));
		grid.getColumnConstraints().add(new ColumnConstraints(260));
		gameiconitem[] gamelist = getGameList();
		for(int x = 0; x < gamelist.length; x++) {
			Button gamebutton = new Button();
			Image icon = new Image(getClass().getResourceAsStream(gamelist[x].getIconFilePath()));
			gamebutton.setGraphic(new ImageView(icon));
			gamebutton.setText(gamelist[x].getName());
			gamebutton.setOnMouseClicked((click) -> {
	           //System.out.println("Button Click!");
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
	private void init_tictactoe() {
		this.setCenter(new TicTacToeControllerView());
	}
	/**
	 * Fetches all the games that are implemented
	 */
	private gameiconitem[] getGameList() {
		gameiconitem[] retval = new gameiconitem[1];
		retval[0]  = new gameiconitem("Tic-tac-toe","/tictactoeicon.png",0);
		return retval;
	}
	/**
	 * Private class, Holds needed data to produce each button that loads each game.
	 * @author Joey McMaster
	 *
	 */
	private class gameiconitem {
		String gamename;
		String iconfilepath;
		int gameid;
		public gameiconitem(String name, String iconfilepath, int gameid) {
			this.gamename = name;
			this.iconfilepath = iconfilepath;
			this.gameid = gameid;
		}
		public String getName() {
			return gamename;
		}
		public String getIconFilePath() {
			return iconfilepath;
		}
		public int getGameID() {
			return gameid;
		}
	}
}
