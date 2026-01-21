package edu.pdx.cs.joy.yollaine;

import org.junit.jupiter.api.Test;

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
      assertThat(line, containsString("This is a README file!"));
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
}
