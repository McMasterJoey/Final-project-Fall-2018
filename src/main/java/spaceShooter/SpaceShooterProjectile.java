package spaceShooter;

import java.awt.Point;
import java.io.Serializable;
/**
 * 
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public class SpaceShooterProjectile extends SpaceShooterObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int movementSpeed;
	private boolean homing;
	
	public SpaceShooterProjectile(Point location, int width, int height, int projectileSpeed, String imagePath) {
		this.location = new Point(location);
		this.movementSpeed = projectileSpeed;
		this.imagePath = imagePath;
		this.hitboxWidth = width;
		this.hitboxHeight = height;
		this.homing = false;
	}
	
	public int getSpeed() {
		return movementSpeed;
	}

	public void setHoming(boolean isHoming) {
		homing = isHoming;
	}
	
	public boolean isHoming() {
		return homing;
	}
}
