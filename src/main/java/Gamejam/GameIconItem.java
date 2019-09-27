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
	public String getName() {
		return gamename;
	}
	public String getIconFilePath() {
		return iconfilepath;
	}

	public int getGameID() {
		return gameid;
	}
}
