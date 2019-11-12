package spaceShooter;

import java.awt.Point;

public class Enemy3 extends SpaceShooterEnemy{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Enemy3(){
		setPointValue(30);
		setShotFrequency(4);
		setHitboxHeight(40);
		setHitboxWidth(40);
		setCurrentHP(4);
		setDamagePerShot(2);
		setLocation(new Point(10, 10));
	}
	
	
	
}