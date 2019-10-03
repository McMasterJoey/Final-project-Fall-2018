package Gamejam;
/**
 * Public class, Holds needed data to produce each button that loads each game.
 * 
 * @author Joey McMaster
 *
 */
public class GameIconItem {
	String gamename;
	String iconfilepath;
	int gameid;
	public GameIconItem(String name, String iconfilepath, int gameid) {
		this.gamename = name;
		this.iconfilepath = iconfilepath;
		this.gameid = gameid;
	}
	/**
	 * Fets the game of the game icon item.
	 * @return
	 */
	public String getName() {
		return gamename;
	}
	/**
	 * Gets the icon file path associated with this object.
	 * @return
	 */
	public String getIconFilePath() {
		return iconfilepath;
	}
	/**
	 * Gets the gameid associated with this object.
	 * @return
	 */
	public int getGameID() {
		return gameid;
	}
	/**
	 * Sets the game id Field.
	 * @param gameid The id to be set to.
	 */
	public void setGameID(int gameid) {
		this.gameid = gameid;
	}
}
