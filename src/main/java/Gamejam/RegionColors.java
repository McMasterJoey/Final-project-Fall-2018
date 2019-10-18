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
	public static final Background RED = new Background(new BackgroundFill(Color.RED,CornerRadii.EMPTY,new Insets(0)));
	public static final Background WHITE = new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,new Insets(0)));
	public static final Background BLACK = new Background(new BackgroundFill(Color.BLACK,CornerRadii.EMPTY,new Insets(0)));
	public static final Background GREY = new Background(new BackgroundFill(Color.GREY,CornerRadii.EMPTY,new Insets(0)));
	public static final Background YELLOW = new Background(new BackgroundFill(Color.YELLOW,CornerRadii.EMPTY,new Insets(0)));
	public static final Background BLUE = new Background(new BackgroundFill(Color.BLUE,CornerRadii.EMPTY,new Insets(0)));
	public static final Background GREEN = new Background(new BackgroundFill(Color.GREEN,CornerRadii.EMPTY,new Insets(0)));
}
