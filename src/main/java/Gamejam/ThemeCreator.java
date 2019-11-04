package Gamejam;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import model.SanityCheckFailedException;

/**
 * The UI and relevant functions to create custom themes.
 * @author Joey McMaster
 *
 */
public class ThemeCreator extends HBox 
{
	private ColorPicker colorpicker;
	private Paint currentworkingcolor;
	
	private Paint curTextColor;
	private Paint curBorderColor;
	private Paint curBackgroundColor;
	
	private VBox leftcol;
	private GamejamMainScreenTheme screen;
	private Rectangle previewarea;
	private ChoiceBox<String> elementselector;
	private ChoiceBox<String> groupselector;
	private ArrayList<RegionPair> regions;
	private HashMap<String,Integer> regiondescriptionmap;
	private HashMap<String,Integer> regiongroupmap;
	private int currentworkingid = 0;
	private int currentgroupid = 0;
	private boolean themegroupmode = false;
	private boolean doinitonce = true;
	private BorderStrokeStyle currentborderstyle = BorderStrokeStyle.SOLID;
	private Border currentborder = GamejamMainScreenTheme.noBorder();
	private int currentborderwidths = 0;
	public ThemeCreator(GamejamMainScreenTheme screen) 
	{
		this.screen = screen;
		this.previewarea = new Rectangle(150,28, Color.WHITESMOKE);
		this.currentworkingcolor = Color.WHITE;
		this.curTextColor = Color.BLACK;
		this.curBorderColor = Color.BLACK;
		this.curBackgroundColor = Color.WHITE;
		this.leftcol = new VBox();
		this.colorpicker = new ColorPicker();
		this.groupselector = new ChoiceBox<String>();
		this.elementselector = new ChoiceBox<String>();
		this.regiondescriptionmap = new HashMap<String,Integer>();
		this.regiongroupmap = new HashMap<String,Integer>();
		populateRegionGroups();
		
		this.groupselector.getItems().addAll(this.regiongroupmap.keySet());
		this.groupselector.setValue("All Buttons");
		this.groupselector.setOnAction((click) -> 
		{
			this.themegroupmode = true;
			this.currentgroupid =  this.regiongroupmap.get(this.groupselector.getValue());
			System.out.println(this.currentgroupid);
		});
		this.colorpicker.setOnAction((click) -> 
		{
			this.currentworkingcolor = this.colorpicker.getValue();
			System.out.println(this.currentworkingcolor);
		});
		
		HBox topleftbox = new HBox();
		HBox middletopleftbox = new HBox();
		Label infotext = new Label("        Color Selector                                            Element Selector                                                          Group Selector                               Preview");
		topleftbox.getChildren().add(infotext);
		middletopleftbox.getChildren().addAll(this.colorpicker,this.elementselector,this.groupselector,this.previewarea);
		
		HBox updatebox = new HBox();
		Button updatePreview = new Button("Update Preview");
		updatePreview.setOnAction((click) -> 
		{
			this.previewarea.setFill(this.currentworkingcolor);
		});
		
		Button updateTheme = new Button("Update Theme");
		updateTheme.setOnAction((click) -> 
		{
			ThemePair p = new ThemePair(GamejamMainScreenTheme.solidBackgroundSetup(this.curBackgroundColor),this.currentborder,this.curTextColor);
			if (this.themegroupmode)
			{
				applySettingToGroup(p);
			} 
			else 
			{
				this.screen.getCustomTheme().setThemeData(this.currentworkingid, p);
			}
		});
		Button setTheme = new Button("Set Custom Theme");
		setTheme.setOnAction((click) -> 
		{
			this.screen.updateTheme(-1);
		});
		Button resetCustomTheme = new Button("Reset Custom Theme");
		resetCustomTheme.setOnAction((click) -> 
		{
			this.screen.resetCustomTheme();
		});
	
		updatebox.getChildren().addAll(updateTheme,updatePreview,setTheme,resetCustomTheme);
		
		
		//HBox backgroundopts = new HBox();
		HBox borderopts = new HBox();
		Button border_fetchPaint = new Button("Set Border Color");
		Button border_finish = new Button("Create Border");
		ChoiceBox<String> border_styleing = new ChoiceBox<String>();
		TextField border_widths = new TextField();
		CheckBox border_top = new CheckBox("Draw Top");
		CheckBox border_bottom = new CheckBox("Draw Bottom");
		CheckBox border_left = new CheckBox("Draw Left");
		CheckBox border_right = new CheckBox("Draw Right");
		border_top.setSelected(true);
		border_bottom.setSelected(true);
		border_left.setSelected(true);
		border_right.setSelected(true);
		
		border_fetchPaint.setOnAction((click) -> 
		{
			this.curBorderColor = this.currentworkingcolor;
		});
		
		border_styleing.getItems().addAll("Solid","Dashed","Dotted");
		border_styleing.setValue("Solid");
		border_styleing.setOnAction((click) -> 
		{
			String borderstyle = border_styleing.getValue();
			if (borderstyle.equals("Solid"))
			{
				this.currentborderstyle = BorderStrokeStyle.SOLID;
			} 
			else if (borderstyle.equals("Dashed"))
			{
				this.currentborderstyle = BorderStrokeStyle.DASHED;
			}
			else
			{
				this.currentborderstyle = BorderStrokeStyle.DOTTED;
			}
		});
		//this.curBorderColor
		border_finish.setOnAction((click) -> 
		{
			Gamejam.DPrint("Border finished!");
			// Apply Checkbox settings
			BorderStrokeStyle ts = this.currentborderstyle;
			BorderStrokeStyle bs = this.currentborderstyle;
			BorderStrokeStyle ls = this.currentborderstyle;
			BorderStrokeStyle rs = this.currentborderstyle;
			if (!border_top.isSelected())
			{
				ts = BorderStrokeStyle.NONE;
			}
			if (!border_bottom.isSelected())
			{
				bs = BorderStrokeStyle.NONE;
			}
			if (!border_left.isSelected())
			{
				ls = BorderStrokeStyle.NONE;
			}
			if (!border_right.isSelected())
			{
				rs = BorderStrokeStyle.NONE;
			}
			// Fetch width
			this.currentborderwidths = getWidthFromStr(border_widths.getText());
			
			this.currentborder = new Border(new BorderStroke(this.curBorderColor,this.curBorderColor,this.curBorderColor,this.curBorderColor,ts,rs,bs,ls,CornerRadii.EMPTY,new BorderWidths(this.currentborderwidths),new Insets(0)));
		});
		borderopts.getChildren().addAll(border_fetchPaint, border_styleing,border_widths,border_top,border_bottom,border_left,border_right,border_finish);
		
		this.leftcol.getChildren().addAll(topleftbox,middletopleftbox,borderopts,updatebox);
		this.getChildren().addAll(this.leftcol);
		
		this.screen.addRegion(400, this.leftcol, "Theme Creator left vbox background", new ThemeRegionProp(ThemeRegionProp.VBOX));
		this.screen.addRegion(401, this.elementselector, "Theme Creator Element Selector Combo Box", new ThemeRegionProp(ThemeRegionProp.COMBOBOX));
		this.screen.addRegion(402, this.groupselector, "Theme Creator Element Group Combo Box", new ThemeRegionProp(ThemeRegionProp.COMBOBOX));
		this.screen.addRegion(403, topleftbox, "Theme Creator top label background box", new ThemeRegionProp(ThemeRegionProp.HBOX));
		this.screen.addRegion(404, middletopleftbox, "Theme Creator top selections background box", new ThemeRegionProp(ThemeRegionProp.HBOX));
		this.screen.addRegion(405, infotext, "Theme Creator top left label", new ThemeRegionProp(ThemeRegionProp.LABEL));
		this.screen.addRegion(406, updatebox, "Theme Creator middle update buttons background box", new ThemeRegionProp(ThemeRegionProp.HBOX));
		this.screen.addRegion(407, updatePreview, "Theme Creator update preview button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT));
		this.screen.addRegion(408, updateTheme, "Theme Creator update theme button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT));
		this.screen.addRegion(409, setTheme, "Theme Creator set theme button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT));
		this.screen.addRegion(410, resetCustomTheme, "Theme Creator reset theme button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT));
		this.screen.addRegion(411, borderopts, "Theme Creator Borders Creator HBox", new ThemeRegionProp(ThemeRegionProp.HBOX));
		this.screen.addRegion(412, border_fetchPaint, "Theme Creator get color Button", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT));
		this.screen.addRegion(413, border_finish, "Theme Creator Border Finalize", new ThemeRegionProp(ThemeRegionProp.BUTTON_WT));
		this.screen.addRegion(414, border_styleing, "Theme Creator Border Styling Choice Box", new ThemeRegionProp(ThemeRegionProp.COMBOBOX));
		this.screen.addRegion(415, border_widths, "Theme Creator Border TextField", new ThemeRegionProp(ThemeRegionProp.TEXTINPUT));
		this.screen.addRegion(416, border_top, "Theme Creator Top Border Toggle Check Box", new ThemeRegionProp(ThemeRegionProp.CHECKBOX));
		this.screen.addRegion(417, border_bottom, "Theme Creator Bottom Border Toggle Check Box", new ThemeRegionProp(ThemeRegionProp.CHECKBOX));
		this.screen.addRegion(418, border_left, "Theme Creator Left Border Toggle Check Box", new ThemeRegionProp(ThemeRegionProp.CHECKBOX));
		this.screen.addRegion(419, border_right, "Theme Creator Right Border Toggle Check Box", new ThemeRegionProp(ThemeRegionProp.CHECKBOX));
	}
	/**
	 * Itended to take numbers from strings and format them as int
	 * @param str The input string containing only numbers.
	 * @return The number within the input string, If no valid number is found, returns 1.
	 */
	private int getWidthFromStr(String str)
	{
		int retval = 0;
		try
		{
			retval = Integer.valueOf(str);
		} 
		catch (NumberFormatException e)
		{
			return 1;
		}
		return retval;
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
			this.regiondescriptionmap.put(this.regions.get(x).getDescription(), this.regions.get(x).getIndex());
		}
		
		this.elementselector.setOnAction((click) -> 
		{
			this.themegroupmode = false;
			this.currentworkingid = this.regiondescriptionmap.get(elementselector.getValue());
			System.out.println("wid: " + this.currentworkingid);
		});
		this.elementselector.setValue("Theme Creator left vbox background");
		this.screen.resetCustomTheme();
	}
	
	
	
	private void populateRegionGroups()
	{
		this.regiongroupmap.put("All Buttons",0);
		this.regiongroupmap.put("All Buttons with Themeable Text",1);
		this.regiongroupmap.put("All Background Regions",2);
		this.regiongroupmap.put("All Labels",3);
		this.regiongroupmap.put("All Text Input Fields",4);
		this.regiongroupmap.put("All Panes",5);
		this.regiongroupmap.put("All Themeable Text",6);
		this.regiongroupmap.put("Everything",7);
	}
	private void applySettingToGroup(ThemePair p)
	{
		for(int x = 0; x < this.regions.size(); x++)
		{
			if (this.currentgroupid == 7)
			{
				this.screen.setCustomThemeData(x,p);
				continue;
			}
			ThemeRegionProp pr = this.regions.get(x).getProperties();
			if (pr.isButton() && this.currentgroupid < 2)
			{
				if (this.currentgroupid == 0)
				{
					this.screen.setCustomThemeData(x,p);
					continue;
				}
				else if (pr.isButtonWithText())
				{
					this.screen.setCustomThemeData(x,p);
					continue;
				}
			}
			
			if (pr.isBoundingArea() && (this.currentgroupid == 2 || this.currentgroupid == 5))
			{
				if (this.currentgroupid == 2)
				{
					this.screen.setCustomThemeData(x,p);
					continue;
				}
				else if (pr.isPane())
				{
					this.screen.setCustomThemeData(x,p);
					continue;
				}
			}
			
			if (pr.canColorText() && (this.currentgroupid == 3 || this.currentgroupid == 6))
			{
				if (this.currentgroupid == 6)
				{
					this.screen.setCustomThemeData(x,p);
					continue;
				}
				else if (pr.isLabel())
				{
					this.screen.setCustomThemeData(x,p);
					continue;
				}
			}
			
			if (pr.isTextInputField() && this.currentgroupid == 4)
			{
				this.screen.setCustomThemeData(x,p);
				continue;
			}
		}
	}
}
