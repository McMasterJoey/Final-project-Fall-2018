package Gamejam;

import javafx.scene.control.Button;
/**
 * The same as a button but with some meta data handling.
 * @author Joey McMaster
 *
 */
public class GameButton extends Button {
	private String metadata = "";
	private int metadataint = 0;
	public GameButton() {
		super();
	}
	/**
	 * Sets meta data in the form of a string for this button.
	 * @param data The meta data to be set
	 */
	public void setMetaDataString(String data) {
		this.metadata = data;
	}
	/**
	 * Sets meta data in the from of a int for this button.
	 * @param data The meta data to be set
	 */
	public void setMetaDataInt(int data) {
		this.metadataint = data;
	}
	/**
	 * Fetches the string meta data stored for this button.
	 * @return The string that was stored.
	 */
	public String getMetaDataString() {
		return this.metadata;
	}
	/**
	 * Fetches the int meta data stored for this button.
	 * @return The int that was stored.
	 */
	public int getMetaDataInt() {
		return this.metadataint;
	}
}
