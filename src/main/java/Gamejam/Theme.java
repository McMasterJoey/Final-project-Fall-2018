package Gamejam;

/**
 * 
 * @author Joey McMaster
 *
 */
public class Theme {
	private String name;
	private Themepair[] themedata;
	
	/**
	 * 
	 * @param name
	 */
	public Theme(String name) {
		this.name = name;
		themedata = new Themepair[10];
	}
	/**
	 * 
	 * @param index
	 * @param data
	 */
	public void setThemeData(int index, Themepair data) {
		this.themedata[index] = data;
	}
	/**
	 * 
	 * @return
	 */
	public Themepair[] getTheme() {
		return this.themedata;
	}
}