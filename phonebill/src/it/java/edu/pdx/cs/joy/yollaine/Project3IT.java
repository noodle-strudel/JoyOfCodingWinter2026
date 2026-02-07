package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.InvokeMainTestCase;
import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.*;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the functionality in the {@link Project3} main class.
 */
class Project3IT extends InvokeMainTestCase {

  /**
   * Invokes the main method of {@link Project3} with the given arguments.
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain(Project3.class, args);
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
    MainMethodResult result = invokeMainCustom();
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  private MainMethodResult invokeMainCustom(String... args) {
    String[] options = {"", "", "", ""};
    HashMap<String, String> cmdLineArgs = new HashMap<>();
    cmdLineArgs.put("name", "Any Name");
    cmdLineArgs.put("caller", "123-456-7890");
    cmdLineArgs.put("callee", "098-765-4321");
    cmdLineArgs.put("beginDate", "4/5/2023");
    cmdLineArgs.put("beginTime", "11:34");
    cmdLineArgs.put("beginAMPM", "AM");
    cmdLineArgs.put("endDate", "4/5/2023");
    cmdLineArgs.put("endTime", "12:59");
    cmdLineArgs.put("endAMPM", "PM");

    /*
     * Parse the args based on my own specification
     * repl - replace the default value of command line options with the given one
     * empty - wipe out values from the hashmap
     * opt - specify an option and supply its needed argument
     */

    int i = 0;
    int iOpt = 0;
    while (i < args.length) {
      switch (args[i]) {
        case "repl" -> cmdLineArgs.put(args[++i], args[++i]);
        case "empty" -> cmdLineArgs.replaceAll((k, v) -> "");
        case "opt" -> {
          options[iOpt++] = args[++i];
          if (args[i].equals("-textFile")) {
            options[iOpt++] = args[++i];
          }
        }
      }
      ++i;
    }

    return invokeMain(options[0],
        options[1],
        options[2],
        options[3],
        cmdLineArgs.get("name"),
        cmdLineArgs.get("caller"),
        cmdLineArgs.get("callee"),
        cmdLineArgs.get("beginDate"),
        cmdLineArgs.get("beginTime"),
        cmdLineArgs.get("beginAMPM"),
        cmdLineArgs.get("endDate"),
        cmdLineArgs.get("endTime"),
        cmdLineArgs.get("endAMPM"));
  }

  @Test
  void invokingMainWithJustNamePrintsMissingCallerNumberToStdErr() {
    MainMethodResult result = invokeMainCustom("empty", "repl", "name", "Koan");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Caller Number"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMainCustom("empty", "opt", "-print", "repl", "name", "Koan");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Caller Number"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithNameCallerNumPrintsMissingCalleeNumberToStdErr() {
    MainMethodResult result = invokeMainCustom("empty", "repl", "name", "Koan", "repl", "caller", "123-456-3984");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Callee Number"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMainCustom( "empty", "opt", "-print", "repl", "name", "Koan", "repl", "caller", "123-456-3984");
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
    result = invokeMain("-print", "Koan", "123-456-3984", "434-556-2321", "02/31/3940");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Begin Time"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithNameCallerNumCalleeNumBeginDateBeginTimeBeginDatePrintsMissingBeginAMPMToStdErr() {
    MainMethodResult result = invokeMain("Koan", "123-456-3984", "434-556-2321", "02/31/3940", "11:34");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Begin Time AM/PM"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMain("-print", "Koan", "123-456-3984", "434-556-2321", "02/31/3940", "11:34");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Begin Time AM/PM"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithNameCallerNumCalleeNumBeginDateBeginTimeBeginAMPMPrintsMissingEndDateToStdErr() {
    MainMethodResult result = invokeMain("Koan", "123-456-3984", "434-556-2321", "02/31/3940", "11:34", "PM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Date"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMain("-print", "Koan", "123-456-3984", "434-556-2321", "02/31/3940", "11:34", "PM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Date"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithNameCallerNumCalleeNumBeginDateBeginTimeEndDatePrintsMissingEndTimeToStdErr() {
    MainMethodResult result = invokeMain("Koan", "123-456-3984", "434-556-2321", "02/31/3940", "11:34", "PM", "02/31/3498");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Time"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMain("-print", "Koan", "123-456-3984", "434-556-2321", "02/31/3940", "11:34", "PM", "02/31/3498");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Time"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithNameCallerNumCalleeNumBeginDateBeginTimeEndDateEndTimePrintsMissingEndAMPMToStdErr() {
    MainMethodResult result = invokeMain("Koan", "123-456-3984", "434-556-2321", "02/31/3940", "11:34", "PM", "02/31/3498", "12:34");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Time AM/PM"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMain("-print", "Koan", "123-456-3984", "434-556-2321", "02/31/3940", "11:34", "PM", "02/31/3498", "12:34");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Time AM/PM"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithValidArgumentsWithPrintPrintsPhoneBillInformation() {
    MainMethodResult result = invokeMainCustom("opt", "-print");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Any Name's phone bill with 1 phone calls"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Phone call from 123-456-7890 to 098-765-4321 from 4/5/2023 11:34 AM to 4/5/2023 12:59 PM"));
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
  }

  @Test
  void invokingMainWithValidArgumentsWithMultiplePrintsPrintsTooManyCommandLineArguments() {
    MainMethodResult result = invokeMain("-print", "Any Name", "123-456-7890", "-print", "098-765-4321", "4/5/2023", "11:34", "AM", "-print", "4/5/2023", "12:59", "PM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] customer callerNumber calleeNumber beginDate beginTime AM/PM endDate endTime AM/PM"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidCallerPhonePrintsInvalidPhoneFormat() {
    MainMethodResult result = invokeMainCustom("repl", "caller", "totally a number");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Caller Phone Format. Phone format should be ###-###-####."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidCalleePhonePrintsInvalidPhoneFormat() {
    MainMethodResult result = invokeMainCustom("repl", "callee", "totally a number");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Callee Phone Format. Phone format should be ###-###-####"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidBeginTimePrintsInvalidDateAndTime() {
    MainMethodResult result = invokeMainCustom("repl", "beginDate", "totally a date");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Begin Time Format. Date and time should be in the format: mm/dd/yyyy hh:mm (AM/PM)"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMainCustom("repl", "beginTime", "totally a time");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Begin Time Format. Date and time should be in the format: mm/dd/yyyy hh:mm (AM/PM)"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMainCustom("repl", "beginAMPM", "totally aMPM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Begin Time Format. Date and time should be in the format: mm/dd/yyyy hh:mm (AM/PM)"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidEndTimePrintsInvalidDateAndTime() {
    MainMethodResult result = invokeMainCustom("repl", "endDate", "totally a date");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid End Time Format. Date and time should be in the format: mm/dd/yyyy hh:mm (AM/PM)"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMainCustom("repl", "endTime", "totally a time");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid End Time Format. Date and time should be in the format: mm/dd/yyyy hh:mm (AM/PM)"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMainCustom("repl", "endAMPM", "totally aMPM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid End Time Format. Date and time should be in the format: mm/dd/yyyy hh:mm (AM/PM)"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithValidArgumentsWithInvalidOptionPrintsUnknownOption() {
    String unknownOption = "-43fdfd";
    MainMethodResult result = invokeMain(unknownOption, "Any Name", "123-456-7890", "-print", "098-765-4321", "4/5/2023", "11:34", "AM", "-print", "4/5/2023", "12:59", "PM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Unknown option " + unknownOption));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithInvalidOptionAndPrintPrintsUnknownOptionToStdErr() {
    String unknownOption = "-43fdfd";
    MainMethodResult result = invokeMain(unknownOption, "-print", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "AM", "4/5/2023", "12:59", "PM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Unknown option " + unknownOption));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    result = invokeMain( "-print", unknownOption, "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "PM", "4/5/2023", "12:59", "PM");
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
    MainMethodResult result = invokeMain("-README", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "AM", "-print", "4/5/2023", "12:59", "PM");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
  }

  @Test
  void invokingMainWithValidArgumentsWithReadMePrintPrintsReadMeOnly() {
    MainMethodResult result = invokeMain("-README", "-print", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "AM", "-print", "4/5/2023", "12:59", "PM");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
  }

  @Test
  void invokingMainWithValidArgumentsWithPrintPrintPrintPrintsPhoneBillInformation() {
    MainMethodResult result = invokeMain("-print", "-print", "-print", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "AM", "4/5/2023", "12:59", "PM");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Any Name's phone bill with 1 phone calls"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Phone call from 123-456-7890 to 098-765-4321 from 4/5/2023 11:34 AM to 4/5/2023 12:59 PM"));
  }

  @Test
  void invokingMainWithValidArgumentsWithREADMEREADMEPrintsReadMe() {
    MainMethodResult result = invokeMain("-README", "-README", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023", "11:34", "AM", "4/5/2023", "12:59", "PM");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
  }

  @Test
  void validPhoneNumberWithTextFileOptionCreatesFileInSystem(@TempDir File tempDir) {
    String fileName = "it-test.txt";
    File f = new File(tempDir, fileName);
    f.delete();
    MainMethodResult result = invokeMainCustom("opt", "-textFile", f.getAbsolutePath());
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    assertTrue(f.exists());
  }

  @Test
  void validPhoneNumberWithTextFileOptionCreatesFileInSystemWithContentsInIt(@TempDir File tempDir) throws FileNotFoundException, ParserException {
    String fileName = "it-test.txt";
    File f = new File(tempDir, fileName);
    f.delete();
    MainMethodResult result = invokeMainCustom("opt", "-textFile", f.getAbsolutePath());
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    assertTrue(f.exists());

    TextParser parser = new TextParser(new FileReader(f.getAbsoluteFile()));
    PhoneBill read = parser.parse();
    assertThat(read.getCustomer(), equalTo("Any Name"));
    PhoneCall o = (PhoneCall) read.getPhoneCalls().toArray()[0];
    assertThat(o.getCaller(), equalTo("123-456-7890"));
    assertThat(o.getCallee(), equalTo("098-765-4321"));
    assertThat(o.getBeginTimeString(), equalTo("4/5/2023 11:34 AM"));
    assertThat(o.getEndTimeString(), equalTo("4/5/2023 12:59 PM"));
  }

  @Test
  void validPhoneNumberWithTextFileOptionAndPrintCreatesFileInSystemWithContentsInItAndPrintsIt(@TempDir File tempDir) throws FileNotFoundException, ParserException {
    String fileName = "it-test.txt";
    File f = new File(tempDir, fileName);
    f.delete();
    MainMethodResult result = invokeMainCustom("opt", "-textFile", f.getAbsolutePath(), "opt", "-print");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Any Name's phone bill with 1 phone calls"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Phone call from 123-456-7890 to 098-765-4321 from 4/5/2023 11:34 AM to 4/5/2023 12:59 PM"));
    assertTrue(f.exists());

    TextParser parser = new TextParser(new FileReader(f.getAbsoluteFile()));
    PhoneBill read = parser.parse();
    assertThat(read.getCustomer(), equalTo("Any Name"));
    PhoneCall o = (PhoneCall) read.getPhoneCalls().toArray()[0];
    assertThat(o.getCaller(), equalTo("123-456-7890"));
    assertThat(o.getCallee(), equalTo("098-765-4321"));
    assertThat(o.getBeginTimeString(), equalTo("4/5/2023 11:34 AM"));
    assertThat(o.getEndTimeString(), equalTo("4/5/2023 12:59 PM"));
  }

  @Test
  void matchingCustomerNameInFileAddsPhoneCallToIt(@TempDir File tempDir) throws IOException, ParserException {
    String fileName = "carlPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()))
    ) {
      pw.println("Carl Jones");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 AM 4/5/2023 12:59 PM");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMainCustom("opt", "-textFile", f.getAbsolutePath(), "repl", "name", "Carl Jones");
    TextParser p = new TextParser(new FileReader(f));
    PhoneBill bill = p.parse();
    assertEquals("Carl Jones's phone bill with 2 phone calls", bill.toString());

    PhoneCall call2 = (PhoneCall) bill.getPhoneCalls().toArray()[1];
    assertThat(call2.toString(), containsString("123-456-7890"));
  }

  @Test
  void matchingCustomerNameInFileWithPrintOptionAddsPhoneCallToItAndPrintsOutUpdatedPhoneBill(@TempDir File tempDir) throws IOException {
    String fileName = "carlPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()))
    ) {
      pw.println("Carl Jones");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 AM 4/5/2023 12:59 PM");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "-print", "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024", "12:55", "AM", "12/12/2043", "12:20", "PM");
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Carl Jones's phone bill with 2 phone calls"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("998-433-4345"));

    result = invokeMain("-print", "-textFile", f.getAbsolutePath(), "Carl Jones", "998-433-4345", "343-343-5446", "3/4/2024", "12:55", "AM", "12/12/2043", "12:20", "PM");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Carl Jones's phone bill with 3 phone calls"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("998-433-4345"));
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
  }

  @Test
  void matchingCustomerNameInFileWithREADMEOptionPrintsReadMeOnly(@TempDir File tempDir) throws IOException {
    String fileName = "carlPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()))
    ) {
      pw.println("Carl Jones");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 AM 4/5/2023 12:59 PM");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "-README", "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024", "12:55", "AM", "12/12/2043", "12:20", "PM");
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));

    result = invokeMain("-README", "-textFile", f.getAbsolutePath(), "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024", "12:55", "AM", "12/12/2043", "12:20", "PM");
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/phonebill-1.0.0.jar [options] <args>"));
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
  }

  @Test
  void matchingCustomerNameInFileWithInvalidArgumentsPrintsCorrectErrorMessagesToStdErr(@TempDir File tempDir) throws IOException {
    String fileName = "carlPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()))
    ) {
      pw.println("Carl Jones");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 AM 4/5/2023 12:59 PM");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024", "2:55", "PM", "12/12/2043", "12:34");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Time AM/PM"));

    result = invokeMain("-textFile", f.getAbsolutePath(), "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024", "2:55", "PM", "12/12/2043");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Time"));

    result = invokeMain("-textFile", f.getAbsolutePath(), "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024", "2:55", "PM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Date"));

    result = invokeMain("-textFile", f.getAbsolutePath(), "Carl Jones", "998-433-4345", "343-343-5445", "3/4/2024", "2:55");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing Begin Time AM/PM"));

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
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()))
    ) {
      pw.println(fileCustomer);
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 AM 4/5/2023 12:59 PM");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), givenCustomer, "998-433-4345", "343-343-5445", "3/4/2024", "2:55", "AM", "12/12/2043", "12:20", "PM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Unmatching customer names in provided file: Expected " + givenCustomer + " but found " + fileCustomer + "."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void parsingFileWithInvalidCallerNumberPrintsCorrespondingErrorToStdErr(@TempDir File tempDir) throws IOException {
    String fileName = "myPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()))
    ) {
      pw.println("Carl");
      pw.println("923-456-7890a 098-765-4321 4/5/2023 11:34 PM 4/5/2023 12:59 AM");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl", "998-433-4345", "343-343-5445", "3/4/2024", "2:55", "AM", "12/12/2043", "12:20", "PM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid phone number format in the given file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void parsingFileWithInvalidCalleeNumberPrintsCorrespondingErrorToStdErr(@TempDir File tempDir) throws IOException {
    String fileName = "myPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()))
    ) {
      pw.println("Carl");
      pw.println("923-456-7890 098--765-4321 4/5/2023 11:34 AM 4/5/2023 12:59 PM");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl", "998-433-4345", "343-343-5445", "3/4/2024", "2:55", "AM", "12/12/2043", "2:20", "PM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid phone number format in the given file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void parsingFileWithInvalidBeginDatePrintsCorrespondingErrorToStdErr(@TempDir File tempDir) throws IOException {
    String fileName = "myPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()))
    ) {
      pw.println("Carl");
      pw.println("923-456-7890 098-765-4321 4//5/2023 11:34 AM 4/5/2023 12:59 PM");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl", "998-433-4345", "343-343-5445", "3/4/2024", "2:55", "AM", "12/12/2043", "12:20", "PM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid date and time format in the given file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void parsingFileWithInvalidBeginTimePrintsCorrespondingErrorToStdErr(@TempDir File tempDir) throws IOException {
    String fileName = "myPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()))
    ) {
      pw.println("Carl");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34[ AM 4/5/2023 12:59 PM");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl", "998-433-4345", "343-343-5445", "3/4/2024", "2:55", "AM", "12/12/2043", "12:20", "PM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid date and time format in the given file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void parsingFileWithInvalidEndDatePrintsCorrespondingErrorToStdErr(@TempDir File tempDir) throws IOException {
    String fileName = "myPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()))
    ) {
      pw.println("Carl");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 AM 4/5wq/2023 12:59 PM");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl", "998-433-4345", "343-343-5445", "3/4/2024", "2:55", "AM", "12/12/2043", "12:20", "PM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid date and time format in the given file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void parsingFileWithInvalidEndTimePrintsCorrespondingErrorToStdErr(@TempDir File tempDir) throws IOException {
    String fileName = "myPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()))
    ) {
      pw.println("Carl");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 AM 4/5/2023 12::59 PM");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "Carl", "998-433-4345", "343-343-5445", "3/4/2024", "2:55", "AM", "12/12/2043", "12:20", "PM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid date and time format in the given file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void supplyingDirectoryToTextFilePrintsErrorToStdErr(@TempDir File tempDir) {
    MainMethodResult result = invokeMain("-textFile", tempDir.getAbsolutePath(), "aa", "456-335-2353", "234-434-4535", "4/5/5004", "03:45", "AM", "2/4/4234", "05:34", "AM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Unable to process file: Given file is a directory."));
  }

  @Test
  void addingDuplicatePhoneCallPrintsErrorMessageToStdErr(@TempDir File tempDir) throws IOException {
    String fileName = "myPhoneBill.txt";
    File f = new File(tempDir, fileName);

    f.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(f.getAbsoluteFile()))
    ) {
      pw.println("Carl");
      pw.println("923-456-7890 098-765-4321 4/5/2023 11:34 AM 4/5/2023 12:59 PM");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    MainMethodResult result = invokeMain("-textFile", f.getAbsolutePath(), "-print", "Carl", "923-456-7890", "098-765-4321", "4/5/2023", "11:34", "AM", "4/5/2023", "12:59", "PM");
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    assertThat(result.getTextWrittenToStandardError(), containsString("Unable to add new phone call to file: Exact phone call already recorded."));
  }

  @Test
  void textFileOptionOnlyWithNoArgsPrintsToStdError() {
    MainMethodResult result = invokeMain("-textFile");
    assertThat(result.getTextWrittenToStandardError(), containsString("-textFile argument requires file path"));
  }

}
