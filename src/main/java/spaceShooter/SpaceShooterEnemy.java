package spaceShooter;

/**
 * Abstract class that has all of the fields and getters/setters that any
 * enemy will have.
 * @author Wes Rodgers
 *
 */

public abstract class SpaceShooterEnemy extends SpaceShooterShip{
	
	private static final long serialVersionUID = 1L;
	
	protected int shotFrequency; //number of time units before the enemy can shoot again
	protected int pointValue; //how many points this enemy is worth when you kill it

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

}
