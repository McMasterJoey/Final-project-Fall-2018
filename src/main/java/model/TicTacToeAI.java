package model;

import java.awt.Point;

/**
 * This class lets a person choose from a variety of different AIs to play
 * against in Tic Tac Toe. Dependent on TODO insert tictactoe strategy name
 * 
 * @author Wes Rodgers
 *
 */

public class TicTacToeAI {

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

}
