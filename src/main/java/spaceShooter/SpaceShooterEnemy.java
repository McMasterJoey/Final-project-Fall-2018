package spaceShooter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

/**
 * Abstract class that has all of the fields and getters/setters that any
 * enemy will have.
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */

public abstract class SpaceShooterEnemy extends SpaceShooterShip implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected int shotFrequency; //number of time units before the enemy can shoot again
	protected int pointValue; //how many points this enemy is worth when you kill it
	protected HashMap<Integer, SpaceShooterBuff> drops = new HashMap<Integer, SpaceShooterBuff>();
	protected int lootChance;

	public int getShotFrequency() {
		return shotFrequency;
	}

	public void setShotFrequency(int shotFrequency) {
		this.shotFrequency = shotFrequency;
	}
	
	public int getPointValue() {
		return pointValue;
	}
	
	public void setPointValue(int pointValue) {
		this.pointValue = pointValue;
	}
	
	/**
	 * returns whether the enemy dropped loot
	 * @return true if the enemy drops loot, false otherwise
	 */
	public boolean didLootDrop() {
		Random rando = new Random(System.nanoTime());
		return rando.nextInt(100) < lootChance;
	}
	
	/**
	 * returns the item the enemy dropped
	 * @return the Item that the enemy dropped
	 */
	public SpaceShooterBuff lootDrop() {
		return drops.get(new Integer((int) (System.nanoTime()%drops.size())));
	}

}
