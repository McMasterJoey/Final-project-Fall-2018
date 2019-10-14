package connectFour;

import java.io.Serializable;

/**
* This class lets a person choose from a variety of different AIs to play
* against in Connect Four. Dependent on TODO insert connect four strategy name
* 
* @author Wes Rodgers
*
*/
public class ConnectFourAI implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConnectFourStrategy AIStrategy;

	/**
	 * Constructor for the ConnectFourAI
	 */
	public ConnectFourAI() {
		//TODO
		//AIStrategy = ConnectFourAI;
	}

	/**
	 * Setter for strategy type
	 */
	public void setStrategy(ConnectFourStrategy NewStrategy) {
	    AIStrategy = NewStrategy;
	}

	/**
	 * Asks the strategy what the next move it would make is.
	 * 
	 * @param ticTacToeModel the current state of the Connect Four game
	 * @return an integer representing the column of the next move
	 */
	public int nextMove(ConnectFourModel connectFourModel) {      
	    return AIStrategy.desiredMove(connectFourModel);
	}

}