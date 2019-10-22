package controller;

/**
 * Class to unify updates to user statistics.
 *
 * @author Nicholas Fiegel
 */
public class StatsManager {
    AccountManager acctMgr;
    // Use the singleton pattern for this class
    private static StatsManager singleton = null;

    private StatsManager() {
        acctMgr = AccountManager.getInstance();
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
    public void logGameStat(String game, int stattype, int time) {
        if (stattype == 0) {
            logGameStat(game,true,false,false,false,time);
        } else if (stattype == 1) {
            logGameStat(game,false,true,false,false,time);
        } else if (stattype == 2) {
            logGameStat(game,false,false,true,false,time);
        } else if (stattype == 3) {
            logGameStat(game,false,false,false,true,time);
        } else {
            throw new IllegalArgumentException("Invalid stattype was out of range or value was below 0.");
        }
    }
    public void logGameStat(String game, boolean win, boolean loss, boolean tie, boolean incomplete, int time) {
        acctMgr.logGameInDB(game, win, loss, tie, incomplete, time);
    }
}
