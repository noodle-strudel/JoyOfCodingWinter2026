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
    PhoneCall call = new PhoneCall("345-678-9012",
        "123-456-7890",
        "11/2/2025 4:00 PM",
        "11/2/2025 5:00 PM");
    bill.addPhoneCall(call);

    File prettyFile = new File(tempDir, TEMP_PRETTY_FILE);
    PrettyPrinter pp = new PrettyPrinter(new FileWriter(prettyFile));
    pp.dump(bill);

    BufferedReader br = new BufferedReader(new FileReader(prettyFile));
    String customer = br.readLine();
    String call1 = br.readLine();

    assertThat(customer, containsString(FORMATTED_CUSTOMER));
  }
}
