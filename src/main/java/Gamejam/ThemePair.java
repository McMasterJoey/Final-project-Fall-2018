package Gamejam;

import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * 
 * @author Joey McMaster
 *
 */
public class ThemePair {
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
}