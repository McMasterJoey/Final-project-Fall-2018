package model;

import java.util.ArrayList;

public class Leaderboard {
    private ArrayList<Score> Scores;
    // Use the singleton pattern for this class
    private static Leaderboard singleton = null;

    private Leaderboard() {
        Scores = new ArrayList<>();

        fillScores();
    }

    synchronized public static Leaderboard getInstance() {

        if (singleton = null) {
            singleton = new Leaderboard();
        }

        return singleton;
    }
}
