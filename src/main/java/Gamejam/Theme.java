package Gamejam;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents a collection of Theme pair objects and other data that compose a theme.
 * @author Joey McMaster
 */
public class Theme {
	// Constants, Define what each section of the internal array mean. Can be used from anywhere.
	public static final int BUTTON_BACKGROUNDS = 0;
	public static final int LEFT_PANEL_UPPER_TEXT_COLOR = 1;
	public static final int UPPER_BARS_BACKGROUND = 2;
	public static final int MIDDLE_AREA_BACKGROUND = 3;
	public static final int LEFT_PANEL_BACKGROUND = 4;
	public static final int NEW_ACCOUNT_BACK_LOGOUT_BUTTON_TEXT = 5;
	public static final int LEFT_PANEL_LOWER_TEXT_COLOR = 6;
	public static final int LOGIN_BUTTON_TEXT_COLOR = 7;
	public static final int DEFAULT_SETTINGS_TEXT_COLOR = 8;
	
	private String name;
	private Themepair[] themedata;
	private ArrayList<Image> themeimg;
	private Image icon; 
	/**
	 * The main constructor.
	 * @param name The name of the theme.
	 * @param iconpath The path of the icon used to present the theme in the menu
	 */
	public Theme(String name, String iconpath) {
		this.name = name;
		this.themedata = new Themepair[10];
		this.icon = new Image(iconpath);
		this.themeimg = new ArrayList<Image>();
	}
	/**
	 * Fetches the icon used by this theme
	 * @return The icon in Imageview form of the theme.
	 */
	public ImageView getIcon() {
		return new ImageView(icon);
	}
	/**
	 * Adds a new image to the stored images stored for use by the theme setter.
	 * @param path The file path to load the image.
	 */
	public void addNewImage(String path) {
		this.themeimg.add(new Image(getClass().getResourceAsStream(path)));
	}
	/**
	 * Fetches a stored image and returns it so its viewable.
	 * @param index The place within the internal array where the image is stored.
	 * @return The ImageView representation of the image.
	 */
	public ImageView getImage(int index) {
		return new ImageView(this.themeimg.get(index));
	}
	/**
	 * Sets the data within the specific section of the theme data.
	 * @param index The index of the element of theme data. Should correspond to the GamejamMainScreen indexing system. 
	 * @param data A Themepair object that represents the styling for that section.
	 */
	public void setThemeData(int index, Themepair data) {
		this.themedata[index] = data;
	}
	/**
	 * Fetches the internal data contained within the theme.
	 * @return The internal data.
	 */
	public Themepair[] getTheme() {
		return this.themedata;
	}
}