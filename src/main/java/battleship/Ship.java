package battleship;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashSet;

/**
 * Ship class, holds all of the relevant model information for battleship Ships
 * @author Wes Rodgers
 *
 */

public class Ship implements Serializable{
	private static final long serialVersionUID = 1L;
	private int size;
	private boolean set;
	private boolean[] slots;
	private Point[] points;
	private Direction direction = Direction.HORIZONTAL;
	private String name;

	public enum Direction {
		HORIZONTAL, VERTICAL;
	}
	
	/**
	 * Constructor
	 * @param size the ship size in board units
	 * @param name the ships name, probably should have used an enum here
	 */
	public Ship(int size, String name) {
		this.size = size;
		this.name = name;
		direction = Direction.HORIZONTAL;
		set = false;
		slots = new boolean[this.size];
		points = new Point[this.size];
	}

	/**
	 * returns the ship's direction
	 * @return the ship's direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * returns the ship's name
	 * @return the ship's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns the points that the ship occupies
	 * @return the points that the ship occupies
	 */
	public Point[] getPoints() {
		return points;
	}

	/**
	 * sets the ships direction
	 * @param direction Direction.HORIZONTAL or Direction.VERTICAL
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * returns the length of the ship in board units
	 * @return the length of the ship in board units
	 */
	public int getSize() {
		return size;
	}

	/**
	 * returns true if the ship is set, false otherwise
	 * @return true if the ship is set, false otherwise
	 */
	public boolean isSet() {
		return set;
	}

	/**
	 * sets the ships positions based on start and end points
	 * @param start the first board space the ship occupies
	 * @param end the last board space the ship occupies
	 */
	public void setPosition(Point start, Point end) {
		for (int i = 0; i < size; i++) {
			slots[i] = false;
		}
		points[0] = start;

		if (start.x == end.x) {
			for (int i = start.y; i < start.y + size; i++) {
				points[i - start.y] = new Point(start.x, i);
			}
			setDirection(Direction.VERTICAL);
		} else {
			for (int i = start.x; i < start.x + size; i++) {
				points[i - start.x] = new Point(i, start.y);
			}
			setDirection(Direction.HORIZONTAL);
		}
		set = true;
	}

	/**
	 * returns true if the ship is hit at point p and sets that value to signify
	 * @param p the point we are checking
	 * @return true if the ship is hit at point p, false otherwise
	 */
	public boolean wasHit(Point p) {
		for (int i = 0; i < size; i++) {
			if (points[i] != null && p.x == points[i].x && p.y == points[i].y) {
				slots[i] = true;
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if the ship has slots that haven't been hit, false otherwise
	 * @return true if the ship has slots that haven't been hit, false otherwise
	 */
	public boolean isDestroyed() {
		for (boolean b : slots) {
			if (!b) {
				return false;
			}
		}
		return true;
	}
}
