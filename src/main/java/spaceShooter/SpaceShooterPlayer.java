package spaceShooter;

import java.awt.Point;
import java.io.Serializable;

public class SpaceShooterPlayer extends SpaceShooterShip implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int movementSpeed;
	private int stallTime;
	private int speedTime;
	
	public SpaceShooterPlayer() {
		currentHP = 1;
		maxHP = 2;
		movementSpeed = 5;
		hitboxWidth = 40;
		hitboxHeight = 40;
	}
	
	public int getMovementSpeed() {
		return movementSpeed;
	}
	
	public void setMovementSpeed(int movementSpeed) {
		this.movementSpeed = movementSpeed;
	}
	
	public boolean stalled() {
		return stallTime > 0;
	}
	
	public void addStall(int time) {
		stallTime = time;
	}
	
	public int getStall() {
		return stallTime;
	}
	
	public void decrementStall() {
		stallTime--;
	}
	
	public Point getLocation() {
		return currentHP == 1 ? location : new Point(location.x - hitboxWidth/2, location.y);	
	}

	public void addSpeedTime(int i) {
		speedTime += i;
	}
	
	public void decrementSpeedTimer() {
		speedTime--;
	}

	public int getSpeedTimer() {
		return speedTime;
	}

}
