package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.jdbc.H2DatabaseHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.sql.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

// TODO
//  PhoneBillDAO
//  get a single phone call from a customer

/**
 * A simple example unit test that demonstrates persisting a PhoneBill
 * to an H2 in-memory database using JDBC.
 * <p>
 * This is a starting point for students to understand how to use
 * Data Access Objects (DAOs) to persist domain objects to a database.
 */
public class PhoneBillDAOTest {

  public static final String CUSTOMER_NAME = "Jane Doe";
  private Connection connection;
  private PhoneBillDAO dao;
  private File tempFile;

  @BeforeEach
  public void setUp(@TempDir File tempDir) throws SQLException {
    tempFile = new File(tempDir, "myDatabase.h2.db");
    // Create an in-memory H2 database
    connection = H2DatabaseHelper.createFileBasedConnection(tempFile.getAbsoluteFile());

    // Create the phone_bills table
    PhoneBillDAO.createTables(connection);

    // Create the DAO
    dao = new PhoneBillDAO(connection);
  }

  @AfterEach
  public void tearDown() throws SQLException {
    if (connection != null && !connection.isClosed()) {
      connection.close();
    }
  }

  @Test
  public void canPersistAndFetchPhoneBillByCustomerName() throws SQLException {
    PhoneBill bill = new PhoneBill(CUSTOMER_NAME);

    // Persist the phone bill using the DAO
    dao.save(bill);

    // Fetch the phone bill by customer name
    PhoneBill fetchedBill = dao.findByCustomerName(CUSTOMER_NAME);

    // Validate that the fetched bill matches the original
    assertThat(fetchedBill, is(notNullValue()));
    assertThat(fetchedBill.getCustomer(), is(equalTo(CUSTOMER_NAME)));
  }

  @Test
  public void returnsNullWhenPhoneBillNotFound() throws SQLException {
    PhoneBill fetchedBill = dao.findByCustomerName("Non-existent Customer");
    assertThat(fetchedBill, is(nullValue()));
  }

  @Test
  public void canPersistAndFetchPhoneBillByCustomerID() throws SQLException {
    String customerNameTwo = "John Doe";
    PhoneBill bill = new PhoneBill(CUSTOMER_NAME);
    PhoneBill billTwo = new PhoneBill(customerNameTwo);

    // Persist the phone bill using the DAO
    dao.save(bill);

    // Fetch the phone bill by customer name
    PhoneBill fetchedBill = dao.findByCustomerID(1);

    // Validate that the fetched bill matches the original
    assertThat(fetchedBill, is(notNullValue()));
    assertThat(fetchedBill.getCustomer(), is(equalTo(CUSTOMER_NAME)));

    dao.save(billTwo);

    PhoneBill fetchedBillTwo = dao.findByCustomerID(2);
    assertThat(fetchedBillTwo, is(notNullValue()));
    assertThat(fetchedBillTwo.getCustomer(), is(equalTo(customerNameTwo)));
  }

  @Test
  public void canPersistAndFetchPhoneBillWithPhoneCallByCustomerName() throws SQLException {
    PhoneBill bill = new PhoneBill(CUSTOMER_NAME);
    PhoneCall call = PhoneCallTest.CALL_1;

    bill.addPhoneCall(call);

    dao.save(bill);

    PhoneBill fetchedBill = dao.findByCustomerName(CUSTOMER_NAME);
    assertThat(fetchedBill, is(notNullValue()));
    assertThat(fetchedBill.getCustomer(), equalTo(CUSTOMER_NAME));
    assertThat(fetchedBill.getPhoneCalls().size(), equalTo(1));
  }

  @Test
  public void canPersistAndSaveDatabaseWithPhoneBillInAFile() throws SQLException {
    PhoneBill bill = new PhoneBill(CUSTOMER_NAME);
    bill.addPhoneCall(PhoneCallTest.CALL_1);

    dao.save(bill);

    PhoneBillDAO.writeOut(connection, tempFile);
    try (BufferedReader br = new BufferedReader(new FileReader(tempFile))) {
      String line = br.readLine();
      assertThat(line, containsString("-- H2 2.4.240;"));
      for (int i = 0; i < 22; ++i) {
        line = br.readLine();
      }
      assertThat(line, containsString("(1, '345-234-4353', '343-342-3434', 1, TIMESTAMP '2005-01-02 16:56:00', TIMESTAMP '2005-01-02 17:34:00')"));

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void canPersistSaveAndLoadDataBaseWithPhoneBillInAFile() throws SQLException {
    PhoneBill bill = new PhoneBill(CUSTOMER_NAME);
    bill.addPhoneCall(PhoneCallTest.CALL_1);

    dao.save(bill);

    PhoneBillDAO.writeOut(connection, tempFile);

    // create a new connection in memory
    try (Connection memConnection = H2DatabaseHelper.createInMemoryConnection("phonebill_test")) {
      PhoneBillDAO newDAO = new PhoneBillDAO(memConnection);
      PhoneBillDAO.readFrom(memConnection, tempFile);

      PhoneBill fetchedBill = newDAO.findByCustomerName(CUSTOMER_NAME);
      assertThat(fetchedBill, is(notNullValue()));
      assertThat(fetchedBill.getCustomer(), equalTo(CUSTOMER_NAME));
      assertThat(fetchedBill.getPhoneCalls().size(), equalTo(1));
    }
  }

  @Test
  public void canPersistSaveAndLoadDataBaseWithPhoneBillWithMultiplePhoneCallsInAFile() throws SQLException {
    PhoneBill bill = new PhoneBill(CUSTOMER_NAME);
    bill.addPhoneCall(PhoneCallTest.CALL_1);
    bill.addPhoneCall(PhoneCallTest.CALL_2);
    bill.addPhoneCall(PhoneCallTest.CALL_4);

    dao.save(bill);

    PhoneBillDAO.writeOut(connection, tempFile);

    // create a new connection in memory
    try (Connection memConnection = H2DatabaseHelper.createInMemoryConnection("phonebill_test")) {
      PhoneBillDAO newDAO = new PhoneBillDAO(memConnection);
      PhoneBillDAO.readFrom(memConnection, tempFile);

      PhoneBill fetchedBill = newDAO.findByCustomerName(CUSTOMER_NAME);
      assertThat(fetchedBill, is(notNullValue()));
      assertThat(fetchedBill.getCustomer(), equalTo(CUSTOMER_NAME));
      assertThat(fetchedBill.getPhoneCalls().size(), equalTo(3));
    }
  }


}

