package spaceShooter;

import java.awt.Point;

public class Enemy1 extends SpaceShooterEnemy{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Enemy1(){
		setPointValue(10);
		setShotFrequency(1);
		setHitboxHeight(40);
		setHitboxWidth(40);
		setCurrentHP(1);
		setDamagePerShot(1);
		setLocation(new Point(10, 10));
	}
	
	
	
}