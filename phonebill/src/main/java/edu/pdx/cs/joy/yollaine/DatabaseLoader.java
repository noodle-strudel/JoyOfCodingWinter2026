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
  private final PhoneBillDAO billDAO;

  /**
   * Constructor for the DatabaseLoader class.
   * @param connection A connection to a database.
   */
  public DatabaseLoader(Connection connection) {
    this.billDAO = new PhoneBillDAO(connection);
  }

  /**
   * Parse the first customer in a database into a PhoneBill.
   *
   * @return a PhoneBill of the customer with ID 1
   * @throws ParserException if an error occurs while accessing the database.
   */
  @Override
  public PhoneBill parse() throws ParserException {
    try {
      return billDAO.findByCustomerID(1);
    } catch (SQLException e) {
      throw new ParserException(e.getMessage());
    }
  }

  /**
   * Parse a named customer in a database into a PhoneBill.
   * @param customerName The customer to recreate the phone bill.
   * @return the PhoneBill associated with the customer.
   * @throws ParserException if an error occurs while accessing the database.
   */
  public PhoneBill parseNamed(String customerName) throws ParserException {
    try {
      PhoneBill bill = billDAO.findByCustomerName(customerName);
      if (bill == null) {
        throw new ParserException("Customer with name "+customerName+"not found in database.");
      }
      return bill;
    } catch (SQLException e) {
      throw new ParserException(e.getMessage());
    }
  }
}
