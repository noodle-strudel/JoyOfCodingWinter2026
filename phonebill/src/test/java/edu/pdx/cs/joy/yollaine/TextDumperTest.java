package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperties;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

  @Test
  void canParseAPhoneBillWithTwoPhoneCallsWrittenByTextDumper(@TempDir File tempDir) throws IOException, ParserException {
    String customer = "Test Phone Bill";
    PhoneBill bill = new PhoneBill(customer);

    PhoneCall call = new PhoneCall("445-345-3353", "409-435-3948", "11/23/2004 3:45", "11/23/2004 4:56");
    PhoneCall calldos = new PhoneCall("239-345-2342", "454-235-7677", "12/4/2004 23:50", "12/5/2004 00:00");
    bill.addPhoneCall(call);
    bill.addPhoneCall(calldos);

    File textFile = new File(tempDir, "apptbook.txt");
    TextDumper dumper = new TextDumper(new FileWriter(textFile));
    dumper.dump(bill);

    TextParser parser = new TextParser(new FileReader(textFile));
    PhoneBill read = parser.parse();
    assertThat(read.getCustomer(), equalTo(customer));
    assertThat(read.getPhoneCalls().size(), equalTo(2));

    PhoneCall o = (PhoneCall) bill.getPhoneCalls().toArray()[1];
    assertThat(o.getCaller(), equalTo("239-345-2342"));
    assertThat(o.getCallee(), equalTo("454-235-7677"));
    assertThat(o.getBeginTimeString(), equalTo("12/4/2004 23:50"));
    assertThat(o.getEndTimeString(), equalTo("12/5/2004 00:00"));
  }

  @Test
  void textDumperStillDumpsMalformedPhoneBillButThrowsExceptionWhenParsed(@TempDir File tempDir) throws IOException {
    String customer = "Test Phone Bill";
    PhoneBill bill = new PhoneBill(customer);
    PhoneCall call = new PhoneCall("445-345-3353", "409-435-3948", "11/23/2004", "4:56");
    bill.addPhoneCall(call);

    File textFile = new File(tempDir, "apptbook.txt");
    TextDumper dumper = new TextDumper(new FileWriter(textFile));
    dumper.dump(bill);

    TextParser parser = new TextParser(new FileReader(textFile));
    assertThrows(ParserException.class, parser::parse);
  }

}
