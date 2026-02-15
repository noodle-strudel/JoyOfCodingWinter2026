package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.jdbc.H2DatabaseHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

// TODO
//  DatabaseLoader implements PhoneBillParser
//  ensure it can parse a given phonebill from the database
//  ensure it can parse a given phonebill by name
//  ensure it can parse a given phone bill by name with other different phone bills in it
//  error if there is no table to take from
//  error if there is no matching customer

public class DatabaseLoaderTest {

  @TempDir
  private File tempDir;

  private File loadPhoneBillIntoDatabase() throws IOException {
    File dbFile = new File(tempDir, "billDatabase.h2.db");
    dbFile.createNewFile();
    PhoneBill bill = PhoneBillTest.createPhoneBillNamed("My Name");
    bill.addPhoneCall(PhoneCallTest.CALL_1);
    bill.addPhoneCall(PhoneCallTest.CALL_2);
    bill.addPhoneCall(PhoneCallTest.CALL_5);
    try (
        Connection connection = H2DatabaseHelper.createFileBasedConnection(dbFile)
    ) {
      PhoneBillDAO.createTables(connection);
      DatabaseDumper dbd = new DatabaseDumper(connection);
      dbd.dump(bill);
      PhoneBillDAO.writeOut(connection, dbFile);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return dbFile;
  }

  @Test
  void canCreatePhoneBillFromDatabase() throws IOException {
    File dbFile = loadPhoneBillIntoDatabase();

    try (
        Connection connection = H2DatabaseHelper.createFileBasedConnection(dbFile)
    ) {
      DatabaseLoader dbl = new DatabaseLoader(connection);
      PhoneBill newBill = dbl.parse();
      assertThat(newBill.getCustomer(), equalTo("My Name"));
      assertThat(newBill.getPhoneCalls().size(), equalTo(3));
    } catch (SQLException | ParserException e) {
      throw new RuntimeException(e);
    }
  }
}
