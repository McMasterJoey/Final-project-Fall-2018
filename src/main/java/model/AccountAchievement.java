package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A Class providing a Java representation of a row in the account_achievements table.
 *
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 */
public class AccountAchievement
{
    private int accountID;
    private int achieveID;
    private LocalDateTime date;
    private String formattedDate;

    /**
     * Constructor for the class.
     *
     * @param accountID An int indicating the accountid of the user in the DB
     * @param achieveID An int indicating the achieveid of the achievement in the DB
     * @param date A LocalDateTime indicating when the achievement was earned
     */
    public AccountAchievement(int accountID, int achieveID, LocalDateTime date)
    {
        this.accountID = accountID;
        this.achieveID = achieveID;
        this.date = date;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        formattedDate = date.format(format);
    }

    // ---
    // Begin Getters

    public int getAccountID()
    {
        return accountID;
    }

    public int getAchieveID()
    {
        return achieveID;
    }

    public LocalDateTime getDate()
    {
        return date;
    }

    public String getFormattedDate()
    {
        return formattedDate;
    }

    // ---
    // End Getters
}
