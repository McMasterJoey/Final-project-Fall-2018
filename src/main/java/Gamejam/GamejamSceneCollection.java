package Gamejam;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.TicTacToeControllerView;
/**
 * Holds all the scenes for each game
 * @author Joey McMaster
 *
 */
public class GamejamSceneCollection {
	public GamejamSceneCollection() {
		
	}
	public Scene getGameSceneById(int id) {
		BorderPane pane = new BorderPane();
		Scene scene = new Scene(pane, 800, 800);
		if (id == 0) {
			pane.setCenter(new TicTacToeControllerView());
		}
		return scene;
	}
}
