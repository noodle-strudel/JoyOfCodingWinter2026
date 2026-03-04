package edu.pdx.cs.joy.yollaine;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    /**
     * Constructor for the Messages Class.
     */
    public Messages() {

    }

    public static String missingRequiredParameter( String parameterName )
    {
        return String.format("The required parameter %s is missing", parameterName);
    }

    public static String addedPhoneCallToCustomer(String customerName, PhoneCall phoneCall)
    {
        return String.format( "Added %s to %s", phoneCall, customerName);
    }

    /**
     * A message confirming all phone bills have been deleted.
     * @return the corresponding message.
     */
    public static String allPhoneBillsDeleted() {
        return "All phone bills have been deleted";
    }

}
