package battleship;

import java.awt.Point;
import java.util.ArrayList;
/**
 * 
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public interface BattleshipStrategy {
	/**
	   * Use Java's Point class to represent the row and column of a Battleship move
	   *
	   * @param theGame Provides access to anything about the game so 
	   * the ComputerPlayer can decide the best square to choose.
	   * 
	   * @return a java.awt.point that stores x and y to represent a 
	   * square in the x row and y column of a battleship game.
	   */
	  public Point desiredMove(BattleshipModel theGame);

	  /**
	   * Set up ships on computer's board for human players to hit
	   * @param battleshipModel
	   */

	public void setComputerBoard(ArrayList<Ship> computerShips);
}
