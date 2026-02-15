package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.PhoneBillDumper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Writes a {@link PhoneBill} to a database file along with its associated {@link PhoneCall}s.
 */
public class DatabaseDumper implements PhoneBillDumper<PhoneBill> {
  private final Connection connection;

  /**
   * Constructor for the DatabaseDumper class.
   * @param connection A connection to a database.
   */
  public DatabaseDumper(Connection connection) {
    this.connection = connection;
  }

  /**
   * Dump a given PhoneBill into the database.
   * @param bill The PhoneBill to insert into the database.
   */
  @Override
  public void dump(PhoneBill bill) {
    String insertCustomer = "INSERT INTO customers (name) VALUES (?)";

    try (PreparedStatement statement = connection.prepareStatement(insertCustomer)) {
      statement.setString(1, bill.getCustomer());
      statement.executeUpdate();

      for (PhoneCall call: bill.getPhoneCalls()) {
        String insertPhoneCall = "INSERT INTO phone_calls (" +
              "caller, callee, customer_id, begin_time, end_time" +
            ") VALUES (" +
              "?, ?, (SELECT id FROM customers WHERE name = ?), ?, ?" +
            ")";

        try (PreparedStatement callStatement = connection.prepareStatement(insertPhoneCall)) {
          callStatement.setString(1, call.getCaller());
          callStatement.setString(2, call.getCallee());
          callStatement.setString(3, bill.getCustomer());
          callStatement.setTimestamp(4, Timestamp.valueOf(call.getBeginTime()));
          callStatement.setTimestamp(5, Timestamp.valueOf(call.getEndTime()));
          callStatement.executeUpdate();
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }
}
