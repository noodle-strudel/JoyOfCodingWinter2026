package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.PhoneBillDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
      pw.println();
      pw.println("Phone calls on record:");
      pw.println("-----------------------------------------------------------------------------------------------------------");
      pw.println("Caller Number | Callee Number | Start Date and Time         | End Date and Time            | Duration (min)");
      pw.println("-----------------------------------------------------------------------------------------------------------");

      DateTimeFormatter dTFormat = DateTimeFormatter.ofPattern("E, MMM d yyyy 'at' h:mm a");
      for (PhoneCall call: phoneBill.getPhoneCalls()) {
        String startDateTime = call.getBeginTime().format(dTFormat);
        String endDateTime =  call.getEndTime().format(dTFormat);
        pw.println(call.getCaller() + "  | " + call.getCallee() + "  | " + startDateTime + " | " + endDateTime + " | " + call.getBeginTime().until(call.getEndTime(), ChronoUnit.MINUTES));
      }
    }

  }
}
