package Gamejam;

import javafx.scene.control.Button;
/**
 * The same as a button but with some meta data handling.
 * @author Joey McMaster
 *
 */
public class GameButton extends Button {
	private String metadata = "";
	public GameButton() {
		super();
	}
	public void setMetaDataString(String data) {
		this.metadata = data;
	}
	public String getMetaDataString() {
		return this.metadata;
	}
}
