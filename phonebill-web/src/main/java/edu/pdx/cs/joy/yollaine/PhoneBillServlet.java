package edu.pdx.cs.joy.yollaine;

import com.google.common.annotations.VisibleForTesting;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet provides a REST API for working with an
 * {@link PhoneBill}.
 */
public class PhoneBillServlet extends HttpServlet
{
  static final String CUSTOMER_PARAMETER = "customer";
  static final String CALLER_NUMBER_PARAMETER = "callerNumber";
  static final String CALLEE_NUMBER_PARAMETER = "calleeNumber";
  static final String BEGIN_TIME_PARAMETER = "beginTime";
  static final String END_TIME_PARAMETER = "endTime";
  private final Map<String, PhoneBill> phoneBills = new HashMap<>();

  /**
   * Handles an HTTP GET request from a client by finding the phone bill associated
   * with the customer.
   * @param request the request to analyze
   * @param response the response to create
   * @throws IOException if an error occurs while processin the request
   */
  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
      response.setContentType( "text/plain" );

      String customerName = getParameter(CUSTOMER_PARAMETER, request);
      String beginTime = getParameter(BEGIN_TIME_PARAMETER, request);
      String endTime = getParameter(END_TIME_PARAMETER, request);
      if (customerName != null) {
          log("GET " + customerName);
          writePhoneBillText(customerName, beginTime, endTime, response);

      } else {
          missingRequiredParameter(response, CUSTOMER_PARAMETER);
      }
  }


  /**
   * Handles an HTTP POST request by storing the phone call associated with
   * the customer.
   * @param request the request to analyze
   * @param response the response to make
   * @throws IOException if an error occurs while processing the request
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    response.setContentType( "text/plain" );

    String customerName = getParameter(CUSTOMER_PARAMETER, request );
    if (customerName == null) {
      missingRequiredParameter(response, CUSTOMER_PARAMETER);
      return;
    }

    String callerNumber = getParameter(CALLER_NUMBER_PARAMETER, request );
    if ( callerNumber == null) {
      missingRequiredParameter( response, CALLER_NUMBER_PARAMETER);
      return;
    }

    String calleeNumber = getParameter(CALLEE_NUMBER_PARAMETER, request );
    if (calleeNumber == null) {
      missingRequiredParameter( response, CALLEE_NUMBER_PARAMETER);
      return;
    }

    String beginTime = getParameter(BEGIN_TIME_PARAMETER, request );
    if (beginTime == null) {
      missingRequiredParameter( response, BEGIN_TIME_PARAMETER);
      return;
    }

    String endTime = getParameter(END_TIME_PARAMETER, request );
    if (endTime == null) {
      missingRequiredParameter( response, END_TIME_PARAMETER);
      return;
    }

    PhoneBill bill = this.phoneBills.computeIfAbsent(customerName, PhoneBill::new);
    PhoneCall call = null;
    try {
      call = new PhoneCall(callerNumber, calleeNumber, beginTime, endTime);
    } catch (IllegalArgumentException e) {
      invalidCallArgument(response, e.getMessage());
      return;
    }

    // avoid duplicate phone calls
    if (!bill.getPhoneCalls().contains(call)) {
      bill.addPhoneCall(call);
    }

    log("POST " + customerName + " -> " + callerNumber);

    PrintWriter pw = response.getWriter();
    pw.println(Messages.addedPhoneCallToCustomer(customerName, call));
    pw.flush();

    response.setStatus( HttpServletResponse.SC_OK);
  }


  /**
   * Removes all phone bills from the servlet
   * @param request the request to proces
   * @param response the response to make
   * @throws IOException if an error occurs while processing the request
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/plain");

    log("DELETE all dictionary entries");

    this.phoneBills.clear();

    PrintWriter pw = response.getWriter();
    pw.println(Messages.allPhoneBillsDeleted());
    pw.flush();

    response.setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Writes an error message about a missing parameter to the HTTP response.
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   * @param response the response to modify
   * @param parameterName the missing parameter
   * @throws IOException if an error occurs while sending the error
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName ) throws IOException {
    String message = Messages.missingRequiredParameter(parameterName);
    response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

  /**
   * Writes an error message about an invalid call argument
   * @param response the response to modify
   * @param message the message to send
   * @throws IOException if an error occurs while sending the message
   */
  private void invalidCallArgument(HttpServletResponse response, String message) throws IOException {
    response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
  }

  /**
   * Writes the phone bill of the given customer to the HTTP response.
   * The text of the message is formatted with {@link TextDumper}
   * @param customerName the customer's name
   * @param beginTime the start of the time range
   * @param endTime the end of the time range
   * @param response the response to make
   * @throws IOException if an error occurs while getting the phone bill
   */
  private void writePhoneBillText(String customerName, String beginTime, String endTime, HttpServletResponse response) throws IOException {
    PhoneBill bill = this.phoneBills.get(customerName);

    if (bill == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      PrintWriter pw = response.getWriter();
      pw.print("No phone bills associated with "+customerName+" found.");
      pw.flush();

    } else {
      // if begin time is not null but end time is null or ""
      if (beginTime != null) {
        if (endTime == null || endTime.isEmpty()) {
          missingRequiredParameter(response, END_TIME_PARAMETER);
          return;
        }
      }

      if (endTime != null) {
        if (beginTime == null || beginTime.isEmpty()) {
          missingRequiredParameter(response, BEGIN_TIME_PARAMETER);
          return;
        }
      }

      if (beginTime != null) {
        PhoneBill parsedBill = new PhoneBill(bill.getCustomer());
        LocalDateTime queriedEndTime;
        LocalDateTime queriedBeginTime;
        try {
          queriedBeginTime = LocalDateTime.parse(beginTime, PhoneCall.DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
          invalidCallArgument(response, "Begin time of "+beginTime+" is an invalid date and time. Date and time should be in the format: mm/dd/yyyy hh:mm (AM/PM), with time in a 12-hour format.");
          return;
        }

        try {
          queriedEndTime = LocalDateTime.parse(endTime, PhoneCall.DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
          invalidCallArgument(response, "End time of "+endTime+" is an invalid date and time. Date and time should be in the format: mm/dd/yyyy hh:mm (AM/PM), with time in a 12-hour format.");
          return;
        }

        if (queriedEndTime.isBefore(queriedBeginTime)) {
          invalidCallArgument(response, "End time is before begin time.");
          return;
        }

        for (PhoneCall call : bill.getPhoneCalls()) {
          if (!call.getBeginTime().isBefore(queriedBeginTime) && !call.getBeginTime().isAfter(queriedEndTime)) {
            parsedBill.addPhoneCall(call);
          }
        }

        if (parsedBill.getPhoneCalls().isEmpty()) {
          response.setStatus(HttpServletResponse.SC_NOT_FOUND);
          PrintWriter pw = response.getWriter();
          pw.print("No phone bills associated with "+customerName+" found between the given date times.");
          pw.flush();
          return;
        }

        bill = parsedBill;
      }

      PrintWriter pw = response.getWriter();

      TextDumper dumper = new TextDumper(pw);
      dumper.dump(bill);

      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

  /**
   * Returns the value of the HTTP request parameter with the given name.
   * @param name the parameter to get
   * @param request the request to analyze
   * @return the value of the parameter or <code>null</code> if the value of the parameter is
   *         <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;
    } else {
      return value;
    }
  }

  /**
   * Gets a phone bill by name
   * @param customerName the customer's name
   * @return a phone bill
   */
  @VisibleForTesting
  PhoneBill getPhoneBill(String customerName) {
      return this.phoneBills.get(customerName);
  }

  /**
   * Logs a message from the server
   * @param msg the message to output
   */
  @Override
  public void log(String msg) {
    System.out.println(msg);
  }
}
