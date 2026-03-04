package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.web.HttpRequestHelper;
import edu.pdx.cs.joy.web.HttpRequestHelper.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PhoneBillRestClientTest {

  @Test
  void getPhoneBillPerformsHttpGetWithCustomerNameParameter() throws ParserException, IOException {
    String customerName = "MY NAME";
    PhoneBill bill = new PhoneBill(customerName);

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of(PhoneBillServlet.CUSTOMER_PARAMETER, customerName)))).thenReturn(phoneBillAsText(bill));

    PhoneBillRestClient client = new PhoneBillRestClient(http);

    PhoneBill newBill = client.getPhoneBill(customerName);
    assertThat(newBill, notNullValue());
    assertThat(newBill.getCustomer(), equalTo(customerName));
  }

  // TODO
  //  correct and incorrect 3 parameter
  //  incorrect 1 parameter
  //  add phonecall correctly
  //  maybe incorrectly too
  //  remove all phone bills
  @Test
  void getPhoneBillBetweenTwoDatesPerformsHttpGetWithCustomerNameBeginTimeEndTime() {

  }

  private Response phoneBillAsText(PhoneBill bill) {
    StringWriter writer = new StringWriter();
    new TextDumper(writer).dump(bill);

    return new Response(writer.toString());
  }
}
