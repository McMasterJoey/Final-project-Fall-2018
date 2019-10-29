package battleship;

import java.awt.Point;

public class Ship {

	private int size;
	private boolean set;
	private boolean[] slots;
	private Point[] points;
	private Direction direction = Direction.HORIZONTAL;
	private String name;
	
	public enum Direction
	{
		HORIZONTAL, VERTICAL;
	}
	

	public Ship(int size, String name) {
		this.size = size;
		this.name = name;
		direction = Direction.HORIZONTAL;
		set = false;
		slots = new boolean[this.size];
		points = new Point[this.size];
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public int getSize() {
		return size;
	}
	
	public boolean isSet() {
		return set;
	}
	
	public void setPosition(Point start, Point end) {
		for(int i=0; i<size; i++) {
			slots[i] = false;
		}
		points[0] = start;
		
		if(start.x == end.x) {
			for(int i=start.y; i<=end.y; i++) {
				points[i-start.y] = new Point(start.y, i); 
			}
		}
		else {
			for(int i=start.x; i<=end.x; i++) {
				points[i-start.x] = new Point(i, start.y); 
			}
		}
		set = true;
	}
	
	public boolean wasHit(Point p) {
		for(int i=0; i<size; i++) {
			if(p.x == points[i].x && p.y == points[i].y) {
				slots[i] = true;
				return true;
			}
		}
		return false;
	}
	
	public boolean isDestroyed() {
		for(boolean b : slots) {
			if(!b) {
				return false;
			}
		}
		return true;
	}
}
