package controller;

import model.Leaderboard;
import model.Score;

import java.time.LocalDateTime;

/**
 * Class to unify updates to user statistics.
 *
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 */
public class StatsManager {
    private AccountManager acctMgr;
    private Leaderboard leaderboard;
    // Use the singleton pattern for this class
    private static StatsManager singleton = null;

    private StatsManager() {
        acctMgr = AccountManager.getInstance();
        leaderboard = Leaderboard.getInstance();
    }

    public synchronized static StatsManager getInstance() {

        if (singleton == null) {
            singleton = new StatsManager();
        }

        return singleton;
    }
    /**
     * Logs a game played for the given user, Alternate function
     * @param game The name of the game that was played
     * @param stattype The type of stat being recorded see logStatType.
     * @param time The amount of time passed during this run of the game.
     */
    public void logGameStat(String game, int stattype, int time, int score) {
        if (stattype == 0) {
            logGameStat(game, true, false, false, false, time, score);
        } else if (stattype == 1) {
            logGameStat(game, false, true, false, false, time, score);
        } else if (stattype == 2) {
            logGameStat(game, false, false, true, false, time, score);
        } else if (stattype == 3) {
            logGameStat(game, false, false, false, true, time, score);
        } else {
            throw new IllegalArgumentException("Invalid stattype was out of range or value was below 0.");
        }
    }
    public void logGameStat(String game, boolean win, boolean loss, boolean tie, boolean incomplete, int time, int score) {
        String outcome = Score.determineOutcome(win, loss, tie, incomplete);

        if (game.equals("Space-Shooter") && loss) {
            outcome = "Game Over";
        }

        acctMgr.logGameInDB(game, win, loss, tie, incomplete, time, score);
        leaderboard.addScore(acctMgr.getAccountID(), game, score, LocalDateTime.now(), outcome);
    }
}
