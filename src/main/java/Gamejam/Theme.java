package Gamejam;

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
	/**
	 * Dumps this theme into a form that can put directly into project code.
	 * @return The hard coded version of this theme.
	 */
	public String dumpThemeToCode() 
	{
		String retval = "public static Theme createThemeGenerated() {\n";
		retval += "    Theme retval = new Theme(" + "\"" + this.name + "\"" +"," + this.themedata.length + ");\n";
		for(int x = 0; x < this.themedata.length;x++)
		{
			retval += "    " + dumpBorder(this.themedata[x],x) + "\n";
			retval += "    " + dumpBackground(this.themedata[x],x) + "\n";
			retval += "    Paint c" + x + " = " + paintToGenString(this.themedata[x].getColor()) + ";\n";
			retval += "    ThemePair p" + x + " = new ThemePair(bg" + x + ",bo" + x + ",c" +  x + ");\n";
			retval += "    retval.setThemeData("+ x + ", p" + x + ");\n";
		}
		retval += "    return retval;\n";
		retval += "}";
		return retval;
	}
	/**
	 * Takes a paint object that is either a Color or LinearGradient. Dumps it as a string so it can be made via code.
	 * @param p The paint object
	 * @return A string in the form of java code.
	 */
	public static String paintToGenString(Paint p)
	{
		if (p instanceof Color)
		{
			Color c = (Color) p;
			return "new Color(" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ", " + c.getOpacity() + ")";
		}
		else if (p instanceof LinearGradient)
		{
			LinearGradient c = (LinearGradient) p;
		
			//new Stop[] { new Stop(0, c.getStops().get(0).getColor()), new Stop(1,  c.getStops().get(1).getColor()) };
			//LinearGradient g = new LinearGradient(0,0,1,1,true,CycleMethod.NO_CYCLE,new Stop[] { new Stop(0, c.getStops().get(0).getColor()), new Stop(1,  c.getStops().get(1).getColor()) });
			String stops = "new Stop[] { ";
			for(int x = 0; x < c.getStops().size(); x++)
			{
				if (x != 0)
				{
					stops += ",";
				}
				stops += "new Stop(" + x + "," + paintToGenString(c.getStops().get(x).getColor()) + ")";
			}
			stops += "}";
			return "new LinearGradient(" + c.getStartX() + "," + c.getStartY() + "," + c.getEndX() + "," + c.getEndY() + ",true,CycleMethod.NO_CYCLE," + stops + ")";
		}
		return null;
	}
	protected String dumpBackground(ThemePair p, int x)
	{
		String color = paintToGenString(p.getBackground().getFills().get(0).getFill());
		return "Background bg" + x + " = new Background(new BackgroundFill(" + color + ",CornerRadii.EMPTY, new Insets(0)));";
	}
	protected String dumpBorder(ThemePair p, int x)
	{
		String color = paintToGenString(p.getBorder().getStrokes().get(0).getTopStroke());
		String style1 = _dumpBorderUtil(p.getBorder().getStrokes().get(0).getTopStyle());
		String style3 = _dumpBorderUtil(p.getBorder().getStrokes().get(0).getBottomStyle());
		String style4 = _dumpBorderUtil(p.getBorder().getStrokes().get(0).getLeftStyle());
		String style2 = _dumpBorderUtil(p.getBorder().getStrokes().get(0).getRightStyle());
		BorderStrokeStyle s = p.getBorder().getStrokes().get(0).getTopStyle();
		
		return "Border bo" + x + " = new Border(new BorderStroke(" + color + "," + color + "," + color + "," + color + "," + style1 + "," + style2 + "," + style3 + "," + style4 + "," + "CornerRadii.EMPTY, new BorderWidths(" + p.getBorder().getStrokes().get(0).getWidths().getTop() + "), new Insets(0)));";
	}
	protected String _dumpBorderUtil(BorderStrokeStyle s)
	{
		String style = "BorderStrokeStyle.SOLID";
		if (s.equals(BorderStrokeStyle.NONE)) 
		{
			style = "BorderStrokeStyle.NONE";
		}
		else if (s.equals(BorderStrokeStyle.DASHED)) 
		{
			style = "BorderStrokeStyle.DASHED";
		}
		else if (s.equals(BorderStrokeStyle.DOTTED)) 
		{
			style = "BorderStrokeStyle.DOTTED";
		}
		return style;
	}
}