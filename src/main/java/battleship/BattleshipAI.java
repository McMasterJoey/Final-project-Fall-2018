package battleship;

import java.awt.Point;
import java.io.Serializable;


public class BattleshipAI implements Serializable{

	private static final long serialVersionUID = 1L;
	private BattleshipStrategy AIStrategy;
	
	/**
     * Constructor for the BattleshipAI
     */
    public BattleshipAI() {
    	/*TODO AIStrategy = new <AIClassName>();*/
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
}
