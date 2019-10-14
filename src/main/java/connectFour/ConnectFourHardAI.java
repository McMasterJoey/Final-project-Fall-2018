package connectFour;

import java.io.Serializable;

import model.IGotNoWhereToGoException;

public class ConnectFourHardAI implements ConnectFourStrategy, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * Find an possible column by looking at 5 steps ahead. 
	 * Use MinMax algorithm.
	 */ 
	static final int SMALL_NUMBER = -9999999;
	
	@Override
	public int desiredMove(ConnectFourModel theGame) throws IGotNoWhereToGoException{
		// if the game just starts
		if (theGame.maxMovesRemaining() > 40) {
			return 3;	
		}
		int[] columnScore = new int[] {0,0,0,0,0,0,0};
		for (int col = 0; col <7; col++) {
			if (!theGame.available(col))
				columnScore[col] = SMALL_NUMBER;
		}
		for (int c =0; c < 7;c++) { //AI moves 1st time
			char[][] testBoard = theGame.getBoard();
			if (theGame.available(c, testBoard)) {
				theGame.computerMove(c, testBoard);
				columnScore[c] += evaluate(theGame, testBoard,1);
				
				for (int h = 0; h < 7; h++) { // human moves 1st time
					if (theGame.available(h, testBoard) ) {
						theGame.humanMove(h, testBoard);
						columnScore[c] += evaluate(theGame, testBoard,1);
						
						for (int c1 = 0; c1 < 7; c1++) { // AI moves 2nd time
							if (theGame.available(c1, testBoard) ) {
								theGame.computerMove(c1, testBoard);
								columnScore[c] += evaluate(theGame, testBoard,2);	
								
								for (int h1 = 0;h1 < 7; h1++) { // human moves 2nd time
									if (theGame.available(h1, testBoard) ) {
										theGame.humanMove(h1, testBoard);
										columnScore[c] += evaluate(theGame, testBoard,2);	
										
										for (int c2 = 0; c2 < 7; c2++) { // AI moves 3rd time
											if (theGame.available(c2)) {
												theGame.computerMove(c2, testBoard);
												columnScore[c] += evaluate (theGame, testBoard, 3);
												theGame.computerCounterMove(c2, testBoard);
											}
										}
										theGame.humanCounterMove(h1, testBoard);
									}
								}
								theGame.computerCounterMove(c1, testBoard);
							}
							
						}
						theGame.humanCounterMove(h, testBoard);
					}
				}
				
				theGame.computerCounterMove(c, testBoard);
			}
		}
		int max = SMALL_NUMBER;
		int maxCol = -1;
		for (int i = 3; i < 7; i++) {
			
			if (columnScore[i] > max) {
				max = columnScore[i];
				maxCol = i;
			}
				
		}
		for (int i = 0; i < 3; i++) {
			if (columnScore[i] > max) {
				max = columnScore[i];
				maxCol = i;
			}
		}
		
		for (int i = 0; i < 7; i++) {
			System.out.print(columnScore[i]+", ");
		}
		return maxCol;
	}
	
	/*
	 * Checks if there is a potential win in the board, return 1 if AI wins
	 * -1 human wins
	 * 0 for ties or game still running
	 */
	private int evaluate(ConnectFourModel theGame, char[][] testBoard, int step) {
		if (theGame.won('Y',testBoard))
			return 1;
		if (theGame.won('R', testBoard))
			return -1;
		
		return 0;
		
	}
	
	
}
