package edu.pdx.cs.joy.yollaine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConverterTest {

  @Test
  void canInstantiateConverterClass() {
    new Converter();
  }

  @Test
  void canConvertPhoneBillToDatabaseFile(@TempDir File tempDir) throws SQLException {
    PhoneBill bill = PhoneBillTest.createPhoneBillNamed("Hello");
    File dbFile = new File(tempDir, "fjdf.h2.db");
    File dbFileReal = new File(tempDir, "fjdf.h2.db.mv.db");
    Converter.phoneBillToDatabase(bill, dbFile);
    assertTrue(dbFile.exists());
    assertTrue(dbFileReal.exists());
  }
}
