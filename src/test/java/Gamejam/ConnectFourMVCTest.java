package Gamejam;

import static org.junit.Assert.*;

import org.junit.Test;

import connectFour.ConnectFourModel;

public class ConnectFourMVCTest {

	@Test
	public void testRWinRow6() {
		ConnectFourModel model = new ConnectFourModel();
		model.humanMove(0, true);
		model.humanMove(1, true);
		model.humanMove(2, true);
		model.humanMove(3, true);
		assertTrue(model.won('R'));
		assertFalse(model.won('Y'));
		assertFalse(model.tied());

		model = new ConnectFourModel();
		model.humanMove(0, true);
		model.humanMove(1, true);
		model.humanMove(3, true);
		model.humanMove(4, true);
		assertFalse(model.won('R'));
		model.humanMove(2, true);
		assertTrue(model.won('R'));
	}

	@Test
	public void testTie() {
		ConnectFourModel m = new ConnectFourModel();
		m.humanMove(0, true);
		m.computerMove(1);
		m.humanMove(0, true);
		m.computerMove(1);
		m.humanMove(0, true);
		m.computerMove(1);

		m.humanMove(1, true);
		m.computerMove(0);
		m.humanMove(1, true);
		m.computerMove(0);
		m.humanMove(1, true);
		m.computerMove(0);

		m.humanMove(2, true);
		m.computerMove(3);
		m.humanMove(2, true);
		m.computerMove(3);
		m.humanMove(2, true);
		m.computerMove(3);

		m.humanMove(3, true);
		m.computerMove(2);
		m.humanMove(3, true);
		m.computerMove(2);
		m.humanMove(3, true);
		m.computerMove(2);

		m.humanMove(4, true);
		m.computerMove(5);
		m.humanMove(4, true);
		m.computerMove(5);
		m.humanMove(4, true);
		m.computerMove(5);

		m.humanMove(6, true);
		m.computerMove(4);
		m.humanMove(6, true);
		m.computerMove(4);
		m.humanMove(6, true);
		m.computerMove(4);

		m.humanMove(5, true);
		m.computerMove(6);
		m.humanMove(5, true);
		m.computerMove(6);
		m.humanMove(5, true);
		m.computerMove(6);

		/*
		 * Y R Y R Y R Y Y R Y R Y R Y Y R Y R Y R Y R Y R Y R Y R R Y R Y R Y R R Y R Y
		 * R Y R
		 */
		assertFalse(m.won('R'));
		assertFalse(m.won('Y'));
		assertTrue(m.tied());
	}

	@Test
	public void testMaxMovesRemaining() {
		ConnectFourModel m = new ConnectFourModel();
		assertEquals(m.maxMovesRemaining(), 42);
		m.humanMove(0, true);
		m.humanMove(1, true);
		assertEquals(m.maxMovesRemaining(), 40);
	}

	@Test
	public void testStillRunning() {
		ConnectFourModel m = new ConnectFourModel();
		assertTrue(m.isStillRunning());
		m.humanMove(0, true);
		assertTrue(m.isStillRunning());
		m.humanMove(0, true);
		m.humanMove(0, true);
		m.humanMove(0, true);
		assertFalse(m.isStillRunning());
	}

	@Test
	public void testBoardState() {
		ConnectFourModel m = new ConnectFourModel();
		char[][] board = m.getBoard();

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				assertEquals(board[row][col], '_');
			}
		}

		m.humanMove(0, true);
		assertEquals(board[5][0], 'R');

		m.computerMove(1);
		assertEquals(board[5][1], 'Y');
	}

	@Test
	public void testYVerticalWin() {
		ConnectFourModel m = new ConnectFourModel();
		m.computerMove(0);
		m.computerMove(0);
		m.computerMove(0);
		m.computerMove(0);
		assertFalse(m.isStillRunning());
		assertTrue(m.won('Y'));
		assertFalse(m.tied());
	}

	@Test
	public void testTryToMakeMoveInFullColumn() {
		ConnectFourModel m = new ConnectFourModel();
		m.humanMove(0, true);
		m.computerMove(0);
		m.humanMove(0, true);
		m.computerMove(0);
		m.humanMove(0, true);
		m.computerMove(0);
		assertFalse(m.available(0));
	}

	@Test
	public void testTopLeftToBottomRightDiagonal() {
		ConnectFourModel m = new ConnectFourModel();
		m.computerMove(0);
		m.computerMove(0);
		m.computerMove(0);
		m.humanMove(0, true);
		m.computerMove(1);
		m.computerMove(1);
		m.humanMove(1, true);
		assertTrue(m.isStillRunning());
		m.computerMove(2);
		m.humanMove(2, true);
		m.humanMove(3, true);

		/*
		 * _ _ _ _ _ _ _ _ _ _ _ _ _ _ R _ _ _ _ _ _ Y R _ _ _ _ _ Y Y R _ _ _ _ Y Y Y R
		 * _ _ _
		 */
		assertTrue(m.won('R'));
		assertFalse(m.won('Y'));
		assertFalse(m.isStillRunning());
	}

	@Test
	public void testBottomLeftToTopRightDiagonal() {
		ConnectFourModel m = new ConnectFourModel();
		m.computerMove(6);
		m.computerMove(6);
		m.computerMove(6);
		m.humanMove(6, true);
		m.computerMove(5);
		m.computerMove(5);
		m.humanMove(5, true);
		assertTrue(m.isStillRunning());
		m.computerMove(4);
		m.humanMove(4, true);
		m.humanMove(3, true);

		/*
		 * _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ R _ _ _ _ _ R Y _ _ _ _ R Y Y _ _ _ R
		 * Y Y Y
		 */

		assertTrue(m.won('R'));
		assertFalse(m.won('Y'));
		assertFalse(m.isStillRunning());
	}
}
