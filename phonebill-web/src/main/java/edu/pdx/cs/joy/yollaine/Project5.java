package edu.pdx.cs.joy.yollaine;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs.joy.ParserException;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The main class that parses the command line and communicates with the
 * Phone Bill server using REST.
 */
public class Project5 {

    public static final String MISSING_ARGS = "Missing command line arguments";

    /**
     * Determines if a date and time is valid.
     * @param dateAndTime
     *      The data and time combo to validate.
     * @return
     *      Returns true if dataAndTime is valid and false if it's invalid.
     */
    @VisibleForTesting
    static boolean isValidDateAndTime(String dateAndTime) {
        Pattern p = Pattern.compile("^(0?[1-9]|1[012])/((3[01])|([12][0-9])|0?[1-9])/[1-9][0-9]{3} (0?[0-9]|1[012]):[0-5][0-9] [AP]M$");
        Matcher m = p.matcher(dateAndTime);
        return m.find();
    }

    /**
     * Determines if a phone number is in a valid format.
     * @param number
     *      The phone number to validate.
     * @return
     *      Returns true if number is valid.
     */
    @VisibleForTesting
    static boolean isValidPhoneNumber(String number) {
        Pattern p = Pattern.compile("^([0-9]{3}-){2}[0-9]{4}$");
        Matcher m = p.matcher(number);
        return m.find();
    }

    public static void main(String... args) {
        String hostName = null;
        String portString = null;
        String customerName = null;
        String callerNumber = null;
        String calleeNumber = null;
        String beginTime = null;
        String endTime = null;

        for (String arg : args) {
            if (hostName == null) {
                hostName = arg;

            } else if (portString == null) {
                portString = arg;

            } else if (customerName == null) {
                customerName = arg;

            } else if (callerNumber == null) {
                callerNumber = arg;

            } else if (calleeNumber == null) {
                calleeNumber = arg;

            } else if (beginTime == null) {
                beginTime = arg;

            } else if (endTime == null) {
                endTime = arg;

            } else {
                usage("Extraneous command line argument: " + arg);
            }
        }

        if (hostName == null) {
            usage( MISSING_ARGS );

        } else if ( portString == null) {
            usage( "Missing port" );
        } else if (customerName == null) {
            usage( "Missing customer name" );

        } else if (callerNumber == null) {
            usage( "Missing caller number" );

        } else if (calleeNumber == null) {
            usage( "Missing callee number" );

        } else if (beginTime == null) {
            usage( "Missing begin time" );

        } else if (endTime == null) {
            usage( "Missing end time" );

        }

        int port;
        try {
            port = Integer.parseInt( portString );
            
        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }

        PhoneBillRestClient client = new PhoneBillRestClient(hostName, port);

        String message;
        try {
            if (customerName == null) {
                // Print all customerName/definition pairs
                throw new UnsupportedOperationException("Missing Customer");

            } else if (callerNumber == null) {
                // Print Phone Bill
                PhoneBill phoneBill = client.getPhoneBill(customerName);
                StringWriter sw = new StringWriter();
                PrettyPrinter pretty = new PrettyPrinter(sw);
                pretty.dump(phoneBill);
                message = sw.toString();

            } else {
                // Post the word/definition pair
                PhoneCall newCall = new PhoneCall(callerNumber, calleeNumber, beginTime, endTime);
                client.addPhoneCall(customerName, newCall);
                message = Messages.addedPhoneCallToCustomer(customerName, newCall);
            }

        } catch (IOException | ParserException ex ) {
            error("While contacting server: " + ex.getMessage());
            return;
        }

        System.out.println(message);
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project5 host port [customerName] [definition]");
        err.println("  host         Host of web server");
        err.println("  port         Port of web server");
        err.println("  customerName Word in dictionary");
        err.println("  definition   Definition of word");
        err.println();
        err.println("This simple program posts  and their definitions");
        err.println("to the server.");
        err.println("If no definition is specified, then the word's definition");
        err.println("is printed.");
        err.println("If no word is specified, all dictionary entries are printed");
        err.println();
    }
}