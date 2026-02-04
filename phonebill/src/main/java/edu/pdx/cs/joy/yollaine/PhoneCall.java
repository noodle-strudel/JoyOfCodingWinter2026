package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.AbstractPhoneCall;

import java.util.Objects;

/**
* Represents a phone call that extends the {@link AbstractPhoneCall} class.
*/
public class PhoneCall extends AbstractPhoneCall {
  private final String caller;
  private final String callee;
  private final String beginTime;
  private final String endTime;

  /**
  * Constructor for the PhoneCall class.
  * @param newCaller
  *       Phone number of the caller.
  * @param newCallee
  *       Phone number of the callee.
  * @param newBeginTime
  *       Date and time of when the phone call began.
  * @param newEndTime
  *       Date and time of when the phone call ended.
  * @throws IllegalArgumentException
  *       Throws {@link IllegalArgumentException} if the arguments are empty.
  */
  public PhoneCall (String newCaller, String newCallee, String newBeginTime, String newEndTime) throws IllegalArgumentException {
    if (newCaller.isEmpty()) {
      throw new IllegalArgumentException("Missing Caller");
    } else if (newCallee.isEmpty()) {
      throw new IllegalArgumentException("Missing Callee");
    } else if (newBeginTime.isEmpty()) {
      throw new IllegalArgumentException("Missing BeginTime");
    } else if (newEndTime.isEmpty()) {
      throw new IllegalArgumentException("Missing EndTime");
    }

    this.caller = newCaller;
    this.callee = newCallee;
    this.beginTime = newBeginTime;
    this.endTime = newEndTime;
  }

  /**
  * Get the phone number of the caller.
  * @return
  *      Returns caller's phone number.
  */
  @Override
  public String getCaller() {
    return this.caller;
  }

  /**
  * Get the phone number of the callee.
  * @return
  *      Returns the callee's phone number.
  */
  @Override
  public String getCallee() {
    return this.callee;
  }

  /**
  * Get the time the call began.
  * @return
  *      Returns the time the call began.
  */
  @Override
  public String getBeginTimeString() {
    return this.beginTime;
  }

  /**
  * Get the time the call ended.
  * @return
  *      Returns the time the call ended.
  */
  @Override
  public String getEndTimeString() {
    return this.endTime;
  }

  /**
   * Overridden equals() function for PhoneCall to be based on PhoneCall fields.
   * @param o   the reference object with which to compare.
   * @return
   *    Returns true if the PhoneCalls are equal and false if they are not equal.
   */
  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    PhoneCall phoneCall = (PhoneCall) o;
    return Objects.equals(caller, phoneCall.caller) && Objects.equals(callee, phoneCall.callee) && Objects.equals(beginTime, phoneCall.beginTime) && Objects.equals(endTime, phoneCall.endTime);
  }

  /**
   * Overridden {@link Object#hashCode() hashcode()} function to utilize PhoneCall fields.
   * @return
   *    Returns the hash code of the PhoneCall.
   */
  @Override
  public int hashCode() {
    return Objects.hash(caller, callee, beginTime, endTime);
  }
}
