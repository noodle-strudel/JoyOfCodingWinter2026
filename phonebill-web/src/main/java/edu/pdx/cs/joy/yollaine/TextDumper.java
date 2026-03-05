package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.PhoneBillDumper;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * Writes a {@link PhoneBill} to a text file along with its associated {@link PhoneCall}s.
 */
public class TextDumper implements PhoneBillDumper<PhoneBill> {
  private final Writer writer;

  /**
   * The constructor for the TextDumper class.
   * @param writer
   *    A writer to a text file for the TextDumper to use.
   */
  public TextDumper(Writer writer) {
    this.writer = writer;
  }

  /**
   * Dump a given PhoneBill into the given text file.
   * @param bill
   *    The PhoneBill to write to the given file.
   */
  @Override
  public void dump(PhoneBill bill) {
    try (
      PrintWriter pw = new PrintWriter(this.writer)
    ) {
      printBill(bill, pw);
      pw.flush();
    }
  }

  /**
   * Helper function to print phone bills into PrintWriters
   * @param bill The PhoneBill to write to the given file.
   * @param pw the PrintWriter to write the phone bill to.
   */
  private static void printBill(PhoneBill bill, PrintWriter pw) {
    pw.println(bill.getCustomer());
    for (PhoneCall call : bill.getPhoneCalls()) {
      pw.println(call.getCaller() + " " + call.getCallee() + " " + call.getBeginTimeRaw() + " " + call.getEndTimeRaw());
    }
  }
}
