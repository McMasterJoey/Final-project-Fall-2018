package battleship;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

/**
 * Class representing the Model part of the M-V-C for Battleship
 * holds data about the ships, hits, and the boards.
 * @author Wes Rodgers, Linjieliu
 *
 */

public class BattleshipModel extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean[][] humanBoard;
	private boolean[][] computerBoard;
	private ArrayList<Ship> humanShips;
	private ArrayList<Ship> computerShips;
	private static final int WIDTH = 10;
	private static final int HEIGHT = 10;
	private BattleshipAI computer;

	/**
	 * Constructor
	 */
	public BattleshipModel() {
		initializeBoard();
		initializeShips();
		computer = new BattleshipAI();
		computer.setStrategy(new BattleshipIntermediateAI());
		computer.setBoard(computerShips);
		potentialHits = new ArrayList<Ship>();
		toString();
		setChanged();
		notifyObservers();
	}
	
	/**
	 * sets up the computer board
	 * @param computerShips an array list of Ship objects
	 */
	public void setComputerBoard(ArrayList<Ship> computerShips) {
		Random rand = new Random();
		for (Ship ship : computerShips) {
			int size = ship.getSize();
			int x = rand.nextInt(11 - size);
			int y = rand.nextInt(10);
			ship.setPosition(new Point (x, y), new Point(x+size, y));
		}
	}
	
	/**
	 * Sets the current AI
	 * @param computer a battleship ai
	 */
	public void setComputer(BattleshipAI computer) {
		this.computer = computer;
	}

	/**
	 * creates the board and sets all to false
	 */
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
	
	/**
	 * Creates the ship objects.
	 */
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

	/**
	 * Sets the AI strategy for this model
	 * @param AI the BattleshipStrategy we want to use
	 */
	public void setAIStrategy(BattleshipStrategy AI) {
		this.computer.setStrategy(AI);
	}

	/**
	 * gets the AI strategy for this model
	 * @return the computer AI
	 */
	public BattleshipAI getBattleshipAI() {
		return computer;
	}

	/**
	 * Calculates how many moves are remaining
	 * @return the number of moves the human player can still make
	 */
	public int maxMovesRemaining() {
		int count = 0;
		for(int row = 0; row < HEIGHT; row++) {
			for(int col = 0; col < WIDTH; col++) {
				count += computerBoard[row][col] ? 0 : 1;
			}
		}
		return count;
	}
	
	/**
	 * makes a human move at (col, row)
	 * @param row the row we want to hit
	 * @param col the column we want to hit
	 */
	public void humanMove(int row, int col) {
		if(available(row, col, false)) {
			Point move = new Point(col, row);
			computerBoard[row][col] = true;
			for(Ship s : computerShips) {
				if(s.wasHit(move)) {
					break;
				}
			}
			
			//this little loop makes sure the strategy returns
			//a legal move
			Point nextMove;
			do {
				nextMove = computer.nextMove(this);
			} while(!available(nextMove.y, nextMove.x, true));
			
			if(isStillRunning()) {
				computerMove(nextMove.y, nextMove.x);
			}
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Returns true if there is a ship at (col, row) on the given board
	 * @param row the row we are checking
	 * @param col the column we are checking
	 * @param human true if we are checking for human ships, false if checking for computer ships
	 * @return true if there is a ship on the given board at (col, row), false otherwise
	 */
	public boolean isShip(int row, int col, boolean human) {
		for(Ship s : (human ? humanShips : computerShips)) {
			for(Point p : s.getPoints()) {
				if(p!= null && p.x == col && p.y == row) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Makes a computer move at (col, row)
	 * @param row the row of the move
	 * @param col the column of the move
	 */
	public void computerMove(int row, int col) {
		if(available(row, col, true)) {
			Point move = new Point(col, row);
			humanBoard[row][col] = true;
			for(Ship s : humanShips) {
				if(s.wasHit(move)) {
					potentialHits.add(s);
				}
			}
		}		
	}

	/**
	 * returns true if the game isn't over, false if it is
	 * @return true if the game isn't over, false if it is
	 */
	public boolean isStillRunning() {
		return !won(true) && !won(false);
	}

	/**
	 * returns true if the given player won the game
	 * @param human true if checking for human wins, false if checking for computer wins
	 * @return
	 */
	public boolean won(boolean human) {
		ArrayList<Ship> list = human ? computerShips : humanShips;
		for(Ship s : list) {
			if(!s.isDestroyed()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if the spot is open at (col, row) for the given player
	 * @param row the row we are checking
	 * @param col the column we are checking
	 * @param human true if we are checking human board, false if we are checking computer board.
	 * @return true if the spot is open at (col, row) for the given player, false otherwise.
	 */
	public boolean available(int row, int col, boolean human) {
		boolean[][] board = human ? humanBoard : computerBoard;
		return !board[row][col];
	}

	/**
	 * Prints out a graphical representation of the board, O for empty spots
	 * X for missed attacks, S for undamaged ship spots, + for damaged ship spots.
	 */
	@Override
	public String toString() {
		String retVal = "Human's board\n";
		for(int row=0; row<HEIGHT; row++) {
			for(int col=0; col<WIDTH; col++) {
				if(isShip(row, col, true)) {
					retVal += "S ";
					retVal = humanBoard[row][col] ? "+ " : retVal;
				} else { 
					retVal += humanBoard[row][col] ? "X " : "O ";
				}
			}
			retVal += "\n";
		}
		retVal += "Computer's board\n";
		
		for(int row=0; row<HEIGHT; row++) {
			for(int col=0; col<WIDTH; col++) {
				if(isShip(row, col, false)) {
					retVal += "S ";
					retVal = computerBoard[row][col] ? "+ " : retVal;
				} else { 
					retVal += computerBoard[row][col] ? "X " : "O ";
				}
			}
			retVal += "\n";
		}
		return retVal;
	}

	/**
	 * Clears the board and creates new ships.
	 */
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

	/**
	 * returns the ArrayList of human Ship objects
	 * @return the ArrayList of human Ship objects
	 */
	public ArrayList<Ship> getHumanShips() {
		return humanShips;
	}

	/**
	 * returns the ArrayList of computer Ship objects
	 * @return the ArrayList of computer Ship objects
	 */
	public ArrayList<Ship> getComputerShips() {
		return computerShips;
	}
	
	/***************************** Methods for strategies Below *********************************/
	
	private ArrayList<Ship> potentialHits;
	public ArrayList<Ship> getPotentialHits() {
		return potentialHits;
	}
}
