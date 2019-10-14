package connectFour;

import java.io.Serializable;
import java.util.Random;

import model.IGotNoWhereToGoException;

public class ConnectFourEasyAI implements ConnectFourStrategy, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Find an available column while ignoring possible wins and blocks to make this
	 * AI very easy to bet
	 * 
	 * @throws IGotNoWhereToGoException whenever asked for a desired move that is
	 *                                  impossible to deliver because all 42 squares
	 *                                  already taken
	 */
	@Override
	public int desiredMove(ConnectFourModel theGame) throws IGotNoWhereToGoException{
		if (theGame.maxMovesRemaining() == 0) {
			throw new IGotNoWhereToGoException("The board is full!");
		} else {
			Random rand = new Random();
			int c = rand.nextInt(7);
			while (!theGame.available(c)) {
				c = rand.nextInt(7);
			}
			return c;
		}
	}

}
