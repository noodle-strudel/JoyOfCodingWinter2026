package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.jdbc.H2DatabaseHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatabaseLoaderTest {

  @TempDir
  private File tempDir;

  private File dbFile;

  @BeforeEach
  public void loadPhoneBillIntoDatabase() throws IOException {
    this.dbFile = new File(tempDir, "billDatabase.h2.db");
    dbFile.createNewFile();
    PhoneBill bill = PhoneBillTest.createPhoneBillNamed("My Name");
    bill.addPhoneCall(PhoneCallTest.CALL_1);
    bill.addPhoneCall(PhoneCallTest.CALL_2);
    bill.addPhoneCall(PhoneCallTest.CALL_3);

    PhoneBill coolBill = PhoneBillTest.createPhoneBillNamed("Cool Customer");
    coolBill.addPhoneCall(PhoneCallTest.CALL_5);
    coolBill.addPhoneCall(new PhoneCall("353-235-3434", "544-543-5453", "3/4/2005 11:00 PM", "3/5/2005 1:00 AM"));
    try (
        Connection connection = H2DatabaseHelper.createFileBasedConnection(dbFile)
    ) {
      PhoneBillDAO.createTables(connection);
      DatabaseDumper dbd = new DatabaseDumper(connection);
      dbd.dump(bill);
      dbd.dump(coolBill);
      PhoneBillDAO.writeOut(connection, dbFile);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void canCreatePhoneBillFromDatabase() {
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

  @Test
  void canCreateNamedPhoneBillFromDatabase() {
    try (
        Connection connection = H2DatabaseHelper.createFileBasedConnection(dbFile)
    ) {
      DatabaseLoader dbl = new DatabaseLoader(connection);
      PhoneBill newBill = dbl.parseNamed("My Name");
      assertThat(newBill.getCustomer(), equalTo("My Name"));
      assertThat(newBill.getPhoneCalls().size(), equalTo(3));
    } catch (SQLException | ParserException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void canCreateNamedPhoneBillFromDatabaseWithOtherCallsInIt() {
    try (
        Connection connection = H2DatabaseHelper.createFileBasedConnection(dbFile)
    ) {
      DatabaseLoader dbl = new DatabaseLoader(connection);
      PhoneBill newBill = dbl.parseNamed("Cool Customer");
      assertThat(newBill.getCustomer(), equalTo("Cool Customer"));
      assertThat(newBill.getPhoneCalls().size(), equalTo(2));
    } catch (SQLException | ParserException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void throwsErrorIfTheresNoTablesToDumpTo(@TempDir File thisTempDir) {
    File thing = new File(thisTempDir, "4343fsd.h2.db");
    try (
        Connection badConnection = H2DatabaseHelper.createFileBasedConnection(thing);
    ) {
      DatabaseLoader dbl = new DatabaseLoader(badConnection);
      assertThrows(ParserException.class, dbl::parse);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void throwsErrorIfTheresNoNamedCustomerInDatabase() {
    try (
        Connection connection = H2DatabaseHelper.createFileBasedConnection(dbFile)
    ) {
      DatabaseLoader dbl = new DatabaseLoader(connection);
      assertThrows(ParserException.class, () -> dbl.parseNamed("Non-existent Customer"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
