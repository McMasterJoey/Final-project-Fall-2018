package spaceShooter;

/**
 * Abstract class that has all of the fields and getters/setters that any
 * enemy will have.
 * @author Wes Rodgers
 *
 */

public abstract class SpaceShooterEnemy extends SpaceShooterShip{
	
	private static final long serialVersionUID = 1L;
	
	protected int shotFrequency; //number of game clock ticks before the enemy can shoot again

	public int getShotFrequency() {
		return shotFrequency;
	}

	public void setShotFrequency(int shotFrequency) {
		this.shotFrequency = shotFrequency;
	}
}
