package Gamejam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import model.SanityCheckFailedException;

/**
 * Represents an instance of a Theme that is generated at run time from a set of rules.
 * @author Joey McMaster
 *
 */
public class ThemeDynamic extends Theme {
	
	private static final long serialVersionUID = 1L;
	protected ArrayList<RulePair> rules;
	protected ArrayList<RegionPair> regions;
	protected GamejamMainScreenTheme screen;
	
	public ThemeDynamic(String name, int arraysize, ArrayList<RegionPair> regions, GamejamMainScreenTheme screen) {
		super(name, arraysize);
		this.rules = new ArrayList<RulePair>();
		this.regions = regions;
		this.screen = screen;
		if (this.regions.size() != arraysize)
		{
			throw new SanityCheckFailedException("ThemeDynamic: Mismatch of Region list size and static theme pair array size!");
		}
		this.addNewImage("/usersettingsbuttonbackground.png");
		this.addNewImage("/usersettingsbuttonbackground.png");
		generateTheme();
	}
	public void generateTheme(String filepath1, String filepath2) {
		generateTheme();
		this.setNewImage(0,filepath1);
		this.setNewImage(1,filepath2);
	}
	/**
	 * Generates stored theme via the stored rules in the order they were inserted.
	 */
	public void generateTheme()
	{
		
		// Reset it
		Theme t = this.screen.generateDefaultTheme();
		for(int x = 0; x < this.regions.size(); x++)
		{
			this.setThemeData(x, t.getTheme()[x]);
			if (this.themedata[x] == null)
			{
				throw new SanityCheckFailedException("ThemeDynamic: Failed to reset the theme data back to default! Some values were null!");
			}
		}
		this.setNewImage(0,"/usersettingsbuttonbackground.png");
		this.setNewImage(1,"/usersettingsbuttonbackground.png");
		
		Gamejam.DPrint("[DEBUG]: rules.size = " + this.rules.size());
		
		for(int x = 0; x < this.rules.size(); x++)
		{
			int debug_countapplies = 0;
			RulePair rp = this.rules.get(x);
			boolean useAnd = this.rules.get(x).getAnd();
			for(int y = 0; y < this.regions.size(); y++)
			{
				if (useAnd && this.regions.get(y).getProperties().isAllTrue(rp.getStatements()))
				{
					this.setThemeData(y, processBasicThemeEditorThemePair(rp.getPair(),y));
					debug_countapplies++;
				}
				else if (!useAnd && this.regions.get(y).getProperties().isAtleastOneTrue(rp.getStatements()))
				{
					this.setThemeData(y, processBasicThemeEditorThemePair(rp.getPair(),y));
					debug_countapplies++;
				}
			}
			Gamejam.DPrint("[DEBUG]: Rule " + x + " applied theme settings to " +  debug_countapplies + " pairs! ");
		}
	}
	private ThemePair processBasicThemeEditorThemePair(ThemePair p, int intendedindex)
	{
		Background b = p.getBackground();
		Paint pa = p.getColor();
		Border bo = p.getBorder();
		if (b != null && pa != null && bo != null)
		{
			return p;
		}
		
		if (b == null)
		{
			b = this.themedata[intendedindex].getBackground();
		}
		if (pa == null)
		{
			pa = this.themedata[intendedindex].getColor();
		}
		if (bo == null)
		{
			bo = this.themedata[intendedindex].getBorder();
		}
		ThemePair retval = new ThemePair(b,bo,pa);
		return retval;
	}
	public static ArrayList<String> getSurportedEnglishRules()
	{
		ArrayList<String> retval = new ArrayList<String>();
		retval.add("Buttons");
		retval.add("Top Bar Background");
		retval.add("Left Bar Background");
		retval.add("Middle Background");
		retval.add("All Text Color");
		retval.add("Progress Bars");
		retval.add("Selection Boxes");
		retval.add("Game Background");
		retval.add("Everything");
		return retval;
	}
	/**
	 * Takes a category
	 * 
	 * @param statement The category in String format.
	 * @return
	 * 0 = Display Text Adjuster
	 * 1 = Display Background Creator
	 * 2 = Display Border Creator
	 */
	public static boolean[] englishToGUIRegionsToShowId(String statement)
	{
		boolean[] retval = new boolean[3];
		if (statement.equals("Buttons"))
		{
			retval[0] = true;
			retval[1] = true;
			retval[2] = true;
		}
		else if (statement.equals("Top Bar Background"))
		{
			retval[0] = false;
			retval[1] = true;
			retval[2] = true;
		}
		else if (statement.equals("Left Bar Background"))
		{
			retval[0] = false;
			retval[1] = true;
			retval[2] = true;
		}
		else if (statement.equals("Middle Background"))
		{
			retval[0] = false;
			retval[1] = true;
			retval[2] = true;
		}
		else if (statement.equals("All Text Color"))
		{
			retval[0] = true;
			retval[1] = false;
			retval[2] = false;
		}
		else if (statement.equals("Progress Bars"))
		{
			retval[0] = false;
			retval[1] = true;
			retval[2] = true;
		}
		else if (statement.equals("Game Background"))
		{
			retval[0] = false;
			retval[1] = true;
			retval[2] = true;
		} 
		else if (statement.equals("Selection Boxes"))
		{
			retval[0] = false;
			retval[1] = true;
			retval[2] = true;
		}
		else if (statement.equals("Everything"))
		{
			retval[0] = true;
			retval[1] = true;
			retval[2] = true;
		}
		return retval;
	}
	public static HashSet<Integer> englishToRuleSet(String statement)
	{
		HashSet<Integer> retval = new HashSet<Integer>();
		if (statement.equals("Buttons"))
		{
			// OR
			retval.add(0);
			retval.add(11);
		}
		else if (statement.equals("Top Bar Background"))
		{
			// AND
			retval.add(14);
			retval.add(12);
			retval.add(35);
		}
		else if (statement.equals("Left Bar Background"))
		{
			// AND
			retval.add(15);
			retval.add(12);
			retval.add(35);
		}
		else if (statement.equals("Middle Background"))
		{
			// AND
			retval.add(16);
			retval.add(12);
			retval.add(35);
		}
		else if (statement.equals("All Text Color"))
		{
			// AND
			retval.add(27);
		}
		else if (statement.equals("Progress Bars"))
		{
			// AND
			retval.add(7);
		}
		else if (statement.equals("Game Background"))
		{
			// AND
			retval.add(31);
			retval.add(35);
		}
		else if (statement.equals("Selection Boxes"))
		{
			//OR
			retval.add(10);
		}
		return retval;
	}
	
	public void addRule(ThemePair p, HashSet<Integer> statements, boolean useAnd)
	{
		this.rules.add(new RulePair(statements, p, useAnd));
	}
	
	public void removeLastRule()
	{
		if (this.rules.size() != 0)
		{
			this.rules.remove(this.rules.size() - 1);
		}
	}
	
	public void removeAllRules()
	{
		this.rules.clear();
	}
	
	/**
	 * Determines if an element exists within the collection
	 * @param <T> The type of the elements in the collection
	 * @param collection A collection
	 * @param e The element that is being checked to see
	 * @return If any of the elements within the elements collection exist within the inputed collection.
	 */
	public static <T> boolean containsAny(Collection<T> collection, Collection<T> elements)
	{
		for(int x = 0; x < collection.size(); x++)
		{
			Iterator<T> e = elements.iterator();
			while(e.hasNext())
			{
				if (collection.contains(e.next()))
				{
					return true;
				}
			}
		}
		return false;
	}
	public boolean isStatic()
	{
		return false;
	}
	public ThemeSerializable dumpCoreData()
	{
		ArrayList<ThemePair> pairs = new ArrayList<ThemePair>();
		for(int x = 0; x < this.themedata.length; x++) 
		{
			pairs.add(this.themedata[x]);
		}
		ThemeSerializable retval =  new ThemeSerializable(name,this.rules, pairs, this.themeimgpaths);
		if (this.iconpath != null && !this.iconpath.equals(""))
		{
			retval.setIconPath(this.iconpath);
		}
		return retval;
	}
	public void importCoreData(ThemeSerializable data)
	{
		if (!data.isDynamic()) 
		{
			throw new SanityCheckFailedException("Expected Dynamic Theme Data, got Static Theme data");
		}
		
		this.themeimgpaths = data.getFilePaths();
		for(int x = 0; x < data.getFilePaths().size(); x++)
		{
			if (this.themeimg.size() >= x) {
				this.themeimg.add(new Image(getClass().getResourceAsStream(this.themeimgpaths.get(x))));
			} else {
				this.themeimg.set(x,new Image(getClass().getResourceAsStream(this.themeimgpaths.get(x))));
			}
		}
		
		this.rules = data.getRules();
		this.generateTheme();
	}
}
