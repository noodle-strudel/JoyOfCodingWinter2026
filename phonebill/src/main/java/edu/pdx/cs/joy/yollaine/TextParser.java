package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.PhoneBillParser;
import org.jspecify.annotations.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Parses a text file to create a {@link PhoneBill} with its associated {@link PhoneCall}s.
 */
public class TextParser implements PhoneBillParser<PhoneBill> {
  private final Reader reader;

  /**
   * The constructor for the TextParser class.
   * @param reader
   *      A reader for a text file for TextParser to utilize.
   */
  public TextParser(Reader reader) {
    this.reader = reader;
  }

  /**
   * Parse the given Reader and create a PhoneBill.
   * @return
   *    Returns a new PhoneBill.
   * @throws ParserException
   *    Throws ParserException when there is missing customer information or a malformed PhoneCall.
   */
  @Override
  public PhoneBill parse() throws ParserException {
    try (
      BufferedReader br = new BufferedReader(this.reader)
    ) {

        String customer = br.readLine();

        if (customer == null) {
            throw new ParserException("Missing customer name in given text file.");
        }

        PhoneBill phoneBill = new PhoneBill(customer);
        String callInfo = br.readLine();

        while(callInfo != null) {
          // use string tokenizer thing
          PhoneCall phoneCall = getPhoneCall(callInfo);
          phoneBill.addPhoneCall(phoneCall);
          callInfo = br.readLine();
        }

        return phoneBill;

    } catch (IOException e) {
      throw new ParserException("While parsing phone bill text", e);
    }
  }

  /**
   * A helper function to parse a PhoneCall given a line from the text file.
   * @param callInfo
   *    The current line from the text file representing a PhoneCall.
   * @return
   *    Returns a new PhoneCall.
   * @throws ParserException
   *    Throws a ParserException if the PhoneCall is missing information.
   */
  private static @NonNull PhoneCall getPhoneCall(String callInfo) throws ParserException {
    StringTokenizer st = new StringTokenizer(callInfo, " ");
    String[] field = {"Caller Number", "Callee Number", "Begin Date", "Begin Time", "Begin Time AM/PM", "End Date", "End Time", "End Time AM/PM"};
    int fieldIdx = 0;
    PhoneCall phoneCall;
    try {
      String newCaller = st.nextToken();
      ++fieldIdx;
      validatePhoneNumber(newCaller);
      String newCallee = st.nextToken();
      ++fieldIdx;
      validatePhoneNumber(newCallee);
      String newBeginDate = st.nextToken();
      ++fieldIdx;
      String newBeginTime = st.nextToken();
      ++fieldIdx;
      String newBeginAMPM = st.nextToken();
      ++fieldIdx;
      String newBeginTimeStamp = newBeginDate+" "+newBeginTime+" "+newBeginAMPM;
      validateDateAndTime(newBeginTimeStamp);

      String newEndDate = st.nextToken();
      ++fieldIdx;
      String newEndTime = st.nextToken();
      ++fieldIdx;
      String newEndAMPM = st.nextToken();
      String newEndTimeStamp = newEndDate+" "+newEndTime+" "+newEndAMPM;
      validateDateAndTime(newEndTimeStamp);
      phoneCall = new PhoneCall(newCaller, newCallee, newBeginTimeStamp, newEndTimeStamp);
    } catch (NoSuchElementException e) {
      throw new ParserException("Missing " + field[fieldIdx] + " from phone call in the given file.");
    } catch (IllegalArgumentException e) {
      throw new ParserException(e.getMessage());
    }
    return phoneCall;
  }

  /**
   * Ensures the parsed date and time is valid based off of {@link Project4#isValidDateAndTime(String) Project4's isValidDateAndTime} method.
   * @param dateAndTime
   *    The date and time string to validate.
   * @throws IllegalArgumentException
   *    Throws an IllegalArgumentException if the date and time is invalid.
   */
  static void validateDateAndTime(String dateAndTime) throws IllegalArgumentException {
    if (!Project4.isValidDateAndTime(dateAndTime)) {
      throw new IllegalArgumentException("Invalid date and time format in the given file.");
    }
  }

  /**
   * Ensures the parsed date and time is valid based off of {@link Project4#isValidPhoneNumber(String)} Project4's isValidPhoneNumber method.
   * @param number
   *    The phone number to validate.
   * @throws IllegalArgumentException
   *    Throws an IllegalArgumentException if the phone number is invalid.
   */
  static void validatePhoneNumber(String number) throws IllegalArgumentException {
    if (!Project4.isValidPhoneNumber(number)) {
      throw new IllegalArgumentException("Invalid phone number format in the given file.");
    }
  }
}
