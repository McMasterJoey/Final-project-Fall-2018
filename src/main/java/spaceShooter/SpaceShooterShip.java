package spaceShooter;

import java.io.Serializable;

public abstract class SpaceShooterShip extends SpaceShooterObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int maxHP;
	protected int currentHP;
	
	protected int shotSpeed; //number of pixels the shot is going to move per game clock tick
	protected int shotFrequency; //number of game clock ticks before the enemy can shoot again
	protected int damagePerShot;

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
}
