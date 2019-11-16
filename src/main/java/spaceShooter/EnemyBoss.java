package spaceShooter;

import java.awt.Point;

public class EnemyBoss extends SpaceShooterEnemy{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EnemyBoss(int x, int y){
		setPointValue(500);
		setShotFrequency(2);
		setHitboxHeight(150);
		setHitboxWidth(300);
		setCurrentHP(40);
		setDamagePerShot(1);
		setLocation(new Point(x, y));
	}	
}