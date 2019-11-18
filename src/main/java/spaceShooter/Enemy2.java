package spaceShooter;

import java.awt.Point;
import java.io.Serializable;

public class Enemy2 extends SpaceShooterEnemy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Enemy2(int x, int y){
		setPointValue(20);
		setShotFrequency(2);
		setHitboxHeight(40);
		setHitboxWidth(40);
		setCurrentHP(2);
		setDamagePerShot(1);
		setLocation(new Point(x, y));
	}
	
	
	
}