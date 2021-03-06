package connectFour;

import java.io.Serializable;
import java.util.Observable;

public class ConnectFourModel extends Observable implements Serializable {

	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private char board[][];
	private int width, height;
	private ConnectFourAI computer;

	public ConnectFourModel() {
		width = 7;
		height = 6;
		initializeBoard();
		computer = new ConnectFourAI();
		// TODO fill name in here
		// computer.setStrategy(/*new EasyAI()*/);
		setChanged();
		notifyObservers();
	}

	/**
	 * initial creation of the board
	 */
	private void initializeBoard() {
		board = new char[height][width];
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				board[row][col] = '_';
			}
		}
	}

	/**
	 * Changes the AI's strategy
	 * 
	 * @param strat the strategy used by the computer to determine its move
	 */
	public void setAIStrategy(ConnectFourStrategy AI) {
		this.computer.setStrategy(AI);
	}

	/**
	 * getter for the computer player
	 * 
	 * @return the ConnectFourAI representing the computer player
	 */
	public ConnectFourAI getConnectFourAI() {
		return computer;
	}

	/**
	 * Checks that the move is legal then toggles the R at that location if so.
	 * Subsequently calls code to have the AI choose their next move and makes that
	 * move as well, unless testing is enabled.
	 * 
	 * @param col  the column value, 0-6
	 * @param test true when we don't want the computer to make a move after
	 */
	public void humanMove(int col, boolean test) {
		if (!available(col) || !isStillRunning()) {
			return;
		}
		int row = 0;
		while (row < height && board[row][col] == '_') {
			row++;
		}
		board[row - 1][col] = 'R';
		setChanged();
		notifyObservers();
		if (!test && this.isStillRunning()) {
			int moveColumn = computer.nextMove(this);
			computerMove(moveColumn);
		}

	}

	/**
	 * Toggles the value on the board to 'Y' indicating the AI's move, notifies the
	 * views that they need to update
	 * 
	 * @param col the column of the move, between 0-6
	 */
	public void computerMove(int col) {
		if (!available(col)) {
			return;
		}
		int row = 0;
		while (row < height && board[row][col] == '_') {
			row++;
		}
		board[row - 1][col] = 'Y';
		setChanged();
		notifyObservers();
	}

	/**
	 * Boolean check to determine whether the game is over
	 * 
	 * @return true if the game is not over, false if it is.
	 */
	public boolean isStillRunning() {
		return !tied() && !won('R') && !won('Y');
	}

	/**
	 * checks if player with move of char c won in any of the three possible ways
	 * 
	 * @param c the player's move, R or Y
	 * @return true if the player with move c won, false otherwise
	 */
	public boolean won(char c) {
		return wonVertically(c) || wonHorizontally(c) || wonDiagonally(c);
	}

	/**
	 * checks the two diagonals to see if the player won
	 * 
	 * @param c the player's move, R or Y
	 * @return true if the player with move c won on a diagonal, false otherwise
	 */
	private boolean wonDiagonally(char c) {
		// check top left to bottom right diagonals

		/*
		 * this loop checks these spots
		 * x _ _ _ _ _ _ 
		 * x x _ _ _ _ _ 
		 * x x x _ _ _ _ 
		 * _ x x x _ _ _ 
		 * _ _ x x x _ _ 
		 * _ _ _ x x x _
		 */
		for (int i = 0; i < height; i++) {
			int numSame = 0;
			int row = i;
			int col = 0;
			while (col < width && row < height) {
				if (board[row][col] == c) {
					numSame++;
				} else {
					numSame = 0;
				}
				if (numSame == 4) {
					return true;
				}
				row++;
				col++;
			}
		}

		/*
		 * this loop checks these spots 
		 * x x x x _ _ _
		 * _ x x x x _ _ 
		 * _ _ x x x x _ 
		 * _ _ _ x x x x 
		 * _ _ _ _ x x x 
		 * _ _ _ _ _ x x
		 */
		for (int i = 0; i < width; i++) {
			int numSame = 0;
			int row = 0;
			int col = i;
			while (col < width && row < height) {
				if (board[row][col] == c) {
					numSame++;
				} else {
					numSame = 0;
				}
				if (numSame == 4) {
					return true;
				}
				row++;
				col++;
			}
		}

		// now check on the bottom left to top right diagonals

		/*
		 * This loop checks these spots 
		 * _ _ _ x x x x 
		 * _ _ x x x x _ 
		 * _ x x x x _ _ 
		 * x x x x _ _ _ 
		 * x x x _ _ _ _ 
		 * x x _ _ _ _ _
		 */
		for (int i = width - 1; i >= 0; i--) {
			int numSame = 0;
			int row = 0;
			int col = i;
			while (col >= 0 && row < height) {
				if (board[row][col] == c) {
					numSame++;
				} else {
					numSame = 0;
				}
				if (numSame == 4) {
					return true;
				}
				row++;
				col--;
			}
		}

		/*
		 * This loop checks these spots 
		 * _ _ _ _ _ _ x 
		 * _ _ _ _ _ x x 
		 * _ _ _ _ x x x 
		 * _ _ _ x x x _ 
		 * _ _ x x x _ _ 
		 * _ x x x _ _ _
		 */
		for (int i = 0; i < height; i++) {
			int numSame = 0;
			int row = i;
			int col = width - 1;
			while (col > 0 && row < height) {
				if (board[row][col] == c) {
					numSame++;
				} else {
					numSame = 0;
				}
				if (numSame == 4) {
					return true;
				}
				row++;
				col--;
			}
		}

		// No win on either diagonal
		return false;
	}

	/**
	 * checks to see if the player won on a row
	 * 
	 * @param c the player's move, R or Y
	 * @return true if the player with move C won on a horizontal, false otherwise
	 */
	private boolean wonHorizontally(char c) {
		for (int row = 0; row < height; row++) {
			int numSame = 0;
			for (int col = 0; col < width; col++) {
				if (board[row][col] == c) {
					numSame++;
				} else {
					numSame = 0;
				}
				if (numSame == 4) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * checks to see if the player won on a column
	 * 
	 * @param c the player's move, R or Y
	 * @return true if the player with move C won on a horizontal, false otherwise
	 */
	private boolean wonVertically(char c) {
		for (int col = 0; col < width; col++) {
			int numSame = 0;
			for (int row = 0; row < height; row++) {
				if (board[row][col] == c) {
					numSame++;
				} else {
					numSame = 0;
				}
				if (numSame == 4) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Simple check to see if the game is a draw, occurs when neither R or Y won and
	 * there are no moves remaining
	 * 
	 * @return a boolean, true if the game is a draw, false otherwise
	 */
	public boolean tied() {
		return maxMovesRemaining() == 0 && !won('Y') && !won('R');
	}

	/**
	 * Gets the state of the board as a 2D character array with possible values of
	 * 'R', 'Y', and '_';
	 * 
	 * @return a char[][] representing the board accessed by [row][col]
	 */
	public char[][] getBoard() {
		return board;
	}

	/**
	 * Counts the spaces on the board without an R or Y, returns that count.
	 * 
	 * @return maximum number of possible moves remaining in int form
	 */
	public int maxMovesRemaining() {
		int numberMoves = 0;
		for (int col = 0; col < width; col++) {
			int row = 0;
			while (row < height && board[row][col] == '_') {
				row++;
			}
			numberMoves += row;
		}
		return numberMoves;
	}

	/**
	 * simple check to see if the board is open at a given column
	 * 
	 * @param col, the column value of the move, between 0-6
	 * @return true if the space is empty, false otherwise
	 */
	public boolean available(int col) {
		return board[0][col] == '_';
	}

	@Override
	public String toString() {
		String result = "";
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				result += " " + board[r][c] + " ";
			}
			if (r < height)
				result += "\n";
		}
		return result;
	}

	/**
	 * clears the board
	 */
	public void clearBoard() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				board[i][j] = '_';
			}
		}

	}
	
	/*********************************************************************************
	 * *******************************************************************************
	 * Everything belows are used for Hard strategies
	 */

	
	
	public boolean available(int col, char[][] testBoard) {
		return testBoard[0][col] == '_';
	}

	/*
	 * test computer moves
	 */
	public void computerMove(int col, char[][] testBoard) {
		int row = 0;
		while (row < height && testBoard[row][col] == '_') {
			row++;
		}
		testBoard[row - 1][col] = 'Y';
		
	}

	public boolean isStillRunning(char[][] testBoard) {
		return !tied(testBoard) && !won('R',testBoard) && !won('Y',testBoard);
	}

	public boolean won(char c, char[][] testBoard) {
		return wonVertically(c,testBoard) || wonHorizontally(c, testBoard) || wonDiagonally(c, testBoard);
	}



	private boolean tied(char[][] testBoard) {
		return maxMovesRemaining(testBoard) == 0 && !won('Y', testBoard) && !won('R', testBoard);
	}
	
	private int maxMovesRemaining(char[][] testBoard) {
		int numberMoves = 0;
		for (int col = 0; col < width; col++) {
			int row = 0;
			while (row < height && testBoard[row][col] == '_') {
				row++;
			}
			numberMoves += row;
		}
		return numberMoves;
	}

	/**
	 * checks to see if the player won on a column
	 * 
	 * @param c the player's move, R or Y
	 * @return true if the player with move C won on a horizontal, false otherwise
	 */
	private boolean wonVertically(char c, char[][] testBoard) {
		for (int col = 0; col < width; col++) {
			int numSame = 0;
			for (int row = 0; row < height; row++) {
				if (testBoard[row][col] == c) {
					numSame++;
				} else {
					numSame = 0;
				}
				if (numSame == 4) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	private boolean wonHorizontally(char c, char[][] testBoard) {
		for (int row = 0; row < height; row++) {
			int numSame = 0;
			for (int col = 0; col < width; col++) {
				if (testBoard[row][col] == c) {
					numSame++;
				} else {
					numSame = 0;
				}
				if (numSame == 4) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean wonDiagonally(char c, char[][] testBoard) {
		// check top left to bottom right diagonals

				/*
				 * this loop checks these spots 
				 * x _ _ _ _ _ _ 
				 * x x _ _ _ _ _ 
				 * x x x _ _ _ _ 
				 * _ x x x _ _ _ 
				 * _ _ x x x _ _ 
				 * _ _ _ x x x _
				 */
				for (int i = 0; i < height - 3; i++) {
					int numSame = 0;
					int row = i;
					int col = 0;
					while (col < width && row < height) {
						if (testBoard[row][col] == c) {
							numSame++;
						} else {
							numSame = 0;
						}
						if (numSame == 4) {
							return true;
						}
						row++;
						col++;
					}
				}

				/*
				 * this loop checks these spots 
				 * x x x x _ _ _ 
				 * _ x x x x _ _ 
				 * _ _ x x x x _ 
				 * _ _ _ x x x x 
				 * _ _ _ _ x x x 
				 * _ _ _ _ _ x x
				 */
				for (int i = 0; i < width - 3; i++) {
					int numSame = 0;
					int row = 0;
					int col = i;
					while (col < width && row < height) {
						if (testBoard[row][col] == c) {
							numSame++;
						} else {
							numSame = 0;
						}
						if (numSame == 4) {
							return true;
						}
						row++;
						col++;
					}
				}

				// now check on the bottom left to top right diagonals

				/*
				 * This loop checks these spots 
				 * _ _ _ x x x x 
				 * _ _ x x x x _ 
				 * _ x x x x _ _ 
				 * x x x x _ _ _ 
				 * x x x _ _ _ _ 
				 * x x _ _ _ _ _
				 */
				for (int i = width - 1; i > 2; i--) {
					int numSame = 0;
					int row = 0;
					int col = i;
					while (col >= 0 && row < height) {
						if (testBoard[row][col] == c) {
							numSame++;
						} else {
							numSame = 0;
						}
						if (numSame == 4) {
							return true;
						}
						row++;
						col--;
					}
				}

				/*
				 * This loop checks these spots 
				 * _ _ _ _ _ _ x 
				 * _ _ _ _ _ x x 
				 * _ _ _ _ x x x 
				 * _ _ _ x x x _ 
				 * _ _ x x x _ _ 
				 * _ x x x _ _ _
				 */
				for (int i = 0; i < height - 3; i++) {
					int numSame = 0;
					int row = i;
					int col = width - 1;
					while (col > 0 && row < height) {
						if (testBoard[row][col] == c) {
							numSame++;
						} else {
							numSame = 0;
						}
						if (numSame == 4) {
							return true;
						}
						row++;
						col--;
					}
				}

				// No win on either diagonal
				return false;
	}

	/*
	 * test human move
	 */
	public void humanMove(int col, char[][] testBoard) {
		int row = 0;
		while (row < height && testBoard[row][col] == '_') {
			row++;
		}
		testBoard[row - 1][col] = 'R';
		
	}

	/*
	 * retrieve the previous computer move on the board
	 */
	public void computerCounterMove(int col, char[][] testBoard) {
		int row = 0;
		while (row < height && testBoard[row][col] != 'Y') {
			row++;
		}
		testBoard[row][col] = '_';
		
	}
	
	/*
	 * retrieve the previous human move on the board
	 */
	public void humanCounterMove(int col, char[][] testBoard) {
		int row = 0;
		while (row < height && testBoard[row][col] != 'R') {
			row++;
		}
		testBoard[row][col] = '_';
		
	}
}