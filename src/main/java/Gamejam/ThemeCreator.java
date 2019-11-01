package Gamejam;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * The UI and relevant functions to create custom themes.
 * @author Joey McMaster
 *
 */
public class ThemeCreator extends HBox {
	private ColorPicker colorpicker;
	private Color currentworkingcolor;
	private VBox leftcol;
	private int currentworkingid;
	private GamejamMainScreenTheme screen;
	public ThemeCreator() {
		this.currentworkingcolor = Color.BLACK;
		this.leftcol = new VBox();
		this.colorpicker = new ColorPicker();
		this.colorpicker.setOnMouseReleased((click) -> {
			this.currentworkingcolor = this.colorpicker.getValue();
			//System.out.println(this.currentworkingcolor);
		});
		ChoiceBox<String> elementselector = new ChoiceBox<String>();
		elementselector.getItems().addAll("All Button Backgrounds","Left Panel Upper Text color","Upper bars background","Middle Area background","Left Panel background","New account/Back/Logout button text","Left Panel Lower text color","Login button text color","Default Settings Text color");
		elementselector.setOnMouseReleased((click) -> {
			this.currentworkingid = elementNameToInt(elementselector.getValue());
			//System.out.println("wid: " + this.currentworkingid);
		});
		elementselector.setValue("All Button Backgrounds");
		this.leftcol.getChildren().addAll(this.colorpicker,elementselector);
		this.getChildren().addAll(this.leftcol);
	}
	private int elementNameToInt(String name) {
		if (name == null) {
			Gamejam.DPrint("elementNameToInt name was null!");
			return 0;
		}
		if (name.equals("All Button Backgrounds")) {
			return 0;
		} else if (name.equals("Left Panel Upper Text color")) {
			return 1;
		} else if (name.equals("Upper bars background")) {
			return 2;
		} else if (name.equals("Middle Area background")) {
			return 3;
		} else if (name.equals("Left Panel background")) {
			return 4;
		} else if (name.equals("New account/Back/Logout button text")) {
			return 5;
		} else if (name.equals("Left Panel Lower text color")) {
			return 6;
		} else if (name.equals("Login button text color")) {
			return 7;
		} else if (name.equals("Default Settings Text color")) {
			return 8;
		}
		return 0;
	}
}
