package edu.pdx.cs.joy.yollaine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * A unit test for code in the <code>Project3</code> class.  This is different
 * from <code>Project1IT</code> which is an integration test and can capture data
 * written to {@link System#out} and the like.
 */
class Project3Test {

  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project3.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      assertThat(line, containsString("Yollaine Brooks"));
    }
  }
  private static boolean isValidDateAndTime(String dateAndTime) {
    return Project3.isValidDateAndTime(dateAndTime);
  }

  private static boolean getValidPhoneNumber(String number) {
    return Project3.isValidPhoneNumber(number);
  }

  @Test
  void canCreateProject2Object() {
    Project3 prog2 = new Project3();
  }

  @Test
  void January212026At1PMIsAValidDateAndTime() {
    assertThat(isValidDateAndTime("1/21/2026 01:00 PM"), equalTo(true));
  }

  @Test
  void emptyStringIsAnInvalidDateAndTime() {
    assertThat(isValidDateAndTime(""), equalTo(false));
  }

  @Test
  void February302000At1PMIsAValidDateAndTime() {
    assertThat(isValidDateAndTime("02/30/2000 01:00 PM"), equalTo(true));
  }

  @Test
  void emptyDataIsAnInvalidDateAndTime() {
    assertThat(isValidDateAndTime(""), equalTo(false));
  }

  @Test
  void noDateIsAnInvalidDateAndTime() {
    assertThat(isValidDateAndTime("19:23"), equalTo(false));
  }

  @Test
  void emptyTimeIsAnInvalidDateAndTime() {
    assertThat(isValidDateAndTime("02/30/2000"), equalTo(false));
  }

  @ParameterizedTest
  @ValueSource(strings = {"000/30/2000 00:00", "139/30/2000 00:00", "/30/2000 00:00", "10334/30/2000 00:00", "a20/30/2000 00:00"})
  void invalidMonthIsAnInvalidDateAndTime(String date) {
    assertThat(isValidDateAndTime(date), equalTo(false));
  }

  @ParameterizedTest
  @ValueSource(strings = {"10//2000 00:00", "2/343/2000 00:00", "12/30th/2000 00:00", "10/000/2000 00:00", "10/??/2000 00:00"})
  void invalidDayIsAnInvalidDateAndTime(String date) {
    assertThat(isValidDateAndTime(date), equalTo(false));
  }

  @ParameterizedTest
  @ValueSource(strings = {"10/01/ 00:00", "10/01/0 00:00", "10/01/23 00:00", "10/01/499 00:00", "10/01/20000 00:00"})
  void invalidYearIsAnInvalidDateAndTime(String date) {
    assertThat(isValidDateAndTime(date), equalTo(false));
  }

  @ParameterizedTest
  @ValueSource(strings = {"10/01/2004 00:00a", "10/01/2000 :00", "10/01/2000 sd:00", "10/01/2000 00:", "10/01/2000 10:", "10/01/2000 10:0", "10/01/2000 q00:00", "10/01/2000 0000"})
  void invalidTimeIsAnInvalidDateAndTime(String date) {
    assertThat(isValidDateAndTime(date), equalTo(false));
  }

  @ParameterizedTest
  @ValueSource(strings = {"10/01/0000 00:00", "10/40/2000 34:80", "30/01/2000 00:00", "00/01/2000 00:60", "10/01/2000 10:90", "43/23/5565 1:30"})
  void syntacticallyCorrectButSemanticallyIncorrectIsAValidDateAndTime(String date) {
    assertThat(isValidDateAndTime(date), equalTo(true));
  }

  @ParameterizedTest
  @ValueSource(strings = {"345-566-3454", "454-233-4563", "556-454-4453", "434-356-5456", "000-000-0000"})
  void validPhoneNumberReturnsTrue(String phoneNumber) {
    assertThat(getValidPhoneNumber(phoneNumber), equalTo(true));
  }

  @ParameterizedTest
  @ValueSource(strings = {"4939454554", "dfkdkfdsf", "495-349-a948", "::FD:FSFDSF", "flg-fgk-fmrk", "[[]-==[-````", "a453-554-3240"})
  void invalidPhoneNumberReturnsFalse(String phoneNumber) {
    assertThat(getValidPhoneNumber(phoneNumber), equalTo(false));
  }

}
