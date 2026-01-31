package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.InvokeMainTestCase;
import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the functionality in the {@link Project2} main class.
 */
class Project2IT extends InvokeMainTestCase {

  /**
   * Invokes the main method of {@link Project2} with the given arguments.
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain(Project2.class, args);
  }

  /**
   * Tests that invoking the main method with no arguments issues an error.
   */
  @Test
  void invokingMainWithNoCommandLineArgumentsPrintsMissingPhoneBillToStdErr() {
    MainMethodResult result = invokeMain();
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing phone bill information"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithAllValidCommandLineArgumentsPrintsNothingToStdErrOrStdOut() {
    MainMethodResult result = invokeMain("Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithJustNamePrintsMissingCallerNumberToStdErr() {
    MainMethodResult result = invokeMain("Koan");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Caller Number"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMain("-print", "Koan");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Caller Number"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithNameCallerNumPrintsMissingCalleeNumberToStdErr() {
    MainMethodResult result = invokeMain("Koan", "123-456-3984");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Callee Number"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMain("-print", "Koan", "123-456-3984");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Callee Number"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithNameCallerNumCalleeNumPrintsMissingBeginDateToStdErr() {
    MainMethodResult result = invokeMain("Koan", "123-456-3984", "434-556-2321");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Begin Date"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMain("-print", "Koan", "123-456-3984", "434-556-2321");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Begin Date"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithNameCallerNumCalleeNumBeginDatePrintsMissingBeginTimeToStdErr() {
    MainMethodResult result = invokeMain("Koan", "123-456-3984", "434-556-2321", "02/31/3940");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Begin Time"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMain("-print", "Koan", "123-456-3984", "434-556-2321");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Begin Date"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithNameCallerNumCalleeNumBeginDateBeginTimePrintsMissingEndDateToStdErr() {
    MainMethodResult result = invokeMain("Koan", "123-456-3984", "434-556-2321", "02/31/3940", "11:34");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Date"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMain("-print", "Koan", "123-456-3984", "434-556-2321", "02/31/3940", "11:34");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Date"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithNameCallerNumCalleeNumBeginDateBeginTimeEndDatePrintsMissingEndTimeToStdErr() {
    MainMethodResult result = invokeMain("Koan", "123-456-3984", "434-556-2321", "02/31/3940", "11:34", "02/31/3498");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Time"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMain("-print", "Koan", "123-456-3984", "434-556-2321", "02/31/3940", "11:34", "02/31/3498");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Time"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithValidArgumentsWithPrintPrintsPhoneBillInformation() {
    MainMethodResult result = invokeMain("-print", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Any Name's phone bill with 1 phone calls"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Phone call from 123-456-7890 to 098-765-4321 from 4/5/2023 11:34 to 4/5/2023 12:59"));
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
  }

  @Test
  void invokingMainWithValidArgumentsWithMultiplePrintsPrintsTooManyCommandLineArguments() {
    MainMethodResult result = invokeMain("-print", "Any Name", "123-456-7890", "-print", "098-765-4321", "4/5/2023", "11:34", "-print", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] customer callerNumber calleeNumber beginDate beginTime endDate endTime"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidCallerPhonePrintsInvalidPhoneFormat() {
    MainMethodResult result = invokeMain("Any Name", "totally a number", "445-234-343", "4/5/2023", "11:34", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Caller Phone Format. Phone format should be ###-###-####."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidCalleePhonePrintsInvalidPhoneFormat() {
    MainMethodResult result = invokeMain("Any Name", "445-234-3433", "totally a number", "4/5/2023", "11:34", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Callee Phone Format. Phone format should be ###-###-####"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidBeginTimePrintsInvalidDateAndTime() {
    MainMethodResult result = invokeMain("Any Name", "445-234-3433", "123-456-7890", "4/5/2023", "11r:34", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Begin Time Format. Date and time should be in the format: mm/dd/yyyy hh:mm"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidEndTimePrintsInvalidDateAndTime() {
    MainMethodResult result = invokeMain("Any Name", "445-234-3433", "123-456-7890", "4/5/2023", "11:34", "4/5/2023", "12:f59");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid End Time Format. Date and time should be in the format: mm/dd/yyyy hh:mm"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithValidArgumentsWithInvalidOptionPrintsUnknownOption() {
    String unknownOption = "-43fdfd";
    MainMethodResult result = invokeMain(unknownOption, "Any Name", "123-456-7890", "-print", "098-765-4321", "4/5/2023", "11:34", "-print", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), containsString("Unknown option " + unknownOption));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidOptionAndPrintPrintsUnknownOptionToStdErr() {
    String unknownOption = "-43fdfd";
    MainMethodResult result = invokeMain(unknownOption, "-print", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), containsString("Unknown option " + unknownOption));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMain( "-print", unknownOption, "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), containsString("Unknown option " + unknownOption));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithJustInvalidOptionAndPrintPrintsUnknownOptionToStdErr() {
    String unknownOption = "-948948539kfdjg";
    MainMethodResult result = invokeMain(unknownOption, "-print");
    assertThat(result.getTextWrittenToStandardError(), containsString("Unknown option " + unknownOption));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMain("-print", unknownOption);
    assertThat(result.getTextWrittenToStandardError(), containsString("Unknown option " + unknownOption));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidOptionAndReadMeStillPrintsReadMeAndExits() {
    String unknownOption = "-43fdfd";
    MainMethodResult result = invokeMain(unknownOption, "-README", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    result = invokeMain( "-README", unknownOption, "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
  }

  @Test
  void invokingMainWithOnlyReadMePrintsReadMeOnly() {
    MainMethodResult result = invokeMain("-README");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
  }

  @Test
  void invokingMainWithOnlyReadMeAndInvalidOptionPrintsReadMeOnly() {
    MainMethodResult result = invokeMain("-README", "-iriroere");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
    result = invokeMain("-iriroere", "-README");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
  }

  @Test
  void invokingMainWithValidArgumentsWithReadMePrintsReadMeOnly() {
    MainMethodResult result = invokeMain("-README", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "-print", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
  }

  @Test
  void invokingMainWithValidArgumentsWithReadMePrintPrintsReadMeOnly() {
    MainMethodResult result = invokeMain("-README", "-print", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "-print", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
  }

  @Test
  void invokingMainWithValidArgumentsWithPrintPrintPrintPrintsPhoneBillInformation() {
    MainMethodResult result = invokeMain("-print", "-print", "-print", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Any Name's phone bill with 1 phone calls"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Phone call from 123-456-7890 to 098-765-4321 from 4/5/2023 11:34 to 4/5/2023 12:59"));
  }

  @Test
  void invokingMainWithValidArgumentsWithREADMEREADMEPrintsReadMe() {
    MainMethodResult result = invokeMain("-README", "-README", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
  }

  @Test
  void validPhoneNumberWithTextFileOptionCreatesFileInSystem() {
    String fileName = "it-test.txt";
    File f = new File(fileName);
    f.delete();
    MainMethodResult result = invokeMain("-textFile", fileName, "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    assertTrue(f.exists());
  }

  @Test
  void validPhoneNumberWithTextFileOptionCreatesFileInSystemWithContentsInIt() throws FileNotFoundException, ParserException {
    String fileName = "it-test.txt";
    File f = new File(fileName);
    f.delete();
    MainMethodResult result = invokeMain("-textFile", fileName, "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    assertTrue(f.exists());

    TextParser parser = new TextParser(new FileReader(fileName));
    PhoneBill read = parser.parse();
    assertThat(read.getCustomer(), equalTo("Any Name"));
    PhoneCall o = (PhoneCall) read.getPhoneCalls().toArray()[0];
    assertThat(o.getCaller(), equalTo("123-456-7890"));
    assertThat(o.getCallee(), equalTo("098-765-4321"));
    assertThat(o.getBeginTimeString(), equalTo("4/5/2023 11:34"));
    assertThat(o.getEndTimeString(), equalTo("4/5/2023 12:59"));
  }

}
