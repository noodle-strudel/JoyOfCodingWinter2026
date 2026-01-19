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

  @VisibleForTesting
  private static PhoneCall createDefaultPhoneCall() throws IllegalArgumentException {
    return new PhoneCall("123-456-7890", "098-765-4321", "00/00/00 00:00", "00/00/00 00:00");
  }

  @Test
  void phoneCallWithCorrectArgumentsThrowsNoException() {
    PhoneCall call = createDefaultPhoneCall();
  }
  @Test
  void phoneCallWithEmptyCallerThrowsExceptionWithMessageMissingCaller() {
    assertThrows(IllegalArgumentException.class, () -> new PhoneCall("",  "098-765-4321", "00/00/00 00:00", "00/00/00 00:00"));
  }

  @Test
  void phoneCallWithEmptyCalleeThrowsExceptionWithMessageMissingCallee() {
    assertThrows(IllegalArgumentException.class, () -> new PhoneCall("098-765-4321", "", "00/00/00 00:00", "00/00/00 00:00"));
  }

  @Test
  void phoneCallWithEmptyBeginTimeThrowsExceptionWithMessageMissingBeginTime() {
    assertThrows(IllegalArgumentException.class, () -> new PhoneCall("098-765-4321", "908-343-5950", "", "00/00/00 00:00"));
  }

  @Test
  void phoneCallWithEmptyEndTimeThrowsExceptionWithMessageMissingEndTime() {
    assertThrows(IllegalArgumentException.class, () -> new PhoneCall("098-765-4321", "494-494-3403", "00/00/00 00:00", ""));
  }

  /**
   * This unit test will need to be modified (likely deleted) as you implement
   * your project.
   */
  @Test
  void initiallyAllPhoneCallsHaveTheSameCallee() {
    PhoneCall call = createDefaultPhoneCall();
    assertThat(call.getCallee(), containsString("not implemented"));
  }

  @Test
  void forProject1ItIsOkayIfGetBeginTimeReturnsNull() {
    PhoneCall call = createDefaultPhoneCall();
    assertThat(call.getBeginTime(), is(nullValue()));
  }
  
}
