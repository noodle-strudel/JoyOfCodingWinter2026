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

  @Test
  void incorrectFormatsForParameters() throws IOException {
    String[] validParams = {"Customer", "123-456-7890", "098-765-4321", "1/2/2002 3:00 PM", "1/3/2002 3:00 PM"};
    String[] servletParams = {
        PhoneBillServlet.CUSTOMER_PARAMETER,
        PhoneBillServlet.CALLER_NUMBER_PARAMETER,
        PhoneBillServlet.CALLEE_NUMBER_PARAMETER,
        PhoneBillServlet.BEGIN_TIME_PARAMETER,
        PhoneBillServlet.END_TIME_PARAMETER
    };

    /* EMPTY PARAMETERS */
    PhoneBillServlet servlet = new PhoneBillServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAMETER)).thenReturn("");

    HttpServletResponse response = mock(HttpServletResponse.class);

    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).sendError(statusCode.capture(), eq("The required parameter "+PhoneBillServlet.CUSTOMER_PARAMETER+" is missing"));
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_PRECONDITION_FAILED));

    for (int i = 1; i < validParams.length; ++i) {
      when(request.getParameter(servletParams[i-1])).thenReturn(validParams[i-1]);
      when(request.getParameter(servletParams[i])).thenReturn("");

      servlet.doPost(request, response);
      verify(response).sendError(statusCode.capture(), eq("The required parameter "+servletParams[i]+" is missing"));
      assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_PRECONDITION_FAILED));
    }

    /* INVALID FORMAT */
    String customerName = "Customer";
    String callerNumber = "123-456-7890";
    String calleeNumber = "098-765-4321";
    String beginTime = "1/2/2002 3:00 PM";
    String endTime = "1/3/2002 3:00 PM";

    HttpServletRequest newRequest = mock(HttpServletRequest.class);
    when(newRequest.getParameter(PhoneBillServlet.CUSTOMER_PARAMETER)).thenReturn(customerName);
    when(newRequest.getParameter(PhoneBillServlet.CALLER_NUMBER_PARAMETER)).thenReturn(callerNumber);
    when(newRequest.getParameter(PhoneBillServlet.CALLEE_NUMBER_PARAMETER)).thenReturn(calleeNumber);
    when(newRequest.getParameter(PhoneBillServlet.BEGIN_TIME_PARAMETER)).thenReturn(beginTime);
    when(newRequest.getParameter(PhoneBillServlet.END_TIME_PARAMETER)).thenReturn(endTime);

    HttpServletResponse newResponse = mock(HttpServletResponse.class);

    when(newResponse.getWriter()).thenReturn(pw);

    when(newRequest.getParameter(PhoneBillServlet.CALLER_NUMBER_PARAMETER)).thenReturn("abc");
    servlet.doPost(newRequest, newResponse);
    verify(newResponse).sendError(statusCode.capture(), eq("Caller number abc is an invalid phone number. Phone numbers are in the format ###-###-####."));
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_BAD_REQUEST));

    when(newRequest.getParameter(PhoneBillServlet.CALLER_NUMBER_PARAMETER)).thenReturn(validParams[1]);
    when(newRequest.getParameter(PhoneBillServlet.CALLEE_NUMBER_PARAMETER)).thenReturn("abc");
    servlet.doPost(newRequest, newResponse);
    verify(newResponse).sendError(statusCode.capture(), eq("Callee number abc is an invalid phone number. Phone numbers are in the format ###-###-####."));
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_BAD_REQUEST));

    when(newRequest.getParameter(PhoneBillServlet.CALLEE_NUMBER_PARAMETER)).thenReturn(validParams[2]);
    when(newRequest.getParameter(PhoneBillServlet.BEGIN_TIME_PARAMETER)).thenReturn("abc");
    servlet.doPost(newRequest, newResponse);
    verify(newResponse).sendError(statusCode.capture(), eq("Begin time of abc is an invalid date and time. Date and time format should be mm/dd/yyyy hh:mm (AM/PM), with time in a 12-hour format."));
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_BAD_REQUEST));

    when(newRequest.getParameter(PhoneBillServlet.BEGIN_TIME_PARAMETER)).thenReturn(validParams[3]);
    when(newRequest.getParameter(PhoneBillServlet.END_TIME_PARAMETER)).thenReturn("abc");
    servlet.doPost(newRequest, newResponse);
    verify(newResponse).sendError(statusCode.capture(), eq("End time of abc is an invalid date and time. Date and time format should be mm/dd/yyyy hh:mm (AM/PM), with time in a 12-hour format."));
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_BAD_REQUEST));
  }

  @Test
  void incorrectFormatForBeginTimeAndEndTimeGet() throws IOException {
    String customerName = "Customer";
    String callerNumber = "123-456-7890";
    String calleeNumber = "098-765-4321";
    String beginTime = "1/2/2002 3:00 PM";
    String endTime = "1/3/2002 3:00 PM";

    PhoneBillServlet servlet = new PhoneBillServlet();

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

    // add phone call to servlet
    servlet.doPost(request, response);

    /* EMPTY PARAMETERS */
    when(request.getParameter(PhoneBillServlet.BEGIN_TIME_PARAMETER)).thenReturn("");
    when(request.getParameter(PhoneBillServlet.END_TIME_PARAMETER)).thenReturn(endTime);

    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    servlet.doGet(request, response);
    verify(response).sendError(statusCode.capture(), eq("The required parameter beginTime is missing"));
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_PRECONDITION_FAILED));

    when(request.getParameter(PhoneBillServlet.BEGIN_TIME_PARAMETER)).thenReturn(beginTime);
    when(request.getParameter(PhoneBillServlet.END_TIME_PARAMETER)).thenReturn("");

    servlet.doGet(request, response);
    verify(response).sendError(statusCode.capture(), eq("The required parameter endTime is missing"));
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_PRECONDITION_FAILED));

    /* INVALID PARAMETERS */
    when(request.getParameter(PhoneBillServlet.BEGIN_TIME_PARAMETER)).thenReturn("123-abc");
    when(request.getParameter(PhoneBillServlet.END_TIME_PARAMETER)).thenReturn(endTime);

    servlet.doGet(request, response);
    verify(response).sendError(statusCode.capture(), eq("Begin time of 123-abc is an invalid date and time. Date and time should be in the format: mm/dd/yyyy hh:mm (AM/PM), with time in a 12-hour format."));

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_BAD_REQUEST));

    when(request.getParameter(PhoneBillServlet.BEGIN_TIME_PARAMETER)).thenReturn(beginTime);
    when(request.getParameter(PhoneBillServlet.END_TIME_PARAMETER)).thenReturn("123-abc");

    servlet.doGet(request, response);
    verify(response).sendError(statusCode.capture(), eq("End time of 123-abc is an invalid date and time. Date and time should be in the format: mm/dd/yyyy hh:mm (AM/PM), with time in a 12-hour format."));
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_BAD_REQUEST));


    /* END TIME BEFORE BEGIN TIME */
    when(request.getParameter(PhoneBillServlet.BEGIN_TIME_PARAMETER)).thenReturn(endTime);
    when(request.getParameter(PhoneBillServlet.END_TIME_PARAMETER)).thenReturn(beginTime);

    servlet.doGet(request, response);
    verify(response).sendError(statusCode.capture(), eq("End time is before begin time."));
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_BAD_REQUEST));
  }

  @Test
  void deleteAllPhoneCallsFromServlet() throws IOException {
    PhoneBillServlet servlet = new PhoneBillServlet();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doDelete(request, response);
    verify(pw).println(eq(Messages.allPhoneBillsDeleted()));
  }

  // TODO:
  //  mock a get
  //  mock a get between two dates
  //  mock a get between two dates where there's nothing
  //  mock a deletion
}
