package Gamejam;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * Defines a set of color constants that can be used on Region UI Elements.
 * Here to reduce the quantity of new objects being made when swapping themes.
 * @author Joey McMaster
 *
 */
public class RegionColors {
	// Constants the define predefined colors.
	public static final int RED = 0;
	public static final Background BRED = new Background(new BackgroundFill(Color.RED,CornerRadii.EMPTY,new Insets(0)));
	public static final int WHITE = 1;
	public static final Background BWHITE = new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,new Insets(0)));
	public static final int BLACK = 2;
	public static final Background BBLACK = new Background(new BackgroundFill(Color.BLACK,CornerRadii.EMPTY,new Insets(0)));
	public static final int GREY = 3;
	public static final Background BGREY = new Background(new BackgroundFill(Color.GREY,CornerRadii.EMPTY,new Insets(0)));
	public static final int YELLOW = 4;
	public static final Background BYELLOW = new Background(new BackgroundFill(Color.YELLOW,CornerRadii.EMPTY,new Insets(0)));
	public static final int BLUE = 5;
	public static final Background BBLUE = new Background(new BackgroundFill(Color.BLUE,CornerRadii.EMPTY,new Insets(0)));
	public static final int GREEN = 6;
	public static final Background BGREEN = new Background(new BackgroundFill(Color.GREEN,CornerRadii.EMPTY,new Insets(0)));
	/**
	 * Takes an id that represents the color. Returns the Background formated version of the color.
	 * @param id The id of the color, Refer to the int constants for this class. If out of range, returns null.
	 * @return The background object of the desired color
	 */
	public static Background getBackgroundColor(int id) {
		if (id == 0) {
			return BRED;
		} else if (id == 1) {
			return BWHITE;
		} else if (id == 2) {
			return BBLACK;
		} else if (id == 3) {
			return BGREY;
		} else if (id == 4) {
			return BYELLOW;
		} else if (id == 5) {
			return BBLUE;
		} else if (id == 6) {
			return BGREEN;
		}
		return null;
	}
	/**
	 * Takes an id that represents the color. Returns the Color formated version of the color.
	 * @param id The id of the color, Refer to the int constants for this class. If out of range, returns null.
	 * @return The color object of the desired color
	 */
	public static Color getColor(int id) {
		if (id == 0) {
			return Color.RED;
		} else if (id == 1) {
			return Color.WHITE;
		} else if (id == 2) {
			return Color.BLACK;
		} else if (id == 3) {
			return Color.GREY;
		} else if (id == 4) {
			return Color.YELLOW;
		} else if (id == 5) {
			return Color.BLUE;
		} else if (id == 6) {
			return Color.GREEN;
		}
		return null;
	}
}
