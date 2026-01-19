package edu.pdx.cs.joy.yollaine;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;


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
        return new PhoneCall("123-456-7890", "098-765-4321", "00/00/00 00:00", "00/00/00 00:00");
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

}
