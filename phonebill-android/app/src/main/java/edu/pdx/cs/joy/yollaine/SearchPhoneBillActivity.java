package edu.pdx.cs.joy.yollaine;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import edu.pdx.cs.joy.ParserException;

public class SearchPhoneBillActivity extends AppCompatActivity {

    PhoneBillArrayAdapter foundPhoneBills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_phone_bill);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Spinner beginSpinner = findViewById(R.id.searchBeginTimeSpinner);
        Spinner endSpinner = findViewById(R.id.searchEndTimeSpinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.am_pm, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        beginSpinner.setAdapter(spinnerAdapter);
        endSpinner.setAdapter(spinnerAdapter);

        SwitchCompat timeRangeSwitch = findViewById(R.id.searchPhoneCallBetweenRangeSwitch);
        timeRangeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LinearLayout timeRangeSearchBlock = findViewById(R.id.searchPhoneBillTimeRangeBlock);
            timeRangeSearchBlock.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });
    }

    public void goBackToMainActivity(View view) {
        finish();
    }

    public void searchForPhoneBillAndCalls(View view) {
        TextView customerNameTextView = findViewById(R.id.searchPhoneBillCustomerNameField);
        String customerName = customerNameTextView.getText().toString();
        String beginTimeString = null;
        String endTimeString = null;

        SwitchCompat timeRangeSwitch = findViewById(R.id.searchPhoneCallBetweenRangeSwitch);
        if (timeRangeSwitch.isChecked()) {
            EditText beginTimeField = findViewById(R.id.searchPhoneCallBeginTimeField);
            beginTimeString = beginTimeField.getText().toString();
            Spinner beginTimeSpinner = findViewById(R.id.searchBeginTimeSpinner);

            String beginTimeAMPM = beginTimeSpinner.getSelectedItem().toString();
            beginTimeString = beginTimeString+" "+beginTimeAMPM;

            EditText endTimeField = findViewById(R.id.searchPhoneCallEndTimeField);
            endTimeString = endTimeField.getText().toString();
            Spinner endTimeSpinner = findViewById(R.id.searchEndTimeSpinner);

            String endTimeAMPM = endTimeSpinner.getSelectedItem().toString();
            endTimeString = endTimeString+" "+endTimeAMPM;

            // input validation
        }

        if (customerName.isEmpty()) {

        } else {
            PhoneBill customerBill = readPhoneBillFromFile(customerName);
            if (customerBill == null) {
                // unable to find phone bill
            } else {
                List<PhoneCall> phoneCalls = customerBill.getPhoneCalls();
                if (beginTimeString != null) {

                }
            }
        }


    }

    private PhoneBill readPhoneBillFromFile(String customerName) {
        File phoneBillFile = new File(this.getDataDir(), customerName + AddPhoneBillActivity.PHONEBILL_TXT);
        try {
            TextParser textParser = new TextParser(new FileReader(phoneBillFile));
            return textParser.parse();
        } catch (IOException | ParserException e) {
            return null;
        }
    }
}