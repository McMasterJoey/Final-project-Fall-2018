package controller;
/**
 * 
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public class ThemeDataBasePair implements Comparable<ThemeDataBasePair> {
	Integer themeid = 0;
	boolean isUser = false;
	String themename = "";
	String filepath = "";
	public ThemeDataBasePair(int themeid, boolean isUser, String themename, String filepath) {
		this.themeid = themeid;
		this.isUser = isUser;
		this.themename = themename;
		this.filepath = filepath;
	}
	public Integer getThemeid() {
		return this.themeid;
	}
	public boolean geIsUser() {
		return this.isUser;
	}
	public String getThemeName() {
		return this.themename;
	}
	public String getFilePath() {
		return this.filepath;
	}
	@Override
	public int compareTo(ThemeDataBasePair o) {
		return this.themeid.compareTo(o.getThemeid());
	}
}
