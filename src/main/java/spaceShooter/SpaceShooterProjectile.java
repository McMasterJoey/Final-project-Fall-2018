package spaceShooter;

import java.awt.Point;

public class SpaceShooterProjectile extends SpaceShooterObject {

	private int movementSpeed;
	
	public SpaceShooterProjectile(Point location, int width, int height, int projectileSpeed, String imagePath) {
		this.location = new Point(location);
		this.movementSpeed = projectileSpeed;
		this.imagePath = imagePath;
		this.hitboxWidth = width;
		this.hitboxHeight = height;
	}
	
	public int getSpeed() {
		return movementSpeed;
	}

}
