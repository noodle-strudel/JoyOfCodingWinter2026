package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.InvokeMainTestCase;
import edu.pdx.cs.joy.UncaughtExceptionInMain;
import edu.pdx.cs.joy.web.HttpRequestHelper.RestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.MethodOrderer.MethodName;

/**
 * Tests the {@link Project5} class by invoking its main method with various arguments
 */
@TestMethodOrder(MethodName.class)
class Project5IT extends InvokeMainTestCase {
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
    void test2HostNameAndPortWithoutArguments() {
        MainMethodResult result = invokeMain( Project5.class, HOSTNAME, PORT );
        assertThat(result.getTextWrittenToStandardError(), containsString("** Missing customer name"));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Test
    void test3NonExistentCustomerPrintsNoCustomerFound() {
        String word = "WORD";
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", word);

        assertThat(result.getTextWrittenToStandardError(), containsString("No phone bills associated with WORD"));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Test
    void test4AddPhoneCall() {
        String customerName = "CUSTOMER NAME";
        String callerNumber = "123-456-7890";
        String calleeNumber = "098-765-4321";
        String beginTime = "1/1/2004 3:00 PM";
        String endTime = "1/2/2004 3:00 PM";

        MainMethodResult result = invokeMain( Project5.class, HOSTNAME, PORT, customerName, callerNumber );

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        String out = result.getTextWrittenToStandardOut();

        PhoneCall newCall = new PhoneCall(callerNumber, calleeNumber, beginTime, endTime);
        assertThat(out, out, containsString(Messages.addedPhoneCallToCustomer(customerName, newCall)));

        result = invokeMain( Project5.class, HOSTNAME, PORT, customerName );

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(PrettyPrinter.formatDictionaryEntry(customerName, callerNumber)));

        result = invokeMain( Project5.class, HOSTNAME, PORT );

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(PrettyPrinter.formatDictionaryEntry(customerName, callerNumber)));
    }
}