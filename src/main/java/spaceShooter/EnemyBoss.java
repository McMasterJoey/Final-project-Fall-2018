package spaceShooter;

import java.awt.Point;
import java.io.Serializable;

import spaceShooter.SpaceShooterBuff.BuffType;

public class EnemyBoss extends SpaceShooterEnemy implements Serializable{

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
		lootChance = 100;
		SpaceShooterBuff hp = new SpaceShooterBuff(BuffType.HP, this.getLocation());
		drops.put(0, hp);
	}	
}