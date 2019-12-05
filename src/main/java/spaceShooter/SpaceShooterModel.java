package spaceShooter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
/**
 * 
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public class SpaceShooterModel extends Observable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<SpaceShooterEnemy> currentEnemies;
	private int currentScore;
	private int numberLives;
	private int currentLevel;
	private EnemyGenerator levelGenerator;

	public SpaceShooterModel() {
		currentEnemies = new ArrayList<SpaceShooterEnemy>();
		currentScore = 0;
		numberLives = 3;
		currentLevel = 1;
		levelGenerator = new EnemyGenerator();
	}
	
	public int getScore() {
		return currentScore;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}
	
	public boolean isStillRunning() {
		return numberLives > 0;
	}

	public void generateLevel() {
		 currentEnemies = levelGenerator.getEnemies(currentLevel);
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

	public void incrementScore(int pointValue) {
		currentScore += pointValue;		
	}
}
