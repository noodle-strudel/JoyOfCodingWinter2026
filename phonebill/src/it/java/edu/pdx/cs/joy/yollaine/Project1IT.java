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
    }

    @Test
    void invokingMainWithAllValidCommandLineArgumentsPrintsNothingToStdErr() {
        MainMethodResult result = invokeMain("Any Name", "123-456-7890", "098-765-4321", "4/5/2023 11:34", "4/5/2023 12:59");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    @Test
    void invokingMainWithJustNamePrintsMissingCallerNumberToStdErr() {
        MainMethodResult result = invokeMain("Koan");
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing Caller Number"));
    }

    @Test
    void invokingMainWithNameCallerNumPrintsMissingCalleeNumberToStdErr() {
        MainMethodResult result = invokeMain("Koan", "123-456-3984");
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing Callee Number"));
    }

    @Test
    void invokingMainWithNameCallerNumCalleeNumPrintsMissingBeginTimeToStdErr() {
        MainMethodResult result = invokeMain("Koan", "123-456-3984", "434-556-2321");
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing Begin Time"));
    }

    @Test
    void invokingMainWithNameCallerNumCalleeNumBeginTimePrintsMissingEndTimeToStdErr() {
        MainMethodResult result = invokeMain("Koan", "123-456-3984", "434-556-2321", "02/31/3940 09:39");
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing End Time"));
    }

    @Test
    void invokingMainWithValidArgumentsWithPrintPrintsPhoneBillInformation() {
        MainMethodResult result = invokeMain("-print", "Any Name", "123-456-7890", "098-765-4321", "4/5/2023 11:34", "4/5/2023 12:59");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Any Name" + "'s phone bill with 1 phone calls"));
    }

    @Test
    void invokingMainWithValidArgumentsWithMultiplePrintsPrintsTooManyCommandLineArguments() {
        MainMethodResult result = invokeMain("-print", "Any Name", "123-456-7890", "-print", "098-765-4321", "4/5/2023 11:34", "-print", "4/5/2023 12:59");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments"));
    }

    @Test
    void invokingMainWithValidArgumentsWithInvalidOptionPrintsUnknownOption() {
        String unknownOption = "-43fdfd";
        MainMethodResult result = invokeMain(unknownOption, "Any Name", "123-456-7890", "-print", "098-765-4321", "4/5/2023 11:34", "-print", "4/5/2023 12:59");
        assertThat(result.getTextWrittenToStandardError(), containsString("Unknown option " + unknownOption));
    }

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
        assertThat(result.getTextWrittenToStandardOut(), containsString("Any Name" + "'s phone bill with 1 phone calls"));
    }

}
