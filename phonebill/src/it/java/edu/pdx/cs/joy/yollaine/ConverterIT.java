package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.InvokeMainTestCase;
import edu.pdx.cs.joy.jdbc.PrintH2DatabaseSchema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
  void invokingMainWithCorrectArgumentsConvertsPhoneBillTextFileToDatabaseFile(@TempDir File tempDir) throws IOException, SQLException {
    File textFile = new File(tempDir, "textFile.txt");
    File dbFileBackup = new File(tempDir, "coolThing");
    File dbFile = new File(tempDir, "coolThing.mv.db");
    PhoneBill bill = PhoneBillTest.createPhoneBillNamed("Loki");
    TextDumper textDumper = new TextDumper(new FileWriter(textFile));
    textDumper.dump(bill);
    MainMethodResult result = invokeMain(textFile.getAbsolutePath(), dbFileBackup.getAbsolutePath());
    assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    assertTrue(DatabaseDumperTest.checkFileFor(dbFileBackup, "-- H2 2.4.240;"));
    assertTrue(dbFileBackup.exists());
    assertTrue(dbFile.exists());
    PrintH2DatabaseSchema.main(new String[]{dbFileBackup.getAbsolutePath()});
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
  void invokingMainWithANonExistentFilePrintsNonExistentFileError(@TempDir File tempDir) throws IOException {
    File textFile = new File(tempDir, "textFile.txt");
    File dbFile = new File(tempDir, "coolThing.h2.db");
    MainMethodResult result = invokeMain(textFile.getAbsolutePath(), dbFile.getAbsolutePath());
    assertThat(result.getTextWrittenToStandardError(),containsString("Phone bill text file does not exist"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithMalformedFilePrintsError(@TempDir File tempDir) throws IOException {
    File textFile = new File(tempDir, "textFile.txt");
    File dbFileBackup = new File(tempDir, "coolThing.h2.db");
    File dbFile = new File(tempDir, "coolThing.h2.mv.db");
    textFile.createNewFile();
    try (PrintWriter pw = new PrintWriter(new FileWriter(textFile))) {
      pw.println("Cool thing");
      pw.println("53453 45345345354 435345");
    }
    MainMethodResult result = invokeMain(textFile.getAbsolutePath(), dbFileBackup.getAbsolutePath());
    assertThat(result.getTextWrittenToStandardError(),containsString("Invalid phone number format in the given file."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    assertFalse(dbFile.exists());
  }

  @Test
  void invokingMainWithTooManyArgumentsPrintsError() {
    MainMethodResult result = invokeMain("dfsfsd", "sdfsf", "643");
    assertThat(result.getTextWrittenToStandardError(),containsString("Too many command line arguments"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage: java -cp target/phonebill-1.0.0.jar edu.pdx.cs.joy.<login-id>.Converter textFile dbFile"));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }

  @Test
  void invokingMainWithSharedFileLocationPrintsError(@TempDir File tempDir) throws IOException {
    File textFile = new File(tempDir, "textFile.txt");
    File dbFileBackup = new File(tempDir, "coolThing");
    File dbFile = new File(tempDir, "coolThing.mv.db");
    PhoneBill bill = PhoneBillTest.createPhoneBillNamed("Loki");
    TextDumper textDumper = new TextDumper(new FileWriter(textFile));
    textDumper.dump(bill);
    MainMethodResult result = invokeMain(textFile.getAbsolutePath(), textFile.getAbsolutePath());
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: Given textFile and dbFile share the same file location."));
    assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
  }
}
