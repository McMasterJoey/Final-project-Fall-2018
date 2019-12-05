package model;

/**
 * This exception may be thrown by a TicTacToeStrategy 
 * when it is asked to return the player's move in the
 * case where there are no more move from which to select
 *  
 * @author Rick Mercer
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 */
public class IGotNoWhereToGoException extends RuntimeException {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public IGotNoWhereToGoException(String message) {
    super(message);
  }
}