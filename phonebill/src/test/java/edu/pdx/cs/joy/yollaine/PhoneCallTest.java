package edu.pdx.cs.joy.yollaine;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link PhoneCall} class.
 *
 */
public class PhoneCallTest {

  public static final PhoneCall CALL_1 = createPhoneCall("345-234-4353", "343-342-3434", "1/2/2005 4:56 PM", "1/2/2005 5:34 PM");
  public static final PhoneCall CALL_2 = createPhoneCall("345-234-4353", "343-342-3434", "3/2/2005 4:56 PM", "3/2/2005 5:34 PM");
  public static final PhoneCall CALL_4 = createPhoneCall("104-234-4353", "343-342-3434", "3/2/2005 4:56 PM", "3/2/2005 5:34 PM");
  public static final PhoneCall CALL_5 = createPhoneCall("345-234-4353", "343-342-3434", "3/2/2005 4:56 PM", "3/2/2005 5:34 PM");
  public static final PhoneCall CALL_3 = createPhoneCall("345-678-9012", "123-456-7890", "11/2/2025 4:00 PM","11/2/2025 5:00 PM");

  static PhoneCall createPhoneCall(String... params) {
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

  @Test
  void phoneCallWithEarlierBeginTimeIsHigherInOrderAndLessThan() {
    String caller = "343-563-5465";
    String callee = "443-452-6775";
    String begin = "3/03/2004 12:34 AM";
    String end = "3/03/2004 12:40 PM";

    String caller2 = "343-563-5465";
    String callee2 = "443-452-6775";
    String begin2 = "3/03/2003 12:34 AM";
    String end2 = "3/03/2004 12:40 PM";
    PhoneCall call = createPhoneCall(caller, callee, begin, end);
    PhoneCall call2 = createPhoneCall(caller2, callee2, begin2, end2);
    assertThat(call2.compareTo(call), lessThan(0));
  }

  @Test
  void phoneCallWithSameBeginTimeSameCallerIsEqual() {
    String caller = "343-563-5465";
    String callee = "443-452-6775";
    String begin = "3/03/2004 12:34 AM";
    String end = "3/03/2004 12:40 PM";

    String caller2 = "343-563-5465";
    String callee2 = "434-452-6775";
    String begin2 = "3/03/2004 12:34 AM";
    String end2 = "3/03/2010 3:45 PM";
    PhoneCall call = createPhoneCall(caller, callee, begin, end);
    PhoneCall call2 = createPhoneCall(caller2, callee2, begin2, end2);
    assertThat(call2.compareTo(call), equalTo(0));
  }

  @Test
  void phoneCallOrderingGoesFromEarliestToLatestAndHighestPhoneCallToLowestPhoneCall() {
    PhoneCall call3 = createPhoneCall();

    List<PhoneCall> phoneCalls = new ArrayList<>();
    phoneCalls.add(CALL_1);
    phoneCalls.add(CALL_2);
    phoneCalls.add(call3);
    phoneCalls.add(CALL_4);
    phoneCalls.add(CALL_5);

    Collections.sort(phoneCalls);

    assertThat(phoneCalls.getFirst(), equalTo(call3));
    assertThat(phoneCalls.get(1), equalTo(CALL_1));
    assertThat(phoneCalls.get(2), equalTo(CALL_4));
    assertThat(phoneCalls.get(3), equalTo(CALL_2));
    assertThat(phoneCalls.get(4), equalTo(CALL_5));
  }
}
