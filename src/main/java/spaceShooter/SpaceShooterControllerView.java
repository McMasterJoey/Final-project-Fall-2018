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

import controller.AccountManager;
import controller.GameControllerView;
import controller.GameMenu;
import controller.LogStatType;
import controller.StatsManager;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

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
	private ArrayList<SpaceShooterBuff> itemDrops;
	private int enemySpeedMultiplier = 1;
	private Point startingLocation, playerDelta;
	private boolean aPressed = false;
	private boolean dPressed = false;
	private SpaceShooterResources resources = new SpaceShooterResources();

	public SpaceShooterControllerView() {
		setUpGameClock();
		accountManager = AccountManager.getInstance();
		statsManager = StatsManager.getInstance();

		gameModel = new SpaceShooterModel();
		gameModel.addObserver(this);
		gameName = "space-shooter";

		player = new SpaceShooterPlayer();
		startingLocation = new Point((WIDTH / 2) - (player.getHitboxWidth() / 2),
				HEIGHT - (2 * player.getHitboxHeight()));
		player.setLocation(startingLocation);

		enemyProjectiles = new ArrayList<SpaceShooterProjectile>();
		playerProjectiles = new ArrayList<SpaceShooterProjectile>();
		enemyList = new ArrayList<SpaceShooterEnemy>();
		itemDrops = new ArrayList<SpaceShooterBuff>();

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
		setupPlayerListeners();

		// this listener will stop the gameClock while the canvas isn't focused.
	}

	private void setupPlayerListeners() {
		Scene myScene = this.getScene();
		myScene.setOnKeyPressed((key) -> {
			if (key.getCode() == KeyCode.A || key.getCode() == KeyCode.S) {
				aPressed = true;
			} else if (key.getCode() == KeyCode.D || key.getCode() == KeyCode.F) {
				dPressed = true;
			} else if (key.getCode() == KeyCode.P) {
				pauseGame();
				displayPauseScreen();
			}
		});
		myScene.setOnKeyReleased((key) -> {
			if (key.getCode() == KeyCode.A || key.getCode() == KeyCode.S) {
				aPressed = false;
			} else if (key.getCode() == KeyCode.D || key.getCode() == KeyCode.F) {
				dPressed = false;
			}
		});
		gameScreen.setOnMouseClicked((click) -> {
			playerAttack();
		});
	}

	private void playerAttack() {
		int x = player.getLocation().x + (player.getHitboxWidth() / 2) - 2;
		Point projectilePosition = new Point(x, player.getLocation().y);

		playerProjectiles
				.add(new SpaceShooterProjectile(projectilePosition, 4, 8, 5, "/spaceShooterPlayerAttackImage.png"));
	}

	private void checkGameOver() {
		if (!gameModel.isStillRunning()) {
			displayGameOver();
		}
	}

	private void displayStartScreen() {
		GraphicsContext gc = gameScreen.getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		gc.drawImage(resources.getPlayerImage(), player.getLocation().getX(), player.getLocation().getY(),
				player.getHitboxWidth(), player.getHitboxHeight());

		// TODO design logo to put top center

		gameScreen.setOnMouseClicked((key) -> {
			startGame();
		});

	}

	private void displayPauseScreen() {
		GraphicsContext gc = gameScreen.getGraphicsContext2D();
		gc.setFill(Color.rgb(0, 0, 0, 0.5));
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		// TODO draw pause text or image
		getScene().setOnKeyPressed((key) -> {
			unPauseGame();
			setupListeners();
		});
	}

	private void displayContinueScreen() {
		// TODO draw continue screen

		getScene().setOnKeyPressed((key) -> {
			startGame();
		});

	}

	private void displayGameOver() {
		gameClock.stop();
		GraphicsContext gc = gameScreen.getGraphicsContext2D();
		gc.setFill(Color.rgb(0, 0, 0, 0.5));
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		// TODO draw game over stuff

	}

	private void startGame() {
		gameModel.generateLevel();
		enemyList = gameModel.getCurrentEnemies();
		setupListeners();
		gameClock.start();
	}

	private void gameClockTick() {
		gameClockCount++;
		updatePlayerPosition();
		updateEnemyPositions();
		updateProjectilePositions();
		checkCollisions();
		checkDeaths();
		checkLevelOver();
		checkGameOver();
		update(gameModel, this);
	}

	private void checkDeaths() {
		if (player.getCurrentHP() <= 0) {
			gameModel.setLives(gameModel.getLives() - 1);
			player.setLocation(startingLocation);
		}

		ArrayList<SpaceShooterEnemy> toRemove = new ArrayList<SpaceShooterEnemy>();
		for (SpaceShooterEnemy enemy : enemyList) {
			if (enemy.getCurrentHP() <= 0) {
				toRemove.add(enemy);
			}
		}
		enemyList.removeAll(toRemove);
	}

	private void updatePlayerPosition() {
		int xSpeed = (dPressed ? player.getMovementSpeed() : 0) - (aPressed ? player.getMovementSpeed() : 0);
		// check that the player won't be off screen
		if (player.getLocation().x + xSpeed < 0 || player.getLocation().x + xSpeed + player.getHitboxWidth() > WIDTH) {
			xSpeed = 0;
		}
		playerDelta = new Point(xSpeed, 0);
		player.updatePosition(playerDelta);
	}

	private void updateEnemyPositions() {
		int xMovement;
		if (gameClockCount % 20 < 10) {
			xMovement = enemySpeedMultiplier * 3;
		} else {
			xMovement = enemySpeedMultiplier * -3;
		}
		Point enemyDelta = new Point(xMovement, 0);

		for (SpaceShooterEnemy enemy : enemyList) {
			enemy.updatePosition(enemyDelta);
		}
	}

	private void updateProjectilePositions() {
		for (SpaceShooterProjectile ssp : enemyProjectiles) {
			ssp.updatePosition(new Point(0, ssp.getSpeed()));
		}

		for (SpaceShooterProjectile ssp : playerProjectiles) {
			ssp.updatePosition(new Point(0, -ssp.getSpeed()));
		}
	}

	private void checkCollisions() {
		ArrayList<SpaceShooterProjectile> toRemove = new ArrayList<SpaceShooterProjectile>();
		for (SpaceShooterEnemy enemy : enemyList) {
			for (SpaceShooterProjectile ssp : playerProjectiles) {
				if (collisionExists(enemy, ssp)) {
					enemy.setCurrentHP(enemy.getCurrentHP() - 1);
					toRemove.add(ssp);
				} else if (isOffScreen(ssp)) {
					toRemove.add(ssp);
				}
			}
		}
		playerProjectiles.removeAll(toRemove);
		toRemove.clear();

		for (SpaceShooterProjectile ssp : enemyProjectiles) {
			if (collisionExists(player, ssp)) {
				player.setCurrentHP(player.getCurrentHP() - 1);
				toRemove.add(ssp);
			} else if (isOffScreen(ssp)) {
				toRemove.add(ssp);
			}
		}
		enemyProjectiles.removeAll(toRemove);
		toRemove.clear();

		ArrayList<SpaceShooterObject> toRemove2 = new ArrayList<SpaceShooterObject>();
		for (SpaceShooterBuff item : itemDrops) {
			if (collisionExists(player, item)) {
				buffPlayer(item);
				toRemove2.add(item);
			} else if (isOffScreen(item)) {
				toRemove2.add(item);
			}
		}
		itemDrops.removeAll(toRemove2);
	}

	private boolean isOffScreen(SpaceShooterObject item) {
		return item.getLocation().y < 0 || item.getLocation().y > HEIGHT;
	}

	private void buffPlayer(SpaceShooterBuff item) {
		item.buffPlayer(player);

	}

	private boolean collisionExists(SpaceShooterObject obj, SpaceShooterObject obj2) {
		if (obj.getLocation().x < obj2.getLocation().x + obj2.getHitboxWidth()
				&& obj.getLocation().x + obj.getHitboxWidth() > obj2.getLocation().x
				&& obj.getLocation().y < obj2.getLocation().y + obj2.getHitboxHeight()
				&& obj.getLocation().y + obj.getHitboxHeight() > obj2.getLocation().y) {
			return true;
		}
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

		// play the soundfx
		Iterator<AudioClip> clips = soundfx.iterator();
		while (clips.hasNext()) {
			AudioClip a = clips.next();
			a.play();
			clips.remove();
		}

		GraphicsContext gc = gameScreen.getGraphicsContext2D();
		gc.clearRect(0, 0, WIDTH, HEIGHT);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, HEIGHT);

		gc.drawImage(resources.getPlayerImage(), player.getLocation().x, player.getLocation().y,
				player.getHitboxWidth(), player.getHitboxHeight());

		for (SpaceShooterEnemy enemy : enemyList) {
			gc.drawImage(resources.getEnemyImage(enemy), enemy.getLocation().x, enemy.getLocation().y,
					enemy.getHitboxWidth(), enemy.getHitboxHeight());
		}

		for (SpaceShooterProjectile ssp : playerProjectiles) {
			gc.drawImage(resources.getPlayerProjectile(), ssp.getLocation().x, ssp.getLocation().y,
					ssp.getHitboxWidth(), ssp.getHitboxHeight());
		}

		for (SpaceShooterProjectile ssp : enemyProjectiles) {
			gc.drawImage(resources.getEnemyProjectile(ssp), ssp.getLocation().x, ssp.getLocation().y,
					ssp.getHitboxWidth(), ssp.getHitboxHeight());
		}

		for (SpaceShooterBuff item : itemDrops) {
			gc.drawImage(resources.getItemImage(item), item.getLocation().getX(), item.getLocation().getY(),
					item.getHitboxWidth(), item.getHitboxHeight());
		}

		gc.setFill(Color.WHITE);
		gc.fillText("Score : " + gameModel.getScore(), 10, 10);

		gc.drawImage(resources.getPlayerImage(), WIDTH - (player.getHitboxWidth() * 2),
				HEIGHT - player.getHitboxHeight() + 5, player.getHitboxWidth() / 2, player.getHitboxHeight() / 2);
		gc.fillText(" : " + gameModel.getLives(), WIDTH - (player.getHitboxWidth() * 2) + player.getHitboxWidth() / 2,
				HEIGHT - player.getHitboxHeight()/2 + 5);

		gc.fillText("Level : " + gameModel.getCurrentLevel(), WIDTH - (player.getHitboxWidth() * 2), 10);

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
			player.setCurrentHP(player.getMaxHP());
			player.setLocation(startingLocation);
			gameModel.newGame();
			displayStartScreen();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
