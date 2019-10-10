package connectFour;

/**
 * Classes that implement this interface allow the ComputerPlayer to "see"
 * anything about the game.
 * 
 * Adapted from Rick Mercer's TicTacToeStrategy
 * 
 * @author Wes Rodgers
 */

public interface ConnectFourStrategy {

	/**
	 * returns the column of an AI's desired move
	 *
	 * @param theGame Provides access to anything about the game so the
	 *                ComputerPlayer can decide the best column to choose.
	 * 
	 * @return an integer representing the computer's desired move.
	 */
	public int desiredMove(ConnectFourModel theGame);
}
