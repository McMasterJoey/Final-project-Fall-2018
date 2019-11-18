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
	private Image enemyImageBoss;
	private Image enemyLaser1;
	private Image enemyLaser2;
	private Image logo;
	private Image gameOver;
	private Image pauseImage;
	private Image continueImage;
	
	public SpaceShooterResources() {
		playerImage = new Image(SpaceShooterResources.class.getResource("/spaceShooterPlayerImage.png").toString());
		playerProjectile = new Image(SpaceShooterResources.class.getResource("/spaceShooterPlayerAttackImage.png").toString());
		enemyImage1_1 = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/enemy1_1.png").toString());
		enemyImage1_2 = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/enemy1_2.png").toString());
		enemyImage2_1 = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/enemy2_1.png").toString());
		enemyImage2_2 = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/enemy2_2.png").toString());
		enemyImage3_1 = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/enemy3_1.png").toString());
		enemyImage3_2 = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/enemy3_2.png").toString());
		enemyImageBoss = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/AlienMothership.png").toString());
		enemyLaser1 = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/laser.png").toString());
		enemyLaser2 = new Image(SpaceShooterResources.class.getResource("/spaceInvaders/enemylaser.png").toString());
		logo = new Image(SpaceShooterResources.class.getResource("/SpaceShooterLogo.png").toString());
		gameOver = new Image(SpaceShooterResources.class.getResource("/gameover.png").toString());
		pauseImage = new Image(SpaceShooterResources.class.getResource("/pauseImage.png").toString());
		continueImage = new Image(SpaceShooterResources.class.getResource("/continueImage.png").toString());
	}
	
	public Image getPlayerImage() {
		return playerImage;
	}
	
	public Image getPlayerProjectile() {
		return playerProjectile;
	}

	public Image getEnemyProjectile(SpaceShooterProjectile ssp) {
		return ssp.getImagePath().equals("/enemylaser.png") ? enemyLaser2 : enemyLaser1;
	}

	public Image getEnemyImage(SpaceShooterEnemy enemy) {
		if (enemy.getPointValue() == 10)
			return enemyImage1_1;
		if (enemy.getPointValue() == 20)
			return enemyImage2_1;
		if (enemy.getPointValue() == 30)
			return enemyImage3_1;
		if (enemy.getPointValue() == 500)
			return enemyImageBoss;
		return null;
	}

	public Image getItemImage(SpaceShooterBuff item) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Image getLogo() {
		return logo;
	}
	
	public Image getGameOver() {
		return gameOver;
	}

	public Image getPauseImage() {
		return pauseImage;
	}

	public Image getContinueImage() {
		return continueImage;
	}
}
