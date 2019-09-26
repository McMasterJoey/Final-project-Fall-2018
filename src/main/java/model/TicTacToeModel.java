package model;

import java.awt.Point;
import java.io.Serializable;
import java.util.Observable;

/**
 * Holds the model information for a 3x3 tic-tac-toe game. This model is heavily
 * based on the TicTacToeGame class provided by Rick Mercer in his CSC436
 * course.
 * 
 * @author Wes Rodgers
 *
 */

public class TicTacToeModel extends Observable implements Serializable {

    /**
     * default serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    private char[][] board;
    private int size;
    private TicTacToeAI computer;

    /**
     * Construct a Tic Toe Game that one human user can play against a Computer
     * player with swappable strategies.
     */
    public TicTacToeModel() {
        size = 3;
        initializeBoard();
        computer = new TicTacToeAI();
        computer.setStrategy(new EasyAI());
        setChanged();
        notifyObservers();
    }

    /**
     * initial creation of the board
     */
    private void initializeBoard() {
        board = new char[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = '_';
            }
        }
    }

    /**
     * starts a new game and sends observers a String that they can use to
     * identify this fact.
     */
    public void startNewGame() {
        initializeBoard();
        setChanged();
        notifyObservers("startNewGame()");
    }

    /**
     * Changes the AI's strategy
     * 
     * @param strat the strategy used by the computer to determine its move
     */
    public void setAIStrategy(TicTacToeStrategy AI) {
    	this.computer.setStrategy(AI);
    }

    /**
     * getter for the computer player
     * 
     * @return the TicTacToeAI representing the computer player
     */
    public TicTacToeAI getTicTacToeAI() {
        return computer;
    }

    /**
     * Checks that the move is legal then toggles the X at that location if so.
     * Subsequently calls code to have the AI choose their next move and makes
     * that move as well, unless testing is enabled.
     * 
     * @param row  the row value, 0-2
     * @param col  the column value, 0-2
     * @param test true when we don't want the computer to make a move after
     */
    public void humanMove(int row, int col, boolean test) {
        if (!available(row, col) || !isStillRunning()) {
            return;
        }
        board[row][col] = 'X';
        if (!test && this.isStillRunning()) {
            Point move = computer.nextMove(this);
            computerMove(move.x, move.y);
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Toggles the value on the board to 'O' indicating the AI's move, notifies
     * the views that they need to update
     * 
     * @param row the row value of the move, 0-2
     * @param col the column value of the move, 0-2
     */
    public void computerMove(int row, int col) {
        if (!available(row, col)) {
            return;
        }
        board[row][col] = 'O';
        setChanged();
        notifyObservers();
    }

    /**
     * Boolean check to determine whether the game is over
     * 
     * @return true if the game is not over, false if it is.
     */
    public boolean isStillRunning() {
        return !tied() && !won('X') && !won('O');
    }

    /**
     * checks if player with move of char c won in any of the three possible
     * ways
     * 
     * @param c the player's move, X or O
     * @return true if the player with move c won, false otherwise
     */
    public boolean won(char c) {
        return wonVertically(c) || wonHorizontally(c) || wonDiagonally(c);
    }

    
    /**
     * checks the two diagonals to see if the player won
     * 
     * @param c the player's move, X or O
     * @return true if the player with move c won on a diagonal, false otherwise
     */
    private boolean wonDiagonally(char c) {
        // Check Diagonal from upper left to lower right
        int sum = 0;
        for (int row = 0; row < size; row++) {
            if (board[row][row] == c) {
                sum++;
            }
        }
        if (sum == size) {
            return true;
        }

        // Check Diagonal from upper right to lower left
        sum = 0;
        for (int row = size - 1; row >= 0; row--) {
            if (board[size - row - 1][row] == c) {
                sum++;
            }
        }
        if (sum == size) {
            return true;
        }

        // No win on either diagonal
        return false;
    }

    /**
     * checks to see if the player won on a row
     * 
     * @param c the players move, X or O
     * @return true if the player with move C won on a horizontal, false
     *         otherwise
     */
    private boolean wonHorizontally(char c) {
        for (int row = 0; row < size; row++) {
            int rowSum = 0;
            for (int col = 0; col < size; col++) {
                if (board[row][col] == c) {
                    rowSum++;
                }
            }
            if (rowSum == size) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks to see if the player won on a column
     * 
     * @param c the player's move, X or O
     * @return true if the player with move C won on a horizontal, false
     *         otherwise
     */
    private boolean wonVertically(char c) {
        for (int col = 0; col < size; col++) {
            int colSum = 0;
            for (int row = 0; row < size; row++) {
                if (board[row][col] == c) {
                    colSum++;
                }
            }
            if (colSum == size)
                return true;
        }
        return false;
    }

    /**
     * Simple check to see if the game is a draw, occurs when neither X or O won
     * and there are no moves remaining
     * 
     * @return a boolean, true if the game is a draw, false otherwise
     */
    public boolean tied() {
        return maxMovesRemaining() == 0 && !won('X') && !won('O');
    }

    /**
     * Gets the state of the board as a 2D character array with possible values
     * of 'X', 'O', and '_';
     * 
     * @return a char[][] representing the board accessed by [row][col]
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Counts the spaces on the board without an X or O, returns that count.
     * 
     * @return maximum number of possible moves remaining in int form
     */
    public int maxMovesRemaining() {
        int numberMoves = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (available(row, col)) {
                    numberMoves++;
                }
            }
        }
        return numberMoves;
    }

    /**
     * simple check to see if the board is open at row, col
     * 
     * @param row the row value of the move, between 0-2
     * @param col the column value of the move, between 0-2
     * @return true if the space is empty, false otherwise
     */
    public boolean available(int row, int col) {
        return board[row][col] == '_';
    }

    public String getWinningDirection() {
        if (wonHorizontally('X') || wonHorizontally('O')) {
            return "horizontal";
        } else if (wonVertically('X') || wonVertically('O')) {
            return "vertical";
        } else {
            return "diagonal";
        }
    }

    public Point[] getWinningSquares(String direction) {
        Point[] retVal = new Point[3];
        char c = ' ';
        switch (direction) {
        case "horizontal":
            for (int row = 0; row < size; row++) {
                int rowSum = 0;
                c = board[row][0];
                for (int col = 0; col < size; col++) {
                    if (board[row][col] == c) {
                        rowSum++;
                    }
                    if (rowSum == size) {
                        retVal[0] = new Point(row, 0);
                        retVal[1] = new Point(row, 1);
                        retVal[2] = new Point(row, 2);
                    }
                }
                if(rowSum == size) {
                    break;
                }
            }
            break;
        case "vertical":
            for (int col = 0; col < size; col++) {
                int colSum = 0;
                c = board[0][col];
                for (int row = 0; row < size; row++) {
                    if (board[row][col] == c) {
                        colSum++;
                    }
                    if (colSum == size) {
                        retVal[0] = new Point(0, col);
                        retVal[1] = new Point(1, col);
                        retVal[2] = new Point(2, col);
                    }
                }
                if(colSum == size) {
                    break;
                }
            }
            break;
        case "diagonal":
            c = board[1][1];
            retVal[1] = new Point(1, 1);
            int sum = 0;
            for (int row = 0; row < size; row++)
                if (board[row][row] == c)
                    sum++;
            if (sum == size)
                retVal[0] = new Point(0, 0);
            retVal[2] = new Point(2, 2);

            // Check Diagonal from upper right to lower left
            sum = 0;
            for (int row = size - 1; row >= 0; row--)
                if (board[size - row - 1][row] == c)
                    sum++;
            if (sum == size)
                retVal[0] = new Point(2, 0);
            retVal[2] = new Point(0, 2);
            break;
        }
        return retVal;
    }

    @Override
    public String toString() {
        String result = "";
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                result += " " + board[r][c] + " ";
            }
            if (r == 0 || r == 1)
                result += "\n";
        }
        return result;
    }
}
