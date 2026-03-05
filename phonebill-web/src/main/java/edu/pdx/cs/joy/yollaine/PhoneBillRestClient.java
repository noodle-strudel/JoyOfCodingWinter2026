package edu.pdx.cs.joy.yollaine;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.web.HttpRequestHelper;
import edu.pdx.cs.joy.web.HttpRequestHelper.Response;
import edu.pdx.cs.joy.web.HttpRequestHelper.RestException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the REST client.
 */
public class PhoneBillRestClient {

  private static final String WEB_APP = "phonebill";
  private static final String SERVLET = "calls";
  private final HttpRequestHelper http;

  /**
   * Creates a client to the Phone Bill REST service running on the given host and port
   * @param hostName The name of the host
   * @param port The port
   */
  public PhoneBillRestClient( String hostName, int port ) {
    this(new HttpRequestHelper(String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET)));
  }

  /**
   * Constructor that takes a HTTPRequestHelper
   * @param http the RequestHelper to use
   */
  @VisibleForTesting
  PhoneBillRestClient(HttpRequestHelper http) {
    this.http = http;
  }

  /**
   * Returns the phone bill for the given customer
   * @param customerName the customer's name
   * @return the phone bill from the server
   * @throws IOException if an error occurs while accessing the server
   * @throws ParserException if an error occurs while parsing the phone bill
   */
  public PhoneBill getPhoneBill(String customerName) throws IOException, ParserException {
    Response response = http.get(Map.of(PhoneBillServlet.CUSTOMER_PARAMETER, customerName));
    throwExceptionIfNotOkayHttpStatus(response);
    String content = response.getContent();

    TextParser parser = new TextParser(new StringReader(content));
    return parser.parse();
  }

  /**
   * Returns the phone bill for the given customer in a time range
   * @param customerName the customer's name
   * @param beginTime the start of the time range
   * @param endTime the end of the time range
   * @return the phone bill from the server
   * @throws IOException if an error occurs while accessing the server
   * @throws ParserException if an error occurs while parsing the phone bill
   */
  public PhoneBill getPhoneBill(String customerName, String beginTime, String endTime) throws IOException, ParserException {
    Response response = http.get(Map.of(PhoneBillServlet.CUSTOMER_PARAMETER, customerName, PhoneBillServlet.BEGIN_TIME_PARAMETER, beginTime, PhoneBillServlet.END_TIME_PARAMETER, endTime));
    throwExceptionIfNotOkayHttpStatus(response);
    String content = response.getContent();

    TextParser parser = new TextParser(new StringReader(content));
    return parser.parse();
  }

  /**
   * Adds a phone call to the servlet
   * @param customerName the customer's name
   * @param phoneCall the phone call to add
   * @throws IOException if an error occurs while adding it to the server
   */
  public void addPhoneCall(String customerName, PhoneCall phoneCall) throws IOException {
    Response response = http.post(
      Map.of(
        PhoneBillServlet.CUSTOMER_PARAMETER, customerName,
        PhoneBillServlet.CALLER_NUMBER_PARAMETER, phoneCall.getCaller(),
        PhoneBillServlet.CALLEE_NUMBER_PARAMETER, phoneCall.getCallee(),
        PhoneBillServlet.BEGIN_TIME_PARAMETER, phoneCall.getBeginTimeRaw(),
        PhoneBillServlet.END_TIME_PARAMETER, phoneCall.getEndTimeRaw())
    );
    throwExceptionIfNotOkayHttpStatus(response);
  }

  /**
   * Removes all phone bills from the server
   * @throws IOException if an error occurs while accessing the server
   */
  public void removeAllPhoneBills() throws IOException {
    Response response = http.delete(Map.of());
    throwExceptionIfNotOkayHttpStatus(response);
  }

  /**
   * Helper function to throw an exception if a status besides 200 is received
   * @param response the response to analyze
   */
  private void throwExceptionIfNotOkayHttpStatus(Response response) {
    int code = response.getHttpStatusCode();
    if (code != HTTP_OK) {
      String message = response.getContent();
      throw new RestException(code, message);
    }
  }
}
