package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.AbstractPhoneCall;

public class PhoneCall extends AbstractPhoneCall {
  private final String caller;
  private final String callee;
  private final String beginTime;
  private final String endTime;

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

  @Override
  public String getCaller() {
    return this.caller;
  }

  @Override
  public String getCallee() {
    return this.callee;
  }

  @Override
  public String getBeginTimeString() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public String getEndTimeString() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }
}
