package edu.pdx.cs.joy.yollaine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.pdx.cs.joy.ParserException;

public class SearchPhoneBillActivity extends AppCompatActivity {

    private static final String PHONE_BILL = "PHONE_BILL";
    private PhoneBillArrayAdapter foundPhoneBills;
    private ListView phoneBillListView;
    public static PhoneBill phoneBillToDisplay;

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

        this.phoneBillListView = findViewById(R.id.searchPhoneBillListView);

        AdapterView.OnItemClickListener quickDisplay = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                phoneBillToDisplay = foundPhoneBills.getItem(position);
                launchQuickDisplay(findViewById(R.id.launchPhoneBillSearch));
            }
        };

        this.phoneBillListView.setOnItemClickListener(quickDisplay);

    }

    public void goBackToMainActivity(View view) {
        finish();
    }

    public void clearForm(View view) {
        EditText customerNameField = findViewById(R.id.searchPhoneBillCustomerNameField);
        customerNameField.setText("");

        EditText beginTimeField = findViewById(R.id.searchPhoneCallBeginTimeField);
        beginTimeField.setText("");

        EditText endTimeField = findViewById(R.id.searchPhoneCallEndTimeField);
        endTimeField.setText("");
    }

    public void searchForPhoneBillAndCalls(View view) {
        TextView customerNameTextView = findViewById(R.id.searchPhoneBillCustomerNameField);
        String customerName = customerNameTextView.getText().toString();
        LocalDateTime queriedBeginTime = null;
        LocalDateTime queriedEndTime = null;

        SwitchCompat timeRangeSwitch = findViewById(R.id.searchPhoneCallBetweenRangeSwitch);

        if (customerName.isEmpty()) {
            foundPhoneBills = new PhoneBillArrayAdapter(this, listAllPhoneBills());
            phoneBillListView.setAdapter(foundPhoneBills);
            return;
        }

        if (timeRangeSwitch.isChecked()) {
            EditText beginTimeField = findViewById(R.id.searchPhoneCallBeginTimeField);
            String beginTimeString = beginTimeField.getText().toString();
            Spinner beginTimeSpinner = findViewById(R.id.searchBeginTimeSpinner);

            String beginTimeAMPM = beginTimeSpinner.getSelectedItem().toString();
            beginTimeString = beginTimeString+" "+beginTimeAMPM;

            EditText endTimeField = findViewById(R.id.searchPhoneCallEndTimeField);
            String endTimeString = endTimeField.getText().toString();
            Spinner endTimeSpinner = findViewById(R.id.searchEndTimeSpinner);

            String endTimeAMPM = endTimeSpinner.getSelectedItem().toString();
            endTimeString = endTimeString+" "+endTimeAMPM;


            try {
                queriedBeginTime = LocalDateTime.parse(beginTimeString, PhoneCall.DATE_TIME_FORMAT);
            } catch (DateTimeParseException e) {
                Toast.makeText(this, "Invalid Begin time. "+PhoneCall.DATE_TIME_PARSE_ERROR, Toast.LENGTH_LONG).show();
                return;
            }

            try {
                queriedEndTime = LocalDateTime.parse(endTimeString, PhoneCall.DATE_TIME_FORMAT);
            } catch (DateTimeParseException e) {
                Toast.makeText(this, "Invalid End time. "+PhoneCall.DATE_TIME_PARSE_ERROR, Toast.LENGTH_LONG).show();
                return;
            }

            if (queriedEndTime.isBefore(queriedBeginTime)) {
                Toast.makeText(this, "End time is before begin time.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        PhoneBill customerBill = readPhoneBillFromFile(customerName);
        if (customerBill == null) {
            Toast.makeText(this, "Customer not found", Toast.LENGTH_LONG).show();
        } else {
            if (queriedBeginTime != null) {
                PhoneBill parsedBill = new PhoneBill(customerName);

                for (PhoneCall call: customerBill.getPhoneCalls()) {
                    if (!call.getBeginTime().isBefore(queriedBeginTime) && !call.getBeginTime().isAfter(queriedEndTime)) {
                        parsedBill.addPhoneCall(call);
                    }
                }
                if (parsedBill.getPhoneCalls().isEmpty()) {
                    Toast.makeText(this, "No phone calls found within given range.", Toast.LENGTH_LONG).show();
                    return;
                }
                customerBill = parsedBill;
            }

            List<PhoneBill> phoneBills = new ArrayList<>();
            phoneBills.add(customerBill);
            foundPhoneBills = new PhoneBillArrayAdapter(this, phoneBills);
            phoneBillListView.setAdapter(foundPhoneBills);
        }
    }
    public void launchQuickDisplay(View view) {
        Intent intent = new Intent(this, QuickDisplayActivity.class);

        intent.putExtra(PHONE_BILL, phoneBillToDisplay);
        startActivity(intent);
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

    private List<PhoneBill> listAllPhoneBills() {
        File dataDirectory = this.getDataDir();

        FilenameFilter phoneBillExtension = (dir, name) -> name.contains(AddPhoneBillActivity.PHONEBILL_TXT);

        List<PhoneBill> phoneBills = new ArrayList<>();

        for (File phoneBillFile : Objects.requireNonNull(dataDirectory.listFiles(phoneBillExtension))) {
            try {
                TextParser textParser = new TextParser(new FileReader(phoneBillFile));
                phoneBills.add(textParser.parse());
            } catch (IOException | ParserException ignored) {
            }
        }

        return phoneBills;
    }
}