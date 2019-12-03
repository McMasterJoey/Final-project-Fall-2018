package spaceShooter;

import java.awt.Point;
import java.io.Serializable;

public class SpaceShooterBuff extends SpaceShooterObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BuffType type;
	
	public enum BuffType {
		HP, SPEED;
	}
	
	public SpaceShooterBuff(BuffType type, Point point) {
		this.type = type;
		this.setLocation(point);
		hitboxHeight = 40;
		hitboxWidth = 40;
	}

	public void buffPlayer(SpaceShooterPlayer player) {
		switch(type) {
		case HP:
			player.setCurrentHP(2);
			break;
		case SPEED:
			player.addSpeedTime(600);
			break;		
		}			
	}

	public BuffType getType() {
		return type;
	}

}
