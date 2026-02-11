package edu.pdx.cs.joy.yollaine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * Unit tests for the PrettyPrinter class.
 */
public class PrettyPrinterTest {

  public static final String CUSTOMER = "Test Customer";
  public static final String FORMATTED_CUSTOMER = "Customer: " + CUSTOMER;
  public static final String TEMP_PRETTY_FILE = "tempPrettyFile";

  @Test
  void phoneBillOwnerIsDumpedInPrettyFormat() {
    PhoneBill bill = new PhoneBill(CUSTOMER);

    StringWriter sw = new StringWriter();
    PrettyPrinter pp = new PrettyPrinter(sw);
    pp.dump(bill);

    String text = sw.toString();
    assertThat(text, containsString(FORMATTED_CUSTOMER));
  }

  @Test
  void phoneBillWithCallIsDumpedInPrettyFormat(@TempDir File tempDir) throws IOException {
    PhoneBill bill = new PhoneBill(CUSTOMER);
    bill.addPhoneCall(PhoneCallTest.CALL_3);
    File prettyFile = new File(tempDir, TEMP_PRETTY_FILE);
    PrettyPrinter pp = new PrettyPrinter(new FileWriter(prettyFile));
    pp.dump(bill);

    BufferedReader br = new BufferedReader(new FileReader(prettyFile));
    br.readLine();
    String customer = br.readLine();
    for (int i = 0; i < 6; ++i) {
      br.readLine();
    }
    String prettyCall = br.readLine();
    br.close();
    assertThat(customer, containsString(FORMATTED_CUSTOMER));
    assertThat(prettyCall, containsString("345-678-9012  | 123-456-7890  | Sun, Nov 2 2025 at 4:00 PM | Sun, Nov 2 2025 at 5:00 PM | 60"));
  }

  @Test
  void phoneBillWithMultipleCallsIsDumpedInPrettyFormatWithCorrectOrdering(@TempDir File tempDir) throws IOException {
    PhoneBill bill = new PhoneBill(CUSTOMER);
    bill.addPhoneCall(PhoneCallTest.CALL_1);
    bill.addPhoneCall(PhoneCallTest.CALL_2);
    bill.addPhoneCall(PhoneCallTest.CALL_3);
    bill.addPhoneCall(PhoneCallTest.CALL_4);
    bill.addPhoneCall(PhoneCallTest.CALL_5);
    File prettyFile = new File(tempDir, TEMP_PRETTY_FILE);
    PrettyPrinter pp = new PrettyPrinter(new FileWriter(prettyFile));
    pp.dump(bill);

    BufferedReader br = new BufferedReader(new FileReader(prettyFile));
    br.readLine();
    String customer = br.readLine();
    for (int i = 0; i < 6; ++i) {
      br.readLine();
    }

    String prettyCall = br.readLine();

    assertThat(customer, containsString(FORMATTED_CUSTOMER));
    assertThat(prettyCall, containsString("345-234-4353  | 343-342-3434  | Sun, Jan 2 2005 at 4:56 PM | Sun, Jan 2 2005 at 5:34 PM | 38"));
    prettyCall = br.readLine();
    assertThat(prettyCall, containsString("104-234-4353  | 343-342-3434  | Wed, Mar 2 2005 at 4:56 PM | Wed, Mar 2 2005 at 5:34 PM | 38"));
    prettyCall = br.readLine();
    assertThat(prettyCall, containsString("345-234-4353  | 343-342-3434  | Wed, Mar 2 2005 at 4:56 PM | Wed, Mar 2 2005 at 5:34 PM | 38"));
    prettyCall = br.readLine();
    assertThat(prettyCall, containsString("345-234-4353  | 343-342-3434  | Wed, Mar 2 2005 at 4:56 PM | Wed, Mar 2 2005 at 5:34 PM | 38"));
    prettyCall = br.readLine();
    assertThat(prettyCall, containsString("345-678-9012  | 123-456-7890  | Sun, Nov 2 2025 at 4:00 PM | Sun, Nov 2 2025 at 5:00 PM | 60"));
    br.close();
  }
}
