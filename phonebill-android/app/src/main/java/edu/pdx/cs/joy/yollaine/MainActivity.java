package edu.pdx.cs.joy.yollaine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    public static final int GET_SUM_CODE = 42;
    private ArrayAdapter<Integer> sums;

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
        sums = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        ListView sumsListView = findViewById(R.id.phoneBills);
        sumsListView.setAdapter(sums);
    }

    public void launchCalculator(View view) {
        Intent intent = new Intent(this, PhoneBillActivity.class);
        startActivityForResult(intent, GET_SUM_CODE);
    }

    public void launchREADME(View view) {
        Intent intent = new Intent(this, READMEActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_SUM_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                int sum = data.getIntExtra(PhoneBillActivity.SUM, 0);
                this.sums.add(sum);
                try {
                    writeSumsToFile();
                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void writeSumsToFile() throws IOException {
        File sumsFile = new File(this.getDataDir(), "sums.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(sumsFile), true)) {
            for (int i = 0; i < this.sums.getCount(); ++i) {
                int sum = this.sums.getItem(i);
                pw.println(sum);
            }
        }
    }
}