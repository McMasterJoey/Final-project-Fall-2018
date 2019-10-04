package model;
/**
 * A common interface for all games to implement to enable common features.
 * @author Joey McMaster
 *
 */
public interface GameJamGameInterface {
	// Takes a path, loads the save game linked
	// Sets the state of that save game to current state of game.
	public boolean loadSaveGame(String filepath);
	// Takes a path, saves the current game state to linked path.
	public boolean saveGame(String filepath);
	// When applicable, pauses the game. 
	public boolean pauseGame();
	// When applicable, unpauses the game if it was paused.
	public boolean unPauseGame();
	// Starts a new game, overrides current save game.
	public boolean newGame();
	
	// All methods return true of false
	// Returning true indicates the action was a success
	// Returning false indicates the action failed or is unimplemented.
}
