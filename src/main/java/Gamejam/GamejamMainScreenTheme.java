package Gamejam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import controller.AccountManager;
import controller.ThemeDataBasePair;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import model.SanityCheckFailedException;

/**
 * @author Joey McMaster
 *
 */
public class GamejamMainScreenTheme 
{
	private ArrayList<Theme> themes;
	private ArrayList<RegionPair> regions;
	private ArrayList<PriorityPair> preInit;
	private ThemeCreator basicthemecreator;
	private boolean doneAddingRegions = false;
	private ArrayList<String> imagecache;
	private ArrayList<Theme> playerCustomThemes;
	private ArrayList<ThemeDataBasePair> databasethemes;
	private ThemeDynamic temptheme;
	public GamejamMainScreenTheme() 
	{
		this.themes = new ArrayList<Theme>(25);
		this.playerCustomThemes = new ArrayList<Theme>(25);
		this.regions = new ArrayList<RegionPair>(50);
		this.preInit = new ArrayList<PriorityPair>(50);
		this.basicthemecreator = new ThemeCreator(this);
		addRegion(228.001, this.basicthemecreator, "Theme Menu Basic Theme Creator", new ThemeRegionProp(ThemeRegionProp.BORDERPANE, ThemeRegionProp.LOC_MI_TM, ThemeRegionProp.INT_REG));
		cacheImages();
		this.databasethemes = AccountManager.getInstance().getAllThemeData();
		
	}
	public void addCustomTheme(ThemeDynamic theme) 
	{
		this.playerCustomThemes.add(theme);
	}
	public ThemeDynamic loadCustomDynamicTheme(String themename, String filepath)
	{
		Gamejam.DPrint("Loaded theme: " + themename + " from disk in main object!");
		ThemeDynamic t = new ThemeDynamic("d", this.regions.size(), this.regions,this);
		if (filepath == null) {
			return null; // Tried to load a non-existant theme
		}
		try {
			File file = new File(filepath);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				ThemeSerializable tse = (ThemeSerializable) ois.readObject();
				t.importCoreData(tse);
				ois.close();
			}
		} catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		this.playerCustomThemes.add(t);
		return t;
	}
	private void cacheImages()
	{
		this.imagecache = new ArrayList<String>();
		this.imagecache.add("/themeDefaultThemeMenuIcon.png");
		this.imagecache.add("/themeNightThemeMenuIcon.png");
		this.imagecache.add("/themeUSAThemeMenuIcon.png");
		
		Theme t1 = new Theme("Default Theme", 10);
		t1.setIcon(this.imagecache.get(0));
		t1.addNewImage("/usersettingsbuttonbackground.png");
		t1.addNewImage("/usersettingsbuttonbackground.png");
		this.themes.add(t1);
		Theme t2 = new Theme("Night Theme", 10);
		t2.setIcon(this.imagecache.get(0));
		t2.addNewImage("/usersettingsbuttonbackgroundnighttheme.png");
		t2.addNewImage("/usersettingsbuttonbackgroundnighttheme.png");
		this.themes.add(t2);
		Theme t3 = new Theme("USA Theme", 10);
		t3.setIcon(this.imagecache.get(0));
		t3.addNewImage("/usersettingsbuttonbackground.png");
		t3.addNewImage("/usersettingsbuttonbackground.png");
		this.themes.add(t3);
	}
	public ThemeCreator getBasicThemeCreator()
	{
		return this.basicthemecreator;
	}
	public ImageView getThemeIcon(int themeid)
	{
		return new ImageView(this.imagecache.get(themeid));
	}
	public ImageView getThemeImages(int themeid, int index)
	{
		return this.themes.get(themeid).getImage(index);
	}
	public void doneAddingRegions()
	{
		if (!this.doneAddingRegions)
		{
			this.doneAddingRegions = true;
			// Sorting this will adjust the locations of elements
			// so they are arranged by priority
			Collections.sort(this.preInit);
			
			// Dump sorted Region Pairs into the Primary Storage array for usage.
			for(int x = 0; x < this.preInit.size(); x++)
			{
				this.regions.add(this.preInit.get(x).getRegionPair());
				this.regions.get(this.regions.size() - 1).setIndex(x);
			}
			this.basicthemecreator.finishConstructing();
			
			this.playerCustomThemes.add(generateDefaultTheme());
			this.playerCustomThemes.add(generateDefaultTheme());
			setUpThemes();
			updateTheme(0);
			
			for(int x = 0; x < this.databasethemes.size(); x++) 
			{
				loadCustomDynamicTheme(this.databasethemes.get(x).getThemeName(),this.databasethemes.get(x).getFilePath());
			}
		}
	}
	public void setThempTheme(ThemeDynamic theme)
	{
		this.temptheme = theme;
		this.temptheme.generateTheme();
		this.updateTheme(Integer.MIN_VALUE);
	}
	public Theme getBufferTheme()
	{
		return this.playerCustomThemes.get(0);
	}
	public ArrayList<RegionPair> getAllRegions()
	{
		return this.regions;
	}
	public int getRegionCount() 
	{
		if (!this.doneAddingRegions)
		{
			throw new SanityCheckFailedException("Can't get region count when we're still adding regions to the object.");
		}
		return this.regions.size();
	}
	public void addRegion(double priority, Region r, String description, ThemeRegionProp properties) 
	{
		if (this.doneAddingRegions)
		{
			throw new SanityCheckFailedException("I'm done adding regions yet there was an attempt to add another.");
		}
		this.preInit.add(new PriorityPair(priority, new RegionPair(r, description, properties)));
	}
	/**
	 * Fetches the theme of the given themeid.
	 * @param themeid The id where the theme object is stored.
	 * @return The theme object that was requested.
	 */
	public Theme getTheme(int themeid) 
	{
		if (!this.doneAddingRegions)
		{
			throw new SanityCheckFailedException("Can't get Themes while we're still adding regions to the object.");
		}
		if (themeid == Integer.MIN_VALUE) 
		{
			return this.temptheme;
		}
		else if (themeid < 0)
		{
			themeid++;
			themeid = Math.abs(themeid);
			return this.playerCustomThemes.get(themeid);
		}
		else 
		{
			return this.themes.get(themeid);
		}
	}
	/**
	 * Resets the Custom Theme to the default theme.
	 */
	public void resetCustomTheme(int id) 
	{
		if (!this.doneAddingRegions)
		{
			throw new SanityCheckFailedException("Can't reset themes when we're still adding regions to the object.");
		}
		this.playerCustomThemes.set(id, generateDefaultTheme());
	}
	/**
	 * Updates the Theme of the Main GUI
	 * @param themeid The id of the theme to be set. -1 is the Custom theme.
	 */
	public void updateTheme(int themeid) 
	{
		if (!this.doneAddingRegions)
		{
			throw new SanityCheckFailedException("Can't update theme while we're still adding regions to the object.");
		}
		ThemePair[] themedata;
		boolean isCustomTheme = false;
		boolean themptheme = false;
		if (themeid < 0)
		{
			if (themeid == Integer.MIN_VALUE) 
			{
				themptheme = true;
				Gamejam.DPrint("[DEBUG]: Temp theme being set!");
				themedata = this.temptheme.getTheme();
			}
			else 
			{
				themeid++;
				themeid = Math.abs(themeid);
				Gamejam.DPrint("[DEBUG]: Custom theme with a custom theme id of " + themeid + " being set!");
				themedata = this.playerCustomThemes.get(themeid).getTheme();
				isCustomTheme = true;
			}
		}
		else 
		{
			themedata = getTheme(themeid).getTheme();
		}
		
		int themeimageid = 0;
		HashSet<Integer> check2 = new HashSet<Integer>();
		check2.add(26);
		check2.add(27);
		
		for(int x = 0; x < this.regions.size(); x++)
		{
			if (this.regions.get(x) == null || this.regions.get(x).getRegion() == null) 
			{
				Gamejam.DPrint("[ERROR]: Region at index " + x + " was null!");
				continue;
			}
			
			if (!this.regions.get(x).getProperties().isTrue(29))
			{
				if (themedata[x] == null)
				{
					Gamejam.DPrint("[ERROR]: Region at index " + x + " had a null theme pair!");
					continue;
				}
				this.regions.get(x).getRegion().setBackground(themedata[x].getBackground());
				this.regions.get(x).getRegion().setBorder(themedata[x].getBorder());
				this.regions.get(x).getRegion().setStyle(themedata[x].getCSS());
			}
			
			if (this.regions.get(x).getProperties().isAllTrue(check2)) 
			{
				if (themedata[x] == null)
				{
					Gamejam.DPrint("[ERROR]: Region at index " + x + " had a null theme pair!");
					continue;
				}
				// All regions that contain text that can be colored are Labeled controls!
				Labeled l = (Labeled) this.regions.get(x).getRegion();
				l.setTextFill(themedata[x].getColor());
			}
			
			if (this.regions.get(x).getProperties().isTrue(28))
			{
				Button b = (Button) this.regions.get(x).getRegion();
				if (isCustomTheme)
				{
					b.setGraphic(getCustomTheme(themeid).getImage(themeimageid));
				}
				else if (themptheme)
				{
					b.setGraphic(this.temptheme.getImage(themeimageid));
				}
				else 
				{
					b.setGraphic(getTheme(themeid).getImage(themeimageid));
				}
			}
		}
	}
	/**
	 * @param id
	 * @param index
	 * @param data
	 */
	public void setCustomThemeData(int id, int index, ThemePair data) 
	{
		if (!this.doneAddingRegions)
		{
			throw new SanityCheckFailedException("Can't set custom theme data when we're still adding regions to the object.");
		}
		this.playerCustomThemes.get(id).setThemeData(index, data);
	}
	/**
	 * Fetches the Stored Custom Theme
	 * @return The Custom theme data
	 */
	public Theme getCustomTheme(int id) 
	{
		if (!this.doneAddingRegions)
		{
			throw new SanityCheckFailedException("Can't get custom theme when we're still adding regions to the object.");
		}
		return this.playerCustomThemes.get(id);
	}
	private void setUpThemes() 
	{
		if (!this.doneAddingRegions)
		{
			throw new SanityCheckFailedException("Can't setup themes when we're still adding regions to the object.");
		}
		themes.set(0, generateDefaultTheme());
		themes.set(1,ThemeGenerate.createThemeGenerated_Night());
		themes.set(2, ThemeGenerate.createThemeGenerated_USA());
	}
	public Theme generateDefaultTheme() 
	{
		Theme t1 = new Theme("Default Theme", getRegionCount());
		t1.setIcon(this.imagecache.get(0));
		t1.addNewImage("/usersettingsbuttonbackground.png");
		t1.addNewImage("/usersettingsbuttonbackground.png");
		
		HashSet<Integer> check3 = new HashSet<Integer>();
		check3.add(26);
		check3.add(27);
		
		for(int x = 0; x < this.regions.size(); x++) 
		{
			ThemePair p;
			if (this.regions.get(x).getProperties().isTrue(37))
			{
				p = new ThemePair(linGrdSimpleBackgroundSetUp(Color.LIGHTGRAY,Color.DARKGRAY, false), quickBorderSetup(Color.DARKBLUE), Color.BLACK);
				t1.setThemeData(x, p);
			}
			else if (this.regions.get(x).getProperties().isTrue(13))
			{
				p = new ThemePair(transparentBackground(), noBorder());
				t1.setThemeData(x, p);
			}
			else if (this.regions.get(x).getProperties().isTrue(12))
			{
				p = new ThemePair(linGrdSimpleBackgroundSetUp(Color.LIGHTGRAY,Color.GRAY, false), noBorder());
				t1.setThemeData(x, p);
			}
			else if (this.regions.get(x).getProperties().isAllTrue(check3))
			{
				p = new ThemePair(linGrdSimpleBackgroundSetUp(Color.LIGHTGRAY,Color.GRAY, false), noBorder(), Color.RED);
				t1.setThemeData(x, p);
			}
			else
			{
				p = new ThemePair(linGrdSimpleBackgroundSetUp(Color.DARKGRAY,Color.GRAY, true), noBorder());
				t1.setThemeData(x, p);
			}
		
		}
		return t1;
	}
	public void addNewCustomTheme(Theme t)
	{
		this.playerCustomThemes.add(t);
	}
	public void displayCustomTheme(int id) 
	{
		Gamejam.DPrint("SCT: id = " + id);
		this.updateTheme((id + 1) * -1); 
	}
	public void setCustomTheme(int id, Theme t)
	{
		Gamejam.DPrint("SCT: id = " + id);
		this.playerCustomThemes.set(id, t);
		this.playerCustomThemes.get(id).generateTheme();
		this.updateTheme((id + 1) * -1); 
	}
	public void removeAllCustomThemes()
	{
		this.playerCustomThemes.clear();
	}
	
	public static Background transparentBackground()
	{
		return new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, new Insets(0)));
	}
	public static LinearGradient linGrdSimpleSetup(Paint start, Paint end, boolean sidetoside)
	{
		Stop[] stops = new Stop[] 
		{ 
			new Stop(0, (Color) start), new Stop(1,(Color) end)  
		};
		LinearGradient grad;
		if (sidetoside) 
		{
			grad = new LinearGradient(0,0,1,0,true, CycleMethod.NO_CYCLE,stops);
		} 
		else 
		{
			grad = new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE,stops);
		}
		return grad;
	}
	public static Background linGrdSimpleBackgroundSetUp(Paint start, Paint end, boolean sidetoside) 
	{
		Stop[] stops = new Stop[] 
		{ 
				new Stop(0, (Color) start), new Stop(1,(Color) end) 
		};
		LinearGradient grad;
		if (sidetoside) 
		{
			grad = new LinearGradient(0,0,1,0,true, CycleMethod.NO_CYCLE,stops);
		} 
		else 
		{
			grad = new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE,stops);
		}
		return new Background(new BackgroundFill(grad, CornerRadii.EMPTY, new Insets(0)));
	}
	/**
	 * Generates a simple border of width 2 with a solid color.
	 * @param color The color of the border.
	 * @return The border object that is a solid with a width of 2 of the inputed color.
	 */
	public static Border quickBorderSetup(Paint color) 
	{
		return new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)));
	}

	public static Border noBorder()
	{
		return new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0)));
	}
	/**
	 * Generates a simple background with a static single color 
	 * @param color The color object of the desired color.
	 * @return A background object of the inputed color
	 */
	public static Background solidBackgroundSetup(Paint color) 
	{
		return new Background(new BackgroundFill(color, CornerRadii.EMPTY, new Insets(0)));
	}
	public static Border SimpleIntBorder(Paint color, String style, int widths)
	{
		BorderStrokeStyle stylee;
		if (style.equals("Solid"))
		{
			stylee = BorderStrokeStyle.SOLID;
		} 
		else if (style.equals("Dashed"))
		{
			stylee = BorderStrokeStyle.DASHED;
		}
		else
		{
			stylee = BorderStrokeStyle.DOTTED;
		}
		return new Border(new BorderStroke(color, stylee, CornerRadii.EMPTY, new BorderWidths(widths)));
	}
	/**
	 * Generates a border.
	 * @param color The color of the border
	 * @param style The style of the border
	 * @param widths The widths of the border
	 * @return The resulting border.
	 */
	public static Border simpleBorder(Paint color, BorderStrokeStyle style, int widths)
	{
		return new Border(new BorderStroke(color, style, CornerRadii.EMPTY, new BorderWidths(widths)));
	}
	/**
	 * Generates a border.
	 * @param color The color of the border
	 * @param style The style of the border
	 * @param widths The widths of the border
	 * @return The resulting border.
	 */
	public static Border simpleBorder(Paint color, BorderStrokeStyle style, double widths)
	{
		return new Border(new BorderStroke(color, style, CornerRadii.EMPTY, new BorderWidths(widths)));
	}
	/**
	 * Generates a simple background with a static single color 
	 * @param r The red value (0 - 255)
	 * @param g The Green value (0 - 255)
	 * @param b The Blue value (0 - 255)
	 * @return A background object of the inputed color
	 */
	public static Background solidBackgroundSetup(int r, int g, int b) 
	{
		double red = (double) r;
		double green = (double) g;
		double blue = (double) b;
		return new Background(new BackgroundFill(Color.color(red / 255.0, green / 255.0, blue / 255.0),
				CornerRadii.EMPTY, new Insets(0)));
	}
	public static Color colorSetUpRBG(int r, int g, int b)
	{
		double red = (double) r;
		double green = (double) g;
		double blue = (double) b;
		return new Color(red / 255, green / 255, blue / 255, 1);
	}
	/**
	 * Acts as a temporary object to be used when initing the object.
	 * Assists in the placement of RegionPair objects within the extablished region array.
	 * @author Joey McMaster
	 *
	 */
	private class PriorityPair implements Comparable<PriorityPair>
	{
		private Double pri;
		private RegionPair r;
		public PriorityPair(double pri, RegionPair r)
		{
			this.pri = pri;
			this.r = r;
		}
		/**
		 * Gets the region pair associated with this object.
		 * @return The region pair that stored within the object.
		 */
		public RegionPair getRegionPair()
		{
			return this.r;
		}
		/**
		 * Getter for priority
		 * @return Fetches the priority of this object.
		 */
		public Double getPriority() 
		{
			return this.pri;
		}
		/**
		 * Implements compareTo to allow for Collections to sort this item.
		 */
		public int compareTo(PriorityPair arg0)
		{
			return this.getPriority().compareTo(arg0.getPriority());
		}
	}
}
