package tests;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import model.TicTacToeModel;

public class TicTacToeMVCTest {

    @Test
    public void testXWinRow0() {
        TicTacToeModel model = new TicTacToeModel();
        model.humanMove(0, 0, true);
        model.computerMove(1, 0);
        model.humanMove(0, 1, true);
        model.computerMove(1, 1);
        model.humanMove(0, 2, true);
        assertTrue(model.won('X'));
        assertFalse(model.won('O'));
        assertFalse(model.tied());
    }

    @Test
    public void testTie() {
        TicTacToeModel m = new TicTacToeModel();
        m.computerMove(0, 0);
        m.computerMove(1, 1);
        m.computerMove(2, 1);
        m.computerMove(1, 2);
        m.humanMove(0, 1, true);
        m.humanMove(0, 2, true);
        m.humanMove(1, 0, true);
        m.humanMove(2, 0, true);
        m.humanMove(2, 2, true);
        /* 
         *  O | X | X
         * ---|---|---
         *  X | O | O
         * ---|---|---
         *  X | O | X
         * 
         */        
        assertFalse(m.won('X'));
        assertFalse(m.won('O'));
        assertTrue(m.tied());
    }

    @Test
    public void testMaxMovesRemaining() {
        TicTacToeModel m = new TicTacToeModel();
        assertEquals(m.maxMovesRemaining(), 9);
        m.humanMove(0, 0, true);
        m.humanMove(0, 1, true);
        assertEquals(m.maxMovesRemaining(), 7);
    }

    @Test
    public void testStillRunning() {
        TicTacToeModel m = new TicTacToeModel();
        assertTrue(m.isStillRunning());
        m.humanMove(0, 0, true);
        assertTrue(m.isStillRunning());
        m.humanMove(1, 1, true);
        m.humanMove(2, 2, true);
        assertFalse(m.isStillRunning());
    }

    @Test
    public void testBoardState() {
        TicTacToeModel m = new TicTacToeModel();
        char[][] board = m.getBoard();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(board[row][col], '_');
            }
        }

        m.humanMove(0, 0, true);
        assertEquals(board[0][0], 'X');

        m.computerMove(1, 1);
        assertEquals(board[1][1], 'O');
    }

    @Test
    public void testOVerticalWin() {
        TicTacToeModel m = new TicTacToeModel();
        m.computerMove(0, 0);
        m.computerMove(1, 0);
        m.computerMove(2, 0);
        assertFalse(m.isStillRunning());
        assertTrue(m.won('O'));
        assertFalse(m.tied());
    }

    @Test
    public void testTryToMakeSameMove() {
        TicTacToeModel m = new TicTacToeModel();
        char[][] board = m.getBoard();
        m.humanMove(0, 0, true);
        assertEquals(board[0][0], 'X');
        m.computerMove(0, 0);
        assertEquals(board[0][0], 'X');
    }
}
