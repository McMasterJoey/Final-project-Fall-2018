package spaceShooter;

import javafx.scene.image.Image;

public class SpaceShooterResources {
	
	// will need to add more images for different enemies and projectiles eventually 
	private Image playerImage;
	private Image enemyImage;
	private Image playerProjectile;
	private Image enemyProjectile;
	
	public SpaceShooterResources() {
		playerImage = new Image(SpaceShooterResources.class.getResource("/spaceShooterPlayerImage.png").toString());
		playerProjectile = new Image(SpaceShooterResources.class.getResource("/spaceShooterPlayerAttackImage.png").toString());
	}
	
	public Image getPlayerImage() {
		return playerImage;
	}
	
	public Image getPlayerProjectile() {
		return playerProjectile;
	}
}
