package edu.pdx.cs.joy.yollaine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TODO
//  convert text file to database
//  error if converting nonexistent file
//  error if converting empty file
public class ConverterTest {

  @Test
  void canConvertPhoneBillToDatabaseFile(@TempDir File tempDir) throws SQLException {
    PhoneBill bill = PhoneBillTest.createPhoneBillNamed("Hello");

    File dbFile = new File(tempDir, "fjdf.h2.db");
    Converter.phoneBillToDatabase(bill, dbFile);
    assertTrue(dbFile.exists());
  }
}
