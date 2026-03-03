package edu.pdx.cs.joy.yollaine;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.web.HttpRequestHelper;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
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
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));

        argList.removeIf(String::isEmpty);

        boolean hostNameFlag = false;
        boolean portFlag = false;
        boolean searchFlag = false;
        boolean printFlag = false;
        boolean readMeFlag = false;
        String hostNameArg = "";
        String portArg = "";

        String currentArg;
        try {
            while ((currentArg = argList.getFirst()).startsWith("-")) {
                if (currentArg.contains("host")) {
                    argList.removeFirst();
                    hostNameFlag = true;
                    hostNameArg = argList.removeFirst();
                } else if (currentArg.contains("port")) {
                    argList.removeFirst();
                    portFlag = true;
                    portArg = argList.removeFirst();
                } else {
                    if (currentArg.contains("print")) {
                        printFlag = true;
                    } else if (currentArg.contains("search")) {
                        searchFlag = true;
                    } else if (currentArg.contains("README")) {
                        readMeFlag = true;
                    }
                    argList.removeFirst();
                }
            }
        } catch (NoSuchElementException e) {
            if (hostNameFlag && hostNameArg.isEmpty()) {
                error("-host requires host name");
                return;
            } else if (portFlag && portArg.isEmpty()) {
                error("-port requires port number ");
                return;
            }
        }

        // check if one is true and the other is false (xor trick!)
        if (hostNameFlag ^ portFlag) {
            error("Both host name and port number must be specified at the same time");
            return;
        }

        String customerName = null;
        String callerNumber = null;
        String calleeNumber = null;
        String beginTime = null;
        String endTime = null;

        for (String arg : argList) {

            if (customerName == null) {
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

        if (hostNameArg.isEmpty()) {
            usage( MISSING_ARGS );
            return;

        } else if (portArg.isEmpty()) {
            usage( "Missing port" );
            return;

        }

        if (customerName == null) {
            usage( "Missing customer name" );
            return;
        }

        // connect to server
        int port;
        try {
            port = Integer.parseInt( portArg );
        } catch (NumberFormatException ex) {
            usage("Port \"" + portArg + "\" must be an integer");
            return;
        }

        PhoneBillRestClient client = new PhoneBillRestClient(hostNameArg, port);

        if (searchFlag) {
            try {
                PhoneBill phoneBill = client.getPhoneBill(customerName);
                PrettyPrinter prettyPrinter = new PrettyPrinter(new OutputStreamWriter(System.out));
                prettyPrinter.dump(phoneBill);
            } catch (IOException | ParserException | HttpRequestHelper.RestException ex) {
                error("While contacting server: " + ex.getMessage());
            }
            return;
        }

        if (callerNumber == null) {
            usage( "Missing caller number" );
            return;

        } else if (calleeNumber == null) {
            usage( "Missing callee number" );
            return;

        } else if (beginTime == null) {
            usage( "Missing begin time" );
            return;

        } else if (endTime == null) {
            usage( "Missing end time" );
            return;
        }


        String message = "";
        try {
          // Post the word/definition pair
          PhoneCall newCall = new PhoneCall(callerNumber, calleeNumber, beginTime, endTime);
          client.addPhoneCall(customerName, newCall);
          message = Messages.addedPhoneCallToCustomer(customerName, newCall);

        } catch (IOException ex ) {
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
        err.println("-jar target/phonebill-client.jar [options] <args>");
        err.println("    args are (in this order):\n" +
            "        customer        Person whose phone bill we're modeling\n" +
            "                        or modifying\n" +
            "        callerNumber    Phone number of caller\n" +
            "        calleeNumber    Phone number of person who was called\n" +
            "        begin           Date and time call began (12-hour time)\n" +
            "        end             Date and time call ended (12-hour time)");
        err.println("options are (options may appear in any order):");
        err.println("  -host hostname\tHost computer on which the server runs");
        err.println("  -port port\tPort on which the server is listening");
        err.println("  -search\tSearch for phone calls");
        err.println("  -print\tPrints a description of the new phone call");
        err.println("  -README\tPrints a README for this project and exits");
        err.println();
        err.println("This simple program posts customer names and their phone bills and phone calls");
        err.println("to the server.");
        err.println("A customer name is required to print phone bills from the server.");
        err.println("is printed.");
        err.println("If no word is specified, all dictionary entries are printed");
        err.println();
    }
}