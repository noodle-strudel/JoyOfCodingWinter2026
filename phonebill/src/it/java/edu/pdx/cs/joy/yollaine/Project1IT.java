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
    return invokeMain(Project1.class, args);
  }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  void invokingMainWithNoCommandLineArgumentsPrintsMissingPhoneBillToStdErr() {
    MainMethodResult result = invokeMain();
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing phone bill information"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithAllValidCommandLineArgumentsPrintsNothingToStdErrOrStdOut() {
    MainMethodResult result = invokeMain("Any Name", "123-456-7890", "098-765-4321", "4/5/2023 11:34", "4/5/2023 12:59");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithJustNamePrintsMissingCallerNumberToStdErr() {
    MainMethodResult result = invokeMain("Koan");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Caller Number"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithNameCallerNumPrintsMissingCalleeNumberToStdErr() {
    MainMethodResult result = invokeMain("Koan", "123-456-3984");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Callee Number"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithNameCallerNumCalleeNumPrintsMissingBeginTimeToStdErr() {
    MainMethodResult result = invokeMain("Koan", "123-456-3984", "434-556-2321");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Begin Time"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithNameCallerNumCalleeNumBeginTimePrintsMissingEndTimeToStdErr() {
    MainMethodResult result = invokeMain("Koan", "123-456-3984", "434-556-2321", "02/31/3940 09:39");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Time"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithValidArgumentsWithPrintPrintsPhoneBillInformation() {
    MainMethodResult result = invokeMain("-print", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023 11:34", "4/5/2023 12:59");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Any Name's phone bill with 1 phone calls"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Phone call from 123-456-7890 to 098-765-4321 from 4/5/2023 11:34 to 4/5/2023 12:59"));
  }

  @Test
  void invokingMainWithValidArgumentsWithMultiplePrintsPrintsTooManyCommandLineArguments() {
    MainMethodResult result = invokeMain("-print", "Any Name", "123-456-7890", "-print", "098-765-4321", "4/5/2023 11:34", "-print", "4/5/2023 12:59");
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] customer callerNumber calleeNumber beginTime endTime"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidCallerPhonePrintsInvalidPhoneFormat() {
    MainMethodResult result = invokeMain("Any Name", "totally a number", "445-234-343", "4/5/2023 11:34", "4/5/2023 12:59");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Caller Phone Format. Phone format should be +[Country Code] ###-###-####. Country code is optional."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidCalleePhonePrintsInvalidPhoneFormat() {
    MainMethodResult result = invokeMain("Any Name", "445-234-3433", "totally a number", "4/5/2023 11:34", "4/5/2023 12:59");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Callee Phone Format. Phone format should be +[Country Code] ###-###-####. Country code is optional."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidBeginTimePrintsInvalidDateAndTime() {
    MainMethodResult result = invokeMain("Any Name", "445-234-3433", "123-456-7890", "4/5/2023 11r:34", "4/5/2023 12:59");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Begin Time Format. Date and time should be in the format: mm/dd/yyyy hh:mm"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidEndTimePrintsInvalidDateAndTime() {
    MainMethodResult result = invokeMain("Any Name", "445-234-3433", "123-456-7890", "4/5/2023 11:34", "4/5/2023 12:f59");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid End Time Format. Date and time should be in the format: mm/dd/yyyy hh:mm"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithValidArgumentsWithInvalidOptionPrintsUnknownOption() {
    String unknownOption = "-43fdfd";
    MainMethodResult result = invokeMain(unknownOption, "Any Name", "123-456-7890", "-print", "098-765-4321", "4/5/2023 11:34", "-print", "4/5/2023 12:59");
    assertThat(result.getTextWrittenToStandardError(), containsString("Unknown option " + unknownOption));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  //TODO: test unusual combinations
  //  print with invalid arguments should error and exit
  //  unknown option with invalid arguments
  //  basically the above but with more invalid arguments

  @Test
  void invokingMainWithValidArgumentsWithReadMePrintsReadMeOnly() {
    MainMethodResult result = invokeMain("-README", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023 11:34", "-print", "4/5/2023 12:59");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
  }

  @Test
  void invokingMainWithValidArgumentsWithReadMePrintPrintsReadMeOnly() {
    MainMethodResult result = invokeMain("-README", "-print", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023 11:34", "-print", "4/5/2023 12:59");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
  }

  @Test
  void invokingMainWithValidArgumentsWithPrintPrintPrintPrintsPhoneBillInformation() {
    MainMethodResult result = invokeMain("-print", "-print", "-print", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023 11:34", "4/5/2023 12:59");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Any Name's phone bill with 1 phone calls"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Phone call from 123-456-7890 to 098-765-4321 from 4/5/2023 11:34 to 4/5/2023 12:59"));
  }

  @Test
  void invokingMainWithValidArgumentsWithREADMEREADMEPhoneBillInformation() {
    MainMethodResult result = invokeMain("-README", "-README", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023 11:34", "4/5/2023 12:59");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
  }

}
