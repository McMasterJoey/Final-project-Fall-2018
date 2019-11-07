package controller;

import Gamejam.Gamejam;
import model.SanityCheckFailedException;
import model.Score;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

/**
 * Manager user accounts such as logging in, creating accounts, and updating user
 * statistics in the database.
 *
 * @Author Nicholas Fiegel
 * @Author Joey McMaster
 */
public class AccountManager extends Observable
{
    private String curUsername;
    private boolean isAdmin;
    private boolean isGuest;
    private int totalExp, expInLevel, level, accountID;
    private DBConnection conn;
    private DBGameManager dbGameManager;
    private HashMap<Integer, Integer> userStatsIDs; // maps gameid (key) to statsid (value)
    private HashMap<String, Integer> databaseGameID;
    private HashMap<Integer, Integer> gameWins; // maps gameid to # of wins
    private HashMap<Integer, Integer> gameLosses; // maps gameid to # of losses
    private HashMap<Integer, Integer> gameTies; // maps gameid to # ties
    private HashMap<Integer, Integer> gameIncompletes; // maps gameid to # incomplete games
    private HashMap<Integer, Integer> numGamesPlayed; // maps gameid (key) to # of times played (value)

    // Use the singleton pattern for this class
    private static AccountManager singleton = null;

    /**
     * Constructor for the class, private because of the singleton pattern.
     */
    private AccountManager()
    {
        curUsername = "guest";
        isAdmin = false;
        isGuest = true;
        totalExp = 0;
        expInLevel = 0;
        level = 1;
        accountID = -1;
        userStatsIDs = null;
        gameWins = null;
        gameLosses = null;
        gameTies = null;
        gameIncompletes = null;
        numGamesPlayed = null;
        conn = DBConnection.getInstance();
        dbGameManager = DBGameManager.getInstance();
        this.databaseGameID = new HashMap<String, Integer>();
    }

    /**
     * Getter for the class, due to singleton pattern.
     *
     * @return The AccountManager single instance of the class
     */
    synchronized public static AccountManager getInstance()
    {

        if (singleton == null)
        {
            singleton = new AccountManager();
        }

        return singleton;
    }

    /**
     * Attempts to login a user.
     *
     * @param username A String indicating the username
     * @param password A String indicating the password
     * @return A boolean true if successful, false if not
     */
    public boolean login(String username, String password)
    {
        ResultSet rs = null;

        try
        {
            rs = conn.executeQuery("SELECT * FROM accounts WHERE username = ?", username);

            if (rs.next())
            {

                if (password.equals(rs.getString("password")))
                {
                    curUsername = username;
                    isAdmin = rs.getBoolean("admin");
                    isGuest = rs.getBoolean("guest");
                    level = rs.getInt("level");
                    expInLevel = rs.getInt("exp");
                    totalExp = getExpForLevel(level) + expInLevel;
                    accountID = rs.getInt("accountid");
                    fillUserStats();

                    System.out.println("Login: " + curUsername + " " + isAdmin + " " + isGuest);

                    setChanged();
                    notifyObservers();
                    return true;
                }
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }

        return false;
    }

    /**
     * Logs a game played for the given user
     *
     * @param game The name of the game that was played
     * @param win Whether the game was won
     * @param loss Whether the game was lost
     * @param tie Whether the game was a tie
     * @param incomplete Whether the game is incomplete
     * @param time The amount of time that elapsed the game
     */
    public void logGameInDB(String game, boolean win, boolean loss, boolean tie, boolean incomplete, int time, int score)
    {
        if (this.isGuest)
        {
            // Do nothing if this is a guest account.
            Gamejam.DPrint("logGameStat, not doing anything!");
            return;
        }
        try
        { // Attempt to log the game in the gamelog table
            Gamejam.DPrint("\nFetching game from a string!");
            int gameID = getGameIdFromString(game);
            Gamejam.DPrint(gameID);
            String transaction = "INSERT INTO gamelog(statsid, win, loss, tie, incomplete, timeplayed, score) VALUES(?, ?, ?, ?, ?, ?, ?)";

            if (!userStatsIDs.containsKey(gameID))
            { // The game the user just played was added after the user's account creation
                createStatisticsEntries(); // So, create an entry in the DB for the user in the statistics table
            }

            conn.execute(transaction, userStatsIDs.get(gameID), win, loss, tie, incomplete, time, score);

            // Update the statistics table with the game outcome
            if (win)
            {
                logGlobalStat(true, game, 0, 1);
            }
            else if (loss)
            {
                logGlobalStat(true, game, 1, 1);
            }
            else if (tie)
            {
                logGlobalStat(true, game, 2, 1);
            }
            else if (incomplete)
            {
                logGlobalStat(true, game, 3, 1);
            }
            else
            {
                System.err.println("logGameInDB: invalid call, not a win, loss, tie, or incomplete game");
            }

            awardExp(score);
            fillUserStats();
            setChanged();
            notifyObservers();

        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }


    }

    /**
     * Logs a stat for the logged in user stattype:
     * 0 = wins
     * 1 = losses
     * 2 = ties
     * 3 = incomplete
     * 4 = time played (in seconds)
     *
     * @param update   Set to true if adding the value to the pre existing value, set to false if setting.
     * @param game     The name of the game whose stats are being set
     * @param stattype The id of the stat type (refer to the enum LogStatType)
     * @param value    The value to be added/set
     */
    private void logGlobalStat(boolean update, String game, int stattype, int value)
    {
        // Arg Check
        if (stattype < 0 || stattype > 4 || value < 0)
        {
            throw new IllegalArgumentException("Invalid stattype was out of range or value was below 0.");
        }
        if (this.isGuest)
        {
            // Do nothing if this is a guest account.
            return;
        }
        // Insert into stat table
        ResultSet rs = null;
        try
        {
            rs = conn.executeQuery("select g.gameid, a.accountid, s.statsid, s.wins, s.losses,s.ties,s.incomplete,s.timeplayed from games g join statistics s on g.gameid = s.gameid and g.name = ? join accounts a on a.accountid = s.accountid and a.username = ?", game, this.curUsername);
            if (rs.next())
            {
                // Should only get 1 row.
                int statsid = rs.getInt("statsid");
                // Need to fix ...
                int time = (int) rs.getTime("timeplayed").getTime();
                time = 0; // Set to 0 as of now so we only have valid values.

                int[] dv = new int[]{rs.getInt("wins"), rs.getInt("losses"), rs.getInt("ties"), rs.getInt("incomplete"), time};
                System.out.println(time);
                if (update)
                {
                    dv[stattype] += value;
                }
                else
                {
                    dv[stattype] = value;
                }
                //System.out.println(rs.getInt("losses"));
                conn.execute("update statistics set wins = ?, losses = ?, ties = ?, incomplete = ?, timeplayed = ? where statsid = ?", dv[0], dv[1], dv[2], dv[3], dv[4], statsid);
                if (rs.next())
                {
                    throw new SanityCheckFailedException("SQL query to fetch global stats returned more than 1 row.");
                }
            }
            else
            {
                // We are missing a stat, create one
                int[] dv = new int[5];
                dv[stattype] = value;
                try
                {
                    int gameid = getGameIdFromString(game);
                    conn.execute("INSERT INTO statistics(accountid, gameid, wins, losses, ties, incomplete, timeplayed) VALUES(?, ?, ?, ?, ?, ?, ?)", this.accountID, gameid, dv[0], dv[1], dv[2], dv[3], dv[4]);
                }
                catch (SQLException se)
                {
                    se.printStackTrace();
                }
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
    }

    /**
     * Logout the current user, that is, switch to the guest account
     */
    public void logout()
    {
        curUsername = "guest";
        isAdmin = false;
        isGuest = true;
        totalExp = 0;
        expInLevel = 0;
        level = 1;
        accountID = -1;
        userStatsIDs = null;

        setChanged();
        notifyObservers();
    }

	/**
	 * Attempt to create an account.
	 *
	 * Return codes:
	 * 1: Success
	 * 2: Username already in use
	 * 3: Other error
	 * @param username A String indicating the username to create
	 * @param password A String indicating the password to associate with the username
	 * @return An int indicating the result of the attempt: 1 success, 2 username already in use, 3 other error
	 */
	public int createAccount(String username, String password) {
		ResultSet rs = null;

		try {
			conn.execute("INSERT INTO accounts(username, password) VALUES(?, ?)", username, password);
			// Fetch userId from database, used to reduce the frequency of queries to database.
			rs = conn.executeQuery("select accountid from accounts where accounts.username = ?", username);
			rs.next();
			curUsername = username;
			isAdmin = false;
			isGuest = false;
			totalExp = 0;
			level = 1;
			accountID = rs.getInt("accountid");
			createStatisticsEntries();
			setChanged();
			notifyObservers();
		}
		catch (SQLException se)
        {
			if (se.getErrorCode() == 1062) // 1062 indicates username is already in the db
			{
				return 2;
			}
			else
            {
                return 3;
            }
		}
		
		return 1;
	}

    /**
     * Helper for createAccount, generates the appropriate entries in the statistics table for the new user.
     */
    private void createStatisticsEntries()
    {
        ResultSet rs = null;
        ArrayList<Integer> games = new ArrayList<>();

        try
        {
            rs = conn.executeQuery("SELECT gameid FROM statistics WHERE accountid = ?", accountID);

//			if (rs.next()) { // An entry already exists for this user
//				return;
//			} else {
//				rs = conn.executeQuery("SELECT gameid FROM games");
//
//				while (rs.next()) {
//					int gameID = rs.getInt("gameid");
//					conn.execute("INSERT INTO statistics(accountid, gameid) VALUES(?, ?)", accountID, gameID);
//				}
//			}

            while (rs.next())
            {
                games.add(rs.getInt("gameid"));
            }

            rs = conn.executeQuery("SELECT gameid FROM  games");

            while (rs.next())
            {
                int gameID = rs.getInt("gameid");
                if (games.contains(gameID))
                {
                    continue;
                }
                else
                {
                    conn.execute("INSERT INTO statistics(accountid, gameid) VALUES(?, ?)", accountID, gameID);
                }
            }

            fillUserStats();
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}
    }
    
    /**
     * Fills the userStatsIDs, gameWins, gameLosses, gameTies, gameIncompletes, and numGamesPlayed HashMaps with the current users statistics.
     */
    private void fillUserStats()
    {
        ResultSet rs = null;
        userStatsIDs = new HashMap<>();
        gameWins = new HashMap<>();
        gameLosses = new HashMap<>();
        gameTies = new HashMap<>();
        gameIncompletes = new HashMap<>();
        numGamesPlayed = new HashMap<>();
        try
        {
            Gamejam.DPrint("\nfillUserStatsIDs: accountID = " + accountID);
            int numGames, numUserStatsEntries;
            rs = conn.executeQuery("SELECT COUNT(gameid) FROM games");
            rs.next();
            numGames = rs.getInt("COUNT(gameid)");
            rs = conn.executeQuery("SELECT COUNT(statsid) FROM statistics WHERE accountid = ?", accountID);
            rs.next();
            numUserStatsEntries = rs.getInt("COUNT(statsid)");

            if (numUserStatsEntries < numGames) // Check to make sure there are entries for every game
            {
                createStatisticsEntries();
            }

            rs = conn.executeQuery("SELECT * FROM statistics WHERE statistics.accountid = ?", accountID);

            while (rs.next())
            {
                int gameID = rs.getInt("gameid");
                int statsID = rs.getInt("statsid");
                int wins = rs.getInt("wins");
                int losses = rs.getInt("losses");
                int ties = rs.getInt("ties");
                int incomplete = rs.getInt("incomplete");
                int total = 0;
                total += wins + losses + ties + incomplete;
                userStatsIDs.put(gameID, statsID);
                gameWins.put(gameID, wins);
                gameLosses.put(gameID, losses);
                gameTies.put(gameID, ties);
                gameIncompletes.put(gameID, incomplete);
                numGamesPlayed.put(gameID, total);
            }

            userStatsIDs.forEach((key, value) ->
                    Gamejam.DPrint("gameID: " + key + "  statsID: " + value)
            );
            System.out.println();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Returns the high score for given game for the current user.
     *
     * @param gameName A String indicating the name of the game to retrieve a high score for
     * @return An int representing the user's high score, a negative value indicates an error occurred
     */
    public int getHighScore(String gameName)
    {
        ResultSet rs = null;
        int gameID = dbGameManager.getGameListByName().get(gameName);

        if (!userStatsIDs.containsKey(gameID))
        { // The game might have been added after the user's account creation
            createStatisticsEntries(); // If so, make sure the user has entries for every game in the DB statistics table
        }

        int statsID = userStatsIDs.get(gameID);

        // Admin and guest accounts don't have a high score
        if (isAdmin || isGuest)
        {
            return 0;
        }

        try
        {
            rs = conn.executeQuery("SELECT score FROM gamelog WHERE statsid = ? ORDER BY score DESC", statsID);

            if (rs.next())
            {
                return rs.getInt("score");
            }
            else
            {
                return 0;
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            return -1;
        }
    }

    /**
     * Gets the list of user's scores for a given game.
     *
     * @param game A String with the name of the game to fetch scores for
     * @return An ArrayList<Score> of the user's scores for that game
     */
    public ArrayList<Score> getScores(String game)
    {
        ArrayList<Score> scores = new ArrayList<>();

        if (game.equals("All Games"))
        {
            for (String gameName : dbGameManager.getGameListByName().keySet())
            {
                ArrayList<Score> temp = scoresQuery(gameName);
                scores.addAll(temp);
            }
        }
        else
        {
            scores = scoresQuery(game);
        }

        return scores;
    }

    /**
     * Queries the database to get the user's scores for a given game.
     *
     * @param game A String indicating the name of the game to fetch scores for
     * @return An ArrayList<Score> of the user's scores for that game
     */
    private ArrayList<Score> scoresQuery(String game)
    {
        ArrayList<Score> scores = new ArrayList<>();
        ResultSet rs = null;
        int gameID = dbGameManager.getGameListByName().get(game);
        int statsID = userStatsIDs.get(gameID);

        try
        {
            rs = conn.executeQuery("SELECT win, loss, tie, incomplete, score FROM gamelog WHERE statsid = ?", statsID);

            while (rs.next())
            {
                boolean win = rs.getBoolean("win");
                boolean loss = rs.getBoolean("loss");
                boolean tie = rs.getBoolean("tie");
                boolean incomplete = rs.getBoolean("incomplete");
                int score = rs.getInt("score");
                String outcome;

                if (win)
                {
                    outcome = "Win";
                }
                else if (loss)
                {
                    outcome = "Loss";
                }
                else if (tie)
                {
                    outcome = "Tie";
                }
                else if (incomplete)
                {
                    outcome = "Incomplete";
                }
                else
                {
                    outcome = "Error: This shouldn't be possible";
                }

                scores.add(new Score(gameID, game, accountID, curUsername, score, outcome));
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }

        return scores;
    }

    /**
     * Public method to trigger fillUserStats.
     */
    public void refreshUserStats()
    {
        fillUserStats();
    }

    /**
     * Add exp to the user's account, then check to see if the should level up, and if so level them up.
     *
     * @param exp An int indicating the amount of exp the user should receive
     */
    public void awardExp(int exp)
    {
        totalExp += exp;

        // Don't award exp to an admin or guest account
        if (isAdmin || isGuest)
        {
            return;
        }

        // Level up the user if appropriate
        while (totalExp >= getTotalExpForLevel(level + 1))
        {
            level++;
        }

        expInLevel = totalExp - getTotalExpForLevel(level); // Determine the remaining exp the user has after leveling

        if (expInLevel < 0)
        {
            expInLevel = 0;
            throw new IllegalStateException("expInLevel became negative!");
        }

        try
        {
            conn.execute("UPDATE accounts SET exp = ?, level = ? WHERE accountID = ?", expInLevel, level, accountID);

        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }

        setChanged();
        notifyObservers();
    }

    /**
     * Returns the total amount of exp a user needs to reach a given level.
     *
     * @param level An int indicating the desired level
     * @return An int indicating the total amount of exp needed to reach that level
     */
    public int getTotalExpForLevel(int level)
    {
        if (level == 2)
        {
            return 1000;
        }
        else if (level < 2)
        {
            return 0;
        }

        return getExpForLevel(level) + getTotalExpForLevel(level - 1);
    }

    /**
     * Returns the amount of exp needed to go from level - 1, to level.
     *
     * @param level An int indicating the desired level
     * @return An int indicating the amount of exp needed to go from level - 1 to level
     */
    public int getExpForLevel(int level)
    {
        if (level == 2)
        {
            return 1000;
        }
        else if (level < 2)
        {
            return 0;
        }

        double percentageMultiplier = (20 * Math.log10(0.2 * level + 1)) / 100;
        return (int) Math.round(getExpForLevel(level - 1) * (1 + percentageMultiplier));
    }

    /**
     * Attempt to delete an account.
     * <p>
     * Return codes: 1: Success 2: No such user
     *
     * @param username A String indicating the user to delete
     * @return An int indicating the result: 1 success, 2 no such user
     */
    public int deleteAccount(String username)
    {
        try
        {
            conn.execute("DELETE from accounts where username = '" + username + "'");
        }
        catch (SQLException se)
        {
            Gamejam.DPrint("Delete account threw an SQLExecption!");
        }

        return 1;
    }

    //---
    // Getters for account information

    public String getCurUsername()
    {
        return curUsername;
    }

    public boolean isAdmin()
    {
        return isAdmin;
    }

    public boolean isGuest()
    {
        return isGuest;
    }

    public int getTotalExp()
    {
        return totalExp;
    }

    public int getExpInLevel()
    {
        return expInLevel;
    }

    public int getLevel()
    {
        return level;
    }

    public int getAccountID()
    {
        return accountID;
    }

    public HashMap<Integer, Integer> getUserStatsIDs()
    {
        return userStatsIDs;
    }

    public HashMap<Integer, Integer> getGameWins()
    {
        return gameWins;
    }

    public HashMap<Integer, Integer> getGameLosses()
    {
        return gameLosses;
    }

    public HashMap<Integer, Integer> getGameTies()
    {
        return gameTies;
    }

    public HashMap<Integer, Integer> getGameIncompletes()
    {
        return gameIncompletes;
    }

    public HashMap<Integer, Integer> getNumGamesPlayed()
    {
        return numGamesPlayed;
    }

    // End getters for account information
    // ---

    /**
     * Returns the gameid in the data base for the given game name
     *
     * @param game The string that is the name of the game
     * @return The gameid in the database of the game.
     */
    private int getGameIdFromString(String game)
    {
        if (this.databaseGameID.containsKey(game))
        {
            return this.databaseGameID.get(game);
        }
        // Cache miss, fetch from database.
        ResultSet rs = null;
        int gameid = -1;
        try
        {
            rs = conn.executeQuery("select gameid from games where name = ?", game);
            rs.next();
            gameid = rs.getInt("gameid");
            if (gameid < 0)
            {
                throw new SanityCheckFailedException("getGameIdFromString fetched a gameid of " + gameid + " from the query!");
            }
            // Add it to Hashmap so we can quickly fetch it.
            this.databaseGameID.put(game, gameid);
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            throw new SanityCheckFailedException("getGameIdFromString threw an SQL execption when adding a game to the cache.");
        }
        if (gameid < 0)
        {
            throw new SanityCheckFailedException("getGameIdFromString tired to return gameid = " + gameid);
        }
        return gameid;
    }
}
