package edu.pdx.cs.joy.yollaine;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link PhoneBillServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */


class PhoneBillServletTest {
  //TODO:
  // Servlet does not allow no arguments, it is a required parameter.
  //
  @Test
  void customerNameIsRequiredParameter() throws ServletException, IOException {
    PhoneBillServlet servlet = new PhoneBillServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    // Nothing is written to the response's PrintWriter
    verify(pw, never()).println(anyString());
    verify(response).sendError(eq(HttpServletResponse.SC_PRECONDITION_FAILED), anyString());
  }

  @Test
  void addOneCallToPhoneBill() throws ServletException, IOException {
    PhoneBillServlet servlet = new PhoneBillServlet();

    String customerName = "Customer";
    String callerNumber = "123-456-7890";
    String calleeNumber = "098-765-4321";
    String beginTime = "1/2/2002 3:00 PM";
    String endTime = "1/3/2002 3:00 PM";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAMETER)).thenReturn(customerName);
    when(request.getParameter(PhoneBillServlet.CALLER_NUMBER_PARAMETER)).thenReturn(callerNumber);
    when(request.getParameter(PhoneBillServlet.CALLEE_NUMBER_PARAMETER)).thenReturn(calleeNumber);
    when(request.getParameter(PhoneBillServlet.BEGIN_TIME_PARAMETER)).thenReturn(beginTime);
    when(request.getParameter(PhoneBillServlet.END_TIME_PARAMETER)).thenReturn(endTime);

    HttpServletResponse response = mock(HttpServletResponse.class);

    // Use a StringWriter to gather the text from multiple calls to println()
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    PhoneCall expectedCall = new PhoneCall(callerNumber, calleeNumber, beginTime, endTime);
    assertThat(stringWriter.toString(), containsString(Messages.addedPhoneCallToCustomer(customerName, expectedCall)));

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));

    PhoneBill phoneBill = servlet.getPhoneBill(customerName);
    assertThat(phoneBill, notNullValue());
    PhoneCall call = phoneBill.getPhoneCalls().iterator().next();
    assertThat(call.getCaller(), equalTo(callerNumber));
  }

}
