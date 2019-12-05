package ticTacToe;

import java.awt.Point;
import java.io.Serializable;

/**
 * This class lets a person choose from a variety of different AIs to play
 * against in Tic Tac Toe. Dependent on TODO insert tictactoe strategy name
 * 
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */

public class TicTacToeAI implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TicTacToeStrategy AIStrategy;

    /**
     * Constructor for the TicTacToeAI
     */
    public TicTacToeAI() {
    	AIStrategy = new EasyAI();
    }

    /**
     * Setter for strategy type
     */
    public void setStrategy(TicTacToeStrategy NewStrategy) {
        AIStrategy = NewStrategy;
    }

    /**
     * Asks the strategy what the next move it would make is.
     * 
     * @param ticTacToeModel the current state of the tictactoe game
     * @return a java.awt.Point storing an x and y (row and col) value
     */
    public Point nextMove(TicTacToeModel ticTacToeModel) {
        
        return AIStrategy.desiredMove(ticTacToeModel);
    }
    
    public TicTacToeStrategy getStrategy() {
    	return AIStrategy;
    }

}
