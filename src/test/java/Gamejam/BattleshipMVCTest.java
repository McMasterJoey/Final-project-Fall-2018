package Gamejam;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import battleship.BattleshipModel;
import battleship.Ship;
import battleship.Ship.Direction;

public class BattleshipMVCTest {
	
	@Test
	public void testConstruction() {
		BattleshipModel model = new BattleshipModel();
		for(Ship s : model.getHumanShips()) {
			assertFalse(s.isSet());
		}
	}
	
	@Test
	public void testSetPosition() {
		BattleshipModel model = new BattleshipModel();
		Ship s = model.getHumanShips().get(0);
		s.setPosition(new Point(0,0), new Point(0, s.getSize()-1));
		assertEquals(s.getDirection(), Direction.VERTICAL);
		assertTrue(s.wasHit(new Point(0, 1)));
		
		s = model.getHumanShips().get(1);
		s.setPosition(new Point(1, 0), new Point(s.getSize(), 0));
		assertEquals(s.getDirection(), Direction.HORIZONTAL);
		assertTrue(s.wasHit(new Point(2, 0)));
	}
	
	@Test
	public void testShipDestruction() {
		BattleshipModel model = new BattleshipModel();
		for(Ship s : model.getComputerShips()) {
			for(Point p : s.getPoints()) {
				s.wasHit(p);
			}
			assertTrue(s.isDestroyed());
		}
		assertTrue(model.won(true));
		assertFalse(model.won(false));
	}
	
	@Test
	public void testMaxMovesRemaining() {
		BattleshipModel model = new BattleshipModel();
		assertEquals(model.maxMovesRemaining(), 100);
		
		int count = 0;
		for(Ship s : model.getHumanShips()) {
			s.setPosition(new Point(count, 0), new Point(count, s.getSize()-1));
			count++;
		}
		
		model.humanMove(0, 0);
		model.humanMove(1, 1);
		model.humanMove(2, 2);
		assertEquals(model.maxMovesRemaining(), 97);
	}
}
