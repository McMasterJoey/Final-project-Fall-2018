package model;

/**
  * Classes that implement this interface allow the ComputerPlayer 
  * to "see" anything about the game.
  * 
  * @author Rick Mercer
 */

import java.awt.Point;

public interface TicTacToeStrategy {

  /**
   * Use Java's Point class to represent the row and column of a TTT square
   *
   * @param theGame Provides access to anything about the game so 
   * the ComputerPlayer can decide the best square to choose.
   * 
   * @return a java.awt.point that stores x and y to represent a 
   * square in the x row and y column of a tic tac toe game.
   */
  public Point desiredMove(TicTacToeModel theGame);
}