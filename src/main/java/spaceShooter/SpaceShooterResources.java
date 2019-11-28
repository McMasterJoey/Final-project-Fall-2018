package spaceShooter;

import javafx.scene.image.Image;

public class SpaceShooterResources {
	
	// will need to add more images for different enemies and projectiles eventually 
	private Image playerImage;
	private Image[] enemy1 = new Image[2];
	private Image[] enemy2 = new Image[2];
	private Image[] enemy3 = new Image[2];
	private Image playerProjectile;
	private Image enemyImageBoss;
	private Image enemyLaser1;
	private Image enemyLaser2;
	private Image logo;
	private Image gameOver;
	private Image pauseImage;
	private Image continueImage;
	private Image[] explosion = new Image[6];
	private Image speedBuff;
	
	public SpaceShooterResources() {
		playerImage = makeImage("/spaceShooterPlayerImage.png");
		playerProjectile = makeImage("/spaceShooterPlayerAttackImage.png");
		enemy1[0] = makeImage("/spaceInvaders/enemy1_1.png");
		enemy1[1] = makeImage("/spaceInvaders/enemy1_2.png");
		enemy2[0] = makeImage("/spaceInvaders/enemy2_1.png");
		enemy2[1] = makeImage("/spaceInvaders/enemy2_2.png");
		enemy3[0] = makeImage("/spaceInvaders/enemy3_1.png");
		enemy3[1] = makeImage("/spaceInvaders/enemy3_2.png");
		enemyImageBoss = makeImage("/spaceInvaders/AlienMothership.png");
		enemyLaser1 = makeImage("/spaceInvaders/laser.png");
		enemyLaser2 = makeImage("/spaceInvaders/enemylaser.png");
		logo = makeImage("/SpaceShooterLogo.png");
		gameOver = makeImage("/gameover.png");
		pauseImage = makeImage("/pauseImage.png");
		continueImage = makeImage("/continueImage.png");
		explosion[0] = makeImage("/spaceInvaders/explosionblue.png");
		explosion[1] = makeImage("/spaceInvaders/explosionblue.png");
		explosion[2] = makeImage("/spaceInvaders/explosiongreen.png");
		explosion[3] = makeImage("/spaceInvaders/explosiongreen.png");
		explosion[4] = makeImage("/spaceInvaders/explosionpurple.png");
		explosion[5] = makeImage("/spaceInvaders/explosionpurple.png");
		speedBuff = makeImage("/speedBuff.png");
	}
	
	public Image getSpeedBuff() {
		return speedBuff;
	}
	
	private Image makeImage(String s) {
		return new Image(SpaceShooterResources.class.getResource(s).toString());
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

	public Image getEnemyImage(EnemyBoss enemy) {
		return enemyImageBoss;
	}
	
	public Image[] getEnemyImage(SpaceShooterEnemy enemy) {
		if(enemy instanceof Enemy1) {
			return enemy1;
		} else if(enemy instanceof Enemy2) {
			return enemy2;
		} else {
			return enemy3;
		}
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
	
	public Image[] getExplosionImage() {
		return explosion;
	}
}
