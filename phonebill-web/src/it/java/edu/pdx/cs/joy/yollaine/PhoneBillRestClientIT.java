package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.web.HttpRequestHelper.RestException;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration test that tests the REST calls made by {@link PhoneBillRestClient}
 */
@TestMethodOrder(MethodName.class)
class PhoneBillRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private PhoneBillRestClient newPhoneBillRestClient() {
    int port = Integer.parseInt(PORT);
    return new PhoneBillRestClient(HOSTNAME, port);
  }

  @Test
  void test0RemoveAllPhoneBills() throws IOException {
    PhoneBillRestClient client = newPhoneBillRestClient();
    client.removeAllPhoneBills();
  }

  @Test
  void test1AddOnePhoneBill() throws IOException, ParserException {
    PhoneBillRestClient client = newPhoneBillRestClient();
    String customerName = "TEST NAME";
    String callerNumber = "123-456-7890";
    String calleeNumber = "098-765-4321";
    String beginTime = "1/1/2004 3:00 PM";
    String endTime = "1/2/2004 3:00 PM";


    PhoneCall phoneCall = new PhoneCall(callerNumber, calleeNumber, beginTime, endTime);
    client.addPhoneCall(customerName, phoneCall);

    PhoneBill bill = client.getPhoneBill(customerName);
    assertThat(bill.getCustomer(), equalTo(customerName));
  }

  @Test
  void test2GetAddedPhoneBill() throws IOException, ParserException {
    PhoneBillRestClient client = newPhoneBillRestClient();
    String customerName = "TEST NAME";

    PhoneBill bill = client.getPhoneBill(customerName);
    assertThat(bill.getCustomer(), equalTo(customerName));
  }

  @Test
  void test3AddMorePhoneBillsWithDifferentCustomerNames() throws IOException, ParserException {
    PhoneBillRestClient client = newPhoneBillRestClient();
    String customerName = "TEST NAME";
    String customerName2 = "123";
    String customerName3 = "321";
    String callerNumber = "123-456-7890";
    String calleeNumber = "098-765-4321";
    String beginTime = "1/1/2004 3:00 PM";
    String endTime = "1/2/2004 3:00 PM";


    PhoneCall phoneCall = new PhoneCall(callerNumber, calleeNumber, beginTime, endTime);
    client.addPhoneCall(customerName, phoneCall);
    client.addPhoneCall(customerName2, phoneCall);
    client.addPhoneCall(customerName3, phoneCall);

    PhoneBill bill = client.getPhoneBill(customerName);
    assertThat(bill.getCustomer(), equalTo(customerName));

    bill = client.getPhoneBill(customerName2);
    assertThat(bill.getCustomer(), equalTo(customerName2));

    bill = client.getPhoneBill(customerName3);
    assertThat(bill.getCustomer(), equalTo(customerName3));
  }

  // The phone call already has input verification built into it
  @Test
  void test4EmptyCustomerNameThrowsException() {
    PhoneBillRestClient client = newPhoneBillRestClient();
    String emptyString = "";

    RestException ex = assertThrows(RestException.class, () -> client.addPhoneCall(emptyString, new PhoneCall("123-456-7890", "098-765-4321", "1/1/2004 3:00 PM", "1/2/2004 3:00 PM")));
    assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
    assertThat(ex.getMessage(), containsString(Messages.missingRequiredParameter(PhoneBillServlet.CUSTOMER_PARAMETER)));
  }

}
