package edu.pdx.cs.joy.yollaine;

import com.google.common.annotations.VisibleForTesting;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link PhoneCall} class.
 *
 * You'll need to update these unit tests as you build out your program.
 */
public class PhoneCallTest {

  private static PhoneCall createPhoneCall(String... params) {
      return switch (params.length) {
          case 1 -> new PhoneCall(params[0], "098-765-4321", "00/00/00 00:00", "00/00/00 00:00");
          case 2 -> new PhoneCall(params[0], params[1], "00/00/00 00:00", "00/00/00 00:00");
          case 3 -> new PhoneCall(params[0], params[1], params[2], "00/00/00 00:00");
          case 4 -> new PhoneCall(params[0], params[1], params[2], params[3]);
          default -> new PhoneCall("123-456-7890", "098-765-4321", "00/00/00 00:00", "00/00/00 00:00");
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
    assertThrows(IllegalArgumentException.class, () -> createPhoneCall("098-765-4321", "494-494-3403", "00/00/00 00:00", ""));
  }

  @Test
  void forProject1ItIsOkayIfGetBeginTimeReturnsNull() {
    PhoneCall call = createPhoneCall();
    assertThat(call.getBeginTime(), is(nullValue()));
  }
  
}
