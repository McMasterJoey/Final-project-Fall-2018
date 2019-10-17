package battleship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import ticTacToe.EasyAI;
import ticTacToe.TicTacToeAI;

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
		// TODO Auto-generated method stub
		
	}

	public void setAIStrategy(BattleshipStrategy AI) {
		this.computer.setStrategy(AI);
	}

	public BattleshipAI getBattleshipAI() {
		return computer;
	}

	public void humanMove(int row, int col, boolean test) {
		
	}

	public void computerMove(int row, int col) {

	}

	public boolean isStillRunning() {
		return false;
	}

	public boolean won(char c) {
		return false;
	}

	public boolean available(int row, int col) {
		return false;
	}

	@Override
	public String toString() {
		return null;

	}

	public void clearBoard() {

	}
}
