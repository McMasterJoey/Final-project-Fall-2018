package spaceShooter;

import java.util.ArrayList;
import java.util.Observable;

public class SpaceShooterModel extends Observable {
	
	private ArrayList<SpaceShooterEnemy> currentEnemies;
	private int currentScore;
	private int numberLives;
	private int currentLevel;
	private EnemyGenerator LevelGenerator;

	public SpaceShooterModel() {
		currentEnemies = new ArrayList<SpaceShooterEnemy>();
		currentScore = 1;
		numberLives = 3;
		currentLevel = 1;
		LevelGenerator = new EnemyGenerator();
	}
	
	public int getScore() {
		return currentScore;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}
	
	public boolean isStillRunning() {
		return numberLives >= 0;
	}

	public void generateLevel() {
		// TODO something like this:
		 currentEnemies = LevelGenerator.getEnemies(currentLevel);
	}

	public ArrayList<SpaceShooterEnemy> getCurrentEnemies() {
		return currentEnemies;
	}

	public void incrementLevel() {
		currentLevel++;		
	}

	public void newGame() {
		currentEnemies = new ArrayList<SpaceShooterEnemy>();
		currentScore = 0;
		numberLives = 3;
		currentLevel = 1;		
	}

	public int getLives() {
		return numberLives;
	}
	
	public void setLives(int numberLives) {
		this.numberLives = numberLives;
	}
}
