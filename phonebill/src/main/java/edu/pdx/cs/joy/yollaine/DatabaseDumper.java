package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.PhoneBillDumper;

import java.sql.SQLException;

public class DatabaseDumper implements PhoneBillDumper<PhoneBill> {
  // dump to a database
  private final PhoneBillDAO billDAO;

  public DatabaseDumper(PhoneBillDAO billDAO) {
    this.billDAO = billDAO;
  }

  @Override
  public void dump(PhoneBill bill) {
    try {
      billDAO.save(bill);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
