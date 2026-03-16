package edu.pdx.cs.joy.yollaine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Objects;

import edu.pdx.cs.joy.ParserException;

public class MainActivity extends AppCompatActivity {

    public static final int GET_PHONE_BILL_CODE = 42;
    private ArrayAdapter<String> phoneBills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        phoneBills = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        loadPhoneBills();

        ListView sumsListView = findViewById(R.id.phoneBills);
        sumsListView.setAdapter(phoneBills);
    }

    public void launchCalculator(View view) {
        Intent intent = new Intent(this, PhoneBillActivity.class);
        startActivityForResult(intent, GET_PHONE_BILL_CODE);
    }

    public void launchREADME(View view) {
        Intent intent = new Intent(this, READMEActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_PHONE_BILL_CODE) {
            if (resultCode == RESULT_OK) {
                loadPhoneBills();
            }
        }
    }

    private void loadPhoneBills() {
        // get the directory
        // for each phone bill in the directory
        // parse it and add it to the phone bills
        // if it doesnt parse then just ignore it
        // sort the phone bills by name (should probably do that by default)

        File dataDirectory = this.getDataDir();

        FilenameFilter phoneBillExtension = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(PhoneBillActivity.PHONEBILL_TXT);
            }
        };

        this.phoneBills.clear();

        for (File phoneBillFile : Objects.requireNonNull(dataDirectory.listFiles(phoneBillExtension))) {
            try {
                TextParser textParser = new TextParser(new FileReader(phoneBillFile));
                this.phoneBills.add(textParser.parse().getCustomer());
            } catch (IOException | ParserException ignored) {
            }
        }

    }
}