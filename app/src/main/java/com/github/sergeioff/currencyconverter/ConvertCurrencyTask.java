package com.github.sergeioff.currencyconverter;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Adapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Created by sergeioff on 2/10/17.
 */

public class ConvertCurrencyTask extends AsyncTask<String, Void, Map<String, Double>> {
    private final String USD = "USD", ILS = "ILS", RUB = "RUB";

    private Activity activity;
    private String selectedCurrency;
    private Double currentValue;

    public ConvertCurrencyTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Map<String, Double> doInBackground(String... params) {
        selectedCurrency = params[0];
        String inputValue = params[1];
        if (inputValue.isEmpty()) {
            inputValue = "0";
        }
        currentValue = Double.parseDouble(inputValue);

        String urlTemplate = "http://api.fixer.io/latest?symbols=USD,ILS,RUB&base=%s";
        String urlString = String.format(urlTemplate, selectedCurrency);
        Map<String, Double> currencies = new HashMap<>();

        String json = getJSON(urlString);

        if (json.length() < 1) {
            return currencies;
        }

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject rates = jsonObject.getJSONObject("rates");

            Iterator<String> keys = rates.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Double value = rates.getDouble(key);

                currencies.put(key, value);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return currencies;
    }

    private String getJSON(String urlString) {
        String json = "";
        BufferedReader reader;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.connect();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder buffer = new StringBuilder();

            for (String line; (line = reader.readLine()) != null; ) {
                buffer.append(line);
            }

            reader.close();

            json = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    protected void onPostExecute(Map<String, Double> currencyRates) {
        TextView selectedCurrencyTextView = getTextView(selectedCurrency);
        selectedCurrencyTextView.setText(String.format(Locale.US, "%.2f", currentValue));

        for (Map.Entry<String, Double> entry : currencyRates.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();

            Double convertedResult = value * currentValue;

            TextView keyTextView = getTextView(key);
            keyTextView.setText(String.format(Locale.US, "%.2f", convertedResult));
        }
    }

    private TextView getTextView(String currency) {
        TextView usdText = (TextView) activity.findViewById(R.id.dollarText);
        TextView ilsText = (TextView) activity.findViewById(R.id.nisText);
        TextView rubText = (TextView) activity.findViewById(R.id.rubText);

        switch (currency) {
            case RUB:
                return rubText;
            case ILS:
                return ilsText;
            case USD:
                return usdText;
        }

        return usdText;
    }
}
