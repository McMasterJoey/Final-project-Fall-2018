package model;

/**
 * Provides information about a user's score for a game.
 *
 * @Author Nicholas Fiegel
 */
public class Score implements Comparable {
    private int gameID;
    private String gameName;
    private int accountID;
    private String username;
    private int score;

    /**
     * Constructor for the class.
     *
     * @param gameID The gameid in the database
     * @param gameName The name of the game in the database, associated with gameid
     * @param userID The userid in the database
     * @param username The account name associated with userid
     * @param score The user's score for this instance of the game
     */
    public Score(int gameID, String gameName, int userID, String username, int score) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.accountID = userID;
        this.username = username;
        this.score = score;
    }

    //---
    // Getters for the various fields

    public int getGameID() {
        return gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public int getAccountID() {
        return accountID;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Object other) {
        int otherScore = ((Score) other).getScore();

        if (score > otherScore) {
            return 1;
        } else if (score < otherScore) {
            return -1;
        } else {
            return 0;
        }
    }

    // End Getters
    //---
}
