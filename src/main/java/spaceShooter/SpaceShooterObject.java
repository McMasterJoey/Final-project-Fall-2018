package spaceShooter;

import java.awt.Point;
import java.io.Serializable;

public abstract class SpaceShooterObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int hitboxWidth; // width in pixels of the ship
	protected int hitboxHeight; // height in pixels of the ship
	protected Point location; // x,y coordinates of the object's top left corner

	protected String imagePath; // "/<fileName>.png", assuming the file is a png and in the resources folder

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
		location.setLocation(location.x + p.x, location.y + p.y);
	}

}
