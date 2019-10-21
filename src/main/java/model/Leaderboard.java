package model;

import controller.DBConnection;
import controller.DBGameManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Leaderboard {
    private ArrayList<Score> scores;
    private DBConnection conn;
    private DBGameManager gameMgr;
    // Use the singleton pattern for this class
    private static Leaderboard singleton = null;

    private Leaderboard() {
        scores = new ArrayList<>();
        conn = DBConnection.getInstance();
        gameMgr = DBGameManager.getInstance();

        fillScores();
    }

    synchronized public static Leaderboard getInstance() {

        if (singleton == null) {
            singleton = new Leaderboard();
        }

        return singleton;
    }

    /**
     * Fills scores with all the scores that have been logged in the gamelog table.
     */
    private void fillScores() {
        ResultSet gameLogs = null;
        ResultSet statistics = null;
        ResultSet accounts = null;
        HashMap<Integer, Integer> statsIDToGameID = new HashMap<>();
        HashMap<Integer, Integer> statsIDToAccountID = new HashMap<>();
        HashMap<Integer, String> accountIDToUsername = new HashMap<>();

        try {
            statistics = conn.executeQuery("SELECT * FROM statistics");
            accounts = conn.executeQuery("SELECT * FROM accounts");
            gameLogs = conn.executeQuery("SELECT * FROM gamelog");

            while (statistics.next()) {
                int statsID = statistics.getInt("statsid");
                statsIDToGameID.put(statsID, statistics.getInt("gameid"));
                statsIDToAccountID.put(statsID, statistics.getInt("accountid"));
            }

            while (accounts.next()) {
                accountIDToUsername.put(accounts.getInt("accountid"), accounts.getString("username"));
            }

            while (gameLogs.next()) {
                int statsID = gameLogs.getInt("statsid");
                int gameID = statsIDToGameID.get(statsID);
                String gameName = gameMgr.getGameListByID().get(gameID);
                int accountID = statsIDToAccountID.get(statsID);
                String username = accountIDToUsername.get(accountID);
                int score = gameLogs.getInt("score");

                scores.add(new Score(gameID, gameName, accountID, username, score));
            }

            Collections.sort(scores);
            Collections.reverse(scores);

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    /**
     * Given an accountID, gameName, and a score, adds that score to the list of scores
     * the leaderboard has a record of.
     *
     * @param accountID The accountid of the user in the database
     * @param gameName The name of the game to record a score for
     * @param score The score the user earned
     */
    public void addScore(int accountID, String gameName, int score) {
        ResultSet rs = null;

        try {
            rs = conn.executeQuery("SELECT username FROM accounts WHERE accountid = ?", accountID);
            boolean idFound = rs.next();

            if (idFound) {
                String username = rs.getString("username");
                int gameID = gameMgr.getGameListByName().get(gameName);
                scores.add(new Score(gameID, gameName, accountID, username, score));
            } else {
                System.err.println("accountid " + accountID + " not found in the database");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    /**
     * Returns all scores.
     * @return An ArrayList<Score> containing all scores
     */
    public ArrayList<Score> getAllScores() {
        return scores;
    }

    /**
     * Returns the top ten scores for a given game name
     * @param game A String giving the name of the game
     * @return An ArrayList<Score> of up to the top ten scores
     */
    public ArrayList<Score> getTopTen(String game) {
        ArrayList<Score> topTen = new ArrayList<>();
        int count = 1;

        for (Score cur : scores) {

            if (cur.getGameName().equals(game)) {
                topTen.add(cur);
                count++;
            }

            if (count > 10) {
                break;
            }
        }

        return topTen;
    }
}
