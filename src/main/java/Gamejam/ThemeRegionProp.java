package Gamejam;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Defines a property set to be filled out by each themeable region element.
 * 
 * Also uses an alternate Bracket formating to shut Nick up. Nick if you're reading this, YOU HAPPY NOW?!?!?!
 * @author Joey McMaster
 *
 */
public class ThemeRegionProp implements Serializable
{
	private static final long serialVersionUID = 3;
	
	private HashMap<String,Integer> nameToIndex = new HashMap<String,Integer>();
	private boolean[] bol_array;
	
	public static final byte NONE = 0;
	public static final byte VBOX = 1;
	public static final byte HBOX = 2;
	public static final byte LABEL = 3;
	public static final byte BORDERPANE = 4;
	public static final byte GRIDPANE = 5;
	public static final byte PROGRESSBAR = 6;
	public static final byte TABLEVIEW = 7;
	public static final byte SCROLLPANE = 8;
	public static final byte TEXTINPUT = 9;
	public static final byte COMBOBOX = 10;
	public static final byte BUTTON_WT = 11;
	public static final byte CHECKBOX = 12;
	public static final byte BUTTON = 13;
	public static final byte COLORPICKER = 14;
	
	// General UI Positioning
	public static final byte LOC_TB = -1;
	public static final byte LOC_LB = -2;
	public static final byte LOC_MI = -3;
	
	// Specific sectional Positioning
	public static final byte LOC_TB_LI = -4;
	public static final byte LOC_TB_NLI = -5;
	public static final byte LOC_TB_UTIL = -6;
	public static final byte LOC_TB_LI_IG = -7;
	public static final byte LOC_TB_NLI_IG = -8;
	
	public static final byte LOC_MI_SG = -9;
	public static final byte LOC_MI_IG = -10;
	public static final byte LOC_MI_CA = -11;
	public static final byte LOC_MI_USM = -12;
	public static final byte LOC_MI_TM = -13;
	public static final byte LOC_MI_BTM = -14;
	public static final byte LOC_MI_ATM = -15;
	public static final byte LOC_MI_LB = -16;
	public static final byte LOC_MI_GH = -17;
	public static final byte LOC_MI_OV = -18;
	// Miss Properties
	public static final byte INT_REG = -100; // The element merely holds another and might not theme well.
	
	public ThemeRegionProp(byte primaryproperty, byte locationid, byte missproperty)
	{
		init_map();
		presetAttributesUtil(primaryproperty);
		presetLocationUtil(locationid);
		if (missproperty == INT_REG)
		{
			this.bol_array[this.nameToIndex.get("IntermediateArea")] = true;
			this.bol_array[this.nameToIndex.get("NotIntermediateArea")] = false;
		}
	}
	public ThemeRegionProp(byte primaryproperty, byte locationid)
	{
		init_map();
		presetAttributesUtil(primaryproperty);
		presetLocationUtil(locationid);
	}
	private void init_map()
	{
		this.nameToIndex.put("Button",0);
		this.nameToIndex.put("HBox",1);
		this.nameToIndex.put("VBox",2);
		this.nameToIndex.put("Label",3);
		this.nameToIndex.put("BorderPane",4);
		this.nameToIndex.put("GridPane",5);
		this.nameToIndex.put("TableView",6);
		this.nameToIndex.put("ProgressBar",7);
		this.nameToIndex.put("ScrollPane",8);
		this.nameToIndex.put("TextInputField",9);
		this.nameToIndex.put("ComboBox",10);
		this.nameToIndex.put("CheckBox",11);
		this.nameToIndex.put("BoundingArea",12);
		this.nameToIndex.put("IntermediateArea",13);
		this.nameToIndex.put("TopBar",14);
		this.nameToIndex.put("LeftBar",15);
		this.nameToIndex.put("Middle",16);
		this.nameToIndex.put("GamesMenu",17);
		this.nameToIndex.put("UserSettingsMenu",18);
		this.nameToIndex.put("ThemeMenu",19);
		this.nameToIndex.put("BasicThemeMenu",20);
		this.nameToIndex.put("AdvancedThemeMenu",21);
		this.nameToIndex.put("CreateAccountMenu",22);
		this.nameToIndex.put("UsedWhileLoggedIn",23);
		this.nameToIndex.put("NotUsedWhileLoggedIn",24);
		this.nameToIndex.put("HasGeneralLocationInfo",25);
		this.nameToIndex.put("ContainsText",26);
		this.nameToIndex.put("ContainsColorableText",27);
		this.nameToIndex.put("ContainsThemeableMainUIImages",28);
		this.nameToIndex.put("DoNotTreatAsRegion",29);
		this.nameToIndex.put("UsedWhileInGame",30);
		this.nameToIndex.put("IsGame",31);
		this.nameToIndex.put("LeaderBoard",32);
		this.nameToIndex.put("ButtonWithImage",33);
		this.nameToIndex.put("GameHistoryMenu",34);
		this.nameToIndex.put("NotIntermediateArea",35);
		this.nameToIndex.put("ColorPicker",36);
		this.nameToIndex.put("TreatAsButton",37);
		this.nameToIndex.put("OverallPane",38);
		
		this.bol_array = new boolean[this.nameToIndex.size()];
		for(int x = 0; x < this.bol_array.length; x++)
		{
			this.bol_array[x] = false;
		}
		this.bol_array[35] = true;
		
	}
	private void presetLocationUtil(byte locationid)
	{
		if (locationid == LOC_MI_GH)
		{
			this.bol_array[this.nameToIndex.get("Middle")] = true;
			this.bol_array[this.nameToIndex.get("GameHistoryMenu")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
			this.bol_array[this.nameToIndex.get("UsedWhileLoggedIn")] = true;
		}
		if (locationid == LOC_MI_OV)
		{
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
			this.bol_array[this.nameToIndex.get("OverallPane")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
		}
		if (locationid == LOC_TB)
		{
			this.bol_array[this.nameToIndex.get("TopBar")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
		}
		if (locationid == LOC_LB)
		{
			this.bol_array[this.nameToIndex.get("LeftBar")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
		}
		if (locationid == LOC_MI)
		{
			this.bol_array[this.nameToIndex.get("Middle")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
		}
		if (locationid == LOC_TB_LI)
		{
			this.bol_array[this.nameToIndex.get("TopBar")] = true;
			this.bol_array[this.nameToIndex.get("UsedWhileLoggedIn")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
		}
		if (locationid == LOC_TB_NLI)
		{
			this.bol_array[this.nameToIndex.get("TopBar")] = true;
			this.bol_array[this.nameToIndex.get("NotUsedWhileLoggedIn")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
		}
		if (locationid == LOC_TB_LI_IG)
		{
			this.bol_array[this.nameToIndex.get("TopBar")] = true;
			this.bol_array[this.nameToIndex.get("UsedWhileLoggedIn")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
			this.bol_array[this.nameToIndex.get("UsedWhileInGame")] = true;
		}
		if (locationid == LOC_TB_NLI_IG)
		{
			this.bol_array[this.nameToIndex.get("TopBar")] = true;
			this.bol_array[this.nameToIndex.get("NotUsedWhileLoggedIn")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
			this.bol_array[this.nameToIndex.get("UsedWhileInGame")] = true;
		}
		if (locationid == LOC_TB_UTIL)
		{
			this.bol_array[this.nameToIndex.get("TopBar")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
		}
		
		if (locationid == LOC_MI_SG)
		{
			this.bol_array[this.nameToIndex.get("Middle")] = true;
			this.bol_array[this.nameToIndex.get("GamesMenu")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
		}
		
		if (locationid == LOC_MI_IG)
		{
			this.bol_array[this.nameToIndex.get("Middle")] = true;
			this.bol_array[this.nameToIndex.get("UsedWhileInGame")] = true;
			this.bol_array[this.nameToIndex.get("IsGame")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
		}
		
		if (locationid == LOC_MI_CA)
		{
			this.bol_array[this.nameToIndex.get("Middle")] = true;
			this.bol_array[this.nameToIndex.get("CreateAccountMenu")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
			this.bol_array[this.nameToIndex.get("NotUsedWhileLoggedIn")] = true;
		}
		
		if (locationid == LOC_MI_USM)
		{
			this.bol_array[this.nameToIndex.get("Middle")] = true;
			this.bol_array[this.nameToIndex.get("UserSettingsMenu")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
			this.bol_array[this.nameToIndex.get("UsedWhileLoggedIn")] = true;
		}
		
		if (locationid == LOC_MI_TM)
		{
			this.bol_array[this.nameToIndex.get("Middle")] = true;
			this.bol_array[this.nameToIndex.get("ThemeMenu")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
			this.bol_array[this.nameToIndex.get("UsedWhileLoggedIn")] = true;
		}
		if (locationid == LOC_MI_BTM)
		{
			this.bol_array[this.nameToIndex.get("Middle")] = true;
			this.bol_array[this.nameToIndex.get("BasicThemeMenu")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
			this.bol_array[this.nameToIndex.get("UsedWhileLoggedIn")] = true;
		}
		if (locationid == LOC_MI_ATM)
		{
			this.bol_array[this.nameToIndex.get("Middle")] = true;
			this.bol_array[this.nameToIndex.get("AdvancedThemeMenu")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
			this.bol_array[this.nameToIndex.get("UsedWhileLoggedIn")] = true;
		}
		if (locationid == LOC_MI_LB)
		{
			this.bol_array[this.nameToIndex.get("Middle")] = true;
			this.bol_array[this.nameToIndex.get("LeaderBoard")] = true;
			this.bol_array[this.nameToIndex.get("HasGeneralLocationInfo")] = true;
		}
		
	}
	private void presetAttributesUtil(byte primaryproperty) 
	{
		if (primaryproperty == BUTTON)
		{
			this.bol_array[this.nameToIndex.get("Button")] = true;
			this.bol_array[this.nameToIndex.get("ButtonWithImage")] = true;
			this.bol_array[this.nameToIndex.get("TreatAsButton")] = true;
		}
		else if (primaryproperty == BUTTON_WT)
		{
			this.bol_array[this.nameToIndex.get("Button")] = true;
			this.bol_array[this.nameToIndex.get("ContainsText")] = true;
			this.bol_array[this.nameToIndex.get("ContainsColorableText")] = true;
			this.bol_array[this.nameToIndex.get("TreatAsButton")] = true;
		}
		else if (primaryproperty == CHECKBOX)
		{
			this.bol_array[this.nameToIndex.get("ContainsText")] = true;
			this.bol_array[this.nameToIndex.get("ContainsColorableText")] = true;
			this.bol_array[this.nameToIndex.get("CheckBox")] = true;
		}
		else if (primaryproperty == VBOX)
		{
			this.bol_array[this.nameToIndex.get("VBox")] = true;
			this.bol_array[this.nameToIndex.get("BoundingArea")] = true;
		}
		else if (primaryproperty == HBOX)
		{
			this.bol_array[this.nameToIndex.get("HBox")] = true;
			this.bol_array[this.nameToIndex.get("BoundingArea")] = true;
		}
		else if (primaryproperty == LABEL)
		{
			this.bol_array[this.nameToIndex.get("Label")] = true;
			this.bol_array[this.nameToIndex.get("ContainsText")] = true;
			this.bol_array[this.nameToIndex.get("ContainsColorableText")] = true;
			this.bol_array[this.nameToIndex.get("DoNotTreatAsRegion")] = true;
		}
		else if (primaryproperty == BORDERPANE)
		{
			this.bol_array[this.nameToIndex.get("BorderPane")] = true;
			this.bol_array[this.nameToIndex.get("BoundingArea")] = true;
		}
		else if (primaryproperty == GRIDPANE)
		{
			this.bol_array[this.nameToIndex.get("GridPane")] = true;
			this.bol_array[this.nameToIndex.get("BoundingArea")] = true;
		}
		else if (primaryproperty == PROGRESSBAR)
		{
			this.bol_array[this.nameToIndex.get("ProgressBar")] = true;
		}
		else if (primaryproperty == TABLEVIEW)
		{
			this.bol_array[this.nameToIndex.get("TableView")] = true;
			this.bol_array[this.nameToIndex.get("ContainsText")] = true;
		}
		else if (primaryproperty == SCROLLPANE)
		{
			this.bol_array[this.nameToIndex.get("ScrollPane")] = true;
			this.bol_array[this.nameToIndex.get("BoundingArea")] = true;
		}
		else if (primaryproperty == TEXTINPUT)
		{
			this.bol_array[this.nameToIndex.get("TextInputField")] = true;
			this.bol_array[this.nameToIndex.get("ContainsText")] = true;
			this.bol_array[this.nameToIndex.get("DoNotTreatAsRegion")] = true;
		}
		else if (primaryproperty == COMBOBOX)
		{
			this.bol_array[this.nameToIndex.get("ComboBox")] = true;
			this.bol_array[this.nameToIndex.get("TreatAsButton")] = true;
		}
		else if (primaryproperty == COLORPICKER)
		{
			this.bol_array[this.nameToIndex.get("ColorPicker")] = true;
			this.bol_array[this.nameToIndex.get("TreatAsButton")] = true;
		}
	}
	/**
	 * Sets the property of is it a Bounding Area
	 * @param value The boolean to set this to.
	 */
	public void setIsBoundingArea(boolean value) 
	{
		this.bol_array[this.nameToIndex.get("BoundingArea")] = value;
	}
	/**
	 * Sets the property of is it a put in the Top Bar
	 * @param value The boolean to set this to.
	 */
	public void setIsInTopBar(boolean value) 
	{
		this.bol_array[this.nameToIndex.get("TopBar")] = value;
	}
	/**
	 * Sets the property of is it a put in the middle
	 * @param value The boolean to set this to.
	 */
	public void setIsInMiddle(boolean value) 
	{
		this.bol_array[this.nameToIndex.get("Middle")] = value;
	}
	/**
	 * Sets the property of is it put in the left Bar
	 * @param value The boolean to set this to.
	 */
	public void setIsInLeftBar(boolean value) 
	{
		this.bol_array[this.nameToIndex.get("LeftBar")] = value;
	}
	/**
	 * Sets the property of is it contains text
	 * @param value The boolean to set this to.
	 */
	public void setIsContainsText(boolean value) 
	{
		this.bol_array[this.nameToIndex.get("ContainsText")] = value;
	}
	/**
	 * Sets the property of is has Colorable text.
	 * @param value The boolean to set this to.
	 */
	public void setIsContainsColorableText(boolean value) 
	{
		this.bol_array[this.nameToIndex.get("ContainsColorableText")] = value;
	}
	/**
	 * 
	 * @param value
	 */
	public void setIsContainsThemeableMainGUIImages(boolean value)
	{
		this.bol_array[this.nameToIndex.get("ContainsThemeableMainUIImages")] = value;
	}
	
	/**
	 * Used primarily for encoding Dynamic Theme generation.
	 * Checks if every value represented by the integers in the input are true.
	 * If they are, then the element this object represents the properties of is true given the expression.
	 * Otherwise returns false.
	 * @param domain A collection of ints that represent a sequence of True statements.
	 * @return If every int in the collection maps to a true value, then return true. Otherwise returns false.
	 */
	public boolean isAllTrue(Collection<Integer> domain)
	{
		Iterator<Integer> iter = domain.iterator();
		while(iter.hasNext())
		{
			if (!this.bol_array[iter.next()])
			{
				return false;
			}
		}
		return true;
	}
	/**
	 * Used primarily for encoding Dynamic Theme generation.
	 * Checks if atleast one value represented by the integers in the input are true.
	 * If they are, then the element this object represents the properties of is true given the expression.
	 * Otherwise returns false.
	 * @param domain A collection of ints that represent a sequence of True statements.
	 * @return If atleast one int in the collection maps to a true value, then return true. Also returns true of the collection is empty. Otherwise returns false.
	 */
	public boolean isAtleastOneTrue(Collection<Integer> domain)
	{
		if (domain.size() == 0)
		{
			return true;
		}
		
		Iterator<Integer> iter = domain.iterator();
		while(iter.hasNext())
		{
			if (this.bol_array[iter.next()])
			{
				return true;
			}
		}
		return false;
	}
	public boolean isTrue(int domain)
	{
		return this.bol_array[domain];
	}
	
	public boolean isTrue(String propertyname)
	{
		return this.bol_array[this.nameToIndex.get(propertyname)];
	}
}
