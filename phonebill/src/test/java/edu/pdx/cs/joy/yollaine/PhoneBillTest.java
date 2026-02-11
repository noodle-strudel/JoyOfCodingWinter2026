package edu.pdx.cs.joy.yollaine;

import org.hamcrest.CoreMatchers;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


/**
 * Unit tests for the {@link PhoneBill} class.
 */
public class PhoneBillTest {

  /**
   * Create PhoneBill with Name
   * @param name
   *      Name of customer
   * @return
   *      Returns PhoneBill
   */
  private static @NonNull PhoneBill createPhoneBillNamed(String name) {
      return new PhoneBill(name);
  }

  private static PhoneCall createDefaultPhoneCall() throws IllegalArgumentException {
    return new PhoneCall("123-456-7890", "098-765-4321", "10/03/2004 00:00 AM", "10/04/2005 10:00 PM");
  }

  @Test
  void canGetCustomerNameOfPhoneBill() {
    String name = "Jane";
    PhoneBill bill = createPhoneBillNamed(name);
    assertThat(bill.getCustomer(), equalTo(name));
  }

  @Test
  void addPhoneCallToPhoneBill() {
    PhoneBill bill = createPhoneBillNamed("Jane");
    PhoneCall call = createDefaultPhoneCall();
    bill.addPhoneCall(call);
    assertThat(bill.getPhoneCalls().toArray()[0], equalTo(call));
  }

  @Test
  void addMultiplePhoneCallsToPhoneBill() {
    PhoneBill bill = createPhoneBillNamed("A");
    for (int i = 0; i < 5; ++i) {
      bill.addPhoneCall(createDefaultPhoneCall());
    }
    assertThat(bill.getPhoneCalls().size(), equalTo(5));
  }

  @Test
  void addedPhoneCallsAutomaticallySortThemselves() {
    PhoneBill bill = createPhoneBillNamed("Jane");
    bill.addPhoneCall(PhoneCallTest.CALL_1);
    bill.addPhoneCall(PhoneCallTest.CALL_2);
    bill.addPhoneCall(PhoneCallTest.createPhoneCall());
    bill.addPhoneCall(PhoneCallTest.CALL_4);
    bill.addPhoneCall(PhoneCallTest.CALL_5);

    List<PhoneCall> phoneCalls = bill.getPhoneCalls();
    assertThat(phoneCalls.getFirst(), CoreMatchers.equalTo(PhoneCallTest.createPhoneCall()));
    assertThat(phoneCalls.get(1), CoreMatchers.equalTo(PhoneCallTest.CALL_1));
    assertThat(phoneCalls.get(2), CoreMatchers.equalTo(PhoneCallTest.CALL_4));
    assertThat(phoneCalls.get(3), CoreMatchers.equalTo(PhoneCallTest.CALL_2));
    assertThat(phoneCalls.get(4), CoreMatchers.equalTo(PhoneCallTest.CALL_5));
  }
}
