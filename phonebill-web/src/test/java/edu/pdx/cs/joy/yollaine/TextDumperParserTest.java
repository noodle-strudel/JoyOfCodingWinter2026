package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TextDumperParserTest {

  private PhoneBill dumpAndParse(PhoneBill bill) throws ParserException, IOException {
    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(bill);

    String text = sw.toString();

    TextParser parser = new TextParser(new StringReader(text));
    return parser.parse();
  }


  @Test
  void emptyPhoneBillCanBeDumpedAndParsed() throws ParserException, IOException {
    String customerName = "Test Customer";
    PhoneBill phoneBill = new PhoneBill(customerName);
    PhoneBill read = dumpAndParse(phoneBill);
    assertThat(read.getCustomer(), equalTo(customerName));
  }



  @Test
  void phoneBillWithOnePhoneCallCanBeDumpedAndParsed() throws ParserException, IOException {
    String customerName = "Test Customer";
    String callerNumber = "123-456-7890";
    String calleeNumber = "098-765-4321";
    String beginTime = "1/1/2004 3:00 PM";
    String endTime = "1/2/2004 3:00 PM";
    PhoneBill phoneBill = new PhoneBill(customerName);
    phoneBill.addPhoneCall(new PhoneCall(callerNumber, calleeNumber, beginTime, endTime));

    PhoneBill read = dumpAndParse(phoneBill);

    assertThat(read.getCustomer(), equalTo(customerName));
    assertThat(read.getPhoneCalls().size(), equalTo(1));
    assertThat(read.getPhoneCalls().iterator().next().getCaller(), equalTo(callerNumber));
  }
}
