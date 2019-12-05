package spaceShooter;

import java.awt.Point;
import java.io.Serializable;

import spaceShooter.SpaceShooterBuff.BuffType;
/**
 * 
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
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
		lootChance = 2;
		SpaceShooterBuff hp = new SpaceShooterBuff(BuffType.HP, this.getLocation());
		SpaceShooterBuff speed = new SpaceShooterBuff(BuffType.SPEED, this.getLocation());
		drops.put(1, speed);
		drops.put(2, speed);
		drops.put(3, speed);
		drops.put(4, hp);
		drops.put(5, hp);
		drops.put(6, hp);
		drops.put(0, speed);
	}
	
	
	
}