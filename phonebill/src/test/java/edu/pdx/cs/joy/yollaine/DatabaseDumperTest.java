package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.jdbc.H2DatabaseHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

// TODO
//  DatabaseDumper implements PhoneBillDumper
//  ensure it can dump a phonebill into the database
//  ensure new phone bills associated with customer are added and duplicate customer is not added
//  ensure multiple phone bills can be dumped without error
//  error if there is no table to dump to

public class DatabaseDumperTest {

  @Test
  void canDumpPhoneBillWithNoCallsIntoFileDatabase(@TempDir File tempDir) throws IOException {
    File tempFile = new File(tempDir, "billDatabase.h2.db");
    tempFile.createNewFile();
    PhoneBill bill = PhoneBillTest.createPhoneBillNamed("My Name");
    try (
        Connection connection = H2DatabaseHelper.createFileBasedConnection(tempFile)
    ) {
      PhoneBillDAO.createTables(connection);
      DatabaseDumper dbd = new DatabaseDumper(connection);
      dbd.dump(bill);
      PhoneBillDAO.writeOut(connection, tempFile);

      try (BufferedReader br = new BufferedReader(new FileReader(tempFile))) {
        String line = br.readLine();
        assertThat(line, containsString("-- H2 2.4.240;"));
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void canDumpPhoneBillWithOneCallIntoFileDatabase(@TempDir File tempDir) throws IOException {
    File tempFile = new File(tempDir, "billDatabase.h2.db");
    tempFile.createNewFile();
    PhoneBill bill = PhoneBillTest.createPhoneBillNamed("My Name");
    bill.addPhoneCall(PhoneCallTest.CALL_1);
    try (
        Connection connection = H2DatabaseHelper.createFileBasedConnection(tempFile)
    ) {
      PhoneBillDAO.createTables(connection);
      DatabaseDumper dbd = new DatabaseDumper(connection);
      dbd.dump(bill);
      PhoneBillDAO.writeOut(connection, tempFile);

      try (BufferedReader br = new BufferedReader(new FileReader(tempFile))) {
        String line = br.readLine();

        for (int i = 0; i < 22; ++i) {
          line = br.readLine();
        }
        assertThat(line, containsString("(1, '345-234-4353', '343-342-3434', 1, TIMESTAMP '2005-01-02 16:56:00', TIMESTAMP '2005-01-02 17:34:00')"));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void canDumpPhoneBillWithMultipleCallsIntoFileDatabase(@TempDir File tempDir) throws IOException {
    File tempFile = new File(tempDir, "billDatabase.h2.db");
    tempFile.createNewFile();
    PhoneBill bill = PhoneBillTest.createPhoneBillNamed("My Name");
    bill.addPhoneCall(PhoneCallTest.CALL_1);
    bill.addPhoneCall(PhoneCallTest.CALL_2);
    bill.addPhoneCall(PhoneCallTest.CALL_5);
    try (
        Connection connection = H2DatabaseHelper.createFileBasedConnection(tempFile)
    ) {
      PhoneBillDAO.createTables(connection);
      DatabaseDumper dbd = new DatabaseDumper(connection);
      dbd.dump(bill);
      PhoneBillDAO.writeOut(connection, tempFile);

      try (BufferedReader br = new BufferedReader(new FileReader(tempFile))) {
        String line = br.readLine();

        for (int i = 0; i < 22; ++i) {
          line = br.readLine();
        }
        assertThat(line, containsString("(1, '345-234-4353', '343-342-3434', 1, TIMESTAMP '2005-01-02 16:56:00', TIMESTAMP '2005-01-02 17:34:00')"));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
