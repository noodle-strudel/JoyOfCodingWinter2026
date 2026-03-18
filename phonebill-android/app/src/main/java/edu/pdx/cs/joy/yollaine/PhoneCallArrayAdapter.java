package edu.pdx.cs.joy.yollaine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PhoneCallArrayAdapter extends ArrayAdapter<PhoneCall> {
    private final List<PhoneCall> phoneCalls;
    private final Context context;
    public PhoneCallArrayAdapter(@NonNull Context context, @NonNull List<PhoneCall> objects) {
        super(context, -1, objects);
        this.phoneCalls = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_phone_call, parent, false);
        int[] ids = {R.id.phoneCallCallerText, R.id.phoneCallCalleeText, R.id.phoneCallBeginTimeText, R.id.phoneCallEndTimeText};
        PhoneCall phoneCall = phoneCalls.get(position);
        String beginTime = phoneCall.getBeginTime().format(PrettyPrinter.DATE_TIME_FORMATTER);
        String endTime = phoneCall.getEndTime().format(PrettyPrinter.DATE_TIME_FORMATTER);
        String[] phoneCallText = {phoneCall.getCaller(), phoneCall.getCallee(), beginTime, endTime};
        for (int i = 0; i < ids.length; ++i) {
            TextView view = rowView.findViewById(ids[i]);
            view.setText(phoneCallText[i]);
        }

        return rowView;
    }
}
