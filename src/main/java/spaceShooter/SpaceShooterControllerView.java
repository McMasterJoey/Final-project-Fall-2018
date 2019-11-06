package spaceShooter;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import controller.GameControllerView;
import controller.GameMenu;
import controller.LogStatType;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	private ArrayList<SpaceShooterEnemy> enemyList;
	private ArrayList<SpaceShooterProjectile> enemyProjectiles;
	private ArrayList<SpaceShooterProjectile> playerProjectiles;
	private ArrayList<SpaceShooterObject> itemDrops;
	private int enemySpeedMultiplier = 1;
	private Point playerDelta;

	public SpaceShooterControllerView() {
		setUpGameClock();
		gameModel = new SpaceShooterModel();
		gameModel.addObserver(this);
		player = new SpaceShooterPlayer();
		playerDelta = new Point(0, 0);

		gameScreen = new Canvas(WIDTH, HEIGHT);
		this.setCenter(gameScreen);

		gameMenu = GameMenu.getMenuBar(this);
		gameMenu.getDiffeasy().setOnAction((event) -> {
			enemySpeedMultiplier = 1;
		});
		gameMenu.getDiffinter().setOnAction((event) -> {
			enemySpeedMultiplier = 2;
		});
		this.setTop(gameMenu);
		displayStartScreen();
	}

	private void setUpGameClock() {
		gameClock = new AnimationTimer() {
			@Override
			public void handle(long now) {
				gameClockTick();
			}
		};
	}
	
	private void setupListeners() {
		// TODO player movement and attack listeners

		// this listener will stop the gameClock while the canvas isn't focused.
		gameScreen.focusedProperty().addListener(new ChangeListener<Boolean>() {
			
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {				
			
				if(!newPropertyValue) {
					pauseGame();
					displayPauseScreen();
				}
			}			
		});
	}

	private void checkGameOver() {
		if(gameModel.getLives() == -1) {
			displayGameOver();
		}
	}

	private void displayStartScreen() {
		// TODO Draw start screen info

		gameScreen.setOnKeyPressed((key) -> {
			startGame();
		});

	}
	
	private void displayPauseScreen() {
		// draw Pause Screen
		gameScreen.setOnKeyPressed((key) ->{
			unPauseGame();
		});
	}
	
	private void displayContinueScreen() {
		// TODO draw continue screen
		
		gameScreen.setOnKeyPressed((key) ->{
			startGame();
		});
		
	}

	private void displayGameOver() {
		// TODO Auto-generated method stub
		
	}

	private void startGame() {
		gameModel.generateLevel();
		enemyList = gameModel.getCurrentEnemies();
		setupListeners();
		gameClock.start();
	}

	private void gameClockTick() {
		gameClockCount++;

		// this is because the game ticks way too fast otherwise.
		if (gameClockCount % 4 == 0) {
			updatePlayerPosition();
			updateEnemyPositions();
			updateProjectilePositions();
			checkCollisions();
			checkLevelOver();
			checkGameOver();
		}
	}
	
	private void updatePlayerPosition() {
		player.updatePosition(playerDelta);
	}

	private void updateEnemyPositions() {
		// TODO
	}

	private void updateProjectilePositions() {
		for(SpaceShooterProjectile ssp : enemyProjectiles) {
			ssp.updatePosition(new Point(0, ssp.getSpeed()));
		}
		
		for(SpaceShooterProjectile ssp : playerProjectiles) {
			ssp.updatePosition(new Point(0, -ssp.getSpeed()));
		}
	}

	private void checkCollisions() {
		ArrayList<SpaceShooterProjectile> toRemove = new ArrayList<SpaceShooterProjectile>();
		for(SpaceShooterEnemy enemy : enemyList) {
			for(SpaceShooterProjectile ssp : playerProjectiles) {
				if(collisionExists(enemy, ssp)) {
					enemy.setCurrentHP(enemy.getCurrentHP() - 1);
					toRemove.add(ssp);
				}
			}
		}
		playerProjectiles.removeAll(toRemove);
		toRemove.clear();
		
		for(SpaceShooterProjectile ssp : enemyProjectiles) {
			if(collisionExists(player, ssp)) {
				player.setCurrentHP(player.getCurrentHP() - 1);
				toRemove.add(ssp);
			}
		}
		enemyProjectiles.removeAll(toRemove);
	}

	private boolean collisionExists(SpaceShooterShip ship, SpaceShooterProjectile ssp) {
		// TODO Auto-generated method stub
		return false;
	}

	private void checkLevelOver() {
		if (enemyList.size() == 0) {
			gameModel.generateLevel();
			enemyList = gameModel.getCurrentEnemies();
			gameModel.incrementLevel();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		//play the soundfx
		Iterator<AudioClip> clips = soundfx.iterator();
		while(clips.hasNext()) {
			AudioClip a = clips.next();
			a.play();
			clips.remove();			
		}
		
		// TODO draw player
		// TODO draw enemies
		// TODO draw projectiles
		// TODO draw buffs
		// TODO background?
		// TODO draw score
		// TODO draw number of lives
		// TODO draw current level somewhere

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
		gameModel.addObserver(this);
		displayContinueScreen();
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
			gameModel.newGame();
			displayStartScreen();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
