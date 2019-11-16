package spaceShooter;

import java.awt.Point;

public class SpaceShooterProjectile extends SpaceShooterObject {

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
