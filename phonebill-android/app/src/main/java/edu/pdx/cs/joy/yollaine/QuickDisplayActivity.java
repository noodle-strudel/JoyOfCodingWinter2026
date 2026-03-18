package edu.pdx.cs.joy.yollaine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.pdx.cs.joy.ParserException;

public class QuickDisplayActivity extends AppCompatActivity {

    private PhoneBill phoneBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quick_display);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = this.getIntent();
        String customerName = intent.getStringExtra(MainActivity.CUSTOMER_NAME);
        loadPhoneBill(customerName);

        if (phoneBill != null) {
            TextView customerNameTextView = findViewById(R.id.quickDisplayCustomer);
            customerNameTextView.setText(customerName);
            ListView phoneCallList = findViewById(R.id.quickDisplayPhoneCallListView);
            phoneCallList.setAdapter(new PhoneCallArrayAdapter(this, phoneBill.getPhoneCalls()));
        }
    }

    public void goBackToMainActivity(View view) {
        finish();
    }

    private void loadPhoneBill(String customerName) {
        File phoneBillFile = new File(getDataDir(), customerName+ AddPhoneBillActivity.PHONEBILL_TXT);
        try {
            TextParser textParser = new TextParser(new FileReader(phoneBillFile));
            phoneBill = textParser.parse();
        } catch (IOException | ParserException ignored) {
        }
    }
}