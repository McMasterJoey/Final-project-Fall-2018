package Gamejam;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.TicTacToeModel;
import view.TicTacToeControllerView;
/**
 * Implementation of the Gamejam project. Acts as the Init class.
 * 
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public class Gamejam extends Application {
	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Game Jam");
		BorderPane basepane = new BorderPane();
		basepane.setCenter(new TicTacToeControllerView());
		Scene scene = new Scene(basepane, 1000, 1000);
		stage.setScene(scene);
		stage.show();
	}
}
