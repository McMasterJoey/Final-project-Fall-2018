package spaceShooter;

import java.awt.Point;

public class SpaceShooterProjectile extends SpaceShooterObject {

	private int movementSpeed;
	
	public SpaceShooterProjectile(Point location, int projectileSpeed, String imagePath) {
		this.location = new Point(location);
		this.movementSpeed = projectileSpeed;
		this.imagePath = imagePath;
	}
	
	public int getSpeed() {
		return movementSpeed;
	}

}
