package spaceShooter;

import java.io.Serializable;

public class SpaceShooterPlayer extends SpaceShooterShip implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int movementSpeed;
	
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

}
