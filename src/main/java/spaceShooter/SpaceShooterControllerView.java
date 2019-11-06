package spaceShooter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;

import controller.GameControllerView;
import controller.GameMenu;
import controller.LogStatType;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.media.AudioClip;

public class SpaceShooterControllerView extends GameControllerView {

	private final int WIDTH = 1090;
	private final int HEIGHT = 660;
	private AnimationTimer gameClock;
	private SpaceShooterModel gameModel;
	private SpaceShooterPlayer player;
	private ArrayList<AudioClip> soundfx = new ArrayList<AudioClip>();
	private GameMenu gameMenu;
	private Canvas gameScreen;
	private double gameClockCount = 0;
	private int currentLevel;
	private ArrayList<SpaceShooterEnemy> enemyList;

	public SpaceShooterControllerView() {
		setUpGameClock();
		gameModel = new SpaceShooterModel();
		gameModel.addObserver(this);
		player = new SpaceShooterPlayer();
		currentLevel = 1;
		
		gameScreen = new Canvas(WIDTH, HEIGHT);
		this.setCenter(gameScreen);
		
		gameMenu = GameMenu.getMenuBar(this);
		gameMenu.getDiffeasy().setOnAction((event) -> {
			// TODO set easy diff stuff
		});
		gameMenu.getDiffinter().setOnAction((event) -> {
			// TODO set intermediate diff stuff
		});
		this.setTop(gameMenu);
	}

	private void setUpGameClock() {
		gameClock = new AnimationTimer() {
			@Override
			public void handle(long now) {
				gameClockTick();
			}
		};
	}

	private void gameClockTick() {
		gameClockCount++;
		
		//this is because the game ticks way too fast otherwise.
		if(gameClockCount%4 == 0) {
			updatePlayerPosition();
			updateEnemyPositions();
			updateProjectilePositions();
			checkCollisions();
			checkLevelOver();
			checkGameOver();
		}
	}

	private void checkGameOver() {
		// TODO Auto-generated method stub
	}

	private void updatePlayerPosition() {
		// TODO
	}

	private void updateEnemyPositions() {
		// TODO
	}

	private void updateProjectilePositions() {
		// TODO
	}

	private void checkCollisions() {
		// TODO
	}
	
	private void checkLevelOver() {
		// TODO Auto-generated method stub	
		if(enemyList.size() == 0) {
			gameModel.getMoreEnemies(currentLevel);
			currentLevel++;
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO draw player
		// TODO draw enemies
		// TODO draw projectiles
		// TODO draw buffs
		// TODO background?
		// TODO draw score
		// TODO draw number of lives
		// TODO play soundfx

	}

	@Override
	protected void updateStatistics() {
		statsManager.logGameStat("Space-Shooter", LogStatType.INCOMPLETE, 1, getScore());
	}

	@Override
	public int getScore() {
		return gameModel.getScore();
	}

	@Override
	/**
	 * saves the game to the given filepath
	 * 
	 * @param filepath the name of the file we want to save
	 * @return true if the save was successful, false otherwise
	 */
	public boolean saveGame() {
		if (accountManager.isGuest()) {
			return false;
		}

		// Don't save if the game was completed
		if (!gameModel.isStillRunning()) {
			newGame();
		}

		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			String fname = accountManager.getCurUsername() + "-" + gameName + ".dat";
			String sep = System.getProperty("file.separator");
			String filepath = System.getProperty("user.dir") + sep + "save-data";
			if (!new File(filepath).exists()) {
				new File(filepath).mkdir();
			}
			filepath += sep + fname;
			fos = new FileOutputStream(filepath);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(gameModel);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	/**
	 * loads a saved game from the given filepath
	 * 
	 * @param filepath the location from which to load the game
	 * @return true if the load was successful, false otherwise
	 */
	public boolean loadSaveGame() {
		pauseGame();
		boolean retVal = true;
		try {
			String fname = accountManager.getCurUsername() + "-" + gameName + ".dat";
			String sep = System.getProperty("file.separator");
			String filepath = System.getProperty("user.dir") + sep + "save-data" + sep + fname;
			File file = new File(filepath);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				gameModel = (SpaceShooterModel) ois.readObject();
				ois.close();
				update(gameModel, this);
				file.delete();
			} else {
				retVal = newGame();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		// TODO pause before starting game.
		gameModel.addObserver(this);
		update(gameModel, this);
		return retVal;
	}

	@Override
	public boolean pauseGame() {
		try {
			gameClock.stop();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	@Override
	public boolean unPauseGame() {
		try {
			gameClock.start();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	@Override
	public boolean newGame() {
		try {
			gameClock.stop();
			gameClockCount = 0;
			currentLevel = 1;
			displayStartScreen();
			return true;
		} catch(Exception ex) {
			return false;
		}
	}

	private void displayStartScreen() {
		// TODO Auto-generated method stub
		
	}

}
