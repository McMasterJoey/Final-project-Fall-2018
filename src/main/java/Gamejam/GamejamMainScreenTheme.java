package Gamejam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
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
import javafx.scene.paint.Stop;
import model.SanityCheckFailedException;

/**
 * @author Joey McMaster
 *
 */
public class GamejamMainScreenTheme 
{
	private ArrayList<Theme> themes;
	private Theme customtheme;
	private ArrayList<RegionPair> regions;
	private ArrayList<PriorityPair> preInit;
	private ThemeCreator themecreator;
	private boolean doneAddingRegions = false;
	public GamejamMainScreenTheme() 
	{
		this.themes = new ArrayList<Theme>(25);
		this.regions = new ArrayList<RegionPair>(50);
		this.preInit = new ArrayList<PriorityPair>(50);
		this.themecreator = new ThemeCreator();
		addRegion(228, this.themecreator, "Theme Menu Theme Creator", new ThemeRegionProp(ThemeRegionProp.HBOX));
	}
	public ThemeCreator getThemeCreator()
	{
		return this.themecreator;
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
			resetCustomTheme();
			setUpThemes();
		}
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
		if (themeid < 0 || themeid >= this.themes.size() || !this.doneAddingRegions) 
		{
			if (!this.doneAddingRegions)
			{
				throw new SanityCheckFailedException("Can't get Themes while we're still adding regions to the object.");
			}
			throw new SanityCheckFailedException("Invalid themeid!");
		}
		return this.themes.get(themeid);
	}
	/**
	 * Fetches a specific section of data from within a theme.
	 * @param themeid The id where the theme object is stored.
	 * @param elementid The element within that theme object the desired themepair is.
	 * @return The desired themepair.
	 */
	public Themepair getThemeData(int themeid, int elementid) 
	{
		if (themeid < 0 || themeid >= this.themes.size() || !this.doneAddingRegions) 
		{
			if (!this.doneAddingRegions)
			{
				throw new SanityCheckFailedException("Can't get Theme data while we're still adding regions to the object.");
			}
			throw new SanityCheckFailedException("Invalid themeid!");
		}
		return this.themes.get(themeid).getTheme()[elementid];
	}
	/**
	 * Resets the Custom Theme to the default theme.
	 */
	public void resetCustomTheme() 
	{
		if (!this.doneAddingRegions)
		{
			throw new SanityCheckFailedException("Can't reset themes when we're still adding regions to the object.");
		}
		this.customtheme = new Theme("Custom Theme","/themeDefaultThemeMenuIcon.png");
		this.customtheme.setThemeData(0, new Themepair(linGrdSimpleBackgroundSetUp(Color.LIGHTGRAY,Color.DARKGRAY, false), quickBorderSetup(Color.DARKBLUE)));
		this.customtheme.setThemeData(1, new Themepair(Color.BLACK));
		this.customtheme.setThemeData(2, new Themepair(linGrdSimpleBackgroundSetUp(Color.LIGHTGRAY,Color.BEIGE, true),
				quickBorderSetup(RegionColors.getColor(RegionColors.DEFAULT_BACKGROUND))));
		this.customtheme.setThemeData(3, new Themepair(linGrdSimpleBackgroundSetUp(Color.LIGHTGRAY,Color.GRAY, false),
				quickBorderSetup(RegionColors.getColor(RegionColors.DEFAULT_BACKGROUND))));
		this.customtheme.setThemeData(4, new Themepair(linGrdSimpleBackgroundSetUp(Color.LIGHTGRAY,Color.GRAY, false),
				quickBorderSetup(Color.BLACK)));
		this.customtheme.setThemeData(5, new Themepair(Color.BLACK));
		this.customtheme.setThemeData(6, new Themepair(Color.BLACK));
		this.customtheme.setThemeData(7, new Themepair(Color.RED));
		this.customtheme.setThemeData(8, new Themepair(Color.BLACK));
		this.customtheme.addNewImage("/usersettingsbuttonbackground.png");
		this.customtheme.addNewImage("/usersettingsbuttonbackground.png");
	}
	/**
	 * Updates the Theme of the Main GUI
	 * @param themeid The id of the theme to be set. 
	 */
	public void updateTheme(int themeid) 
	{
		if (!this.doneAddingRegions)
		{
			throw new SanityCheckFailedException("Can't update theme while we're still adding regions to the object.");
		}
	}
	/**
	 * 
	 * @param index
	 * @param data
	 */
	public void setCustomThemeData(int index, Themepair data) 
	{
		if (!this.doneAddingRegions)
		{
			throw new SanityCheckFailedException("Can't set custom theme data when we're still adding regions to the object.");
		}
		this.customtheme.setThemeData(index, data);
	}
	/**
	 * Fetches the Stored Custom Theme
	 * @return The Custom theme data
	 */
	public Theme getCustomTheme() 
	{
		if (!this.doneAddingRegions)
		{
			throw new SanityCheckFailedException("Can't get custom theme when we're still adding regions to the object.");
		}
		return this.customtheme;
	}
	/**
	 * 0 = Button Backgrounds 
	 * 1 = Left Panel Upper Text color 
	 * 2 = Upper bars background 
	 * 3 = Middle Area background / Overall background 
	 * 4 = Left Panel background 
	 * 5 = New account/Back/Logout button text 
	 * 6 = Left Panel Lower text color 
	 * 7 = Login button text color 
	 * 8 = Default Settings Text color
	 */
	private void setUpThemes() 
	{
		if (!this.doneAddingRegions)
		{
			throw new SanityCheckFailedException("Can't setup themes when we're still adding regions to the object.");
		}
		// Default Theme:
		Theme t1 = new Theme("Default Theme","/themeDefaultThemeMenuIcon.png");
		t1.setThemeData(0, new Themepair(linGrdSimpleBackgroundSetUp(Color.LIGHTGRAY,Color.DARKGRAY, false), quickBorderSetup(Color.DARKBLUE)));
		t1.setThemeData(1, new Themepair(Color.BLACK));
		t1.setThemeData(2, new Themepair(linGrdSimpleBackgroundSetUp(Color.LIGHTGRAY,Color.BEIGE, true),
				quickBorderSetup(RegionColors.getColor(RegionColors.DEFAULT_BACKGROUND))));
		t1.setThemeData(3, new Themepair(linGrdSimpleBackgroundSetUp(Color.LIGHTGRAY,Color.GRAY, false),
				quickBorderSetup(RegionColors.getColor(RegionColors.DEFAULT_BACKGROUND))));
		t1.setThemeData(4, new Themepair(linGrdSimpleBackgroundSetUp(Color.LIGHTGRAY,Color.GRAY, false),
				quickBorderSetup(Color.BLACK)));
		t1.setThemeData(5, new Themepair(Color.BLACK));
		t1.setThemeData(6, new Themepair(Color.BLACK));
		t1.setThemeData(7, new Themepair(Color.RED));
		t1.setThemeData(8, new Themepair(Color.BLACK));
		t1.addNewImage("/usersettingsbuttonbackground.png");
		t1.addNewImage("/usersettingsbuttonbackground.png");
		themes.add(t1);
		
		Theme t2 = new Theme("Night Theme","/themeNightThemeMenuIcon.png");
		t2.setThemeData(0, new Themepair(solidBackgroundSetup(Color.BLACK), quickBorderSetup(Color.ORANGE)));
		t2.setThemeData(1, new Themepair(Color.WHITE));
		t2.setThemeData(2, new Themepair(solidBackgroundSetup(Color.BLACK), quickBorderSetup(Color.ORANGERED)));
		t2.setThemeData(3, new Themepair(solidBackgroundSetup(Color.BLACK), quickBorderSetup(Color.ORANGERED)));
		t2.setThemeData(4, new Themepair(solidBackgroundSetup(Color.BLACK), quickBorderSetup(Color.ORANGERED)));
		t2.setThemeData(5, new Themepair(Color.WHITE));
		t2.setThemeData(6, new Themepair(Color.WHITE));
		t2.setThemeData(7, new Themepair(Color.RED));
		t2.setThemeData(8, new Themepair(Color.WHITE));
		t2.addNewImage("/usersettingsbuttonbackgroundnighttheme.png");
		t2.addNewImage("/usersettingsbuttonbackgroundnighttheme.png");
		themes.add(t2);

		Theme t3 = new Theme("USA Theme","/themeUSAThemeMenuIcon.png");
		t3.setThemeData(0, new Themepair(solidBackgroundSetup(Color.WHITE), quickBorderSetup(Color.RED)));
		t3.setThemeData(1, new Themepair(Color.RED));
		t3.setThemeData(2, new Themepair(solidBackgroundSetup(Color.BLUE), quickBorderSetup(Color.DARKBLUE)));
		t3.setThemeData(3, new Themepair(solidBackgroundSetup(Color.BLUE), quickBorderSetup(Color.DARKBLUE)));
		t3.setThemeData(4, new Themepair(solidBackgroundSetup(Color.BLUE), quickBorderSetup(Color.DARKBLUE)));
		t3.setThemeData(5, new Themepair(Color.RED));
		t3.setThemeData(6, new Themepair(Color.RED));
		t3.setThemeData(7, new Themepair(Color.RED));
		t3.setThemeData(8, new Themepair(Color.RED));
		t3.addNewImage("/usersettingsbuttonbackground.png");
		t3.addNewImage("/usersettingsbuttonbackground.png");
		themes.add(t3);
		
		Theme t4 = new Theme("Experimental Theme","/themeExperimentalThemeMenuIcon.png");
		t4.setThemeData(0, new Themepair(solidBackgroundSetup(211, 211, 211), quickBorderSetup(Color.DARKBLUE)));
		t4.setThemeData(1, new Themepair(Color.BLACK));
		t4.setThemeData(2, new Themepair(solidBackgroundSetup(RegionColors.getColor(RegionColors.DEFAULT_BACKGROUND)),
				quickBorderSetup(RegionColors.getColor(RegionColors.DEFAULT_BACKGROUND))));
		t4.setThemeData(3, new Themepair(linGrdSimpleBackgroundSetUp(Color.LIGHTGRAY,Color.DARKGRAY, false),
				quickBorderSetup(RegionColors.getColor(RegionColors.DEFAULT_BACKGROUND))));
		t4.setThemeData(4, new Themepair(solidBackgroundSetup(RegionColors.getColor(RegionColors.DEFAULT_BACKGROUND)),
				quickBorderSetup(Color.BLACK)));
		t4.setThemeData(5, new Themepair(Color.BLACK));
		t4.setThemeData(6, new Themepair(Color.BLACK));
		t4.setThemeData(7, new Themepair(Color.RED));
		t4.setThemeData(8, new Themepair(Color.BLACK));
		t4.addNewImage("/usersettingsbuttonbackground.png");
		t4.addNewImage("/usersettingsbuttonbackground.png");
		themes.add(t4);
	}
	public static Background linGrdSimpleBackgroundSetUp(Color start, Color end, boolean sidetoside) 
	{
		Stop[] stops = new Stop[] 
		{ 
				new Stop(0, start), new Stop(1,end) 
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
	public static Border quickBorderSetup(Color color) 
	{
		return new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)));
	}

	/**
	 * Generates a simple background with a static single color 
	 * @param color The color object of the desired color.
	 * @return A background object of the inputed color
	 */
	public static Background solidBackgroundSetup(Color color) 
	{
		return new Background(new BackgroundFill(color, CornerRadii.EMPTY, new Insets(0)));
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
	/**
	 * Updates a Region objects look.
	 * @param r An object that is a region or a child of region.
	 * @param index The index of the value to be fetched to theme.
	 */
	private void _updateRegionUtil(Region r, int index) 
	{
		//r.setBackground(this.initthemes.getThemeData(this.initthemeinuseid,index).getBackground());
		//r.setBorder(this.initthemes.getThemeData(this.initthemeinuseid,index).getBorder());
		//r.setStyle(this.initthemes.getThemeData(this.initthemeinuseid,index).getCSS());
	}
	private class RegionPair 
	{
		private Region region;
		private String description;
		private int index;
		private ThemeRegionProp prop;
		public RegionPair(Region region, String description, ThemeRegionProp properties) 
		{
			this.region = region;
			this.description = description;
			this.prop = properties;
		}
		public void setIndex(int index) 
		{
			this.index = index;
		}
		public Region getRegion() 
		{
			return this.region;
		}
		public String getDescription() 
		{
			return this.description;
		}
		public int getIndex() 
		{
			return this.index;
		}
	}
	private class PriorityPair implements Comparable<PriorityPair>
	{
		private Double pri;
		private RegionPair r;
		public PriorityPair(double pri, RegionPair r)
		{
			this.pri = pri;
			this.r = r;
		}
		public RegionPair getRegionPair()
		{
			return this.r;
		}
		public Double getPriority() 
		{
			return this.pri;
		}
		
		public int compareTo(PriorityPair arg0)
		{
			return this.getPriority().compareTo(arg0.getPriority());
		}
	}
}
