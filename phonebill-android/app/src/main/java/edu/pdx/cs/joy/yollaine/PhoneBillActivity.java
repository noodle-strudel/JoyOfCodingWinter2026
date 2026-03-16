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

public class PhoneBillActivity extends AppCompatActivity {

    public static final String PHONE_BILL = "SUM";
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
        Intent intent = new Intent();
        intent.putExtra(PHONE_BILL, this.sum);
        setResult(RESULT_OK, intent);
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

        try {
            phoneBill = new PhoneBill(customerNameString);
            PhoneCall phoneCall = new PhoneCall(callerString, calleeString, beginTimeString, endTimeString);
            phoneBill.addPhoneCall(phoneCall);
            TextView phoneBillConfirmationField = findViewById(R.id.phoneBillConfirmation);
            phoneBillConfirmationField.setText(R.string.new_phone_bill_created);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /*
    public void computeSum(View view) {
        EditText leftOperandField = findViewById(R.id.leftOperand);
        String leftOperandString = leftOperandField.getText().toString();

        EditText rightOperandField = findViewById(R.id.rightOperand);
        String rightOperandString = rightOperandField.getText().toString();

        int leftOperand = Integer.parseInt(leftOperandString);
        int rightOperand = Integer.parseInt(rightOperandString);

        sum = leftOperand + rightOperand;

        TextView sumField = findViewById(R.id.sum);
        sumField.setText(String.valueOf(sum));
    }

     */
}