package spaceShooter;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Random;

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
import javafx.scene.input.MouseButton;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import spaceShooter.SpaceShooterBuff.BuffType;
/**
 * 
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public class SpaceShooterControllerView extends GameControllerView {

	private final int WIDTH = 1090;
	private final int HEIGHT = 660;
	private AnimationTimer gameClock;
	private SpaceShooterModel gameModel;
	private SpaceShooterPlayer player;
	private ArrayList<AudioClip> soundfx = new ArrayList<AudioClip>();
	private ArrayList<Point> animations = new ArrayList<Point>();
	private GameMenu gameMenu;
	private Canvas gameScreen;
	private int gameClockCount = 0;
	private ArrayList<SpaceShooterEnemy> enemyList;
	private ArrayList<SpaceShooterProjectile> enemyProjectiles;
	private ArrayList<SpaceShooterProjectile> playerProjectiles;
	private HashMap<SpaceShooterEnemy, Point> referenceEnemyMap;
	private ArrayList<SpaceShooterBuff> itemDrops;
	private int enemySpeedMultiplier = 1;
	private int transitionClockCount;
	private Point startingLocation, playerDelta;
	private boolean aPressed = false;
	private boolean dPressed = false;
	private SpaceShooterResources resources = new SpaceShooterResources();
	private boolean beginningOfLevel = false;
	private Random shooter = new Random(System.nanoTime());

	public SpaceShooterControllerView() {
		setUpGameClock();
		accountManager = AccountManager.getInstance();
		statsManager = StatsManager.getInstance();

		gameModel = new SpaceShooterModel();
		gameModel.addObserver(this);
		gameName = "Space-Shooter";

		player = new SpaceShooterPlayer();
		startingLocation = new Point((WIDTH / 2) - (player.getHitboxWidth() / 2),
				HEIGHT - (2 * player.getHitboxHeight()));
		player.setLocation(new Point(startingLocation.x, startingLocation.y));

		enemyProjectiles = new ArrayList<SpaceShooterProjectile>();
		playerProjectiles = new ArrayList<SpaceShooterProjectile>();
		enemyList = new ArrayList<SpaceShooterEnemy>();
		gameModel.generateLevel();
		enemyList = gameModel.getCurrentEnemies();
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

	private void setupEnemyLevelTransition() {
		transitionClockCount = 0;
		beginningOfLevel = true;
		referenceEnemyMap = new HashMap<SpaceShooterEnemy, Point>();
		for (int i = 0; i < enemyList.size(); i++) {
			SpaceShooterEnemy enemy = enemyList.get(i);
			referenceEnemyMap.put(enemy, new Point(enemy.getLocation().x, enemy.getLocation().y));
		}
		for (SpaceShooterEnemy enemy : enemyList) {
			enemy.setLocation(new Point(WIDTH / 2 - enemy.getHitboxWidth() / 2, 0));
		}

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
			if(click.getButton().equals(MouseButton.PRIMARY)) {
				playerAttack();
			}
		});
	}

	private void disableListeners() {
		Scene myScene = this.getScene();
		myScene.setOnKeyPressed((key) -> {
		});
		myScene.setOnKeyReleased((key) -> {
		});
		gameScreen.setOnMouseClicked((click) -> {
		});
	}

	private void playerAttack() {
		if (player.getCurrentHP() == 2) {
			int x = player.getLocation().x + (player.getHitboxWidth() / 2) - 2;
			Point projectilePosition = new Point(x, player.getLocation().y);
			Point projectilePosition2 = new Point(x + player.getHitboxWidth(), player.getLocation().y);
			playerProjectiles
					.add(new SpaceShooterProjectile(projectilePosition, 4, 8, 5, "/spaceShooterPlayerAttackImage.png"));
			playerProjectiles.add(
					new SpaceShooterProjectile(projectilePosition2, 4, 8, 5, "/spaceShooterPlayerAttackImage.png"));
		} else {
			int x = player.getLocation().x + (player.getHitboxWidth() / 2) - 2;
			Point projectilePosition = new Point(x, player.getLocation().y);

			playerProjectiles
					.add(new SpaceShooterProjectile(projectilePosition, 4, 8, 5, "/spaceShooterPlayerAttackImage.png"));
		}
		soundfx.add(new AudioClip(sformat("/laserShot.wav")));
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

		gc.drawImage(resources.getLogo(), 250, 100, WIDTH - 500, 250);

		gameScreen.setOnMouseClicked((key) -> {
			startGame();
		});

	}

	private void displayPauseScreen() {
		GraphicsContext gc = gameScreen.getGraphicsContext2D();
		gc.setFill(Color.rgb(0, 0, 0, 0.7));
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		gc.drawImage(resources.getPauseImage(), 300, 150, WIDTH - 600, 350);
		getScene().setOnKeyPressed((key) -> {
			unPauseGame();
			setupListeners();
		});
	}

	private void displayContinueScreen() {
		GraphicsContext gc = gameScreen.getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		gc.drawImage(resources.getContinueImage(), 300, 150, WIDTH - 600, 350);

		gameScreen.setOnMouseClicked((click) -> {
			startGame();
		});

	}

	private void displayGameOver() {
		gameClock.stop();
		GraphicsContext gc = gameScreen.getGraphicsContext2D();
		gc.setFill(Color.rgb(0, 0, 0, 0.8));
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		gc.drawImage(resources.getGameOver(), 300, 150, WIDTH - 600, 350);
		updateStatistics();
		showEndScreen();
	}

	private void startGame() {
		gameModel.generateLevel();
		enemyList = gameModel.getCurrentEnemies();
		enemyProjectiles.clear();
		playerProjectiles.clear();
		player.setLocation(new Point(startingLocation.x, startingLocation.y));
		itemDrops.clear();
		setupListeners();
		gameClock.start();
	}

	private void gameClockTick() {
		gameClockCount++;
		updatePlayerPosition();
		updateEnemyPositions();
		enemyAttack();
		updateProjectilePositions();
		checkCollisions();
		updateDropPosition();
		checkDeaths();
		checkLevelOver();
		decrementStall();
		update(gameModel, this);
		checkGameOver();
	}

	private void updateDropPosition() {
		for (SpaceShooterBuff buff : itemDrops) {
			Point delta = new Point(0, 2);
			buff.updatePosition(delta);
		}
	}

	private void decrementStall() {
		if (player.stalled()) {
			player.decrementStall();
		}
	}

	private void enemyAttack() {
		if (!beginningOfLevel && gameClockCount % 15 == 0) {
			for (SpaceShooterEnemy enemy : enemyList) {
				int frequency = enemy.getShotFrequency() * 40;
				int randShot = shooter.nextInt();
				boolean shotThisTick = randShot % frequency == 0;
				if (shotThisTick) {
					if (!(enemy instanceof EnemyBoss)) {
						SpaceShooterProjectile enemyShot = new SpaceShooterProjectile(
								new Point(enemy.getLocation().x + enemy.getHitboxWidth() / 2,
										enemy.getLocation().y + enemy.getHitboxHeight()),
								8, 10, 2, "/enemylaser.png");
						if (enemy instanceof Enemy3) {
							enemyShot = new SpaceShooterProjectile(
									new Point(enemy.getLocation().x + enemy.getHitboxWidth() / 2,
											enemy.getLocation().y + enemy.getHitboxHeight()),
									8, 10, 2, "/laser.png");
							enemyShot.setHoming(true);
						}
						enemyProjectiles.add(enemyShot);
					} else {
						SpaceShooterProjectile enemyShot1 = new SpaceShooterProjectile(
								new Point(enemy.getLocation().x + 50, enemy.getLocation().y + enemy.getHitboxHeight()),
								12, 15, 4, "/laser.png");
						SpaceShooterProjectile enemyShot2 = new SpaceShooterProjectile(
								new Point(enemy.getLocation().x + enemy.getHitboxWidth() - 50,
										enemy.getLocation().y + enemy.getHitboxHeight()),
								12, 15, 4, "/laser.png");
						SpaceShooterProjectile enemyShot3 = new SpaceShooterProjectile(
								new Point(enemy.getLocation().x + enemy.getHitboxWidth() / 2,
										enemy.getLocation().y + enemy.getHitboxHeight()),
								12, 15, 4, "/enemylaser.png");
						enemyShot3.setHoming(true);
						enemyProjectiles.add(enemyShot1);
						enemyProjectiles.add(enemyShot2);
						enemyProjectiles.add(enemyShot3);
					}

				}
			}
		}
	}

	private void checkDeaths() {
		if (player.getCurrentHP() <= 0) {
			soundfx.add(new AudioClip(sformat("/explosion.wav")));
			player.setLocation(new Point(startingLocation.x, startingLocation.y));
			gameModel.setLives(gameModel.getLives() - 1);
			player.setCurrentHP(1);
		}

		ArrayList<SpaceShooterEnemy> toRemove = new ArrayList<SpaceShooterEnemy>();
		for (SpaceShooterEnemy enemy : enemyList) {
			if (enemy.getCurrentHP() <= 0) {
				gameModel.incrementScore(enemy.getPointValue() * enemySpeedMultiplier);
				toRemove.add(enemy);
				if (enemy.didLootDrop()) {
					itemDrops.add(enemy.lootDrop());
				}
			}
		}
		enemyList.removeAll(toRemove);
	}

	private void updatePlayerPosition() {
		int speed = player.getMovementSpeed();
		if (player.getSpeedTimer() > 0) {
			speed += 3;
			player.decrementSpeedTimer();
		}
		int xSpeed = (dPressed ? speed : 0) - (aPressed ? speed : 0);
		// check that the player won't be off screen
		if (player.getLocation().x + xSpeed < 0 || player.getLocation().x + xSpeed + player.getHitboxWidth() > WIDTH) {
			xSpeed = 0;
		}
		playerDelta = new Point(xSpeed, 0);
		player.updatePosition(playerDelta);
	}

	private void updateEnemyPositions() {
		int xMovement;
		if (beginningOfLevel) {
			synchronized (this) {
				for (int i = 0; i < enemyList.size(); i++) {
					SpaceShooterEnemy enemy = enemyList.get(i);
					Point referenceEnemy = referenceEnemyMap.get(enemy);

					double ticksLeft = 240 - transitionClockCount;
					double xMovementPerTick = (referenceEnemy.getX() - enemy.getLocation().getX()) / ticksLeft;
					double yMovementPerTick = (referenceEnemy.getY() - enemy.getLocation().getY()) / ticksLeft;
					Point enemyDelta = new Point();
					enemyDelta.setLocation(xMovementPerTick, yMovementPerTick);
					enemy.updatePosition(enemyDelta);
				}
			}
			transitionClockCount++;
			if (transitionClockCount == 240) {
				beginningOfLevel = false;
			}
			return;
		}
		if (gameClockCount % 240 < 120) {
			xMovement = enemySpeedMultiplier * 1;
		} else {
			xMovement = enemySpeedMultiplier * -1;
		}
		Point enemyDelta = new Point(xMovement, 0);

		for (SpaceShooterEnemy enemy : enemyList) {
			enemy.updatePosition(enemyDelta);
		}
	}

	private void updateProjectilePositions() {
		for (SpaceShooterProjectile ssp : enemyProjectiles) {
			int xDelta = 0;
			if (ssp.isHoming()) {
				xDelta = ssp.getSpeed() / 2 * (player.getLocation().x > ssp.getLocation().x ? 1 : -1);
			}
			ssp.updatePosition(new Point(xDelta, ssp.getSpeed()));
		}

		for (SpaceShooterProjectile ssp : playerProjectiles) {
			int xDelta = 0;
			if (ssp.isHoming()) {
				xDelta = ssp.getSpeed() / 2 * (player.getLocation().x > ssp.getLocation().x ? 1 : -1);
			}
			ssp.updatePosition(new Point(xDelta, -ssp.getSpeed()));
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
				if (!player.stalled()) {
					player.setCurrentHP(player.getCurrentHP() - 1);
					player.addStall(179);
				}
				if (player.getCurrentHP() == 0) {
					animations.add(new Point(player.getLocation().x, player.getLocation().y));
					disableListeners();
					aPressed = false;
					dPressed = false;
				}
				toRemove.add(ssp);
			} else if (isOffScreen(ssp)) {
				toRemove.add(ssp);
			}
		}
		enemyProjectiles.removeAll(toRemove);
		toRemove.clear();

		ArrayList<SpaceShooterBuff> toRemove2 = new ArrayList<SpaceShooterBuff>();
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
		return obj.getLocation().x < obj2.getLocation().x + obj2.getHitboxWidth()
				&& obj.getLocation().x + obj.getHitboxWidth() > obj2.getLocation().x
				&& obj.getLocation().y < obj2.getLocation().y + obj2.getHitboxHeight()
				&& obj.getLocation().y + obj.getHitboxHeight() > obj2.getLocation().y;
	}

	private void checkLevelOver() {
		if (enemyList.size() == 0) {
			gameModel.incrementLevel();
			gameModel.generateLevel();
			enemyList = gameModel.getCurrentEnemies();
			setupEnemyLevelTransition();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {

		// play the soundfx
		Iterator<AudioClip> clips = soundfx.iterator();
		while (clips.hasNext()) {
			AudioClip a = clips.next();
			a.setVolume(0.5);
			a.play();
			clips.remove();
		}

		GraphicsContext gc = gameScreen.getGraphicsContext2D();
		gc.clearRect(0, 0, WIDTH, HEIGHT);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, HEIGHT);

		if (animations.isEmpty()) {
			if (!player.stalled() || player.getStall() % 20 == 0) {
				if (player.getCurrentHP() == 2) {
					gc.drawImage(resources.getPlayerImage(), player.getLocation().x, player.getLocation().y,
							player.getHitboxWidth(), player.getHitboxHeight());
					gc.drawImage(resources.getPlayerImage(), player.getLocation().x + player.getHitboxWidth(),
							player.getLocation().y, player.getHitboxWidth(), player.getHitboxHeight());
				} else {
					gc.drawImage(resources.getPlayerImage(), player.getLocation().x, player.getLocation().y,
							player.getHitboxWidth(), player.getHitboxHeight());
				}
			}
		} else {
			drawExplosion();
		}

		for (SpaceShooterEnemy enemy : enemyList) {
			if (enemy instanceof EnemyBoss) {
				gc.drawImage(resources.getEnemyImage((EnemyBoss) enemy), enemy.getLocation().x, enemy.getLocation().y,
						enemy.getHitboxWidth(), enemy.getHitboxHeight());
			} else {
				gc.drawImage(resources.getEnemyImage(enemy)[(gameClockCount / 45) % 2], enemy.getLocation().x,
						enemy.getLocation().y, enemy.getHitboxWidth(), enemy.getHitboxHeight());
			}
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
			if (item.getType() == BuffType.SPEED) {
				gc.drawImage(resources.getSpeedBuff(), item.getLocation().getX(), item.getLocation().getY(),
						item.getHitboxWidth(), item.getHitboxHeight());
			} else {
				gc.drawImage(resources.getPlayerImage(), item.getLocation().getX() + item.getHitboxWidth() / 4,
						item.getLocation().getY() + item.getHitboxWidth() / 4, player.getHitboxWidth() / 2,
						item.getHitboxHeight() / 2);
				gc.setStroke(Color.WHITE);
				gc.setLineWidth(2);
				gc.strokeOval(item.getLocation().getX(), item.getLocation().getY(), item.getHitboxWidth(), item.getHitboxHeight());
			}

		}

		gc.setFill(Color.WHITE);
		gc.fillText("Score : " + gameModel.getScore(), 10, 10);

		gc.drawImage(resources.getPlayerImage(), WIDTH - (player.getHitboxWidth() * 2),
				HEIGHT - player.getHitboxHeight() + 5, player.getHitboxWidth() / 2, player.getHitboxHeight() / 2);
		gc.fillText(" : " + gameModel.getLives(), WIDTH - (player.getHitboxWidth() * 2) + player.getHitboxWidth() / 2,
				HEIGHT - player.getHitboxHeight() / 2 + 5);

		gc.fillText("Level : " + gameModel.getCurrentLevel(), WIDTH - (player.getHitboxWidth() * 2), 10);

	}

	private void drawExplosion() {
		int time = player.getStall();
		if (time < 120) {
			animations.clear();
			setupListeners();
			return;
		}
		time = (time - 120) / 10;
		int x = animations.get(0).x;
		int y = animations.get(0).y;
		GraphicsContext gc = gameScreen.getGraphicsContext2D();
		int width = 5 + (player.getHitboxWidth() - 5) / (time + 1);
		int height = 5 + (player.getHitboxHeight() - 5) / (time + 1);
		int xLocation = x + (player.getHitboxWidth() / 2 - width / 2);
		int yLocation = y + (player.getHitboxHeight() / 2 - height / 2);
		gc.drawImage(resources.getExplosionImage()[5 - time], xLocation, yLocation, width, height);
	}

	@Override
	protected void updateStatistics() {
		if (gameModel.isStillRunning()) {
			statsManager.logGameStat("Space-Shooter", LogStatType.INCOMPLETE, 1, getScore());
		} else {
			statsManager.logGameStat("Space-Shooter", LogStatType.LOSS, 1, getScore());
		}
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
		gameClock.stop();
		gameClockCount = 0;
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
			player.setLocation(new Point(startingLocation.x, startingLocation.y));
			gameModel.newGame();
			displayStartScreen();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * formats a passed in string as a URL string to be used in getting audio
	 * resources
	 * 
	 * @param s the string representing the audio clip's path relative to the
	 *          project src folder
	 * @return the url string representing the string's location
	 */
	private String sformat(String s) {
		URL url = SpaceShooterControllerView.class.getResource(s);
		return url.toString();
	}

	@Override
	protected String wonString() {
		return "lost";
	}
}
