package spaceShooter;

import java.io.Serializable;

public abstract class SpaceShooterBuff extends SpaceShooterObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract void buffPlayer(SpaceShooterPlayer player);

}
