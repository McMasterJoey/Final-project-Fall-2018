package spaceShooter;

import java.awt.Point;
import java.io.Serializable;

import spaceShooter.SpaceShooterBuff.BuffType;

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
		lootChance = 6;
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