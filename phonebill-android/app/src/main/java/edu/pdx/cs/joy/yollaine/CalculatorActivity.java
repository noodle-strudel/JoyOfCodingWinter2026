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

public class CalculatorActivity extends AppCompatActivity {

    public static final String SUM = "SUM";
    private int sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goBackToMainActivity(View view) {
        Intent intent = new Intent();
        intent.putExtra(SUM, this.sum);
        setResult(RESULT_OK, intent);
        finish();
    }

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
}