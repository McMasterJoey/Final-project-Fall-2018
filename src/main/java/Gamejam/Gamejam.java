package Gamejam;
import controller.logStatType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
	// Print everything debug related.
	public static final boolean ShowDebugOutput = true;
	private GamejamMainScreen screen;
	public static void main(String args[]) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Game Jam");
		this.screen = new GamejamMainScreen();
		Scene scene = new Scene(this.screen, 1280, 720);
		stage.setScene(scene);
		stage.setOnCloseRequest((event)-> {
			// On Application close, run this
			onAppClose();
		});
		stage.show();
		
	}
	/**
	 * Prints the string that is passed in.
	 * Only prints if the ShowDebugOutput constant is true.
	 * Allows for easy toggling of debug related output;
	 * @param string The string to be printed.
	 */
	public static void DPrint(String string) {
		if (ShowDebugOutput) {
			System.out.println(string);
		}
	}
	/**
	 * Prints the int that is passed in.
	 * Only prints if the ShowDebugOutput constant is true.
	 * Allows for easy toggling of debug related output;
	 * @param num The number to be printed.
	 */
	public static void DPrint(int num) {
		if (ShowDebugOutput) {
			System.out.println(num);
		}
	}
	/**
	 * Handler for the closing of the app.
	 */
	private void onAppClose() {
		DPrint("Closing App");
		this.screen.stopAndSaveCurrentGame();
	}
}
