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
 * A skeletal implementation of the <code>TextParser</code> class for Project 2.
 */
public class TextParser implements PhoneBillParser<PhoneBill> {
  private final Reader reader;

  public TextParser(Reader reader) {
    this.reader = reader;
  }

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

  private static @NonNull PhoneCall getPhoneCall(String callInfo) throws ParserException {
    StringTokenizer st = new StringTokenizer(callInfo, " ");
    PhoneCall phoneCall;
    try {
      String newCaller = st.nextToken();
      String newCallee = st.nextToken();
      String newBeginDate = st.nextToken();
      String newBeginTime = st.nextToken();
      String newEndDate = st.nextToken();
      String newEndTime = st.nextToken();
      phoneCall = new PhoneCall(newCaller, newCallee, newBeginDate + " " + newBeginTime, newEndDate + " " + newEndTime);
    } catch (NoSuchElementException e) {
      throw new ParserException("Missing essential info from phone call");
    }
    return phoneCall;
  }
}
