package edu.pdx.cs.joy.yollaine;

import com.sun.tools.javac.Main;
import edu.pdx.cs.joy.InvokeMainTestCase;
import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.*;

import static org.hamcrest.CoreMatchers.*;
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

  @Test
  void validPhoneNumberWithTextFileOptionAndPrintCreatesFileInSystemWithContentsInItAndPrintsIt(@TempDir File tempDir) throws FileNotFoundException, ParserException {
    String fileName = "it-test.txt";
    File f = new File(tempDir, fileName);
    f.delete();
    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "-print", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "4/5/2023", "12:59");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Any Name's phone bill with 1 phone calls"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Phone call from 123-456-7890 to 098-765-4321 from 4/5/2023 11:34 to 4/5/2023 12:59"));
    assertTrue(f.exists());

    TextParser parser = new TextParser(new FileReader(f.getAbsoluteFile()));
    PhoneBill read = parser.parse();
    assertThat(read.getCustomer(), equalTo("Any Name"));
    PhoneCall o = (PhoneCall) read.getPhoneCalls().toArray()[0];
    assertThat(o.getCaller(), equalTo("123-456-7890"));
    assertThat(o.getCallee(), equalTo("098-765-4321"));
    assertThat(o.getBeginTimeString(), equalTo("4/5/2023 11:34"));
    assertThat(o.getEndTimeString(), equalTo("4/5/2023 12:59"));
  }

  @Test
  void matchingCustomerNameInFileAddsPhoneCallToIt(@TempDir File tempDir) throws IOException, ParserException {
    String fileName = "carlPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()));
    ) {
      pw.println("Carl Jones");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 4/5/2023 12:59");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024", "20:55", "12/12/2043", "12:20");
    TextParser p = new TextParser(new FileReader(f));
    PhoneBill bill = p.parse();
    assertEquals("Carl Jones's phone bill with 2 phone calls", bill.toString());

    PhoneCall call2 = (PhoneCall) bill.getPhoneCalls().toArray()[1];
    assertThat(call2.toString(), containsString("998-433-4345"));
  }

  @Test
  void matchingCustomerNameInFileWithPrintOptionAddsPhoneCallToItAndPrintsOutUpdatedPhoneBill(@TempDir File tempDir) throws IOException, ParserException {
    String fileName = "carlPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()));
    ) {
      pw.println("Carl Jones");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 4/5/2023 12:59");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "-print", "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024", "20:55", "12/12/2043", "12:20");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Carl Jones's phone bill with 2 phone calls"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("998-433-4345"));
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));

    result = invokeMain("-print", "-textFile", f.getAbsolutePath(), "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024", "20:55", "12/12/2043", "12:20");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Carl Jones's phone bill with 3 phone calls"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("998-433-4345"));
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
  }

  @Test
  void matchingCustomerNameInFileWithREADMEOptionPrintsReadMeOnly(@TempDir File tempDir) throws IOException, ParserException {
    String fileName = "carlPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()));
    ) {
      pw.println("Carl Jones");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 4/5/2023 12:59");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "-README", "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024", "20:55", "12/12/2043", "12:20");
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));

    result = invokeMain("-README", "-textFile", f.getAbsolutePath(), "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024", "20:55", "12/12/2043", "12:20");
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
  }

  @Test
  void matchingCustomerNameInFileWithInvalidArgumentsPrintsCorrectErrorMessagesToStdErr(@TempDir File tempDir) throws IOException, ParserException {
    String fileName = "carlPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()));
    ) {
      pw.println("Carl Jones");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 4/5/2023 12:59");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024", "20:55", "12/12/2043");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Time"));

    result = invokeMain("-textFile", f.getAbsolutePath(), "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024", "20:55");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Date"));

    result = invokeMain("-textFile", f.getAbsolutePath(), "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Begin Time"));

    result = invokeMain("-textFile", f.getAbsolutePath(), "Carl Jones", "998-433-4345", "343-343-5445");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Begin Date"));

    result = invokeMain("-textFile", f.getAbsolutePath(), "Carl Jones", "998-433-4345");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Callee Number"));

    result = invokeMain("-textFile", f.getAbsolutePath(), "Carl Jones");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Caller Number"));

    result = invokeMain("-textFile", f.getAbsolutePath());
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing phone bill information"));

    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @ParameterizedTest
  @CsvSource({"Carl Jones,Cameron Jones", "Jane Doe,John Doe", "Linda,Polly"})
  void unmatchingCustomerNameInFilePrintsUnmatchingCustomerNameInFileToStdError(String fileCustomer, String givenCustomer, @TempDir File tempDir) throws IOException {
    String fileName = "myPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()));
    ) {
      pw.println(fileCustomer);
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 4/5/2023 12:59");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), givenCustomer, "998-433-4345", "343-343-5445", "3/4/2024", "20:55", "12/12/2043", "12:20");
    assertThat(result.getTextWrittenToStandardError(), containsString("Unmatching customer names in provided file: Expected " + givenCustomer + " but found " + fileCustomer + "."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void parsingFileWithInvalidCallerNumberPrintsCorrespondingErrorToStdErr(@TempDir File tempDir) throws IOException {
    String fileName = "myPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()));
    ) {
      pw.println("Carl");
      pw.println("923-456-7890a 098-765-4321 4/5/2023 11:34 4/5/2023 12:59");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl", "998-433-4345", "343-343-5445", "3/4/2024", "20:55", "12/12/2043", "12:20");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid phone number format in the given file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void parsingFileWithInvalidCalleeNumberPrintsCorrespondingErrorToStdErr(@TempDir File tempDir) throws IOException {
    String fileName = "myPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()));
    ) {
      pw.println("Carl");
      pw.println("923-456-7890 098--765-4321 4/5/2023 11:34 4/5/2023 12:59");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl", "998-433-4345", "343-343-5445", "3/4/2024", "20:55", "12/12/2043", "12:20");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid phone number format in the given file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void parsingFileWithInvalidBeginDatePrintsCorrespondingErrorToStdErr(@TempDir File tempDir) throws IOException {
    String fileName = "myPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()));
    ) {
      pw.println("Carl");
      pw.println("923-456-7890 098-765-4321 4//5/2023 11:34 4/5/2023 12:59");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl", "998-433-4345", "343-343-5445", "3/4/2024", "20:55", "12/12/2043", "12:20");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid date and time format in the given file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void parsingFileWithInvalidBeginTimePrintsCorrespondingErrorToStdErr(@TempDir File tempDir) throws IOException {
    String fileName = "myPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()));
    ) {
      pw.println("Carl");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34[ 4/5/2023 12:59");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl", "998-433-4345", "343-343-5445", "3/4/2024", "20:55", "12/12/2043", "12:20");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid date and time format in the given file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void parsingFileWithInvalidEndDatePrintsCorrespondingErrorToStdErr(@TempDir File tempDir) throws IOException {
    String fileName = "myPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()));
    ) {
      pw.println("Carl");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 4/5wq/2023 12:59");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl", "998-433-4345", "343-343-5445", "3/4/2024", "20:55", "12/12/2043", "12:20");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid date and time format in the given file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void parsingFileWithInvalidEndTimePrintsCorrespondingErrorToStdErr(@TempDir File tempDir) throws IOException {
    String fileName = "myPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()));
    ) {
      pw.println("Carl");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 4/5/2023 12::59");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl", "998-433-4345", "343-343-5445", "3/4/2024", "20:55", "12/12/2043", "12:20");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid date and time format in the given file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void supplyingDirectoryToTextFilePrintsErrorToStdErr(@TempDir File tempDir) {
    MainMethodResult result = invokeMain("-textFile", tempDir.getAbsolutePath(), "aa", "456-335-2353", "234-434-4535", "4/5/5004", "03:45", "2/4/4234", "05:34");
    assertThat(result.getTextWrittenToStandardError(), containsString("Unable to process file: Given file is a directory."));
  }

  // TODO:
  //  not allowing duplicate phone calls in the text file
  //  missing format for text files? test them in here??

  @Test
  void textFileOptionOnlyWithNoArgsPrintsToStdError(@TempDir File tempDir) {
    MainMethodResult result = invokeMain("-textFile");
    assertThat(result.getTextWrittenToStandardError(), containsString("-textFile argument requires file path"));
  }

}
