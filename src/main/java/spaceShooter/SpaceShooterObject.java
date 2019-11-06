package spaceShooter;

import java.awt.Point;

public abstract class SpaceShooterObject {
	
	protected int hitboxWidth; //width in pixels of the ship
	protected int hitboxHeight; //height in pixels of the ship
	protected Point location;

	protected String imagePath; // "/<fileName>.png", assuming the file is a png and placed in the resources folder.

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

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public void updatePosition(Point p) {
		
	}
	
}
