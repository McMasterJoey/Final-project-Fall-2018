package ticTacToe;

import java.awt.Point;
import java.io.Serializable;
import java.util.Random;

import model.IGotNoWhereToGoException;

/**
 * This strategy selects the first available move at random. It is easy to beat.
 * 
 * @author Rick Mercer
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 */
public class EasyAI implements TicTacToeStrategy, Serializable {

	/**
	 * The constant required to serialize the object.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Find an available square while ignoring possible wins and blocks to make this
	 * AI very easy to bet
	 * 
	 * @throws IGotNoWhereToGoException whenever asked for a desired move that is
	 * impossible to deliver because all 9 squares already taken
	 */
	@Override
	public Point desiredMove(TicTacToeModel theGame) throws IGotNoWhereToGoException {
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