package Gamejam;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import model.SanityCheckFailedException;

/**
 * Represents a collection of Theme pair objects and other data that compose a theme.
 * @author Joey McMaster
 */
public class Theme
{
	protected String name;
	protected ThemePair[] themedata;
	protected ArrayList<Image> themeimg;
	protected ArrayList<String> themeimgpaths;
	protected String iconpath;
	protected Image icon;
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
		this.themeimgpaths = new ArrayList<String>();
	}
	public void setIcon(String path)
	{
		this.iconpath = path;
		this.icon = new Image(getClass().getResourceAsStream(path));
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
		this.themeimgpaths.add(path);
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
	/**
	 * @return
	 */
	public boolean isStatic()
	{
		return true;
	}
	public void generateTheme()
	{
		// Does nothing, is only implemented by the Dynamic Theme class
	}
	public void generateTheme(String path1, String path2)
	{
		// Does nothing, is only implemented by the Dynamic Theme class
	}
	public void setNewImage(int index, String path) 
	{
		this.themeimgpaths.set(index,path);
		this.themeimg.set(index,new Image(getClass().getResourceAsStream(path)));
	}
	public ThemeSerializable dumpCoreData()
	{
		
		ArrayList<ThemePair> pairs = new ArrayList<ThemePair>();
		for(int x = 0; x < this.themedata.length; x++) 
		{
			pairs.add(this.themedata[x]);
		}
		ThemeSerializable retval =  new ThemeSerializable(name,pairs, this.themeimgpaths);
		if (this.iconpath != null && !this.iconpath.equals(""))
		{
			retval.setIconPath(this.iconpath);
		}
		return retval;
	}
	public void importCoreData(ThemeSerializable data)
	{
		if (data.isDynamic()) 
		{
			throw new SanityCheckFailedException("Expected Static Theme Data, got Dyanmic Theme data");
		}
		ArrayList<ThemePair> pairs = data.getThemePairs();
		if (pairs.size() != this.themedata.length) 
		{
			throw new SanityCheckFailedException("Static Theme Data theme pair count mismatch. Data is invalid!");
		}
		this.themeimgpaths = data.getFilePaths();
		for(int x = 0; x < data.getFilePaths().size(); x++)
		{
			if (this.themeimg.size() >= x) {
				this.themeimg.add(new Image(getClass().getResourceAsStream(this.themeimgpaths.get(x))));
			} else {
				this.themeimg.set(x,new Image(getClass().getResourceAsStream(this.themeimgpaths.get(x))));
			}
		}
		
		for(int x = 0; x < this.themedata.length; x++)
		{
			this.themedata[x] = pairs.get(x);
		}
	}
}