package model;

import java.awt.Point;
import java.util.Random;

/**
 * This TTT strategy tries to prevent the opponent from winning by checking for
 * a space to win then to block where the opponent is about to win. If neither
 * is found, this strategy randomly picks a an available choice, which sometimes
 * could be a win if luck prevails.
 * 
 * @author Rick Mercer
 */
public class IntermediateAI implements TicTacToeStrategy {

	/**
	 * Ask the computer player to return a Point that would win. If not possible to
	 * win, try to block the human player from winning. If neither is possible, pick
	 * a random tic tac toe square.
	 * 
	 * Precondition: During testing the AI is associated with the 'O', the odd
	 * number move.
	 * 
	 * @author Rick Mercer and Linjie Liu
	 * 
	 * @param theGame This is needed so the AI can examine the board.
	 * 
	 * @throws IGotNoWhereToGoException whenever asked for a desired move that is
	 *                                  impossible to deliver because all 9 squares
	 *                                  already taken.
	 */
	@Override
	public Point desiredMove(TicTacToeModel theGame) throws IGotNoWhereToGoException {
		// TODO 2: Write a better strategy and run TestStrategies.java to make sure
		if (theGame.maxMovesRemaining() == 0) {
			throw new IGotNoWhereToGoException("The board is full!");
		} else {

			char[][] board = theGame.getBoard();
			Point possibleBlock = null;
			for (int r = 0; r < board.length; r++) {
				for (int c = 0; c < board[r].length; c++) {
					if (c == 0) { //
						// check row
						int sumComputer = 0, sumPlayer = 0;
						Point possible = null;
						for (int tempC = c; tempC < board[r].length; tempC++) {
							if (board[r][tempC] == 'O') {
								sumComputer++;
							} else if (board[r][tempC] == 'X') {
								sumPlayer++;
							} else if (board[r][tempC] == '_'){
								possible = new Point(r, tempC);
							}
						}
						if (sumComputer == 2 && possible != null) {
							return possible;
						} else if (sumPlayer == 2 && possible != null) {
							possibleBlock = possible;
						}

						if (r == 0) {
							// check col
							sumComputer = 0;
							sumPlayer = 0;
							possible = null;
							for (int tempR = r; tempR < board.length; tempR++) {
								if (board[tempR][c] == 'O') {
									sumComputer++;
								} else if (board[tempR][c] == 'X') {
									sumPlayer++;
								} else if (board[tempR][c] == '_') {
									possible = new Point(tempR, c);
								}
							}
							if (sumComputer == 2 && possible != null) {
								return possible;
							} else if (sumPlayer == 2 && possible != null) {
								possibleBlock = possible;
							}
							// check diagonal
							sumComputer = 0;
							sumPlayer = 0;
							possible = null;
							int tempC = c;
							for (int tempR = r; tempR < board.length; tempR++, tempC++) {
								if (board[tempR][tempC] == 'O') {
									sumComputer++;
								} else if (board[tempR][tempC] == 'X') {
									sumPlayer++;
								} else if (board[tempR][tempC] == '_'){
									possible = new Point(tempR, tempC);
								}
							}
							if (sumComputer == 2 && possible != null) {
								return possible;
							} else if (sumPlayer == 2 && possible != null) {
								possibleBlock = possible;
							}
						}
						if (r == board[r].length - 1) {
							// check diagonal
							sumComputer = 0;
							sumPlayer = 0;
							possible = null;
							int tempC = c;
							for (int tempR = r; tempR > -1; tempR--, tempC++) {
								if (board[tempR][tempC] == 'O') {
									sumComputer++;
								} else if (board[tempR][tempC] == 'X') {
									sumPlayer++;
								} else if (board[tempR][tempC] == '_'){
									possible = new Point(tempR, tempC);
								}
							}
							if (sumComputer == 2 && possible != null) {
								return possible;
							} else if (sumPlayer == 2&& possible != null) {
								possibleBlock = possible;
							}
						}
					} else {
						if (r == 0) {
							// check col
							// check col 
							int sumComputer = 0, sumPlayer = 0;
							Point possible = null;
							for (int tempR = r; tempR < board.length; tempR++) {
								if (board[tempR][c] == 'O') {
									sumComputer++;
								} else if (board[tempR][c] == 'X') {
									sumPlayer++;
								} else if (board[tempR][c] == '_'){
									possible = new Point(tempR, c);
								}
							}
							if (sumComputer == 2 && possible != null) {
								return possible;
							} else if (sumPlayer == 2&& possible != null) {
								possibleBlock = possible;
							}
						}
					}
				}
			}
			// at this point there is no possible win, choose a possible block
			if (possibleBlock != null) {
				return possibleBlock;
			} else {
				// choose a random point
				Random rand = new Random();
				int r = rand.nextInt(3);
				int c = rand.nextInt(3);
				while (!theGame.available(r, c)) {
					r = rand.nextInt(3);
					c = rand.nextInt(3);
				}
				return new Point(r, c);
			}
		}
	}
}