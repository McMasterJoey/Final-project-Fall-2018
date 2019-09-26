package model;

import java.awt.Point;
import java.util.Random;

/**
 * This strategy selects the first available move at random. It is easy to beat.
 * 
 * @author Rick Mercer and Linjie Liu
 */
public class EasyAI implements TicTacToeStrategy {

	/**
	 * Find an available square while ignoring possible wins and blocks to make this
	 * AI very easy to bet
	 * 
	 * @throws IGotNoWhereToGoException whenever asked for a desired move that is
	 *                                  impossible to deliver because all 9 squares
	 *                                  already taken
	 */
	@Override
	public Point desiredMove(TicTacToeModel theGame) throws IGotNoWhereToGoException {
		// TODO 1: Return a random available Point as a Beginner strategy
		if (theGame.maxMovesRemaining() == 0) {
			throw new IGotNoWhereToGoException("The board is full!");
		} else {
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