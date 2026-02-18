package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.InvokeMainTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TODO
//  incorrect format for database file
public class ConverterIT extends InvokeMainTestCase {
  private MainMethodResult invokeMain(String... args) {
    return invokeMain(Converter.class, args);
  }

  @Test
  void invokingMainWithoutArgsPrintsMissingArgsToStdError() {
    MainMethodResult result = invokeMain();
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage: java -cp target/phonebill-1.0.0.jar edu.pdx.cs.joy.<login-id>.Converter textFile dbFile"));
  }

  @Test
  void invokingMainWithOnlyTextFileErrorsMissingDbFileToStdError(@TempDir File tempDir) {
    File file = new File(tempDir, "textFile.txt");
    MainMethodResult result = invokeMain(file.getAbsolutePath());
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing database file"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage: java -cp target/phonebill-1.0.0.jar edu.pdx.cs.joy.<login-id>.Converter textFile dbFile"));
  }

  @Test
  void invokingMainWithCorrectArgumentsConvertsPhoneBillTextFileToDatabaseFile(@TempDir File tempDir) throws IOException {
    File textFile = new File(tempDir, "textFile.txt");
    File dbFile = new File(tempDir, "coolThing.h2.db");
    PhoneBill bill = PhoneBillTest.createPhoneBillNamed("Loki");
    TextDumper textDumper = new TextDumper(new FileWriter(textFile));
    textDumper.dump(bill);
    MainMethodResult result = invokeMain(textFile.getAbsolutePath(), dbFile.getAbsolutePath());
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    assertTrue(DatabaseDumperTest.checkFileFor(dbFile, "-- H2 2.4.240;"));
    assertTrue(dbFile.exists());
  }

  @Test
  void invokingMainWithADirectoryPrintsError(@TempDir File tempDir) {
    File textFile = new File(tempDir, "textFile.txt");
    File dbFile = new File(tempDir, "coolThing.h2.db");
    MainMethodResult result = invokeMain(textFile.getAbsolutePath(), tempDir.getAbsolutePath());
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    assertThat(result.getTextWrittenToStandardError(), containsString("File is a directory"));
    result = invokeMain(tempDir.getAbsolutePath(), dbFile.getAbsolutePath());
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    assertThat(result.getTextWrittenToStandardError(), containsString("File is a directory"));
  }

  @Test
  void invokingMainWithAnEmptyTextFilePrintsEmptyTextFileError(@TempDir File tempDir) throws IOException {
    File textFile = new File(tempDir, "textFile.txt");
    File dbFile = new File(tempDir, "coolThing.h2.db");
    textFile.createNewFile();
    MainMethodResult result = invokeMain(textFile.getAbsolutePath(), dbFile.getAbsolutePath());
    assertThat(result.getTextWrittenToStandardError(),containsString("Missing customer name in given text file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithMalformedFilePrintsError(@TempDir File tempDir) throws IOException {
    File textFile = new File(tempDir, "textFile.txt");
    File dbFile = new File(tempDir, "coolThing.h2.db");
    textFile.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(textFile))) {
      pw.println("Cool thing");
      pw.println("53453 45345345354 435345");
    }
    MainMethodResult result = invokeMain(textFile.getAbsolutePath(), dbFile.getAbsolutePath());
    assertThat(result.getTextWrittenToStandardError(),containsString("Invalid phone number format in the given file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithTooManyArgumentsPrintsError() {
    MainMethodResult result = invokeMain("dfsfsd", "sdfsf", "643");
    assertThat(result.getTextWrittenToStandardError(),containsString("Too many command line arguments"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage: java -cp target/phonebill-1.0.0.jar edu.pdx.cs.joy.<login-id>.Converter textFile dbFile"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }
}
