package battleship;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

public class BattleshipModel extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean[][] humanBoard;
	private boolean[][] computerBoard;
	private ArrayList<Ship> humanShips;
	private ArrayList<Ship> computerShips;
	private static final int WIDTH = 10;
	private static final int HEIGHT = 10;
	private BattleshipAI computer;

	public BattleshipModel() {
		initializeBoard();
		initializeShips();
		computer = new BattleshipAI();
		/*TODO computer.setStrategy(new <AIClassName>());*/
		setChanged();
		notifyObservers();
	}

	private void initializeBoard() {
		humanBoard = new boolean[HEIGHT][WIDTH];
		computerBoard = new boolean[HEIGHT][WIDTH];
		for (int row = 0; row < HEIGHT; row++) {
			for (int col = 0; col < WIDTH; col++) {
				humanBoard[row][col] = false;
				computerBoard[row][col] = false;
			}
		}
	}
	
	private void initializeShips() {
		humanShips = new ArrayList<Ship>();
		computerShips = new ArrayList<Ship>();
		
		humanShips.add(new Ship(5, "carrier"));
		humanShips.add(new Ship(4, "battleship"));
		humanShips.add(new Ship(3, "destroyer"));
		humanShips.add(new Ship(3, "submarine"));
		humanShips.add(new Ship(2, "patrolBoat"));
		
		computerShips.add(new Ship(5, "carrier"));
		computerShips.add(new Ship(4, "battleship"));
		computerShips.add(new Ship(3, "destroyer"));
		computerShips.add(new Ship(3, "submarine"));
		computerShips.add(new Ship(2, "patrolBoat"));
		
	}
	
	public int movesRemaining() {
		int count = 0;
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				count += available(i, j, true) ? 1 : 0;
			}
		}
		return count;
	}
	
	public void setShip(Ship ship, int rowStart, int colStart, int rowEnd, int colEnd, Boolean human) {
		ship.setPosition(new Point(colStart, rowStart), new Point(colEnd, rowEnd));
	}

	public void setAIStrategy(BattleshipStrategy AI) {
		this.computer.setStrategy(AI);
	}

	public BattleshipAI getBattleshipAI() {
		return computer;
	}

	public int maxMovesRemaining() {
		int count = 0;
		for(int row = 0; row < HEIGHT; row++) {
			for(int col = 0; col < WIDTH; col++) {
				count += computerBoard[row][col] ? 0 : 1;
			}
		}
		return count;
	}
	
	public Ship humanMove(int row, int col) {
		if(available(row, col, true)) {
			Point move = new Point(col, row);
			computerBoard[row][col] = true;
			for(Ship s : computerShips) {
				if(s.wasHit(move)) {
					setChanged();
					notifyObservers();
					return s;
				}
			}
		}
		setChanged();
		notifyObservers();
		return null;
	}

	public Ship computerMove(int row, int col) {
		if(available(row, col, false)) {
			Point move = new Point(col, row);
			humanBoard[row][col] = true;
			for(Ship s : humanShips) {
				if(s.wasHit(move)) {
					setChanged();
					notifyObservers();
					return s;
				}
			}
		}
		setChanged();
		notifyObservers();
		return null;
		
	}

	public boolean isStillRunning() {
		return !won(true) && !won(false);
	}

	public boolean won(boolean human) {
		ArrayList<Ship> list = human ? computerShips : humanShips;
		for(Ship s : list) {
			if(!s.isDestroyed()) {
				return false;
			}
		}
		return true;
	}

	public boolean available(int row, int col, boolean human) {
		boolean[][] board = human ? computerBoard : humanBoard;
		return !board[row][col];
	}

	@Override
	public String toString() {
		String retVal = "Human's board\n";
		for(int row=0; row<HEIGHT; row++) {
			for(int col=0; col<WIDTH; col++) {
				retVal += humanBoard[row][col] ? "X " : "O ";
			}
			retVal += "\n";
		}
		retVal += "Computer's board\n";
		
		for(int row=0; row<HEIGHT; row++) {
			for(int col=0; col<WIDTH; col++) {
				retVal += computerBoard[row][col] ? "X " : "O ";
			}
			retVal += "\n";
		}
		return retVal;
	}

	public void clearBoard() {
		for(int row=0; row<HEIGHT; row++) {
			for(int col=0; col<WIDTH; col++) {
				humanBoard[row][col] = false;
				computerBoard[row][col] = false;
			}
			humanShips.clear();
			computerShips.clear();
			initializeShips();
		}
	}
	
	public ArrayList<Ship> getHumanShips() {
		return humanShips;
	}

	public ArrayList<Ship> getComputerShips() {
		return computerShips;
	}
}
