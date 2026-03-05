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

    /**
     * String for a missing required parameter
     * @param parameterName the parameter that's missing
     * @return the formatted string
     */
    public static String missingRequiredParameter( String parameterName )
    {
        return String.format("The required parameter %s is missing", parameterName);
    }

    /**
     * String for adding a phone call to the customer
     * @param customerName the customer's name
     * @param phoneCall the phone call that was added
     * @return the formatted string
     */
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
