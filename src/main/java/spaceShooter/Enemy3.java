package spaceShooter;

import java.awt.Point;
import java.io.Serializable;

public class Enemy3 extends SpaceShooterEnemy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Enemy3(int x, int y){
		setPointValue(30);
		setShotFrequency(4);
		setHitboxHeight(40);
		setHitboxWidth(40);
		setCurrentHP(4);
		setDamagePerShot(1);
		setLocation(new Point(x, y));
	}
	
	
	
}