package Gamejam;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Houses all critical data for a Theme or Dynamic Theme. 
 * This is only to be used as a medium between the disk and the program.
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public class ThemeSerializable implements Serializable
{
	private static final long serialVersionUID = 1;
	private String icon;
	private boolean isDynamic;
	private ArrayList<RulePairSerializable> rules;
	private ArrayList<ThemePairSerializable> themepairs;
	private ArrayList<String> filepaths;
	private String name;
	public ThemeSerializable(String name, ArrayList<RulePair> rules, ArrayList<ThemePair> themepairs, ArrayList<String> filepaths) 
	{
		this.isDynamic = true;
		
		this.rules =  new ArrayList<RulePairSerializable>();
		for(int x = 0; x < rules.size(); x++)
		{
			this.rules.add(rules.get(x).dumpCoreData());
		}
		this.themepairs = new ArrayList<ThemePairSerializable>();
		for(int x = 0; x < themepairs.size(); x++) 
		{
			this.themepairs.add(themepairs.get(x).dumpCoreData());
		}
		
		this.filepaths = filepaths;
		this.name = name;
	}
	public ThemeSerializable(String name, ArrayList<ThemePair> themepairs, ArrayList<String> filepaths) 
	{
		this.isDynamic = false;
		this.rules = null;
		this.themepairs = new ArrayList<ThemePairSerializable>();
		for(int x = 0; x < themepairs.size(); x++) 
		{
			this.themepairs.add(themepairs.get(x).dumpCoreData());
		}
		this.filepaths = filepaths;
		this.name = name;
	}
	public void setIconPath(String icon) 
	{
		this.icon = icon;
	}
	public String getIcon()
	{
		return this.icon;
	}
	public boolean isCustomTheme() 
	{
		return true;
	}
	public boolean isDynamic()
	{
		return this.isDynamic;
	}
	public String getName()
	{
		return this.name;
	}
	public ArrayList<RulePair> getRules() 
	{
		ArrayList<RulePair> retval = new ArrayList<RulePair>();
		for(int x = 0; x < this.rules.size(); x++)
		{
			retval.add(new RulePair(this.rules.get(x)));
		}
		return retval;
	}
	public ArrayList<ThemePair> getThemePairs() 
	{
		ArrayList<ThemePair> retval = new ArrayList<ThemePair>();
		for(int x = 0; x < themepairs.size(); x++) 
		{
			retval.add(new ThemePair(themepairs.get(x)));
		}
		return retval;
	}
	public ArrayList<String> getFilePaths() 
	{
		return this.filepaths;
	}
	public String toString()
	{
		return "Theme Serializable: " + this.name;
	}
}
