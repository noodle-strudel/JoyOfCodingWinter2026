package edu.pdx.cs.joy.yollaine;

import com.google.common.annotations.VisibleForTesting;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>PhoneBill</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
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
     * Handles an HTTP GET request from a client by writing the definition of the
     * word specified in the "word" HTTP parameter to the HTTP response.  If the
     * "word" parameter is not specified, all of the entries in the dictionary
     * are written to the HTTP response.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        response.setContentType( "text/plain" );

        String customerName = getParameter(CUSTOMER_PARAMETER, request );
        if (customerName != null) {
            log("GET " + customerName);
            writePhoneBillText(customerName, response);

        } else {
            missingRequiredParameter(response, CUSTOMER_PARAMETER);
        }
    }

    /**
     * Handles an HTTP POST request by storing the dictionary entry for the
     * "word" and "definition" request parameters.  It writes the dictionary
     * entry to the HTTP response.
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
        PhoneCall call = new PhoneCall(callerNumber, calleeNumber, beginTime, endTime);
        bill.addPhoneCall(call);

        log("POST " + customerName + " -> " + callerNumber);

        PrintWriter pw = response.getWriter();
        pw.println(Messages.definedWordAs(customerName, callerNumber));
        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK);
    }

    /**
     * Handles an HTTP DELETE request by removing all dictionary entries.  This
     * behavior is exposed for testing purposes only.  It's probably not
     * something that you'd want a real application to expose.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        log("DELETE all dictionary entries");

        this.phoneBills.clear();

        PrintWriter pw = response.getWriter();
        pw.println(Messages.allDictionaryEntriesDeleted());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);

    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * Writes the definition of the given word to the HTTP response.
     *
     * The text of the message is formatted with {@link TextDumper}
     */
    private void writePhoneBillText(String customerName, HttpServletResponse response) throws IOException {
        PhoneBill bill = this.phoneBills.get(customerName);

        if (bill == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        } else {
            PrintWriter pw = response.getWriter();

            TextDumper dumper = new TextDumper(pw);
            dumper.dump(bill);

            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
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

    @VisibleForTesting
    PhoneBill getPhoneBill(String customerName) {
        return this.phoneBills.get(customerName);
    }

    @Override
    public void log(String msg) {
      System.out.println(msg);
    }
}
