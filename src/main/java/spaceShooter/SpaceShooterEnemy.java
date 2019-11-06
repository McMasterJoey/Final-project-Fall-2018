package spaceShooter;

import java.io.Serializable;

/**
 * Abstract class that has all of the fields and getters/setters that any
 * enemy will have.
 * @author Wes Rodgers
 *
 */

public abstract class SpaceShooterEnemy implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected int maxHP;
	protected int currentHP;
	
	protected int shotSpeed; //number of pixels the shot is going to move per game clock tick
	protected int shotFrequency; //number of game clock ticks before the enemy can shoot again
	protected int damagePerShot;

	protected int hitboxWidth; //width in pixels of the ship
	protected int hitboxHeight; //height in pixels of the ship
	
	protected String imagePath; // "/<fileName>.png", assuming the file is a png and placed in the resources folder.

	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(int currentHP) {
		this.currentHP = currentHP;
	}

	public int getShotSpeed() {
		return shotSpeed;
	}

	public void setShotSpeed(int shotSpeed) {
		this.shotSpeed = shotSpeed;
	}

	public int getShotFrequency() {
		return shotFrequency;
	}

	public void setShotFrequency(int shotFrequency) {
		this.shotFrequency = shotFrequency;
	}

	public int getDamagePerShot() {
		return damagePerShot;
	}

	public void setDamagePerShot(int damagePerShot) {
		this.damagePerShot = damagePerShot;
	}

	public int getHitboxWidth() {
		return hitboxWidth;
	}

	public void setHitboxWidth(int hitboxWidth) {
		this.hitboxWidth = hitboxWidth;
	}

	public int getHitboxHeight() {
		return hitboxHeight;
	}

	public void setHitboxHeight(int hitboxHeight) {
		this.hitboxHeight = hitboxHeight;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
