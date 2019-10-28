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
		t1.setThemeData(0, new Themepair(solidBackgroundSetup(211,211,211), quickBorderSetup(Color.LIGHTBLUE)));
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
	}
	public static Border quickBorderSetup(Color color) {
		return new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)));
	}
	public static Background solidBackgroundSetup(Color color) {
		return new Background(new BackgroundFill(color,CornerRadii.EMPTY,new Insets(0)));
	}
	public static Background solidBackgroundSetup(int r, int g, int b) {
		double red = (double) r;
		double green = (double) g;
		double blue = (double) b;
		return new Background(new BackgroundFill(Color.color(red / 255.0, green / 255.0, blue / 255.0),CornerRadii.EMPTY,new Insets(0)));
	}
}
