package model;

import java.io.Serializable;
import java.util.Observable;

import ticTacToe.EasyAI;
import ticTacToe.IntermediateAI;
/**
 * 
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public abstract class GameModel extends Observable implements Serializable{
	private static final long serialVersionUID = 1L;

	public abstract void setAIStrategy(IntermediateAI intermediateAI);

	public abstract void setAIStrategy(EasyAI easyAI);
	
	

}
