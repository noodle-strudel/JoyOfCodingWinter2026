package edu.pdx.cs.joy.yollaine;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.PhoneBillParser;
import org.jspecify.annotations.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            throw new ParserException("Missing customer");
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

  // TODO:
  //  validate each part of the phone call
  //  figure out what part of the exception is unique so i can have special comments

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
    PhoneCall phoneCall;
    try {
      String newCaller = st.nextToken();
      validatePhoneNumber(newCaller);
      String newCallee = st.nextToken();
      validatePhoneNumber(newCallee);
      String newBeginDate = st.nextToken();
      String newBeginTime = st.nextToken();
      validateDateAndTime(newBeginDate + " " + newBeginTime);
      String newEndDate = st.nextToken();
      String newEndTime = st.nextToken();
      validateDateAndTime(newEndDate + " " + newEndTime);
      phoneCall = new PhoneCall(newCaller, newCallee, newBeginDate + " " + newBeginTime, newEndDate + " " + newEndTime);
    } catch (NoSuchElementException e) {
      throw new ParserException("Missing essential info from phone call");
    } catch (IllegalArgumentException e) {
      throw new ParserException(e.getMessage());
    }
    return phoneCall;
  }

  static void validateDateAndTime(String dateAndTime) {
    if (!Project2.isValidDateAndTime(dateAndTime)) {
      throw new IllegalArgumentException("Invalid data and time format.");
    }
  }

  static void validatePhoneNumber(String number) {
    if (!Project2.isValidPhoneNumber(number)) {
      throw new IllegalArgumentException("Invalid phone number format.");
    }
  }
}
