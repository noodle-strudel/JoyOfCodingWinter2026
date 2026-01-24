package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.AbstractPhoneCall;

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
  *       Phone number of the caller
  * @param newCallee
  *       Phone number of the callee
  * @param newBeginTime
  *       Date and time of when the phone call began
  * @param newEndTime
  *       Date and time of when the phone call ended
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
  * Get the phone number of the caller
  * @return
  *      Returns caller's phone number
  */
  @Override
  public String getCaller() {
    return this.caller;
  }

  /**
  * Get the phone number of the callee
  * @return
  *      Returns the callee's phone number
  */
  @Override
  public String getCallee() {
    return this.callee;
  }

  /**
  * Get the time the call began
  * @return
  *      Returns the time the call began
  */
  @Override
  public String getBeginTimeString() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  /**
  * Get the time the call ended
  * @return
  *      Returns the time the call ended
  */
  @Override
  public String getEndTimeString() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }
}
