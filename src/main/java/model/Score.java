package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides information about a user's score for a game.
 *
 * @Author Nicholas Fiegel
 */
public class Score implements Comparable
{
    private int gameID;
    private String gameName;
    private int accountID;
    private String username;
    private int score;
    private LocalDateTime date;
    private String formattedDate;
    private String outcome;

    /**
     * Constructor for the class with no outcome set.
     *
     * @param gameID The gameid in the database
     * @param gameName The name of the game in the database, associated with gameid
     * @param accountID The userid in the database
     * @param username The account name associated with userid
     * @param score The user's score for this instance of the game
     */
    public Score(int gameID, String gameName, int accountID, String username, int score, LocalDateTime date)
    {
        this.gameID = gameID;
        this.gameName = gameName;
        this.accountID = accountID;
        this.username = username;
        this.score = score;
        this.date = date;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        formattedDate = date.format(format);
    }

    /**
     * Constructor for the class with an outcome set.
     *
     * @param gameID The gameid in the database
     * @param gameName The name of the game in the database, associated with gameid
     * @param accountID The userid in the database
     * @param username The account name associated with userid
     * @param outcome A String indicating the outcome of the game (win, loss, tie, incomplete)
     */
    public Score(int gameID, String gameName, int accountID, String username, int score, LocalDateTime date, String outcome)
    {
        this(gameID, gameName, accountID, username, score, date);
        this.outcome = outcome;
    }

    /**
     * Returns a String giving the outcome of a game, based on the boolean values stored
     * in the gamelog table.
     *
     * @param win A boolean indicating a win
     * @param loss A boolean indicating a loss
     * @param tie A boolean indicating a tie
     * @param incomplete A boolean indicating an incomplete game
     * @return A String indicating the outcome of the game (Win, Loss, Tie, Incomplete)
     * @throws IllegalArgumentException if all boolean values are false
     */
    public static String determineOutcome(boolean win, boolean loss, boolean tie, boolean incomplete)
    {
        if (win)
        {
            return "Win";
        }
        else if (loss)
        {
            return "Loss";
        }
        else if (tie)
        {
            return "Tie";
        }
        else if (incomplete)
        {
            return "Incomplete";
        }
        else
        {
            throw new IllegalArgumentException("Error: Game must result in a win, loss, tie, or incomplete (all were false)");
        }
    }

    //---
    // Getters for the various fields

    public int getGameID()
    {
        return gameID;
    }

    public String getGameName()
    {
        return gameName;
    }

    public int getAccountID()
    {
        return accountID;
    }

    public String getUsername()
    {
        return username;
    }

    public int getScore()
    {
        return score;
    }

    public LocalDateTime getDate()
    {
        return date;
    }

    public String getFormattedDate()
    {
        return formattedDate;
    }

    public String getOutcome()
    {
        return outcome;
    }

    //---
    // End Getters

    @Override
    public int compareTo(Object other)
    {
        int otherScore = ((Score) other).getScore();

        if (score > otherScore)
        {
            return 1;
        }
        else if (score < otherScore)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
}
