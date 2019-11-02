package Gamejam;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.SanityCheckFailedException;

/**
 * Represents a collection of Theme pair objects and other data that compose a theme.
 * @author Joey McMaster
 */
public class Theme 
{
	
	private String name;
	private ThemePair[] themedata;
	private ArrayList<Image> themeimg;
	private Image icon;
	/**
	 * The main constructor.
	 * @param name The name of the theme.
	 * @param arraysize The size of the internal array used for storing theme pairs.
	 */
	public Theme(String name, int arraysize) 
	{
		this.name = name;
		this.themedata = new ThemePair[arraysize];
		this.themeimg = new ArrayList<Image>();
	}
	public void setIcon(Image icon)
	{
		this.icon = icon;
	}
	/**
	 * Fetches the icon used by this theme
	 * @return The icon in Imageview form of the theme.
	 */
	public ImageView getIcon() 
	{
		return new ImageView(icon);
	}
	/**
	 * Adds a new image to the stored images stored for use by the theme setter.
	 * @param path The file path to load the image.
	 */
	public void addNewImage(String path) 
	{
		this.themeimg.add(new Image(getClass().getResourceAsStream(path)));
	}
	/**
	 * Fetches a stored image and returns it so its viewable.
	 * @param index The place within the internal array where the image is stored.
	 * @return The ImageView representation of the image.
	 */
	public ImageView getImage(int index) 
	{
		return new ImageView(this.themeimg.get(index));
	}
	/**
	 * Sets the data within the specific section of the theme data.
	 * @param index The index of the element of theme data. Should correspond to the GamejamMainScreen indexing system. 
	 * @param data A Themepair object that represents the styling for that section.
	 */
	public void setThemeData(int index, ThemePair data) 
	{
		if (data == null)
		{
			throw new SanityCheckFailedException("Inserted null data into a theme!");
		}
		this.themedata[index] = data;
	}
	/**
	 * Fetches the internal data contained within the theme.
	 * @return The internal data.
	 */
	public ThemePair[] getTheme() 
	{
		return this.themedata;
	}
	public String dumpThemeToCode() 
	{
		return null;
	}
}