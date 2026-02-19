package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.ParserException;
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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseDumperTest {

  public static boolean checkFileFor(File file, String strToFind) {
    try (
        BufferedReader br = new BufferedReader(new FileReader(file))
    ) {
      boolean foundString = false;
      String line;
      while ((line = br.readLine()) != null) {
        if (line.contains(strToFind)) {
          foundString = true;
          break;
        }
      }
      return foundString;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

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


      assertTrue(checkFileFor(tempFile, "(1, 'My Name')"));
      assertTrue(new File(tempDir, "billDatabase.h2.db.mv.db").exists());
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

      assertTrue(checkFileFor(tempFile, "(1, '345-234-4353', '343-342-3434', 1, TIMESTAMP '2005-01-02 16:56:00', TIMESTAMP '2005-01-02 17:34:00')"));
      assertTrue(new File(tempDir, "billDatabase.h2.db.mv.db").exists());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void databaseDumperThrowsErrorIfThereIsNoTableToDumpTo(@TempDir File tempDir) throws IOException {
    File tempFile = new File(tempDir, "billDatabase.h2.db");
    tempFile.createNewFile();
    PhoneBill bill = PhoneBillTest.createPhoneBillNamed("My Name");
    bill.addPhoneCall(PhoneCallTest.CALL_1);
    try (
        Connection connection = H2DatabaseHelper.createFileBasedConnection(tempFile)
    ) {
      DatabaseDumper dbd = new DatabaseDumper(connection);
      assertThrows(RuntimeException.class ,() -> dbd.dump(bill));
      assertTrue(new File(tempDir, "billDatabase.h2.db.mv.db").exists());
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

      assertTrue(checkFileFor(tempFile, "(1, '345-234-4353', '343-342-3434', 1, TIMESTAMP '2005-01-02 16:56:00', TIMESTAMP '2005-01-02 17:34:00')"));
      // ensure it returns false for nonexistent data
      assertFalse(checkFileFor(tempFile, "(1, '345-234-4353', '343-342-3435', 1, TIMESTAMP '2005-01-02 16:56:00', TIMESTAMP '2005-01-02 17:34:00')"));
      assertTrue(new File(tempDir, "billDatabase.h2.db.mv.db").exists());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void addMorePhoneCallsToCustomerInDifferentDumpsSuccessfully(@TempDir File tempDir) throws IOException {
    File tempFile = new File(tempDir, "4324.h2.db");
    tempFile.createNewFile();
    PhoneBill bill = PhoneBillTest.createPhoneBillNamed("My Name");
    bill.addPhoneCall(PhoneCallTest.CALL_1);

    try (
        Connection connection = H2DatabaseHelper.createFileBasedConnection(tempFile)
    ) {
      PhoneBillDAO.createTables(connection);
      DatabaseDumper dbd = new DatabaseDumper(connection);
      dbd.dump(bill);
      bill.addPhoneCall(PhoneCallTest.CALL_2);
      dbd.dump(bill);
      PhoneBillDAO.writeOut(connection, tempFile);

      assertTrue(checkFileFor(tempFile, "(1, '345-234-4353', '343-342-3434', 1, TIMESTAMP '2005-01-02 16:56:00', TIMESTAMP '2005-01-02 17:34:00')"));
      assertTrue(checkFileFor(tempFile, "(2, '345-234-4353', '343-342-3434', 1, TIMESTAMP '2005-03-02 16:56:00', TIMESTAMP '2005-03-02 17:34:00')"));
      // ensure it returns false for nonexistent data
      assertFalse(checkFileFor(tempFile, "(1, '345-234-4353', '343-342-3435', 1, TIMESTAMP '2005-01-02 16:56:00', TIMESTAMP '2005-01-02 17:34:00')"));
      assertTrue(new File(tempDir, "4324.h2.db.mv.db").exists());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
