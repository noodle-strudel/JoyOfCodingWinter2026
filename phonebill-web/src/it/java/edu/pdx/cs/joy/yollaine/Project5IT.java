package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.InvokeMainTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.MethodOrderer.MethodName;

// TODO:
//  print option, readme option

/**
 * Tests the {@link Project5} class by invoking its main method with various arguments
 */
@TestMethodOrder(MethodName.class)
class Project5IT extends InvokeMainTestCase {
    public static final String CUSTOMER_NAME = "CUSTOMER NAME";
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");


    @Test
    void test0RemoveAllMappings() throws IOException {
      PhoneBillRestClient client = new PhoneBillRestClient(HOSTNAME, Integer.parseInt(PORT));
      client.removeAllPhoneBills();
    }

    @Test
    void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain( Project5.class );
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.MISSING_ARGS));
    }

    @Test
    void test2HostNameAndPortMissingTheirArguments() {
        MainMethodResult result = invokeMain( Project5.class, "-host");
        assertThat(result.getTextWrittenToStandardError(), containsString("** -host requires host name"));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));

        result = invokeMain( Project5.class, "-host", HOSTNAME, "-port");
        assertThat(result.getTextWrittenToStandardError(), containsString("** -port requires port number"));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Test
    void test3HostNameAndPortWithoutCustomerArguments() {
        MainMethodResult result = invokeMain( Project5.class, "-host", HOSTNAME, "-port", PORT );
        assertThat(result.getTextWrittenToStandardError(), containsString("** Missing customer name"));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Test
    void test4HostNameWithoutPortAndViceVersa() {
        MainMethodResult result = invokeMain( Project5.class, "-host", HOSTNAME, "WORD");
        assertThat(result.getTextWrittenToStandardError(), containsString("** Both host name and port number must be specified at the same time"));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));

        result = invokeMain( Project5.class, "-port", PORT, "WORD");
        assertThat(result.getTextWrittenToStandardError(), containsString("** Both host name and port number must be specified at the same time"));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Test
    void test5NonExistentCustomerPrintsNoCustomerFound() {
        String word = "WORD";
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", word);

        assertThat(result.getTextWrittenToStandardError(), containsString("No phone bills associated with WORD found."));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Test
    void test6AddPhoneCall() {
        String callerNumber = "123-456-7890";
        String calleeNumber = "098-765-4321";
        String beginTime = "1/1/2004 3:00 PM";
        String endTime = "1/2/2004 3:00 PM";

        MainMethodResult result = invokeMain( Project5.class, "-host", HOSTNAME, "-port", PORT, CUSTOMER_NAME, callerNumber, calleeNumber, beginTime, endTime);

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        String out = result.getTextWrittenToStandardOut();

        PhoneCall newCall = new PhoneCall(callerNumber, calleeNumber, beginTime, endTime);
        assertThat(out, out, containsString(Messages.addedPhoneCallToCustomer(CUSTOMER_NAME, newCall)));

        // pretty print it
        result = invokeMain( Project5.class, "-search", "-host", HOSTNAME, "-port", PORT, CUSTOMER_NAME);

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        out = result.getTextWrittenToStandardOut();
        assertThat(out, containsString("Customer: "+CUSTOMER_NAME));
    }

    @ParameterizedTest
    @CsvSource({
        CUSTOMER_NAME+","+"123-456-7890,123-456-7860,12/1/2004 3:45 PM,12/1/2004 4:23 PM,",
        CUSTOMER_NAME+","+"098-765-4321,078-765-4321,3/4/2025 4:00 AM,3/4/2025 11:00 AM,",
        CUSTOMER_NAME+","+"456-789-0123,434-789-0123,5/1/2012 8:00 PM,5/1/2012 9:14 PM,",
        CUSTOMER_NAME+","+"345-678-9012,654-678-9012,7/3/2004 12:00 PM,7/3/2004 3:45 PM"
    })
    void test7AddMorePhoneCallsToCustomer(String customerName, String callerNumber, String calleeNumber, String beginTime, String endTime) {
        MainMethodResult result = invokeMain( Project5.class, "-host", HOSTNAME, "-port", PORT, customerName, callerNumber, calleeNumber, beginTime, endTime);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        String out = result.getTextWrittenToStandardOut();
        PhoneCall newCall = new PhoneCall(callerNumber, calleeNumber, beginTime, endTime);
        assertThat(out, out, containsString(Messages.addedPhoneCallToCustomer(CUSTOMER_NAME, newCall)));

        // pretty print it
        result = invokeMain( Project5.class, "-search", "-host", HOSTNAME, "-port", PORT, CUSTOMER_NAME);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        out = result.getTextWrittenToStandardOut();
        assertThat(out, containsString(customerName));
        assertThat(out, containsString(callerNumber));
        assertThat(out, containsString(calleeNumber));
        assertThat(out, containsString(newCall.getBeginTime().format(PrettyPrinter.PRETTY_DATE_TIME)));
        assertThat(out, containsString(newCall.getEndTime().format(PrettyPrinter.PRETTY_DATE_TIME)));
    }

    @ParameterizedTest
    @CsvSource({
        CUSTOMER_NAME+" 2,"+"123-456-7890,123-456-7860,12/1/2004 3:45 PM,12/1/2004 4:23 PM,",
        CUSTOMER_NAME+" 2,"+"098-765-4321,078-765-4321,3/4/2025 4:00 AM,3/4/2025 11:00 AM,",
        CUSTOMER_NAME+" 2,"+"456-789-0123,434-789-0123,5/1/2012 8:00 PM,5/1/2012 9:14 PM,",
        CUSTOMER_NAME+" 2,"+"345-678-9012,654-678-9012,7/3/2004 12:00 PM,7/3/2004 3:45 PM"
    })
    void test8AddMorePhoneCallsToDifferentCustomer(String customerName, String callerNumber, String calleeNumber, String beginTime, String endTime) {
        MainMethodResult result = invokeMain( Project5.class, "-host", HOSTNAME, "-port", PORT, customerName, callerNumber, calleeNumber, beginTime, endTime);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        String out = result.getTextWrittenToStandardOut();
        PhoneCall newCall = new PhoneCall(callerNumber, calleeNumber, beginTime, endTime);
        assertThat(out, out, containsString(Messages.addedPhoneCallToCustomer(CUSTOMER_NAME, newCall)));

        // pretty print it
        result = invokeMain( Project5.class, "-search", "-host", HOSTNAME, "-port", PORT, customerName);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        out = result.getTextWrittenToStandardOut();
        assertThat(out, containsString(customerName));
        assertThat(out, containsString(callerNumber));
        assertThat(out, containsString(calleeNumber));
        assertThat(out, containsString(newCall.getBeginTime().format(PrettyPrinter.PRETTY_DATE_TIME)));
        assertThat(out, containsString(newCall.getEndTime().format(PrettyPrinter.PRETTY_DATE_TIME)));
    }

    @Test
    void test9SearchCustomerCallsBetweenTwoDates() {
        MainMethodResult result = invokeMain( Project5.class, "-host", HOSTNAME, "-search", "-port", PORT, CUSTOMER_NAME, "1/1/2004 12:00 AM", "12/31/2004 11:59 PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        String out = result.getTextWrittenToStandardOut();
        assertThat(out, containsString(CUSTOMER_NAME));
        assertThat(out, containsString("Jul 3 2004"));
        assertThat(out, containsString("Dec 1 2004"));
        assertThat(out, not(containsString("May 1 2012")));
        assertThat(out, not(containsString("Mar 4 2025")));

        result = invokeMain( Project5.class, "-host", HOSTNAME, "-search", "-port", PORT, CUSTOMER_NAME, "7/3/2004 12:00 PM", "12/1/2004 4:23 PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        out = result.getTextWrittenToStandardOut();
        assertThat(out, containsString(CUSTOMER_NAME));
        assertThat(out, containsString("Jul 3 2004"));
        assertThat(out, containsString("Dec 1 2004"));
        assertThat(out, not(containsString("May 1 2012")));
        assertThat(out, not(containsString("Mar 4 2025")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"localhos","12kj","[[][p,l","\\v[=-p"})
    void test10InvalidCommandLineSyntax(String invalidPort) {
        MainMethodResult result = invokeMain( Project5.class, "-host", HOSTNAME, "-search", "-port", invalidPort, CUSTOMER_NAME);
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), containsString("** Port \""+invalidPort+"\" must be an integer"));
    }

    @Test
    void test12BeginTimeWithoutEndTime() {
        MainMethodResult result = invokeMain(Project5.class, "-search", "-host", HOSTNAME, "-port", PORT, CUSTOMER_NAME, "1/1/2024 1:00 PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), containsString("** Time range to search for calls is missing end time"));
    }


    // TODO:
    //  end time is before begin time
}