package battleship;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShipView extends ImageView {
	private Ship ship;
	
	public ShipView(Image img, Ship ship) {
		super(img);
		this.ship = ship;
	}
	
	public Ship getShip() {
		return ship;
	}
}
