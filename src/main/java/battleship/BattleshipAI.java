package battleship;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;


public class BattleshipAI implements Serializable{

	private static final long serialVersionUID = 1L;
	private BattleshipStrategy AIStrategy;
	
	/**
     * Constructor for the BattleshipAI
     */
    public BattleshipAI() {
    	AIStrategy = new BattleshipEasyAI();
    }

    /**
     * Setter for strategy type
     */
    public void setStrategy(BattleshipStrategy NewStrategy) {
        AIStrategy = NewStrategy;
    }

    /**
     * Asks the strategy what the next move it would make is.
     * 
     * @param battleshipModel the current state of the battleship game
     * @return a java.awt.Point storing an x and y (row and col) value
     */
    public Point nextMove(BattleshipModel battleshipModel) {
        
        return AIStrategy.desiredMove(battleshipModel);
    }
    
    /**
     * Ask the strategy to set up its ships on computer's board.
     * 
     * @param computerShips the current state of the battleship game
     */
    public void setBoard(ArrayList<Ship> computerShips) {
    	AIStrategy.setComputerBoard(computerShips);
    }
}
