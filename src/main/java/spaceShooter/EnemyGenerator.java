package spaceShooter;

import java.awt.Point;
import java.util.ArrayList;

public class EnemyGenerator {

	public ArrayList<SpaceShooterEnemy> getEnemies(int currentLevel) {
		ArrayList<SpaceShooterEnemy> enemyList = new ArrayList<SpaceShooterEnemy>();
		int x = 0;
		int y = 10;
		for (int i = 0; i< 10; i++) {
			Enemy1 type1 = new Enemy1();
			type1.setLocation(new Point(x+40*i, y));
			enemyList.add(type1);
		}
		
		
		return enemyList;
	}

}
