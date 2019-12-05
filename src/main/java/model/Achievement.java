package model;

/**
 * A Class providing a Java representation of row in the achievements table.
 *
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 */
public class Achievement
{
    private int achieveID;
    private int gameID;
    private String description;
    private String condition;
    private int amount;
    private int exp; // Exp reward for earning this achievment
    private String iconPath;

    /**
     * Constructor for the class.
     *
     * @param achieveID An int indicating the achieveid in theDB
     * @param gameID An int indicating the gameid in the DB
     * @param description A String giving the description of the achievement
     * @param condition A String indicating the type of action the player must make to earn the achievement IN (play, win, score)
     * @param amount An int indicating the number of above actions the player must make to earn the achievement
     * @param exp An int giving the exp reward of the achievement >= 0
     * @param iconPath A String indicating where to find the icon for the achievement
     */
    public Achievement(int achieveID, int gameID, String description, String condition, int amount, int exp, String iconPath)
    {
        this.achieveID = achieveID;
        this.gameID = gameID;
        this.description = description;
        this.condition = condition;
        this.amount = amount;
        this.exp = exp;
        this.iconPath = iconPath;
    }

    // ---
    // Begin Getters

    public int getAchieveID()
    {
        return achieveID;
    }

    public int getGameID()
    {
        return gameID;
    }

    public String getDescription()
    {
        return description;
    }

    public String getCondition()
    {
        return condition;
    }

    public int getAmount()
    {
        return amount;
    }

    public int getExp()
    {
        return exp;
    }

    public String getIconPath()
    {
        return iconPath;
    }

    // ---
    // End Getters
}
