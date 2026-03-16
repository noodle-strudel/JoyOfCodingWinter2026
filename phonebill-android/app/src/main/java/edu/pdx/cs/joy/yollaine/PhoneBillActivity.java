package edu.pdx.cs.joy.yollaine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import edu.pdx.cs.joy.ParserException;

public class PhoneBillActivity extends AppCompatActivity {

    public static final String PHONE_BILL_NAME = "PHONE_BILL_NAME";
    public static final String PHONEBILL_TXT = "-phonebill.txt";
    private int sum;
    private PhoneBill phoneBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phone_bill);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goBackToMainActivity(View view) {
        if (phoneBill != null) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    public void clearForm(View view) {
        EditText customerNameField = findViewById(R.id.nameField);
        customerNameField.setText("");

        EditText callerField = findViewById(R.id.callerField);
        callerField.setText("");

        EditText calleeField = findViewById(R.id.calleeField);
        calleeField.setText("");

        EditText beginTimeField = findViewById(R.id.beginTimeField);
        beginTimeField.setText("");

        EditText endTimeField = findViewById(R.id.endTimeField);
        endTimeField.setText("");
    }

    public void createPhoneBill(View view) {
        boolean newPhoneBill = false;

        EditText customerNameField = findViewById(R.id.nameField);
        String customerNameString = customerNameField.getText().toString();

        EditText callerField = findViewById(R.id.callerField);
        String callerString = callerField.getText().toString();

        EditText calleeField = findViewById(R.id.calleeField);
        String calleeString = calleeField.getText().toString();

        EditText beginTimeField = findViewById(R.id.beginTimeField);
        String beginTimeString = beginTimeField.getText().toString();

        EditText endTimeField = findViewById(R.id.endTimeField);
        String endTimeString = endTimeField.getText().toString();


        // see if a phone bill with the corresponding text file exists
        // load the phone bill in
        // then add a new one
        // result ok should refresh the page with all the new phone bills

        phoneBill = readPhoneBillFromFile(customerNameString);

        if (phoneBill == null) {
            phoneBill = new PhoneBill(customerNameString);
            newPhoneBill = true;
        }

        try {
            PhoneCall phoneCall = new PhoneCall(callerString, calleeString, beginTimeString, endTimeString);
            phoneBill.addPhoneCall(phoneCall);
            TextView phoneBillConfirmationField = findViewById(R.id.phoneBillConfirmation);
            if (newPhoneBill) {
                phoneBillConfirmationField.setText(R.string.new_phone_bill_created);
            } else {
                phoneBillConfirmationField.setText(R.string.add_phone_call_to_existing);
            }

            writePhoneBillToFile(phoneBill);

        } catch (IllegalArgumentException | IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void writePhoneBillToFile(PhoneBill phoneBill) throws IOException {
        File newPhoneBillFile = new File(this.getDataDir(), phoneBill.getCustomer() + PHONEBILL_TXT);
        TextDumper textDumper = new TextDumper(new FileWriter(newPhoneBillFile));
        textDumper.dump(phoneBill);
    }

    private PhoneBill readPhoneBillFromFile(String customerName) {
        File phoneBillFile = new File(this.getDataDir(), customerName + PHONEBILL_TXT);
        try {
            TextParser textParser = new TextParser(new FileReader(phoneBillFile));
            return textParser.parse();
        } catch (IOException | ParserException e) {
            return null;
        }
    }
}

/*
try {
                    writeSumsToFile();
                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

 */