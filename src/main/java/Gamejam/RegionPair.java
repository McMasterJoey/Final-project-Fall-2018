package Gamejam;

import javafx.scene.layout.Region;

public class RegionPair 
{
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