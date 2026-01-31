package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TextDumperTest {

  @Test
  void phoneBillOwnerIsDumpedInTextFormat() {
    String customer = "Test Phone Bill";
    PhoneBill bill = new PhoneBill(customer);

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(bill);

    String text = sw.toString();
    assertThat(text, containsString(customer));
  }

  @Test
  void canParseTextWrittenByTextDumper(@TempDir File tempDir) throws IOException, ParserException {
    String customer = "Test Phone Bill";
    PhoneBill bill = new PhoneBill(customer);

    File textFile = new File(tempDir, "apptbook.txt");
    TextDumper dumper = new TextDumper(new FileWriter(textFile));
    dumper.dump(bill);

    TextParser parser = new TextParser(new FileReader(textFile));
    PhoneBill read = parser.parse();
    assertThat(read.getCustomer(), equalTo(customer));
  }

  @Test
  void canParseAPhoneBillWithPhoneCallWrittenByTextDumper(@TempDir File tempDir) throws IOException, ParserException {
    String customer = "Test Phone Bill";
    PhoneBill bill = new PhoneBill(customer);

    PhoneCall call = new PhoneCall("445-345-3353", "409-435-3948", "11/23/2004 3:45", "11/23/2004 4:56");
    bill.addPhoneCall(call);

    File textFile = new File(tempDir, "apptbook.txt");
    TextDumper dumper = new TextDumper(new FileWriter(textFile));
    dumper.dump(bill);

    TextParser parser = new TextParser(new FileReader(textFile));
    PhoneBill read = parser.parse();
    assertThat(read.getCustomer(), equalTo(customer));
    assertThat(read.getPhoneCalls().size(), equalTo(1));

    PhoneCall o = (PhoneCall) bill.getPhoneCalls().toArray()[0];
    assertThat(o.getCaller(), equalTo("445-345-3353"));
    assertThat(o.getCallee(), equalTo("409-435-3948"));
    assertThat(o.getBeginTimeString(), equalTo("11/23/2004 3:45"));
    assertThat(o.getEndTimeString(), equalTo("11/23/2004 4:56"));
  }
}
