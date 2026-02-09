package edu.pdx.cs.joy.yollaine;

import com.google.common.annotations.VisibleForTesting;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link PhoneCall} class.
 *
 */
public class PhoneCallTest {

  private static PhoneCall createPhoneCall(String... params) {
    return switch (params.length) {
      case 1 -> new PhoneCall(params[0], "098-765-4321", "10/01/1000 00:00 AM", "10/01/1000 10:00 PM");
      case 2 -> new PhoneCall(params[0], params[1], "10/01/1000 00:00 AM", "10/10/1000 10:00 PM");
      case 3 -> new PhoneCall(params[0], params[1], params[2], "10/10/1000 10:00 PM");
      case 4 -> new PhoneCall(params[0], params[1], params[2], params[3]);
      default -> new PhoneCall("123-456-7890", "098-765-4321", "10/01/1000 00:00 AM", "10/10/1000 10:00 PM");
    };
  }

  @Test
  void phoneCallWithCorrectArgumentsThrowsNoException() {
    PhoneCall call = createPhoneCall();
  }

  @Test
  void phoneCallWithEmptyCallerThrowsExceptionWithMessageMissingCaller() {
    assertThrows(IllegalArgumentException.class, () -> createPhoneCall(""));
  }

  @Test
  void phoneCallWithEmptyCalleeThrowsExceptionWithMessageMissingCallee() {
    assertThrows(IllegalArgumentException.class, () -> createPhoneCall("098-765-4321", ""));
  }

  @Test
  void phoneCallWithEmptyBeginTimeThrowsExceptionWithMessageMissingBeginTime() {
    assertThrows(IllegalArgumentException.class, () -> createPhoneCall("098-765-4321", "494-394-2948", ""));
  }

  @Test
  void phoneCallWithEmptyEndTimeThrowsExceptionWithMessageMissingEndTime() {
    assertThrows(IllegalArgumentException.class, () -> createPhoneCall("098-765-4321", "494-494-3403", "10/01/1000 00:00 AM", ""));
  }

  @Test
  void phoneCallWithInvalidDateOrTimeThrowsDateTimeException() {
    assertThrows(IllegalArgumentException.class, () -> createPhoneCall("098-765-4321", "494-494-3403", "00/00/0000 00:00", "2/4/2054 4:35 AM"));
    assertThrows(IllegalArgumentException.class, () -> createPhoneCall("098-765-4321", "494-494-3403", "2/4/2054 4:35 AM", "00/00/0000 00:00"));
    assertThrows(IllegalArgumentException.class, () -> createPhoneCall("098-765-4321", "494-494-3403", "2/32/2054 4:35 AM", "10/01/1000 00:00 AM"));
  }

  @Test
  void getCallerReturnsCallerNumber() {
    PhoneCall call = createPhoneCall();
    assertThat(call.getCaller(), equalTo("123-456-7890"));
  }

  @Test
  void getCalleeReturnsCalleeNumber() {
    PhoneCall call = createPhoneCall();
    assertThat(call.getCallee(), equalTo("098-765-4321"));
  }

  @Test
  void getBeginTimeStringReturnsBeginTimeAsAString() {
    PhoneCall call = createPhoneCall("098-765-4321", "494-494-3403", "10/03/2004 00:00 AM", "4/12/3001 3:32 PM");
    assertThat(call.getBeginTimeString(), equalTo("10/3/04, 12:00 AM"));
  }

  @Test
  void getBeginTimeReturnsBeginTimeAsALocalDateObject() {
    PhoneCall call = createPhoneCall("098-765-4321", "494-494-3403", "10/03/2004 00:00 AM", "4/12/3001 3:32 PM");
    LocalDateTime beginTime = call.getBeginTime();
    assertThat(beginTime.toString(), equalTo("2004-10-03T00:00"));
  }

  @Test
  void getEndTimeStringReturnsBeginTimeAsAString() {
    PhoneCall call = createPhoneCall("098-765-4321", "494-494-3403", "10/03/2004 00:00 AM", "4/12/3001 3:32 PM");
    assertThat(call.getEndTimeString(), equalTo("4/12/01, 3:32 PM"));
  }


  @Test
  void getEndTimeReturnsEndTimeAsALocalDateObject() {
    PhoneCall call = createPhoneCall("098-765-4321", "494-494-3403", "10/03/2004 00:00 AM", "4/12/3001 3:32 PM");
    LocalDateTime endTime = call.getEndTime();
    assertThat(endTime.toString(), equalTo("3001-04-12T15:32"));
  }

  @Test
  void toStringReturnsPhoneCallAsAString() {
    String caller = "343-563-5465";
    String callee = "443-452-6775";
    String begin = "3/03/2004 12:34 AM";
    String end = "3/03/2004 12:40 PM";
    PhoneCall call = createPhoneCall(caller, callee, begin, end);
    assertThat(call.toString(), equalTo("Phone call from " + caller + " to " + callee + " from 3/3/04, 12:34 AM to 3/3/04, 12:40 PM"));
  }
}
