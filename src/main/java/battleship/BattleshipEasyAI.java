package battleship;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
/**
 * 
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public class BattleshipEasyAI implements BattleshipStrategy, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Point desiredMove(BattleshipModel theGame) {
		 ArrayList<Ship> potentialHits = theGame.getPotentialHits();
		 if (potentialHits.size() > 0) {
			 for (Ship ship: potentialHits) {
				 if (!ship.isDestroyed()) {
					 Point[] points;
						points = ship.getPoints();
						Random rand = new Random();
						int idx = -1;
						for (int i = 0;i < points.length;i++) {
							if (!theGame.available(points[i].y, points[i].x, true))
								idx = i;
						}
						if (idx == -1)
							idx = rand.nextInt(points.length);
						for (int i = idx; i < points.length;i++) {
							if (theGame.available(points[i].y, points[i].x, true))
								return points[i];
						}
						for (int i = idx; i >= 0;i--) {
							if (theGame.available(points[i].y, points[i].x, true))
								return points[i];
						}
				 }
			 }	 
		 }
		 
		 Random rand = new Random();
		 int row = rand.nextInt(10);
		 int col = rand.nextInt(10);
		 while (!theGame.available(row, col, true)) {
			 row = rand.nextInt(10);
			 col = rand.nextInt(10);
		 }
		return new Point(col, row);
	}

	/**
	 * Direction: 0 for horizontal, 1 for vertical
	 * Randomly modifying positions of ships of computer Ships
	 * PARAM: ArrayList<Ships> computerShips
	 */
	@Override
	public void setComputerBoard(ArrayList<Ship> computerShips) {
		boolean[][] computerBoard = initBoard();
		Random rand = new Random();
		for (Ship ship : computerShips) {
			int size = ship.getSize();
			int direction = rand.nextInt(2);
			if (direction == 0) {  //horizontal
				int x = rand.nextInt(11 - size);
				int y = rand.nextInt(10);
				while (overlapping(x, y, size,computerBoard, true)) { // true for horizontal
					 x = rand.nextInt(11 - size);
					 y = rand.nextInt(10);
				}
				ship.setPosition(new Point (x, y), new Point(x+size-1, y));
				setPoints(x, y, size,computerBoard, true);
			}else {	// vertical
				 int x = rand.nextInt(10);
				 int y = rand.nextInt(11 - size);
				while (overlapping(x, y, size,computerBoard, false)) {
					 x = rand.nextInt(10);
					 y = rand.nextInt(11 - size);
				}
				ship.setPosition(new Point (x, y), new Point(x, y+size-1));
				setPoints(x, y, size,computerBoard, false);
			}
			
		}
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @param computerBoard
	 * @param horizontal true for horizontal, false for vertical
	 */
	private void setPoints(int x, int y, int size, boolean[][] computerBoard, boolean horizontal) {
		if (horizontal) {
			int xEnd = x+size;
			while (x < xEnd) {
				computerBoard[x][y] = true;
				x++;
			}
		}else {
			int yEnd = y +size;
			while (y < yEnd) {
				computerBoard[x][y] = true;
				y++;
			}
		}
		
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @param computerBoard
	 * @param horizontal: true for horizontal. false for vertical
	 * @return
	 */
	private boolean overlapping(int x, int y, int size, boolean[][] computerBoard, boolean horizontal) {
		if (horizontal) {
			int xEnd = x+size;
			while (x < xEnd) {
				if (computerBoard[x][y])
					return true;
				x++;
			}
		}else {
			int yEnd = y +size;
			while (y < yEnd) {
				if (computerBoard[x][y])
					return true;
				y++;
			}
		}
		return false;
	}

	private boolean[][] initBoard() {
		boolean[][]computerBoard = new boolean[10][10];
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) 
				computerBoard[row][col] = false;
			}
		return computerBoard;
	}
		
	

}
