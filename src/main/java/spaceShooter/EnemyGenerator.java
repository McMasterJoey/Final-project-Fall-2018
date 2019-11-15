package spaceShooter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class EnemyGenerator {
	Random rand;
	ArrayList<SpaceShooterEnemy> enemyList;
	public ArrayList<SpaceShooterEnemy> getEnemies(int currentLevel) {
		enemyList = new ArrayList<SpaceShooterEnemy>();
		rand = new Random();
		if (currentLevel == 1) {
			GenerateUofA(enemyList);
		}
		else if (currentLevel  == 2) {
			GenerateClassic(enemyList, 2);
		}
		else if (currentLevel  == 3) {
			GenerateBoss(enemyList);
		}else { // randomly generate 40 enemies
			int i = 0;
			while (i < 50) {
				int x = rand.nextInt(1040);
				int y = rand.nextInt(300);
				int type = rand.nextInt(3)+1;
				SpaceShooterEnemy enemy = drawEnemy(type, new Point(x,y));
				boolean collide=false;
				for (int k = 0; k<enemyList.size();k++) {
					if (collisionExists(enemyList.get(k), enemy)){
						collide = true;
						break;
					}
				}
				if (!collide) {
					enemyList.add(enemy);
					i++;
				}
			}
		}

		return enemyList;
	}

	/*
	 * Generate UofA shpae on Screen
	 */
	private void GenerateUofA(ArrayList<SpaceShooterEnemy> enemyList) {
		// find out what enemies are in u of a
		int uType = rand.nextInt(3)+1;
		int oType = rand.nextInt(3)+1;
		while (oType == uType) {
			oType = rand.nextInt(3)+1;
		}
		int aType = rand.nextInt(3)+1;
		while (aType == uType || aType == oType) {
			aType = rand.nextInt(3)+1;
		}
		// Generate U
		int x = 20;
		int y = 10;
		for (int i = 0; i< 8; i ++) {
			SpaceShooterEnemy enemy = drawEnemy(uType, new Point(x, y));
			enemyList.add(enemy);
			enemy = drawEnemy(uType, new Point(x+120,y));
			enemyList.add(enemy);
			y+=40;
		}
		for (int i = 1; i < 3;i++) {
			SpaceShooterEnemy enemy = drawEnemy(uType, new Point(x+40*i, y));
			enemyList.add(enemy);
		}
		
		// Generate "of"
		x = 300;
		y = 170;
		for (int i = 0; i < 2;i++) {
			SpaceShooterEnemy enemy = drawEnemy(oType, new Point(x+40*i, y));
			enemyList.add(enemy);
			enemy = drawEnemy(oType, new Point(x+40*i, y+160));
			enemyList.add(enemy);
		}
		for (int i = 1; i < 4;i++) {
			SpaceShooterEnemy enemy = drawEnemy(oType, new Point(x-40, y+40*i));
			enemyList.add(enemy);
			enemy = drawEnemy(oType, new Point(x+80, y+40*i));
			enemyList.add(enemy);
		}
		x = 460;
		y = 10;
		for (int i = 1; i < 9;i++) {
			SpaceShooterEnemy enemy = drawEnemy(oType, new Point(x, y+40*i));
			enemyList.add(enemy);
		}
		for (int i = -1; i < 4;i++) {
			if (i == 0) continue;
			if (i == -1) {
				SpaceShooterEnemy enemy = drawEnemy(oType, new Point(x+40*i, y+40*4));
				enemyList.add(enemy);	
				continue;
			}
			SpaceShooterEnemy enemy = drawEnemy(oType, new Point(x+40*i, y));
			enemyList.add(enemy);
			enemy = drawEnemy(oType, new Point(x+40*i, y+40*4));
			enemyList.add(enemy);
		}
		SpaceShooterEnemy enemy = drawEnemy(oType, new Point(x+120, y+40));
		enemyList.add(enemy);
		//Generate "A"
		x += 240;
		y = 330;
		for (int i = 0; i < 3; i++) {
			enemy = drawEnemy(aType, new Point(x, y));
			enemyList.add(enemy);
			enemy = drawEnemy(aType, new Point(x+40*6, y));
			enemyList.add(enemy);
			y-=40;
		}
		for (int i = 0; i < 3; i++) {
			enemy = drawEnemy(aType, new Point(x+40, y));
			enemyList.add(enemy);
			enemy = drawEnemy(aType, new Point(x+40*5, y));
			enemyList.add(enemy);
			if(i == 1) {
				for(int j = 2; j<5;j++) {
					enemy = drawEnemy(aType, new Point(x+40*j, y));
					enemyList.add(enemy);
				}
			}
			y-=40;
		}
		for (int i = 0; i < 2; i++) {
			enemy = drawEnemy(aType, new Point(x+80, y));
			enemyList.add(enemy);
			enemy = drawEnemy(aType, new Point(x+40*4, y));
			enemyList.add(enemy);
			y-=40;
		}
		enemy = drawEnemy(aType, new Point(x+120, y));
		enemyList.add(enemy);
	}

	private void GenerateBoss(ArrayList<SpaceShooterEnemy> enemyList) {
		int x = (1090 - 300) / 2+20;
		int y = 160;
		EnemyBoss boss = new EnemyBoss();
		boss.setLocation(new Point(x, y));
		enemyList.add(boss);
		GenerateHeart(enemyList, 2);
	}

	/*
	 * Generate 3x3 squares made of enemy ships on the screen
	 */
	private void GenerateSquare(ArrayList<SpaceShooterEnemy> enemyList, int enemyType, int x, int y) {
		for (int i = 0; i < 3; i++) {
			SpaceShooterEnemy enemy = drawEnemy(-1, new Point(x + 40*i, y));
			enemyList.add(enemy);
			enemy = drawEnemy(-1, new Point(x+40*i, y+ 80));
			enemyList.add(enemy);
		}
		SpaceShooterEnemy enemy = drawEnemy(-1, new Point(x, y+40));
		enemyList.add(enemy);
		enemy = drawEnemy(-1, new Point(x+80, y+40));
		enemyList.add(enemy);	
	}
	
	/*
	 * Generate small V made of enemy ships on the screen
	 */
	private void GenerateV(ArrayList<SpaceShooterEnemy> enemyList, int enemyType, int x, int y) {
		for (int i = 0; i < 3; i++) {
			if (i == 2) {
				SpaceShooterEnemy enemy = drawEnemy(-1, new Point(x+80, y));
				enemyList.add(enemy);
				continue;
			}
			SpaceShooterEnemy enemy = drawEnemy(-1, new Point(x+40*i, y));
			enemyList.add(enemy);
			enemy = drawEnemy(-1, new Point(x+40*(4-i), y));
			enemyList.add(enemy);
			y+=40;
		}	
	}
	
	// Heart shape of enemies
	private void GenerateHeart(ArrayList<SpaceShooterEnemy> enemyList, int enemyType) {
		int x = 1090/2;
		int y = 120;
		// step 1
		int type = -1;
		int i = 0;
		SpaceShooterEnemy enemy = drawEnemy(type, new Point(x, y));
		enemyList.add(enemy);
		// step 2
		enemy = drawEnemy(type, new Point(x+40, y-40));
		enemyList.add(enemy);
		enemy = drawEnemy(type, new Point(x-40, y-40));
		enemyList.add(enemy);
		//step 3-6
		for (i = 2; i < 6;i++) {
			enemy = drawEnemy(type, new Point(x+40*i, y-80));
			enemyList.add(enemy);
			enemy = drawEnemy(type, new Point(x-40*i, y-80));
			enemyList.add(enemy);
		}
		//step 7
		enemy = drawEnemy(type, new Point(x+40*6, y-40));
		enemyList.add(enemy);
		enemy = drawEnemy(type, new Point(x-40*6, y-40));
		enemyList.add(enemy);
		// step 8,9, 10
		for (i = 8; i <11; i++) {
			enemy = drawEnemy(type, new Point(x+40*7, y));
			enemyList.add(enemy);
			enemy = drawEnemy(type, new Point(x-40*7, y));
			enemyList.add(enemy);
			y+=40;
		}
		// step 11 - 16
		for (i = 6; i >0; i--) {
			enemy = drawEnemy(type, new Point(x+40*i, y));
			enemyList.add(enemy);
			enemy = drawEnemy(type, new Point(x-40*i, y));
			enemyList.add(enemy);
			y+=40;
		}
		// step 17
		enemy = drawEnemy(type, new Point(x, y));
		enemyList.add(enemy);
		
		
	}

	/**
	 * Draw an enemy with specific location on the screen.
	 * @param type
	 * @param point
	 * @return SpaceShooterEnemy with specific type. If type == -1, randomly generate
	 * a type of enemy
	 */
	private SpaceShooterEnemy drawEnemy(int type, Point point) {
		if (type == -1) {
			Random rand = new Random();
			type = rand.nextInt(3)+1;
		}
		SpaceShooterEnemy enemy = null;
		if (type == 1) {
			enemy = new Enemy1();
		}
		if (type == 2) {
			enemy = new Enemy2();
		}
		if (type == 3) {
			enemy = new Enemy3();
		}
		if (type == 4) {
			enemy = new EnemyBoss();
		}
		if (enemy != null)
			enemy.setLocation(point);
		
		
		return enemy;
	}

	/**
	 * Generate the classic enemyList
	 * Each row has 10 enemies of the different types
	 * @param enemyList
	 */
	private void GenerateClassic(ArrayList<SpaceShooterEnemy> enemyList, int Level) {
		for (int j = 0; j < Level; j++) {
			int x = 1090 / 4;
			int y = 660 / 3 - j * 50;
			for (int i = 0; i < 10; i++) {
				Enemy1 type1 = new Enemy1();
				type1.setLocation(new Point(x + 50 * i, y));
				enemyList.add(type1);
			}
			x = 1090 / 4;
			y -= 100;
			for (int i = 0; i < 10; i++) {
				Enemy2 type2 = new Enemy2();
				type2.setLocation(new Point(x + 50 * i, y));
				enemyList.add(type2);
			}

			if (j + 1 == 2)
				continue;
			x = 1090 / 4;
			y -= 100;
			for (int i = 0; i < 10; i++) {
				Enemy3 type3 = new Enemy3();
				type3.setLocation(new Point(x + 50 * i, y));
				enemyList.add(type3);
			}
		}

	}
	private boolean collisionExists(SpaceShooterObject obj, SpaceShooterObject obj2) {
		if (obj.getLocation().x < obj2.getLocation().x + obj2.getHitboxWidth()
				&& obj.getLocation().x + obj.getHitboxWidth() > obj2.getLocation().x
				&& obj.getLocation().y < obj2.getLocation().y + obj2.getHitboxHeight()
				&& obj.getLocation().y + obj.getHitboxHeight() > obj2.getLocation().y) {
			return true;
		}
		return false;
	}
	

}
