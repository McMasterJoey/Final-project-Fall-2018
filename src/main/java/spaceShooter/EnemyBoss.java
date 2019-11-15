package spaceShooter;

import java.awt.Point;

public class EnemyBoss extends SpaceShooterEnemy{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EnemyBoss(){
		setPointValue(500);
		setShotFrequency(10);
		setHitboxHeight(150);
		setHitboxWidth(300);
		setCurrentHP(40);
		setDamagePerShot(1);
		setLocation(new Point(10, 10));
	}
	
	
	
}