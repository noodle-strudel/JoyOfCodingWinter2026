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
  private final PhoneBillDAO billDAO;

  /**
   * Constructor for the DatabaseDumper class.
   * @param connection A connection to a database.
   */
  public DatabaseDumper(Connection connection) {
    this.billDAO = new PhoneBillDAO(connection);
  }

  /**
   * Dump a given PhoneBill into the database.
   * @param bill The PhoneBill to insert into the database.
   */
  @Override
  public void dump(PhoneBill bill) {
    try {
      billDAO.save(bill);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
