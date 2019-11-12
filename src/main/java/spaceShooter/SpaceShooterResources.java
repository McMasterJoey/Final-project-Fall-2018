package spaceShooter;

import javafx.scene.image.Image;

public class SpaceShooterResources {
	
	// will need to add more images for different enemies and projectiles eventually 
	private Image playerImage;
	private Image enemyImage1_1;
	private Image enemyImage1_2;
	private Image enemyImage2_1;
	private Image enemyImage2_2;
	private Image enemyImage3_1;
	private Image enemyImage3_2;
	private Image playerProjectile;
	private Image enemyProjectile;
	
	public SpaceShooterResources() {
		playerImage = new Image(SpaceShooterResources.class.getResource("/spaceShooterPlayerImage.png").toString());
		playerProjectile = new Image(SpaceShooterResources.class.getResource("/spaceShooterPlayerAttackImage.png").toString());
		enemyImage1_1 = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/enemy1_1.png").toString());
		enemyImage1_2 = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/enemy1_2.png").toString());
		enemyImage2_1 = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/enemy2_1.png").toString());
		enemyImage2_2 = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/enemy2_2.png").toString());
		enemyImage3_1 = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/enemy3_1.png").toString());
		enemyImage3_2 = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/enemy3_2.png").toString());
	}
	
	public Image getPlayerImage() {
		return playerImage;
	}
	
	public Image getPlayerProjectile() {
		return playerProjectile;
	}

	public Image getEnemyProjectile(SpaceShooterProjectile ssp) {
		// TODO Auto-generated method stub
		return null;
	}

	public Image getEnemyImage(SpaceShooterEnemy enemy) {
		if (enemy.getPointValue() ==10)
			return enemyImage1_1;
		return null;
	}

	public Image getItemImage(SpaceShooterBuff item) {
		// TODO Auto-generated method stub
		return null;
	}
}
