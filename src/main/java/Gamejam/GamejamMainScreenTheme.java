package Gamejam;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import model.SanityCheckFailedException;

/**
 * @author Joey McMaster
 *
 */
public class GamejamMainScreenTheme {
	private ArrayList<Theme> themes;
	public GamejamMainScreenTheme() {
		this.themes = new ArrayList<Theme>();
		setUpThemes();
	}
	/**
	 * 
	 * @param themeid
	 * @param elementid
	 * @return
	 */
	public Themepair getThemeData(int themeid, int elementid) {
		if (themeid < 0 || themeid >= this.themes.size()) {
			throw new SanityCheckFailedException("Invalid themeid!");
		}
		return this.themes.get(themeid).getTheme()[elementid];
	}
	/**
	 * 
	 */
	private void setUpThemes() {
		// Default Theme:
		Theme t1 = new Theme("Default Theme");
		t1.setThemeData(0, new Themepair(solidBackgroundSetup(211,211,211), quickBorderSetup(Color.DARKBLUE)));
		t1.setThemeData(1, new Themepair(Color.BLACK));
		t1.setThemeData(2, new Themepair(solidBackgroundSetup(RegionColors.getColor(RegionColors.DEFAULT_BACKGROUND)), quickBorderSetup(RegionColors.getColor(RegionColors.DEFAULT_BACKGROUND))));
		t1.setThemeData(3, new Themepair(solidBackgroundSetup(RegionColors.getColor(RegionColors.DEFAULT_BACKGROUND)), quickBorderSetup(RegionColors.getColor(RegionColors.DEFAULT_BACKGROUND))));
		t1.setThemeData(4, new Themepair(solidBackgroundSetup(RegionColors.getColor(RegionColors.DEFAULT_BACKGROUND)), quickBorderSetup(Color.BLACK)));
		t1.setThemeData(5, new Themepair(Color.BLACK));
		t1.setThemeData(6, new Themepair(Color.BLACK));
		t1.setThemeData(7, new Themepair(Color.RED));
		t1.setThemeData(8, new Themepair(Color.BLACK));
		themes.add(t1);
		/*
		0 = Button Backgrounds
		1 = Left Panel Upper Text color 
		2 = Upper bars background
		3 = Middle Area background / Overall background
		4 = Left Panel background
		5 = New account/Back/Logout button text
		6 = Left Panel Lower text color
		7 = Login button text color
		8 = Default Settings Text color
		*/
		Theme t2 = new Theme("Night Theme");
		t2.setThemeData(0, new Themepair(solidBackgroundSetup(Color.BLACK), quickBorderSetup(Color.ORANGE)));
		t2.setThemeData(1, new Themepair(Color.WHITE));
		t2.setThemeData(2, new Themepair(solidBackgroundSetup(Color.BLACK), quickBorderSetup(Color.ORANGERED)));
		t2.setThemeData(3, new Themepair(solidBackgroundSetup(Color.BLACK), quickBorderSetup(Color.ORANGERED)));
		t2.setThemeData(4, new Themepair(solidBackgroundSetup(Color.BLACK), quickBorderSetup(Color.ORANGERED)));
		t2.setThemeData(5, new Themepair(Color.WHITE));
		t2.setThemeData(6, new Themepair(Color.WHITE));
		t2.setThemeData(7, new Themepair(Color.RED));
		t2.setThemeData(8, new Themepair(Color.WHITE));
		themes.add(t2);
		
		Theme t3 = new Theme("USA Theme");
		t3.setThemeData(0, new Themepair(solidBackgroundSetup(Color.WHITE), quickBorderSetup(Color.RED)));
		t3.setThemeData(1, new Themepair(Color.RED));
		t3.setThemeData(2, new Themepair(solidBackgroundSetup(Color.BLUE), quickBorderSetup(Color.DARKBLUE)));
		t3.setThemeData(3, new Themepair(solidBackgroundSetup(Color.BLUE), quickBorderSetup(Color.DARKBLUE)));
		t3.setThemeData(4, new Themepair(solidBackgroundSetup(Color.BLUE), quickBorderSetup(Color.DARKBLUE)));
		t3.setThemeData(5, new Themepair(Color.RED));
		t3.setThemeData(6, new Themepair(Color.RED));
		t3.setThemeData(7, new Themepair(Color.RED));
		t3.setThemeData(8, new Themepair(Color.RED));
		themes.add(t3);
		/*
		 * this.themeSettings[0] = RegionColors.BLACK;   // Button Backgrounds
			this.themeSettings[1] = RegionColors.BLACK;  // Left Panel Upper Text color 
			this.themeSettings[2] = RegionColors.BLACK;   // Upper bars background
			this.themeSettings[3] = RegionColors.BLACK;   // Middle Area background / Overall background
			this.themeSettings[4] = RegionColors.BLACK;   // Left Panel background
			this.themeSettings[5] = RegionColors.WHITE;  // New account/Back/Logout button text
			this.themeSettings[6] = RegionColors.WHITE;  // Left Panel Lower text color
			this.themeSettings[7] = RegionColors.RED;    // Login button text color
			this.themeSettings[8] = RegionColors.WHITE;    // Default Settings Text color
			
			this.themeSettings[0] = RegionColors.WHITE;   // Button Backgrounds
			this.themeSettings[1] = RegionColors.RED;  // Left Panel Upper Text color 
			this.themeSettings[2] = RegionColors.BLUE;   // Upper bars background
			this.themeSettings[3] = RegionColors.BLUE;   // Middle Area background / Overall background
			this.themeSettings[4] = RegionColors.BLUE;   // Left Panel background
			this.themeSettings[5] = RegionColors.RED;  // New account/Back/Logout button text
			this.themeSettings[6] = RegionColors.RED;  // Left Panel Lower text color
			this.themeSettings[7] = RegionColors.RED;    // Login button text color
			this.themeSettings[8] = RegionColors.RED;    // Default Settings Text color
		 */
	}
	/**'
	 * 
	 * @param color
	 * @return
	 */
	public static Border quickBorderSetup(Color color) {
		return new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)));
	}
	/**
	 * 
	 * @param color
	 * @return
	 */
	public static Background solidBackgroundSetup(Color color) {
		return new Background(new BackgroundFill(color,CornerRadii.EMPTY,new Insets(0)));
	}
	/**
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static Background solidBackgroundSetup(int r, int g, int b) {
		double red = (double) r;
		double green = (double) g;
		double blue = (double) b;
		return new Background(new BackgroundFill(Color.color(red / 255.0, green / 255.0, blue / 255.0),CornerRadii.EMPTY,new Insets(0)));
	}
}
