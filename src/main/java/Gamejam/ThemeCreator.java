package Gamejam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import connectFour.ConnectFourModel;
import controller.AccountManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ThemeCreator extends GridPane
{
	public static final double BUTTON_PREF_X_SIZE = 250;
	public static final double BUTTON_PREF_Y_SIZE = 75;
	
	private GamejamMainScreenTheme screen;
	private VBox borderSetter;
	private VBox textSetter;
	private VBox backgroundSetter;
	private VBox mainarea;
	private VBox themeUtil;
	
	private ComboBox<String> elementSetter;
	private ComboBox<String> userThemeNames;
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
	private CheckBox updateinrealtimecheckbox;
	
	
	private ThemeDynamic workingtheme;
	private String customthemename = "Custom Theme 1";
	
	Label textlabel;
	Label borderlabel;
	Label backgroundlabel;
	
	private Button returntomainmenubutton;
	private Button returntomainmenubutton1;
	private Button returntomainmenubutton2;
	private Button returntomainmenubutton3;
	
	private VBox textintermediatevbox;
	private VBox backgroundintermediatevbox;
	private VBox borderintermediatevbox;
	
	private int workingthemeindex = 1;
	
	boolean debugger = true;
	
	public ThemeCreator(GamejamMainScreenTheme screen)
	{
		this.screen = screen;
		this.borderthickness = 2.0;
		this.borderstyle = BorderStrokeStyle.SOLID;
		this.returntomainmenubutton = new Button("Return to Previous Screen");
		this.returntomainmenubutton.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		this.returntomainmenubutton1 = new Button("Return to Previous Screen");
		this.returntomainmenubutton1.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		this.returntomainmenubutton2 = new Button("Return to Previous Screen");
		this.returntomainmenubutton2.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		this.returntomainmenubutton3 = new Button("Return to Previous Screen");
		this.returntomainmenubutton3.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		this.elementSetter = setUpElementSetter();
		this.textSetter = setUpTextSetter();
		this.borderSetter = setUpBorderSetter();
		this.backgroundSetter = setUpBackgroundSetter();
		this.mainarea = new VBox();
		this.elementStr = "Buttons";
		this.textlabel = new Label("Select the color of the text of the selected parts of the GUI");
		this.borderlabel = new Label("Select the properties of the borders of the selected parts of the GUI");
		this.backgroundlabel = new Label("Select background color of the selected parts of the GUI");
		this.textintermediatevbox = new VBox();
		this.backgroundintermediatevbox = new VBox();
		this.borderintermediatevbox = new VBox();
		this.updateinrealtimecheckbox = new CheckBox("Update after making any change");
		this.updateinrealtimecheckbox.setSelected(true);
		this.themeUtil = setUpOtherThemeSettings();
		
		
		Label des = new Label("Select the part of the GUI to edit");
		Button updateScreen = new Button("Send changes to GUI!");
		Button loadTextEditor = new Button("Edit Text Color");
		Button loadBackgroundEditor = new Button("Edit Background Color");
		Button loadBorderEditor = new Button("Edit Border");
		
		Button restartThemeButton =  new Button("Theme Options");
		restartThemeButton.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		
		des.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		this.elementSetter.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		loadTextEditor.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		loadBackgroundEditor.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		loadBorderEditor.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		updateScreen.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		
		this.mainarea.getChildren().addAll(des,this.updateinrealtimecheckbox, this.elementSetter,loadTextEditor,loadBackgroundEditor,loadBorderEditor,restartThemeButton,updateScreen);
		this.textintermediatevbox.getChildren().addAll(this.textlabel,this.textSetter);
		this.backgroundintermediatevbox.getChildren().addAll(this.backgroundlabel,this.backgroundSetter);
		this.borderintermediatevbox.getChildren().addAll(this.borderlabel,this.borderSetter);
		
		this.textintermediatevbox.setPadding(new Insets(50,0,50,0));
		this.backgroundintermediatevbox.setPadding(new Insets(50,0,50,0));
		this.borderintermediatevbox.setPadding(new Insets(50,0,50,0));
		this.mainarea.setPadding(new Insets(50,0,50,0));
		
		this.textintermediatevbox.setAlignment(Pos.CENTER);
		this.backgroundintermediatevbox.setAlignment(Pos.CENTER);
		this.borderintermediatevbox.setAlignment(Pos.CENTER);
		this.mainarea.setAlignment(Pos.CENTER);
		
		updateScreen.setOnAction((click) -> 
		{
			updateThemeData();
			this.workingtheme.generateTheme();
			this.screen.updateTheme(-1);
			printCurrentTheme();
		});
		loadTextEditor.setOnAction((click) -> 
		{
			this.getChildren().clear();
			this.add(this.textintermediatevbox,1,0);
		});
		loadBackgroundEditor.setOnAction((click) -> 
		{
			this.getChildren().clear();
			this.add(this.backgroundintermediatevbox,1,0);
		});
		loadBorderEditor.setOnAction((click) -> 
		{
			this.getChildren().clear();
			this.add(this.borderintermediatevbox,1,0);
		});
		this.returntomainmenubutton.setOnAction((click) -> 
		{
			this.getChildren().clear();
			this.add(this.mainarea,1,0);
		});
		this.returntomainmenubutton1.setOnAction((click) -> 
		{
			this.getChildren().clear();
			this.add(this.mainarea,1,0);
		});
		this.returntomainmenubutton2.setOnAction((click) -> 
		{
			this.getChildren().clear();
			this.add(this.mainarea,1,0);
		});
		this.returntomainmenubutton3.setOnAction((click) -> 
		{
			this.getChildren().clear();
			this.add(this.mainarea,1,0);
		});
		
		restartThemeButton.setOnAction((click) -> 
		{
			this.getChildren().clear();
			this.add(this.themeUtil,1,0);
		});
		
		ColumnConstraints c1 = new ColumnConstraints();
		ColumnConstraints c2 = new ColumnConstraints();
		ColumnConstraints c3 = new ColumnConstraints();
		c1.setPercentWidth(5);
		c2.setPercentWidth(90);
		c3.setPercentWidth(5);
		this.getColumnConstraints().addAll(c1,c2,c3);
		
		this.add(this.mainarea,1,0);
		
		this.screen.addRegion(450, this.returntomainmenubutton , "Basic Theme Editor: Return to Main Menu Button (Background Editor) Button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT, ThemeRegionProp.LOC_MI_BTM));
		this.screen.addRegion(451, this.returntomainmenubutton1 , "Basic Theme Editor: Return to Main Menu Button (Text Editor) Button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT, ThemeRegionProp.LOC_MI_BTM));
		this.screen.addRegion(452, this.returntomainmenubutton2 , "Basic Theme Editor: Return to Main Menu Button (Border Editor) Button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT, ThemeRegionProp.LOC_MI_BTM));
		this.screen.addRegion(453, updateScreen, "Basic Theme Editor: Update Screen Button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT, ThemeRegionProp.LOC_MI_BTM));
		//this.screen.addRegion(454, loadTextEditor, "Basic Theme Editor: Load Text Editor Button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT, ThemeRegionProp.LOC_MI_BTM));
		//this.screen.addRegion(455, loadBackgroundEditor, "Basic Theme Editor: Load Background Editor Button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT, ThemeRegionProp.LOC_MI_BTM));
		//this.screen.addRegion(456, loadBorderEditor, "Basic Theme Editor: Load Border Editor Button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT, ThemeRegionProp.LOC_MI_BTM));
		this.screen.addRegion(457, this.mainarea, "Basic Theme Editor: Main Section VBox", new ThemeRegionProp(ThemeRegionProp.VBOX, ThemeRegionProp.LOC_MI_BTM, ThemeRegionProp.INT_REG));
		this.screen.addRegion(457.01, this.textintermediatevbox, "Basic Theme Editor: Text Editor Intermediate VBox", new ThemeRegionProp(ThemeRegionProp.VBOX, ThemeRegionProp.LOC_MI_BTM, ThemeRegionProp.INT_REG));
		this.screen.addRegion(457.02, this.backgroundintermediatevbox, "Basic Theme Editor: Background Editor Intermediate VBox", new ThemeRegionProp(ThemeRegionProp.VBOX, ThemeRegionProp.LOC_MI_BTM, ThemeRegionProp.INT_REG));
		this.screen.addRegion(457.03, this.borderintermediatevbox, "Basic Theme Editor: Border Editor Intermediate VBox", new ThemeRegionProp(ThemeRegionProp.VBOX, ThemeRegionProp.LOC_MI_BTM, ThemeRegionProp.INT_REG));
		
		this.screen.addRegion(458, this.textlabel, "Basic Theme Editor:  Text Editor Top Label", new ThemeRegionProp(ThemeRegionProp.LABEL, ThemeRegionProp.LOC_MI_BTM));
		this.screen.addRegion(459, this.backgroundlabel, "Basic Theme Editor:  Background Editor Top Label", new ThemeRegionProp(ThemeRegionProp.LABEL, ThemeRegionProp.LOC_MI_BTM));
		this.screen.addRegion(460, this.borderlabel, "Basic Theme Editor:  Border Editor Top Label", new ThemeRegionProp(ThemeRegionProp.LABEL, ThemeRegionProp.LOC_MI_BTM));
		this.screen.addRegion(461, this.updateinrealtimecheckbox, "Basic Theme Editor:  Update UI after each edit Checkbox", new ThemeRegionProp(ThemeRegionProp.CHECKBOX, ThemeRegionProp.LOC_MI_BTM));
		this.screen.addRegion(462, des, "Basic Theme Editor:  Primary Top Label", new ThemeRegionProp(ThemeRegionProp.LABEL, ThemeRegionProp.LOC_MI_BTM));
	
	}
	/**
	 * Should be called everytime a new user logs in.
	 */
	public void updateObjectOnUserChange()
	{
		ArrayList<String> themenames = AccountManager.getInstance().getThemeNames();
		if (themenames.size() == 0)
		{
			themenames.add("Default Custom Theme");
			this.customthemename = "Default Custom Theme";
		}
		this.userThemeNames = new ComboBox<String>();
		this.userThemeNames.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		this.userThemeNames.getItems().addAll(themenames);
		this.userThemeNames.setValue(themenames.get(0));
	}
	private void updateMainScreenOnElementSelection(String element)
	{
		boolean[] enable = ThemeDynamic.englishToGUIRegionsToShowId(element);
		
		// Fixes the bug where initially, all buttons are disabled. Assumes we are using an element that accepts everything such as Buttons.
		// Also assumes if englishToGUIRegionsToShowId returns all false, that we have the start up case.    
		if (!enable[0] && !enable[1] && !enable[2])
		{
			enable[0] = true;
			enable[1] = true;
			enable[2] = true;
		}
		
		if (enable[0])
		{
			Button target = (Button) this.mainarea.getChildren().get(3);
			target.setBackground(GamejamMainScreenTheme.solidBackgroundSetup(Color.LIGHTGREEN));
			target.setBorder(GamejamMainScreenTheme.simpleBorder(Color.BLACK, BorderStrokeStyle.SOLID, 2));
			target.setDisable(false);
		} 
		else 
		{
			Button target = (Button) this.mainarea.getChildren().get(3);
			target.setBackground(GamejamMainScreenTheme.solidBackgroundSetup(Color.INDIANRED));
			target.setBorder(GamejamMainScreenTheme.simpleBorder(Color.BLACK, BorderStrokeStyle.SOLID, 2));
			target.setDisable(true);
		}
		if (enable[1])
		{
			Button target = (Button) this.mainarea.getChildren().get(5);
			target.setBackground(GamejamMainScreenTheme.solidBackgroundSetup(Color.LIGHTGREEN));
			target.setBorder(GamejamMainScreenTheme.simpleBorder(Color.BLACK, BorderStrokeStyle.SOLID, 2));
			target.setDisable(false);
		}
		else 
		{
			Button target = (Button) this.mainarea.getChildren().get(5);
			target.setBackground(GamejamMainScreenTheme.solidBackgroundSetup(Color.INDIANRED));
			target.setBorder(GamejamMainScreenTheme.simpleBorder(Color.BLACK, BorderStrokeStyle.SOLID, 2));
			target.setDisable(true);
		}
		if (enable[2])
		{
			Button target = (Button) this.mainarea.getChildren().get(4);
			target.setBackground(GamejamMainScreenTheme.solidBackgroundSetup(Color.LIGHTGREEN));
			target.setBorder(GamejamMainScreenTheme.simpleBorder(Color.BLACK, BorderStrokeStyle.SOLID, 2));
			target.setDisable(false);
		}
		else 
		{
			Button target = (Button) this.mainarea.getChildren().get(4);
			target.setBackground(GamejamMainScreenTheme.solidBackgroundSetup(Color.INDIANRED));
			target.setBorder(GamejamMainScreenTheme.simpleBorder(Color.BLACK, BorderStrokeStyle.SOLID, 2));
			target.setDisable(true);
		}
		
		// Resets the coloring
		this.brodergen = null;
		this.backgroundgen = null;
		this.textPaint = null;
	}
	public void finishConstructing()
	{
		this.workingtheme = new ThemeDynamic(this.customthemename, screen.getRegionCount(), screen.getAllRegions(),this.screen);
		updateMainScreenOnElementSelection("Button");
		this.setPrefHeight(9999);
	}
	private VBox setUpBackgroundSetter()
	{
		VBox internalbox = new VBox();
		internalbox.setAlignment(Pos.CENTER);
		
		this.backgroundPreview = new Button("Background Preview");
		ColorPicker color = new ColorPicker();
		color.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		color.setOnAction((click) -> 
		{
			this.backgroundPaint = color.getValue();
			this.backgroundgen = GamejamMainScreenTheme.solidBackgroundSetup(this.backgroundPaint);
			this.backgroundPreview.setBackground(GamejamMainScreenTheme.solidBackgroundSetup(this.backgroundPaint));
			doAutoUpdate();
		});
		this.backgroundPreview.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		internalbox.getChildren().addAll(color,this.backgroundPreview,this.returntomainmenubutton);
		internalbox.setPadding(new Insets(50,0,50,0));
		
		this.screen.addRegion(470, internalbox, "Basic Theme Editor: Intermidate Background Setter VBox", new ThemeRegionProp(ThemeRegionProp.VBOX, ThemeRegionProp.LOC_MI_BTM, ThemeRegionProp.INT_REG));
		this.screen.addRegion(471, color, "Basic Theme Editor: Background Color Picker", new ThemeRegionProp(ThemeRegionProp.COLORPICKER, ThemeRegionProp.LOC_MI_BTM));
		
		return internalbox;
	}
	private VBox setUpOtherThemeSettings()
	{
		VBox retval = new VBox();
		retval.setAlignment(Pos.CENTER);
		
		Label namefieldlabel = new Label("Input your desired theme name");
		TextField box = new TextField(this.customthemename);
		box.setPrefSize(100, BUTTON_PREF_Y_SIZE);
		updateObjectOnUserChange();
		this.userThemeNames.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		this.userThemeNames.setOnAction((click) -> 
		{
			boolean result = loadCustomDynamicTheme(this.userThemeNames.getValue(),AccountManager.getInstance().getThemePath(this.userThemeNames.getValue()));
			if (!result)
			{
				Gamejam.DPrint("[DEBUG]: Failed to load Custom Theme " + this.userThemeNames.getValue());
			}
		});
		
		Button b = new Button("Save Current Theme");
		b.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		b.setOnAction((click) -> 
		{
			this.customthemename = box.getText();
			boolean result =  saveCustomDynamicTheme(this.customthemename);
			if (!result)
			{
				Gamejam.DPrint("[DEBUG]: Failed to save Custom Theme " + this.userThemeNames.getValue());
			}
		});
		
		Button debug_button = new Button("DEBUG: Load");
		debug_button.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		debug_button.setOnAction((click) -> 
		{
			boolean result =  loadCustomDynamicTheme("debugtheme","");
			if (!result)
			{
				Gamejam.DPrint("[DEBUG]: Failed to load Custom debug Theme ");
			}
		});
		retval.getChildren().addAll(namefieldlabel, box, this.userThemeNames, b, debug_button, this.returntomainmenubutton3);
		return retval;
	}
	
	private boolean saveCustomDynamicTheme(String themename)
	{
		if (themename == null)
		{
			return false;
		}
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			String fname = AccountManager.getInstance().getCurUsername() + "-" + themename + ".ct";
			if (debugger)
			{
				fname = AccountManager.getInstance().getCurUsername() + "-debugtheme.ct";
			}
			String sep = System.getProperty("file.separator");
			String filepath = System.getProperty("user.dir") + sep + "custom-theme";
			if(!new File(filepath).exists()) {
				new File(filepath).mkdir();
			}
			filepath += sep + fname;
			fos = new FileOutputStream(filepath);			
			oos = new ObjectOutputStream(fos);
			oos.writeObject(this.workingtheme);
			oos.close();
			
			AccountManager.getInstance().addNewTheme(themename, filepath);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	private boolean loadCustomDynamicTheme(String themename, String filepath)
	{
		if (debugger)
		{
			String fname = AccountManager.getInstance().getCurUsername() + "-debugtheme.ct";
			String sep = System.getProperty("file.separator");
			filepath = System.getProperty("user.dir") + sep + "custom-theme" + fname;
		} 
		
		if (filepath == null) {
			return false; // Tried to load a non-existant theme
		}
		try {
			File file = new File(filepath);
			if(file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				this.workingtheme = (ThemeDynamic) ois.readObject();
				this.customthemename = themename;
				ois.close();
				file.delete();
			}
		} catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		doAutoUpdate();
		return true;
	}
	private VBox setUpTextSetter()
	{
		VBox retval = new VBox();
		retval.setAlignment(Pos.CENTER);
		this.textPreview = new Button("Text Preview");
		this.textPreview.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		ColorPicker color = new ColorPicker();
		color.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		color.setOnAction((click) -> 
		{
			this.textPaint = color.getValue();
			this.textPreview.setTextFill(this.textPaint);
			doAutoUpdate();
		});
		
		this.screen.addRegion(472, retval, "Basic Theme Editor: Intermidate Text Setter VBox", new ThemeRegionProp(ThemeRegionProp.VBOX, ThemeRegionProp.LOC_MI_BTM, ThemeRegionProp.INT_REG));
		this.screen.addRegion(473, color, "Basic Theme Editor: Text Color Picker", new ThemeRegionProp(ThemeRegionProp.COLORPICKER, ThemeRegionProp.LOC_MI_BTM));
		
		retval.getChildren().addAll(color,this.textPreview,this.returntomainmenubutton1);
		
		return retval;
	}
	private ComboBox<String> setUpElementSetter()
	{
		ComboBox<String> box = new ComboBox<String>();
		ArrayList<String> rules = ThemeDynamic.getSurportedEnglishRules();
		box.getItems().addAll(rules);
		box.setValue(rules.get(0));
		box.setOnAction((click) -> 
		{
			this.elementStr = box.getValue();
			updateMainScreenOnElementSelection(this.elementStr);
			doAutoUpdate();
		});
		this.screen.addRegion(478, box, "Basic Theme Editor: Combobox Element Selector", new ThemeRegionProp(ThemeRegionProp.COMBOBOX, ThemeRegionProp.LOC_MI_BTM));
		return box;
	}
	private VBox setUpBorderSetter()
	{
		VBox retval = new VBox();
		retval.setAlignment(Pos.CENTER);
		ColorPicker color = new ColorPicker();
		color.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		color.setOnAction((click) -> 
		{
			this.borderPaint = color.getValue();
			updateBorderPreview();
			doAutoUpdate();
		});
		ComboBox<String> box = new ComboBox<String>();
		box.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		box.getItems().addAll("None","Very Thin","Thin","Normal","Wide","Thick","Thicker","Very Thick","VERY Thick");
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
			doAutoUpdate();
		});
		box.setValue("Normal");
		ComboBox<String> box1 = new ComboBox<String>();
		box1.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
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
			doAutoUpdate();
		});
		box1.setValue("Solid");
		
		this.borderPreview = new Button("Border Preview");
		this.borderPreview.setPrefSize(BUTTON_PREF_X_SIZE, BUTTON_PREF_Y_SIZE);
		retval.getChildren().addAll(color,box,box1,this.borderPreview,this.returntomainmenubutton2);
		
		this.screen.addRegion(474, retval, "Basic Theme Editor: Intermidate Border Setter VBox", new ThemeRegionProp(ThemeRegionProp.VBOX, ThemeRegionProp.LOC_MI_BTM, ThemeRegionProp.INT_REG));
		this.screen.addRegion(475, color, "Basic Theme Editor: Text Color Picker", new ThemeRegionProp(ThemeRegionProp.COLORPICKER, ThemeRegionProp.LOC_MI_BTM));
		
		this.screen.addRegion(476, box, "Basic Theme Editor: Combo box Thickness", new ThemeRegionProp(ThemeRegionProp.COMBOBOX, ThemeRegionProp.LOC_MI_BTM));
		this.screen.addRegion(477, box1, "Basic Theme Editor: Combo box Style", new ThemeRegionProp(ThemeRegionProp.COMBOBOX, ThemeRegionProp.LOC_MI_BTM));
		return retval;
	}
	private void updateBorderPreview()
	{
		this.brodergen = GamejamMainScreenTheme.simpleBorder(this.borderPaint,this.borderstyle,this.borderthickness);
		this.borderPreview.setBorder(this.brodergen);
		printCurrentTheme();
	}
	private void doAutoUpdate()
	{
		if (this.updateinrealtimecheckbox.isSelected())
		{
			this.workingtheme.removeLastRule();
			this.workingtheme.addRule(new ThemePair(this.backgroundgen, this.brodergen, this.textPaint), ThemeDynamic.englishToRuleSet(this.elementStr), (this.elementStr.equals("Button") || this.elementStr.equals("Selection Boxes"))) ;
			this.screen.setCustomTheme(0,this.workingtheme);
		}
	}
	private void updateThemeData()
	{
		this.workingtheme.addRule(new ThemePair(this.backgroundgen, this.brodergen, this.textPaint), ThemeDynamic.englishToRuleSet(this.elementStr), (this.elementStr.equals("Button") || this.elementStr.equals("Selection Boxes"))) ;
		this.screen.setCustomTheme(this.workingthemeindex,this.workingtheme);
	}
	private void printCurrentTheme()
	{
	}
}
