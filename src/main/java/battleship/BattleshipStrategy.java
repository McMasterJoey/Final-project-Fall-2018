package battleship;

import java.awt.Point;

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
}
