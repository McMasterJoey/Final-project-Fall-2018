package spaceShooter;

import java.util.ArrayList;
import java.util.Observable;

public class SpaceShooterModel extends Observable {
	
	private ArrayList<SpaceShooterEnemy> currentEnemies;

	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isStillRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	public void generateLevel() {
		// TODO Auto-generated method stub
	}

	public ArrayList<SpaceShooterEnemy> getCurrentEnemies() {
		return currentEnemies;
	}

	public void incrementLevel() {
		// TODO Auto-generated method stub
		
	}

	public void newGame() {
		// TODO Auto-generated method stub
		
	}

	public int getLives() {
		// TODO Auto-generated method stub
		return 0;
	}
}
