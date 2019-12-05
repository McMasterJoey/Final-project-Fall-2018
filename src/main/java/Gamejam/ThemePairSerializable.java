package Gamejam;

import java.io.Serializable;

/**
 * A serializeable form of the Theme Pair
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public class ThemePairSerializable implements Serializable {
	
	private static final long serialVersionUID = 1;
	
	int brred;
	int tred;
	int bared;
	int tblue;
	int brblue;
	int bablue;
	int tgreen;
	int brgreen;
	int bagreen;
	int borderWidth;
	String css;
	String borderstyle;
	
	public ThemePairSerializable(String css, int tred, int tgreen, int tblue, int borderWidth, String borderstyle,int brred, int brgreen, int brblue,int bared, int bagreen, int bablue)
	{
		this.brred = brred;
		this.tred = tred;
		this.bared = bared;
		this.tblue = tblue;
		this.brblue = brblue;
		this.bablue = bablue;
		this.tgreen = tgreen;
		this.brgreen = brgreen;
		this.bagreen = bagreen;
		this.borderWidth = borderWidth;
		this.css = css;
		this.borderstyle = borderstyle;
	}
	public String getCSS()
	{
		return this.css;
	}
	public int[] getTextColor()
	{
		int[] retval = new int[3];
		retval[0] = this.tred; 
		retval[1] = this.tgreen;
		retval[2] = this.tblue;
		return retval;
	}
	public int[] getBorderColor()
	{
		int[] retval = new int[3];
		retval[0] = this.brred; 
		retval[1] = this.brgreen;
		retval[2] = this.brblue;
		return retval;
	}
	public String getBorderStyle()
	{
		return this.borderstyle;
	}
	public int getborderWidth()
	{
		return this.borderWidth;
	}
	public int[] getBackgroundColor()
	{
		int[] retval = new int[3];
		retval[0] = this.bared; 
		retval[1] = this.bagreen;
		retval[2] = this.bablue;
		return retval;
	}
}
