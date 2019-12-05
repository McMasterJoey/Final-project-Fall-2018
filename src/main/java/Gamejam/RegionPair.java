package Gamejam;

import java.io.Serializable;

import javafx.scene.layout.Region;
/**
 * 
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public class RegionPair implements Serializable
{
	private static final long serialVersionUID = 3;
	
	private Region region;
	private String description;
	private int index;
	private ThemeRegionProp prop;
	public RegionPair(Region region, String description, ThemeRegionProp properties) 
	{
		this.region = region;
		this.description = description;
		this.prop = properties;
	}
	public void setIndex(int index) 
	{
		this.index = index;
	}
	public Region getRegion() 
	{
		return this.region;
	}
	public String getDescription() 
	{
		return this.description;
	}
	public int getIndex() 
	{
		return this.index;
	}
	public ThemeRegionProp getProperties()
	{
		return this.prop;
	}
}