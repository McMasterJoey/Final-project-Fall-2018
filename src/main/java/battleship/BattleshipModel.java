package battleship;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Observable;

public class BattleshipModel extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean[][] humanBoard;
	private boolean[][] computerBoard;
	private HashMap<String, Ship> humanShips;
	private HashMap<String, Ship> computerShips;
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
		humanShips.put("Carrier", new Ship(5));
		humanShips.put("Battleship", new Ship(4));
		humanShips.put("Destroyer", new Ship(3));
		humanShips.put("Submarine", new Ship(3));
		humanShips.put("Patrol Boat", new Ship(2));
		
		computerShips.put("Carrier", new Ship(5));
		computerShips.put("Battleship", new Ship(4));
		computerShips.put("Destroyer", new Ship(3));
		computerShips.put("Submarine", new Ship(3));
		computerShips.put("Patrol Boat", new Ship(2));
		
	}
	
	public void setShip(String shipName, int rowStart, int colStart, int rowEnd, int colEnd, Boolean human) {
		HashMap<String, Ship> currMap = human ? humanShips : computerShips;
		currMap.get(shipName).setPosition(new Point(colStart, rowStart), new Point(colEnd, rowEnd));
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
			for(Ship s : computerShips.values()) {
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
			for(Ship s : humanShips.values()) {
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
		HashMap<String, Ship> map = human ? computerShips : humanShips;
		for(Ship s : map.values()) {
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
}
