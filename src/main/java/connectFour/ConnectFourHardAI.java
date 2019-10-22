package connectFour;

import java.util.ArrayList;
import java.util.Random;

import model.IGotNoWhereToGoException;

public class ConnectFourHardAI implements ConnectFourStrategy {
	/*
	 * Find an possible column by looking at 5 steps ahead. Use MinMax algorithm.
	 */
	static final int SMALL_NUMBER = Integer.MIN_VALUE;

	@Override
	public int desiredMove(ConnectFourModel theGame) throws IGotNoWhereToGoException {
		// if the game just starts
		if (theGame.maxMovesRemaining() > 40) {
			return 3;
		}
		int remainedMoves = theGame.maxMovesRemaining();
		int[] columnScore = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		boolean[] wonAI1 = createWonArray();
		for (int col = 0; col < 7; col++) {
			if (!theGame.available(col))
				columnScore[col] = SMALL_NUMBER;
		}
		for (int c = 0; c < 7; c++) { // AI moves 1st time
			char[][] testBoard = theGame.getBoard();
			if (theGame.available(c, testBoard)) {
				theGame.computerMove(c, testBoard);
				columnScore[c] += evaluate(theGame, testBoard, c, wonAI1, 1);

				for (int h = 0; h < 7; h++) { // human moves 1st time
					if (theGame.available(h, testBoard) && !wonAI1[c]) {
						boolean[] wonHuman1 = createWonArray();
						theGame.humanMove(h, testBoard);
						columnScore[c] += evaluate(theGame, testBoard, h, wonHuman1, 1);

						for (int c1 = 0; c1 < 7; c1++) { // AI moves 2nd time
							if (theGame.available(c1, testBoard) && !wonHuman1[h]) {
								boolean[] wonAI2 = createWonArray();
								theGame.computerMove(c1, testBoard);
								columnScore[c] += evaluate(theGame, testBoard, c1, wonAI2, 2);

								for (int h1 = 0; h1 < 7; h1++) { // human moves 2nd time
									if (theGame.available(h1, testBoard) && !wonAI2[c1]) {
										boolean[] wonHuman2 = createWonArray();
										theGame.humanMove(h1, testBoard);
										columnScore[c] += evaluate(theGame, testBoard, h1, wonHuman2, 2);
										if (remainedMoves < 40) {

											for (int c2 = 0; c2 < 7; c2++) { // AI moves 3rd time
												if (theGame.available(c2, testBoard) && !wonHuman2[h1]) {
													boolean[] wonAI3 = createWonArray();
													theGame.computerMove(c2, testBoard);
													columnScore[c] += evaluate(theGame, testBoard, c2, wonAI3, 3);

//													for (int h2 = 0; h2 < 7; h2++) { // human moves 3rd time
//														if (theGame.available(h2, testBoard) && !wonAI3[c2]) {
//															boolean[] wonHuman3 = createWonArray();
//															theGame.humanMove(h2, testBoard);
//															columnScore[c] += evaluate(theGame, testBoard, h2,
//																	wonHuman3, 3);
//
//															for (int c3 = 0; c3 < 7; c3++) { // AI moves 4th time
//																if (theGame.available(c3, testBoard)
//																		&& !wonHuman3[h2]) {
//																	boolean[] wonAI4 = createWonArray();
//																	theGame.computerMove(c3, testBoard);
//																	columnScore[c] += evaluate(theGame, testBoard, c3,
//																			wonAI4, 4);
//
//																	for (int h3 = 0; h3 < 7; h3++) { // human moves 4th
//																		if (theGame.available(h3) && !wonAI4[c3]) {
//																			boolean[] wonHuman4 = createWonArray();
//																			theGame.humanMove(h3, testBoard);
//																			columnScore[c] += evaluate(theGame,
//																					testBoard, h3, wonHuman4, 4);
//
//																			theGame.humanCounterMove(h3, testBoard);
//																		}
//																	}
//
//																	theGame.computerCounterMove(c3, testBoard);
//																}
//
//															}
//															theGame.humanCounterMove(h2, testBoard);
//														}
//													}
													theGame.computerCounterMove(c2, testBoard);
												}
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
		ArrayList<Integer> tie = new ArrayList<Integer>();  // used to store the highest tie

		int max = SMALL_NUMBER;
		for (int i = 0; i < 7; i++) {

			if (columnScore[i] > max) {
				max = columnScore[i];
			}

		}
		for (int i = 0; i < 7; i++) {
			if (columnScore[i] == max) {
				tie.add(i);
			}
		}
		Random rand = new Random();
		int chosenIdx = rand.nextInt(tie.size());
//		for (int i = 0; i < 7; i++) {
//			System.out.print(columnScore[i] + ", ");
//		}
		return tie.get(chosenIdx);
	}

	/*
	 * helper to create an array to track if a move creates a win.
	 */
	private boolean[] createWonArray() {
		return new boolean[] { false, false, false, false, false, false, false };
	}

	/*
	 * Checks if there is a potential win in the board, return 1 if AI wins -1 human
	 * wins 0 for ties or game still running
	 */
	private int evaluate(ConnectFourModel theGame, char[][] testBoard, int column, boolean[] won, int step) {
		if (theGame.won('Y', testBoard)) {
			won[column] = true;
			if (step == 1)
				return 999999999;
			return 1;

		}

		if (theGame.won('R', testBoard)) {
			won[column] = true;
			if (step == 1)
				return -999999999;
			if (step == 2)
				return -7000;
			return -1;
		}

		return 0;

	}

}
