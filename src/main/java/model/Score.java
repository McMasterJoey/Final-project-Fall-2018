package model;

/**
 * Provides information about a user's score for a game.
 *
 * @Author Nicholas Fiegel
 */
public class Score {
    private int gameID;
    private String gameName;
    private int userID;
    private String userName;
    private int score;

    /**
     * Constructor for the class.
     *
     * @param gameID The gameid in the database
     * @param gameName The name of the game in the database, associated with gameid
     * @param userID The userid in the database
     * @param userName The account name associated with userid
     * @param score The user's score for this instance of the game
     */
    public Score(int gameID, String gameName, int userID, String userName, int score) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.userID = userID;
        this.userName = userName;
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

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return score;
    }

    // End Getters
    //---
}
