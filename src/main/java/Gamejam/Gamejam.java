package Gamejam;
import controller.LogStatType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/**
 * Implementation of the Gamejam project. Acts as the Init class.
 * 
 * A little final note here by Joey McMaster
 * This hopefully will be my last edit before we turn this project in on 12/5/2019 at 3:30 PM 
 * Its been a pleasure working with everyone, thanks to our efforts we created this monster of a project.
 * I am aware that some of us will be graduating soon, I wish everyone good luck in your future projects.
 * As Product owner of this awesome project, I say thank you for all your hard work that went into this. 
 * This here is my stamp of approval
 * [---------------------------------------]
 * [  Good Job Good Job Good Job Good Job  ]
 * [ {}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}  ]
 * [  Good Job Good Job Good Job Good Job  ]
 * [ {}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}  ]
 * [---------------------------------------]
 * Bear Down! We are almost done with the last semester of the decade!
 * 
 * 
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public class Gamejam extends Application {
	// Print everything debug related.
	public static final boolean ShowDebugOutput = false;
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
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/gamejamapplicationicon.png")));
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
