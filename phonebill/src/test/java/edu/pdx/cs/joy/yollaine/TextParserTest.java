package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextParserTest {
  @Test
  void validTextFileCanBeParsed() throws ParserException {
    InputStream resource = getClass().getResourceAsStream("valid-phonebill.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    PhoneBill bill = parser.parse();
    assertThat(bill.getCustomer(), equalTo("Test Phone Bill"));
    assertThat(bill.getPhoneCalls().size(), equalTo(0));
  }

  @Test
  void validTextFileWithOnePhoneCallCanBeParsed() throws ParserException {
    InputStream resource = getClass().getResourceAsStream("valid-phonebill-1-phonecall.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    PhoneBill bill = parser.parse();
    assertThat(bill.getCustomer(), equalTo("Test Phone Bill"));
    assertThat(bill.getPhoneCalls().size(), equalTo(1));

    PhoneCall o = (PhoneCall) bill.getPhoneCalls().toArray()[0];
    assertThat(o.getCaller(), equalTo("445-345-3353"));
    assertThat(o.getCallee(), equalTo("409-435-3948"));
    assertThat(o.getBeginTimeString(), equalTo("11/23/2004 3:45"));
    assertThat(o.getEndTimeString(), equalTo("11/23/2004 4:56"));
  }

  @Test
  void validTextFileWithTwoPhoneCallCanBeParsed() throws ParserException {
    InputStream resource = getClass().getResourceAsStream("valid-phonebill-2-phonecalls.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    PhoneBill bill = parser.parse();
    assertThat(bill.getCustomer(), equalTo("Test Phone Bill"));
    assertThat(bill.getPhoneCalls().size(), equalTo(2));
  }

  @Test
  void invalidTextFileThrowsParserException() {
    InputStream resource = getClass().getResourceAsStream("empty-phonebill.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    assertThrows(ParserException.class, parser::parse);
  }

  @Test
  void invalidTextFileWith1InvalidPhoneCallThrowsParserException() {
    InputStream resource = getClass().getResourceAsStream("invalid-phonebill-1-phonecall.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    assertThrows(ParserException.class, parser::parse);
  }

  @Test
  void invalidTextFileWithCrazyTextThrowsParserException() {
    InputStream resource = getClass().getResourceAsStream("invalid-phonebill-crazy.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    assertThrows(ParserException.class, parser::parse);
  }

  @ParameterizedTest
  @MethodSource("missingArguments")
  void textfilesWithMissingInfoThrowsParserExceptionWithSpecificErrors(String fileName, String msg) {
    InputStream resource = getClass().getResourceAsStream(fileName);
    assertThat(resource, notNullValue());
    ParserException exception = assertThrows(ParserException.class, () -> {
      TextParser parser = new TextParser(new InputStreamReader(resource));
      PhoneBill parsedBill = parser.parse();
    });
    assertThat(exception.getMessage(), containsString(msg));
  }

  @ParameterizedTest
  @MethodSource("invalidArguments")
  void textFilesWithInvalidPhoneCallInfoThrowsParserExceptionWithSpecificErrors(String fileName, String msg) {
    InputStream resource = getClass().getResourceAsStream(fileName);
    assertThat(resource, notNullValue());
    ParserException exception = assertThrows(ParserException.class, () -> {
      TextParser parser = new TextParser(new InputStreamReader(resource));
      PhoneBill parsedBill = parser.parse();
    });
    assertThat(exception.getMessage(), containsString(msg));
  }

  private static Stream<Arguments> missingArguments() {
    return Stream.of (
      Arguments.of("invalid-phonebill-1-phonecall.txt", "Missing End Time from phone call in the given file."),
      Arguments.of("invalid-phonebill-1-phonecall-2.txt", "Missing End Date from phone call in the given file."),
      Arguments.of("invalid-phonebill-1-phonecall-3.txt", "Missing Begin Time from phone call in the given file."),
      Arguments.of("invalid-phonebill-1-phonecall-4.txt", "Missing Begin Date from phone call in the given file."),
      Arguments.of("invalid-phonebill-1-phonecall-5.txt", "Missing Callee Number from phone call in the given file.")
    );
  }

  private static Stream<Arguments> invalidArguments() {
    return Stream.of (
      Arguments.of("invalid-phonebill-format-phonecall.txt", "Invalid phone number format in the given file."),
      Arguments.of("invalid-phonebill-format-phonecall-2.txt", "Invalid phone number format in the given file."),
      Arguments.of("invalid-phonebill-format-phonecall-3.txt", "Invalid date and time format in the given file."),
      Arguments.of("invalid-phonebill-format-phonecall-4.txt", "Invalid date and time format in the given file."),
      Arguments.of("invalid-phonebill-format-phonecall-5.txt", "Invalid date and time format in the given file."),
      Arguments.of("invalid-phonebill-format-phonecall-6.txt", "Invalid date and time format in the given file.")
    );
  }
}
