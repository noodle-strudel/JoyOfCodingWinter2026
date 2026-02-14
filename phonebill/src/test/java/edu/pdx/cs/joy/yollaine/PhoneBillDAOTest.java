package edu.pdx.cs.joy.yollaine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * A simple example unit test that demonstrates persisting a PhoneBill
 * to an H2 in-memory database using JDBC.
 *
 * This is a starting point for students to understand how to use
 * Data Access Objects (DAOs) to persist domain objects to a database.
 */
public class PhoneBillDAOTest {

  public static final String CUSTOMER_NAME = "Jane Doe";
  private Connection connection;
  private PhoneBillDAO dao;

  @BeforeEach
  public void setUp() throws SQLException {
    // Create an in-memory H2 database
    connection = DriverManager.getConnection("jdbc:h2:mem:phonebill_test");

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

  //TODO
  // get a phone call from a customer
}

