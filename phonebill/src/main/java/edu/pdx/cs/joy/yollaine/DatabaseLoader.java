package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.PhoneBillParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses a database file meant for {@link PhoneBill PhoneBills} to create a PhoneBill with its associated {@link PhoneCall}s.
 */
public class DatabaseLoader implements PhoneBillParser<PhoneBill> {
  private final Connection connection;

  /**
   * Constructor for the DatabaseLoader class.
   * @param connection A connection to a database.
   */
  public DatabaseLoader(Connection connection) {
    this.connection = connection;
  }

  /**
   * Parse the first customer in a database into a PhoneBill.
   *
   * @return a PhoneBill of the customer with ID 1
   * @throws ParserException if an error occurs while accessing the database.
   */
  @Override
  public PhoneBill parse() throws ParserException {
    String selectFirstCustomer = "SELECT id, name FROM customers WHERE id = 1";

    try (Statement statement = connection.createStatement()) {
      try (ResultSet resultSet = statement.executeQuery(selectFirstCustomer)) {
        if (!resultSet.next())
          return null;

        PhoneBill newBill;
        String name = resultSet.getString("name");
        int customerId = resultSet.getInt("id");
        newBill = new PhoneBill(name);
        for (PhoneCall call : findCallsByCustomerID(customerId)) {
          newBill.addPhoneCall(call);
        }
        return newBill;
      }

    } catch (SQLException e) {
      throw new ParserException(e.getMessage());
    }
  }

  /**
   * Parse PhoneCalls associated with a given customerID in the database.
   * @param customerID Customer ID to match.
   * @return a List of PhoneCalls associated with a customer
   * @throws SQLException if an error occurs accessing the database.
   */
  private List<PhoneCall> findCallsByCustomerID(int customerID) throws SQLException {
    String selectCalls = "SELECT caller, callee, begin_time, end_time FROM phone_calls WHERE customer_id = ?";

    try (PreparedStatement statement_two = connection.prepareStatement(selectCalls)) {
      statement_two.setInt(1, customerID);
      try (ResultSet resultSet = statement_two.executeQuery()) {
        List<PhoneCall> callList = new ArrayList<>();
        while (resultSet.next()) {
          String caller = resultSet.getString("caller");
          String callee = resultSet.getString("callee");
          String beginTime = resultSet.getTimestamp("begin_time").toLocalDateTime().format(PhoneCall.DATE_TIME_FORMAT);
          String endTime = resultSet.getTimestamp("end_time").toLocalDateTime().format(PhoneCall.DATE_TIME_FORMAT);
          PhoneCall newCall = new PhoneCall(caller, callee, beginTime, endTime);
          callList.add(newCall);
        }
        return callList;
      }
    }
  }
}
