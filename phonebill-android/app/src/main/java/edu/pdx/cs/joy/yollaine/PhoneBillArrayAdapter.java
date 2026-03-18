package edu.pdx.cs.joy.yollaine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PhoneBillArrayAdapter extends ArrayAdapter<PhoneBill> {
    private final List<PhoneBill> phoneBills;
    private final Context context;
    public PhoneBillArrayAdapter(@NonNull Context context, @NonNull List<PhoneBill> objects) {
        super(context, -1, objects);
        this.phoneBills = objects;
        this.context = context;
    }

    // Create the phone bill view and load the phone calls as well
    // TODO:
    //  potentially no phone bills. handle that case!
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_phone_bill, parent, false);
        TextView phoneBillCustomer = rowView.findViewById(R.id.phoneBillCustomerName);
        phoneBillCustomer.setText(phoneBills.get(position).getCustomer());
        PhoneCallArrayAdapter phoneCallArrayAdapter = new PhoneCallArrayAdapter(getContext(), phoneBills.get(position).getPhoneCalls());
        /*
        ListView phoneCallsListView = rowView.findViewById(R.id.phoneCallLayoutListView);
        phoneCallsListView.setAdapter(phoneCallArrayAdapter);
        
         */

        return rowView;
    }
}
