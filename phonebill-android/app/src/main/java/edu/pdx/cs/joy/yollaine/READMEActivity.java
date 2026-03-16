package edu.pdx.cs.joy.yollaine;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class READMEActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_readme);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView readMeTextView = findViewById(R.id.readMeTextView);
        try (InputStream readMe = getBaseContext().getResources().openRawResource(R.raw.readme)) {
            StringBuffer readMeOutput = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(readMe));
            String line;

            while ((line = br.readLine()) != null) {
                readMeOutput.append(line);
                readMeOutput.append('\n');
            }

            readMeTextView.setText(readMeOutput);

        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void goBackToMainActivity(View view) {
        finish();
    }
}