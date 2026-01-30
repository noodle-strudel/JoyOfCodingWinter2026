package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.PhoneBillParser;

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

        if (callInfo != null) {
          // use stringtokenizer thing
          StringTokenizer st = new StringTokenizer(callInfo, " ");
          PhoneCall phoneCall;
          try {
            phoneCall = new PhoneCall(st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken());
          } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Missing essential info from phone call");
          }
          phoneBill.addPhoneCall(phoneCall);
        }

        return phoneBill;

    } catch (IOException e) {
      throw new ParserException("While parsing phone bill text", e);
    }
  }
}
