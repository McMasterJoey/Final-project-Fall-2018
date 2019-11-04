package Gamejam;
/**
 * Defines a property set to be filled out by each themeable region element.
 * 
 * Also uses an alternate Bracket formating to shut Nick up. Nick if you're reading this, YOU HAPPY NOW?!?!?!
 * @author Joey McMaster
 *
 */
public class ThemeRegionProp 
{
	private boolean isButton = false;
	private boolean isHBox = false;
	private boolean isVBox = false;
	private boolean isLabel = false;
	private boolean isBorderPane = false;
	private boolean isGridPane = false;
	private boolean isTableView = false;
	private boolean isProgressBar = false;
	private boolean isScrollPane = false;
	private boolean isTextInputField = false;
	private boolean isComboBox = false;
	private boolean isCheckBox = false;
	
	private boolean isGeneralPane = false;
	private boolean isBoundingArea = false;
	private boolean isPutInUpperBar = false;
	private boolean isPutInMiddle = false;
	private boolean isPutInLeftPane = false;
	private boolean isContainsText = false;
	private boolean isContainsColorableText = false;
	private boolean isContainsThemeableMainUIImages = false;
	private boolean doNotTreatAsRegion = false;
	
	public static final byte BUTTON = 0;
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
	
	/**
	 * Primary Constructor for the ThemeRegionProp class
	 * Takes an int that represents what the region object is.
	 * @param primaryproperty Refer to the Constants included with the class.
	 */
	public ThemeRegionProp(byte primaryproperty)
	{
		if (primaryproperty == 0)
		{
			this.isButton = true;
		}
		else if (primaryproperty == 11)
		{
			this.isButton = true;
			this.isContainsText = true;
			this.isContainsColorableText = true;
		}
		else if (primaryproperty == 12)
		{
			this.isContainsColorableText = true;
			this.isContainsText = true;
			this.isCheckBox = true;
		}
		else if (primaryproperty == 1)
		{
			this.isVBox = true;
			this.isBoundingArea = true;
		}
		else if (primaryproperty == 2)
		{
			this.isHBox = true;
			this.isBoundingArea = true;
		}
		else if (primaryproperty == 3)
		{
			this.isLabel = true;
			this.isContainsText = true;
			this.isContainsColorableText = true;
			this.doNotTreatAsRegion = true;
		}
		else if (primaryproperty == 4)
		{
			this.isBorderPane = true;
			this.isGeneralPane = true;
			this.isBoundingArea = true;
		}
		else if (primaryproperty == 5)
		{
			this.isGridPane = true;
			this.isGeneralPane = true;
			this.isBoundingArea = true;
		}
		else if (primaryproperty == 6)
		{
			this.isProgressBar = true;
		}
		else if (primaryproperty == 7)
		{
			this.isTableView = true;
			this.isContainsText = true;
		}
		else if (primaryproperty == 8)
		{
			this.isScrollPane = true;
			this.isGeneralPane = true;
			this.isBoundingArea = true;
		}
		else if (primaryproperty == 9)
		{
			this.isTextInputField = true;
			this.isContainsText = true;
			this.doNotTreatAsRegion = true;
		}
		else if (primaryproperty == 10)
		{
			this.isComboBox = true;
			//this.isContainsText = true;
		}
	}
	/**
	 * Sets the property is it a Button.
	 * @param value The boolean to set this to.
	 */
	public void setIsButton(boolean value) 
	{
		this.isButton = value;
	}
	/**
	 * Sets the property of is it a HBox
	 * @param value The boolean to set this to.
	 */
	public void setIsHBox(boolean value) 
	{
		this.isHBox = value;
	}
	
	/**
	 * Sets the property of is it a BorderPane
	 * @param value The boolean to set this to.
	 */
	public void setIsBorderPane(boolean value) 
	{
		this.isBorderPane = value;
	}
	/**
	 * Sets the property of is it a VBox
	 * @param value The boolean to set this to.
	 */
	public void setIsVBox(boolean value) 
	{
		this.isVBox = value;
	}
	/**
	 * Sets the property of is it a Label
	 * @param value The boolean to set this to.
	 */
	public void setIsLabel(boolean value) 
	{
		this.isLabel = value;
	}
	/**
	 * Sets the property of is it a GridPane
	 * @param value The boolean to set this to.
	 */
	public void setIsGridPane(boolean value) 
	{
		this.isGridPane = value;
	}
	/**
	 * Sets the property of is it a TableView
	 * @param value The boolean to set this to.
	 */
	public void setIsTableView(boolean value) 
	{
		this.isTableView = value;
	}
	/**
	 * Sets the property of is it a Progress Bar
	 * @param value The boolean to set this to.
	 */
	public void setIsProgressBar(boolean value) 
	{
		this.isProgressBar = value;
	}
	/**
	 * Sets the property of is it a Scroll Pane
	 * @param value The boolean to set this to.
	 */
	public void setIsScrollPane(boolean value) 
	{
		this.isScrollPane = value;
	}
	
	/**
	 * Sets the property of is it a General Pane
	 * @param value The boolean to set this to.
	 */
	public void setIsPane(boolean value) 
	{
		this.isGeneralPane = value;
	}
	/**
	 * Sets the property of is it a Bounding Area
	 * @param value The boolean to set this to.
	 */
	public void setIsBoundingArea(boolean value) 
	{
		this.isBoundingArea = value;
	}
	/**
	 * Sets the property of is it a put in the Upper Bar
	 * @param value The boolean to set this to.
	 */
	public void setIsPutInUpperBar(boolean value) 
	{
		this.isPutInUpperBar = value;
	}
	/**
	 * Sets the property of is it a put in the middle
	 * @param value The boolean to set this to.
	 */
	public void setIsPutInMiddle(boolean value) 
	{
		this.isPutInMiddle = value;
	}
	/**
	 * Sets the property of is it put in the left Pane
	 * @param value The boolean to set this to.
	 */
	public void setIsPutInLeftPane(boolean value) 
	{
		this.isPutInLeftPane = value;
	}
	/**
	 * Sets the property of is it contains text
	 * @param value The boolean to set this to.
	 */
	public void setIsContainsText(boolean value) 
	{
		this.isContainsText = value;
	}
	/**
	 * Sets the property of is it a text input field includes password and text fields.
	 * @param value The boolean to set this to.
	 */
	public void setIsTextInputField(boolean value)
	{
		this.isTextInputField = value;
	}
	/**
	 * Sets the property of is it a combo box.
	 * @param value The boolean to set this to.
	 */
	public void setIsComboBox(boolean value) 
	{
		this.isContainsText = value;
	}
	/**
	 * Sets the property of is has Colorable text.
	 * @param value The boolean to set this to.
	 */
	public void setIsContainsColorableText(boolean value) 
	{
		this.isContainsColorableText = value;
	}
	public void setIsContainsThemeableMainGUIImages(boolean value)
	{
		this.isContainsThemeableMainUIImages = value;
	}
	///////////////////////// ANSWER QUESTIONS METHODS ////////////////
	public boolean canColorText() 
	{
		return this.isContainsColorableText;
	}
	public boolean isButton()
	{
		return this.isButton;
	}
	public boolean isPane()
	{
		return this.isGeneralPane;
	}
	public boolean isBoundingArea()
	{
		return this.isBoundingArea;
	}
	public boolean isButtonWithText()
	{
		return (this.isButton && this.isContainsColorableText);
	}
	public boolean isContainsThemeableMainGUIImages()
	{
		return this.isContainsThemeableMainUIImages;
	}
	public boolean doNotTreatAsRegion()
	{
		return this.doNotTreatAsRegion;
	}
	public boolean isLabel()
	{
		return this.isLabel;
	}
	public boolean isComboBox()
	{
		return this.isComboBox;
	}
	public boolean isBorderPane()
	{
		return this.isBorderPane;
	}
	public boolean isGridPane()
	{
		return this.isGridPane;
	}
	public boolean isHBox()
	{
		return this.isHBox;
	}
	public boolean isVBox()
	{
		return this.isVBox;
	}
	public boolean isTableView()
	{
		return this.isTableView;
	}
	public boolean isProgressBar()
	{
		return this.isProgressBar;
	}
	public boolean isScrollPane()
	{
		return this.isScrollPane;
	}
	public boolean isTextInputField()
	{
		return this.isTextInputField;
	}
	public boolean isCheckBox()
	{
		return this.isCheckBox;
	}
}
