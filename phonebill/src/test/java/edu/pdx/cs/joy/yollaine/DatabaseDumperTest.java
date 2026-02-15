package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.jdbc.H2DatabaseHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseDumperTest {

  @Test
  void canDumpPhoneBillWithNoCallsIntoFileDatabase(@TempDir File tempDir) {
    File tempFile = new File(tempDir, "billDataBase.h2.db");
    PhoneBill bill = PhoneBillTest.createPhoneBillNamed("My Name");
    try (
        Connection connection = H2DatabaseHelper.createFileBasedConnection(tempFile)
    ) {
      PhoneBillDAO.createTables(connection);

      PhoneBillDAO billDAO = new PhoneBillDAO(connection);
      DatabaseDumper dbd = new DatabaseDumper(billDAO);
      dbd.dump(bill);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
