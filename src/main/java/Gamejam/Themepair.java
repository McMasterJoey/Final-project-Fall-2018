package Gamejam;

import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;

/**
 * 
 * @author Joey McMaster
 *
 */
public class Themepair {
	private byte type; // 0 = A Region, 1 = Text based
	private Background background;
	private Border border;
	private Color color;
	private String cssstr = "";
	/**
	 * Constructor for collections that represent a Region item coloration.
	 */
	public Themepair(Background bg, Border bd) {
		this.type = 0;
		this.background = bg;
		this.border = bd;
	}
	public Themepair(Background bg, Border bd, String css) {
		this.type = 1;
		this.background = bg;
		this.border = bd;
		this.cssstr = css;
	}
	/**
	 * Constructor for collections that represent simple Color object based coloration.
	 * @param color
	 */
	public Themepair(Color color) {
		this.type = 2;
		this.color = color;
	}
	public Themepair(Color color, String css) {
		this.type = 3;
		this.color = color;
		this.cssstr = css;
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
	public Color getColor() {
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