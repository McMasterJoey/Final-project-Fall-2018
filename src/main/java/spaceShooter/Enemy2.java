package spaceShooter;

import java.awt.Point;

public class Enemy2 extends SpaceShooterEnemy{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Enemy2(){
		setPointValue(20);
		setShotFrequency(2);
		setHitboxHeight(40);
		setHitboxWidth(40);
		setCurrentHP(2);
		setDamagePerShot(1);
		setLocation(new Point(10, 10));
	}
	
	
	
}