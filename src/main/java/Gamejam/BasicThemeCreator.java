package Gamejam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class BasicThemeCreator extends VBox
{
	private GamejamMainScreenTheme screen;
	private HBox borderSetter;
	private HBox textSetter;
	private HBox backgroundSetter;
	private VBox mainarea;
	
	private HBox elementSetter;
	private String elementStr;
	
	private Paint borderPaint;
	private Paint backgroundPaint;
	private Paint textPaint;
	
	private Background backgroundgen;
	private Border brodergen;
	
	private double borderthickness;
	private BorderStrokeStyle borderstyle;
	private Button borderPreview;
	private Button textPreview;
	private Button backgroundPreview;
	
	private ThemeDynamic workingtheme;
	private String customthemename;
	
	Label textlabel;
	Label borderlabel;
	Label backgroundlabel;
	
	private boolean[] ui_enable_editors;
	
	private Button returntomainmenubutton;
	public BasicThemeCreator(GamejamMainScreenTheme screen)
	{
		this.screen = screen;
		this.borderthickness = 2.0;
		this.borderstyle = BorderStrokeStyle.SOLID;
		this.elementSetter = setUpElementSetter();
		this.textSetter = setUpTextSetter();
		this.borderSetter = setUpBorderSetter();
		this.backgroundSetter = setUpBackgroundSetter();
		this.mainarea = new VBox();
		this.elementStr = "Buttons";
		this.textlabel = new Label("Select the color of the text of the selected parts of the GUI");
		this.borderlabel = new Label("Select the properties of the borders of the selected parts of the GUI");
		this.backgroundlabel = new Label("Select background color of the selected parts of the GUI");
		this.ui_enable_editors = new boolean[3];
		this.returntomainmenubutton = new Button("Return to Previous Screen");
		
		
		Label des = new Label("Select the part of the GUI to edit");
		Button updateScreen = new Button("Send changes to GUI!");
		Button loadTextEditor = new Button("Edit Text Color");
		Button loadBackgroundEditor = new Button("Edit Border Color");
		Button loadBorderEditor = new Button("Edit Border");
		
		this.mainarea.getChildren().addAll(des,this.elementSetter,loadTextEditor,loadBackgroundEditor,loadBorderEditor,updateScreen);
		
		updateScreen.setOnAction((click) -> 
		{
			updateThemeData();
			this.workingtheme.generateTheme();
			this.screen.updateTheme(-1);
			printCurrentTheme();
		});
		loadTextEditor.setOnAction((click) -> 
		{
			if (this.ui_enable_editors[0])
			{
				this.getChildren().clear();
				this.getChildren().addAll(this.textlabel,this.textSetter,this.returntomainmenubutton);
			}
		});
		loadBackgroundEditor.setOnAction((click) -> 
		{
			if (this.ui_enable_editors[2])
			{
				this.getChildren().clear();
				this.getChildren().addAll(this.backgroundlabel,this.backgroundSetter,this.returntomainmenubutton);
			}
		});
		loadBorderEditor.setOnAction((click) -> 
		{
			if (this.ui_enable_editors[1])
			{
				this.getChildren().clear();
				this.getChildren().addAll(this.borderlabel,this.borderSetter,this.returntomainmenubutton);
			}
		});
		this.returntomainmenubutton.setOnAction((click) -> 
		{
			this.getChildren().clear();
			this.getChildren().add(this.mainarea);
		});
		this.getChildren().add(this.mainarea);
	}
	private void updateMainScreenOnElementSelection(String element)
	{
		this.ui_enable_editors = ThemeDynamic.englishToGUIRegionsToShowId(element);
		
		// Fixes the bug where initially, all buttons are disabled. Assumes we are using an element that accepts everything such as Buttons.
		// Also assumes if englishToGUIRegionsToShowId returns all false, that we have the start up case.    
		if (!this.ui_enable_editors[0] && !this.ui_enable_editors[1] && !this.ui_enable_editors[2])
		{
			this.ui_enable_editors[0] = true;
			this.ui_enable_editors[1] = true;
			this.ui_enable_editors[2] = true;
		}
		
		if (this.ui_enable_editors[0])
		{
			Button target = (Button) this.mainarea.getChildren().get(2);
			target.setBackground(GamejamMainScreenTheme.solidBackgroundSetup(Color.LIGHTGREEN));
			target.setBorder(GamejamMainScreenTheme.simpleBorder(Color.BLACK, BorderStrokeStyle.SOLID, 2));
			target.arm();
		} 
		else 
		{
			Button target = (Button) this.mainarea.getChildren().get(2);
			target.setBackground(GamejamMainScreenTheme.solidBackgroundSetup(Color.INDIANRED));
			target.setBorder(GamejamMainScreenTheme.simpleBorder(Color.BLACK, BorderStrokeStyle.SOLID, 2));
			target.disarm();
		}
		if (this.ui_enable_editors[1])
		{
			Button target = (Button) this.mainarea.getChildren().get(4);
			target.setBackground(GamejamMainScreenTheme.solidBackgroundSetup(Color.LIGHTGREEN));
			target.setBorder(GamejamMainScreenTheme.simpleBorder(Color.BLACK, BorderStrokeStyle.SOLID, 2));
			target.arm();
		}
		else 
		{
			Button target = (Button) this.mainarea.getChildren().get(4);
			target.setBackground(GamejamMainScreenTheme.solidBackgroundSetup(Color.INDIANRED));
			target.setBorder(GamejamMainScreenTheme.simpleBorder(Color.BLACK, BorderStrokeStyle.SOLID, 2));
			target.disarm();
		}
		if (this.ui_enable_editors[2])
		{
			Button target = (Button) this.mainarea.getChildren().get(3);
			target.setBackground(GamejamMainScreenTheme.solidBackgroundSetup(Color.LIGHTGREEN));
			target.setBorder(GamejamMainScreenTheme.simpleBorder(Color.BLACK, BorderStrokeStyle.SOLID, 2));
			target.arm();
		}
		else 
		{
			Button target = (Button) this.mainarea.getChildren().get(3);
			target.setBackground(GamejamMainScreenTheme.solidBackgroundSetup(Color.INDIANRED));
			target.setBorder(GamejamMainScreenTheme.simpleBorder(Color.BLACK, BorderStrokeStyle.SOLID, 2));
			target.disarm();
		}
	}
	public void finishConstructing()
	{
		this.workingtheme = new ThemeDynamic("Test Theme", screen.getRegionCount(), screen.getAllRegions());
		updateMainScreenOnElementSelection("Button");
	}
	private void updateThemeData()
	{
		this.workingtheme.addRule(new ThemePair(this.backgroundgen, this.brodergen, this.textPaint), ThemeDynamic.englishToRuleSet(this.elementStr), (this.elementStr.equals("Button") || this.elementStr.equals("Selection Boxes"))) ;
	}
	private HBox setUpBackgroundSetter()
	{
		HBox retval = new HBox();
		this.backgroundPreview = new Button("Background Preview");
		ColorPicker color = new ColorPicker();
		color.setOnAction((click) -> 
		{
			this.backgroundPaint = color.getValue();
			this.backgroundgen = GamejamMainScreenTheme.solidBackgroundSetup(this.backgroundPaint);
			this.backgroundPreview.setBackground(GamejamMainScreenTheme.solidBackgroundSetup(this.backgroundPaint));
		});
		
		retval.getChildren().addAll(color,this.backgroundPreview);
		return retval;
	}
	private HBox setUpTextSetter()
	{
		HBox retval = new HBox();
		this.textPreview = new Button("Text Preview");
		ColorPicker color = new ColorPicker();
		color.setOnAction((click) -> 
		{
			this.textPaint = color.getValue();
			this.textPreview.setTextFill(this.textPaint);
		});
		retval.getChildren().addAll(color,this.textPreview);
		return retval;
	}
	private HBox setUpElementSetter()
	{
		HBox retval = new HBox();
		ComboBox<String> box = new ComboBox<String>();
		ArrayList<String> rules = ThemeDynamic.getSurportedEnglishRules();
		box.getItems().addAll(rules);
		box.setValue(rules.get(0));
		box.setOnAction((click) -> 
		{
			this.elementStr = box.getValue();
			updateMainScreenOnElementSelection(this.elementStr);
		});
		retval.getChildren().add(box);
		return retval;
	}
	private HBox setUpBorderSetter()
	{
		HBox retval = new HBox();
		ColorPicker color = new ColorPicker();
		color.setOnAction((click) -> 
		{
			this.borderPaint = color.getValue();
			updateBorderPreview();
		});
		ComboBox<String> box = new ComboBox<String>();
		box.getItems().addAll("None","Very Thin","Thin","Normal","Wide","Thick","Thicker","Very Thick","????? Thick");
		box.setOnAction((click) -> 
		{
			String value = box.getValue();
			if (value.equals("None"))
			{
				this.borderthickness = 0.0;
			}
			else if (value.equals("Very Thin"))
			{
				this.borderthickness = 0.5;
			}
			else if (value.equals("Thin"))
			{
				this.borderthickness = 1.0;
			}
			else if (value.equals("Normal"))
			{
				this.borderthickness = 2.0;
			}
			else if (value.equals("Wide"))
			{
				this.borderthickness = 3.0;
			}
			else if (value.equals("Thick"))
			{
				this.borderthickness = 4.0;
			}
			else if (value.equals("Thicker"))
			{
				this.borderthickness = 6.0;
			}
			else if (value.equals("Very Thick"))
			{
				this.borderthickness = 10.0;
			}
			else
			{
				this.borderthickness = 25.0;
			}
			updateBorderPreview();
		});
		box.setValue("Normal");
		ComboBox<String> box1 = new ComboBox<String>();
		box1.getItems().addAll("Solid","Dashed","Dotted");
		box1.setOnAction((click) -> 
		{
			String borderstyle = box1.getValue();
			if (borderstyle.equals("Solid"))
			{
				this.borderstyle = BorderStrokeStyle.SOLID;
			} 
			else if (borderstyle.equals("Dashed"))
			{
				this.borderstyle = BorderStrokeStyle.DASHED;
			}
			else
			{
				this.borderstyle = BorderStrokeStyle.DOTTED;
			}
			updateBorderPreview();
		});
		box1.setValue("Solid");
		
		this.borderPreview = new Button("Border Preview");
		
		retval.getChildren().addAll(color,box,box1,this.borderPreview);
		return retval;
	}
	private void updateBorderPreview()
	{
		this.brodergen = GamejamMainScreenTheme.simpleBorder(this.borderPaint,this.borderstyle,this.borderthickness);
		this.borderPreview.setBorder(this.brodergen);
	}
	private void printCurrentTheme()
	{
		for(int x = 0; x < this.workingtheme.getTheme().length; x++)
		{
			System.out.println(this.workingtheme.getTheme()[x].getColor());
		}
	}
}
