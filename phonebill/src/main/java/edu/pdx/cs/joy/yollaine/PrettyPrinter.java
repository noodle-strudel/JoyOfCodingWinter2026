package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.PhoneBillDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.format.DateTimeFormatter;

public class PrettyPrinter implements PhoneBillDumper<PhoneBill> {
  private final Writer writer;

  public PrettyPrinter(Writer writer) {
    this.writer = writer;
  }

  @Override
  public void dump(PhoneBill phoneBill) {
    try (
        PrintWriter pw = new PrintWriter(this.writer)
    ) {
      int customerNameSize = phoneBill.getCustomer().length();
      pw.println("-=".repeat(customerNameSize));
      pw.println("Customer: " + phoneBill.getCustomer());
      pw.println("-=".repeat(customerNameSize));
      pw.println("Phone calls on record:");

      DateTimeFormatter dTFormat = DateTimeFormatter.ofPattern("E, MMM d yyyy h:mm a");
      for (PhoneCall call: phoneBill.getPhoneCalls()) {
        pw.println("\t"+call.getCaller() + " to " + call.getCallee());
        pw.println(call.getBeginTime().format(dTFormat));
      }
    }

  }
}
