package spaceShooter;

import java.awt.Point;
import java.io.Serializable;

public class Enemy1 extends SpaceShooterEnemy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Enemy1(int x, int y){
		setPointValue(10);
		setShotFrequency(1);
		setHitboxHeight(40);
		setHitboxWidth(40);
		setCurrentHP(1);
		setDamagePerShot(1);
		setLocation(new Point(x, y));
	}
	
	
	
}