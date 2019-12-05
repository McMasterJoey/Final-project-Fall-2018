package Gamejam;

import java.io.Serializable;

import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;

/**
 * Represents theme coloration information for a single GUI Element.
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public class ThemePair
{
	
	private Background background;
	private Border border;
	private Paint color;
	private String cssstr = "";
	/**
	 * Constructor for collections that represent a Region item coloration.
	 */
	public ThemePair(Background bg, Border bd) {
		this.background = bg;
		this.border = bd;
	}
	
	public ThemePair(Background bg, Border bd, String css) {
		this.background = bg;
		this.border = bd;
		this.cssstr = css;
	}
	public ThemePair(Background bg, Border bd, String css, Paint p) {
		this.background = bg;
		this.border = bd;
		this.cssstr = css;
		this.color = p;
	}
	public ThemePair(Background bg, Border bd, Paint p) {
		this.color = p;
		this.background = bg;
		this.border = bd;
	}
	public ThemePair(ThemePairSerializable data)
	{
		int[] color_bg = data.getBackgroundColor();
		this.background = GamejamMainScreenTheme.solidBackgroundSetup(color_bg[0],color_bg[1],color_bg[2]);
		int[] color_txt = data.getTextColor();
		this.color =  GamejamMainScreenTheme.colorSetUpRBG(color_txt[0],color_txt[1],color_txt[2]);
		int[] color_br = data.getBorderColor();
		this.border = GamejamMainScreenTheme.SimpleIntBorder(GamejamMainScreenTheme.colorSetUpRBG(color_br[0],color_br[1],color_br[2]), data.getBorderStyle(), data.getborderWidth());
		this.cssstr = data.getCSS();
	}
	public ThemePairSerializable dumpCoreData() 
	{
		Color col_br;
		Color col_ba;
		String style = "Solid";
		int borderwidth = 1;
		if (this.border == null) 
		{
			col_br = Color.DARKGREY;
		}
		else 
		{
			col_br = (Color) this.border.getStrokes().get(0).getTopStroke();
			borderwidth = (int) this.border.getStrokes().get(0).getWidths().getTop();
			if (this.border.getStrokes().get(0).getTopStyle().equals(BorderStrokeStyle.DASHED))
			{
				style = "Dashed";
			} 
			else if (this.border.getStrokes().get(0).getTopStyle().equals( BorderStrokeStyle.SOLID))
			{
				style = "Solid";
			} 
			else 
			{
				style = "Dotted";
			}
		}
		if (this.background == null) 
		{
			col_ba = Color.DARKGREY;
		} 
		else if (this.background.getFills().get(0).getFill() instanceof LinearGradient) 
		{
			col_ba = Color.DARKGREY;
		}
		else 
		{
			col_ba = (Color) this.background.getFills().get(0).getFill();
		}
		Color col;
		if (this.color == null)
		{
			col = Color.DARKGREY;
		}
		else 
		{
			col = (Color) color;
		}
		ThemePairSerializable retval = new ThemePairSerializable(cssstr, (int) (col.getRed() * 255), (int) (col.getGreen() * 255), (int) (col.getBlue() * 255), borderwidth, style, (int) (col_br.getRed() * 255), (int) (col_br.getGreen() * 255), (int) (col_br.getBlue() * 255), (int) (col_ba.getRed() * 255), (int) (col_ba.getGreen() * 255), (int) (col_ba.getBlue() * 255));
		return retval;
	}
	/**
	 * 
	 * @param css
	 */
	public void setCSS(String css) {
		this.cssstr = css;
	}
	/**
	 * 
	 * @return
	 */
	public String getCSS() {
		return this.cssstr;
	}
	/**
	 * 
	 * @return
	 */
	public Paint getColor() {
		return this.color;
	}
	/**
	 * 
	 * @return
	 */
	public Background getBackground() {
		return this.background;
	}
	/**
	 * 
	 * @return
	 */
	public Border getBorder() {
		return this.border;
	}
	public String toString()
	{
		return "<ThemePair>: " + this.background + " " + this.border + " " + this.color;
	}
}