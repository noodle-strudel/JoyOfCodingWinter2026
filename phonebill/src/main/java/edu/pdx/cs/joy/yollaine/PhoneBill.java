package edu.pdx.cs.joy.yollaine;

import edu.pdx.cs.joy.AbstractPhoneBill;

import java.util.ArrayList;
import java.util.Collection;

/**
* Represents a phone bill that extends the {@link AbstractPhoneBill} class.
*/
public class PhoneBill extends AbstractPhoneBill<PhoneCall> {
  private final String customer;
  private final Collection<PhoneCall> phoneCalls;

  /**
  * Constructor for the PhoneBill class.
  * @param customer
  *      Name of the customer
  */
  public PhoneBill(String customer) {
    this.customer = customer;
    this.phoneCalls = new ArrayList<>();
  }

  /**
  * Get the name of the customer
  * @return
  *      Returns the name of the customer
  */
  @Override
  public String getCustomer() {
    return this.customer;
  }

  /**
  * Add a {@link PhoneCall} to the PhoneBill.
  * @param call
  *      The PhoneCall to add
  */
  @Override
  public void addPhoneCall(PhoneCall call) {
    this.phoneCalls.add(call);
  }

  /**
  * Get the phone calls in the phone bill
  * @return
  *      Returns the collection of PhoneCalls
  */
  @Override
  public Collection<PhoneCall> getPhoneCalls() {
    return this.phoneCalls;
  }
}
