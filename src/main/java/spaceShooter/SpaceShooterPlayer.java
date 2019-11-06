package spaceShooter;


public class SpaceShooterPlayer extends SpaceShooterShip{

	private static final long serialVersionUID = 1L;
	
	private int movementSpeed;
	
	public SpaceShooterPlayer() {
		movementSpeed = 5;
	}
	
	public int getMovementSpeed() {
		return movementSpeed;
	}
	
	public void setMovementSpeed(int movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

}
