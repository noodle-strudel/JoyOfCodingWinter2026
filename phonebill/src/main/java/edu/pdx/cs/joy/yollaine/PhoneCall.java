package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.AbstractPhoneCall;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Objects;

/**
* Represents a phone call that extends the {@link AbstractPhoneCall} class.
*/
public class PhoneCall extends AbstractPhoneCall implements Comparable<PhoneCall>{
  public static final String DATE_TIME_PARSE_ERROR = " is an invalid date and time. Date and time format should be mm/dd/yyyy hh:mm (AM/PM), with time in a 12-hour format.";
  private final String caller;
  private final String callee;
  private final String beginTimeString;
  private final String endTimeString;
  private final LocalDateTime beginTime;
  private final LocalDateTime endTime;

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
  *       Throws IllegalArgumentException if the arguments are formatted incorrectly or are empty.
  */
  public PhoneCall (String newCaller, String newCallee, String newBeginTime, String newEndTime) throws IllegalArgumentException {
    if (newCaller.isEmpty()) {
      throw new IllegalArgumentException("Missing Caller");
    } else if (newCallee.isEmpty()) {
      throw new IllegalArgumentException("Missing Callee");
    } else if (newBeginTime.isEmpty()) {
      throw new IllegalArgumentException("Missing Begin Time");
    } else if (newEndTime.isEmpty()) {
      throw new IllegalArgumentException("Missing End Time");
    }

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");

    try {
      this.beginTime = LocalDateTime.parse(newBeginTime, dateTimeFormatter);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Begin time of " + newBeginTime + DATE_TIME_PARSE_ERROR);
    }

    try {
      this.endTime = LocalDateTime.parse(newEndTime, dateTimeFormatter);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("End time of " + newEndTime + DATE_TIME_PARSE_ERROR);
    }

    if (this.beginTime.isAfter(this.endTime)) {
      throw new IllegalArgumentException("Error: End Time is before Begin Time");
    }

    this.caller = newCaller;
    this.callee = newCallee;
    this.beginTimeString = newBeginTime;
    this.endTimeString = newEndTime;
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
    return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(this.beginTime);
  }

  /**
  * Get the time the call ended.
  * @return
  *      Returns the time the call ended.
  */
  @Override
  public String getEndTimeString() {
    return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(this.endTime);
  }

  /**
   * Get the raw unformatted begin time of the phone call, suitable for printing to a file.
   * @return
   *    Returns the raw begin time.
   */
  public String getBeginTimeRaw() {
    return this.beginTimeString;
  }

  /**
   * Get the raw unformatted end time of the phone call, suitable for printing to a file.
   * @return
   *    Returns the raw end time.
   */
  public String getEndTimeRaw() {
    return this.endTimeString;
  }

  /**
   * Get the LocalDateTime begin time object of the PhoneCall.
   * @return
   *    Returns the call's begin time as a LocalDateTime object.
   */
  @Override
  public LocalDateTime getBeginTime() {
    return this.beginTime;
  }

  /**
   * Get the LocalDateTime end time object of the PhoneCall.
   * @return
   *    Returns the call's end time as a LocalDateTime object.
   */
  @Override
  public LocalDateTime getEndTime() {
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
    return Objects.hash(caller, callee, beginTimeString, endTimeString);
  }

  /**
   * Compares this PhoneCall to another PhoneCall for sorting. Comparison is <b>different</b> from equals() for this class.
   * @param o the object to be compared.
   * @return
   *    a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
   *    Return comment taken from {@link Comparable#compareTo(Object)}.
   */
  @Override
  public int compareTo(PhoneCall o) {
    int cmp = this.beginTime.compareTo(o.beginTime);
    if (cmp == 0) {
      cmp = this.caller.compareTo(o.caller);
    }
    return cmp;
  }
}
