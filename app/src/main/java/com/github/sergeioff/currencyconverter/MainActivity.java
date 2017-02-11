package com.github.sergeioff.currencyconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private RadioGroup currencyGroup;
    private RadioButton dollarButton, nisButton, rubButton;
    private TextView dollarText, nisText, rubText;
    private EditText inputField;
    private Button convertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currencyGroup = (RadioGroup) findViewById(R.id.currencyGroup);
        inputField = (EditText) findViewById(R.id.editText);
        convertButton = (Button) findViewById(R.id.convertButton);
    }

    public void onConvertClick(View view) {
        int selectedCurrencyId = currencyGroup.getCheckedRadioButtonId();
        RadioButton selectedCurrencyButton = (RadioButton) findViewById(selectedCurrencyId);
        String selectedCurrency = selectedCurrencyButton.getText().toString();
        String inputValue = inputField.getText().toString();

        new ConvertCurrencyTask(this).execute(selectedCurrency, inputValue);

    }
}
