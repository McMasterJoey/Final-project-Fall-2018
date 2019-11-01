package Gamejam;

import java.util.ArrayList;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.SanityCheckFailedException;

/**
 * The UI and relevant functions to create custom themes.
 * @author Joey McMaster
 *
 */
public class ThemeCreator extends HBox 
{
	private ColorPicker colorpicker;
	private Color currentworkingcolor;
	private VBox leftcol;
	private int currentworkingid;
	private boolean doinitonce = true;
	private GamejamMainScreenTheme screen;
	private ChoiceBox<String> elementselector;
	private ArrayList<RegionPair> regions;
	public ThemeCreator(GamejamMainScreenTheme screen) 
	{
		this.screen = screen;
		this.currentworkingcolor = Color.BLACK;
		this.leftcol = new VBox();
		this.colorpicker = new ColorPicker();
		this.colorpicker.setOnMouseReleased((click) -> 
		{
			this.currentworkingcolor = this.colorpicker.getValue();
			System.out.println(this.currentworkingcolor);
		});
		this.elementselector = new ChoiceBox<String>();
		this.screen.addRegion(400, this.leftcol, "Theme Creator left vbox background", new ThemeRegionProp(ThemeRegionProp.VBOX));
		this.screen.addRegion(401, this.elementselector, "Theme Creator Element Selector Combo Box", new ThemeRegionProp(ThemeRegionProp.COMBOBOX));
	}
	public void doOnMainScreenThemeFinishInit()
	{
		if (!this.doinitonce) 
		{
			return;
		}
		this.doinitonce = false;
		this.regions = this.screen.getAllRegions();
		for(int x = 0; x < this.screen.getRegionCount(); x++) 
		{
			this.elementselector.getItems().add(this.regions.get(x).getDescription());
		}
		this.elementselector.setOnMouseReleased((click) -> 
		{
			this.currentworkingid = getIndexFromDescription(elementselector.getValue());
			System.out.println("wid: " + this.currentworkingid);
		});
		this.elementselector.setValue("Theme Creator left vbox background");
		this.leftcol.getChildren().addAll(this.colorpicker,this.elementselector);
		this.getChildren().addAll(this.leftcol);
	}
	private int getIndexFromDescription(String description)
	{
		for(int x = 0; x < this.regions.size(); x++)
		{
			if (this.regions.get(x).getDescription().equals(description))
			{
				return x;
			}
		}
		throw new SanityCheckFailedException("When fetching the index from description, didn't find a matching description!");
	}
}
