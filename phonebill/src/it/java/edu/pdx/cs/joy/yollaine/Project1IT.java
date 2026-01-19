package edu.pdx.cs.joy.yollaine;

import com.sun.tools.javac.Main;
import edu.pdx.cs.joy.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the functionality in the {@link Project1} main class.
 */
class Project1IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project1} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project1.class, args );
    }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  void invokingMainWithNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing phone bill information"));
  }

  @Test
  void invokingMainWithAllValidCommandLineArguments() {
    MainMethodResult result = invokeMain("Any Name", "123-456-7890", "098-765-4321", "4/5/2023 11:34", "4/5/2023 12:59");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
  }
}