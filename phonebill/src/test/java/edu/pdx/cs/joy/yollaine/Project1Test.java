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
 * A unit test for code in the <code>Project1</code> class.  This is different
 * from <code>Project1IT</code> which is an integration test (and can capture data
 * written to {@link System#out} and the like.
 */
class Project1Test {

  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project1.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      assertThat(line, containsString("Yollaine Brooks"));
    }
  }
  private static boolean isValidDateAndTime(String dateAndTime) {
    return Project1.isValidDateAndTime(dateAndTime);
  }
  @Test
  void January212026At1PMIsAValidDateAndTime() {
    assertThat(isValidDateAndTime("1/21/2026 01:00"), equalTo(true));
  }

  @Test
  void emptyStringIsAnInvalidDateAndTime() {
    assertThat(isValidDateAndTime(""), equalTo(false));
  }

  @Test
  void February302000At1PMIsAValidDateAndTime() {
    assertThat(isValidDateAndTime("02/30/2000 01:00"), equalTo(true));
  }

  @Test
  void EmptyDataIsAnInvalidDateAndTime() {
    assertThat(isValidDateAndTime(""), equalTo(false));
  }

  @ParameterizedTest
  @ValueSource(strings = {"000/30/2000 00:00", "139/30/2000 00:00", "/30/2000 00:00", "10334/30/2000 00:00"})
  void InvalidMonthIsAnInvalidDateAndTime(String date) {
    assertThat(isValidDateAndTime(date), equalTo(false));
  }

  // TODO: invalid month, invalid year, invalid time, semantically incorrect is still correct
}
