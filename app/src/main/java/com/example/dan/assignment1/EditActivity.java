package com.example.dan.assignment1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class EditActivity extends Activity {

    private static final String FILENAME = "file.sav";

    private int index;

    private ArrayList<Entry> entries = new ArrayList<Entry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        String indexString = getIntent().getExtras().getString("entryNum");
        if (indexString!=null){
            this.index = Integer.parseInt(indexString) - 1;
        }
        else{
            finish();
        }
        loadFromFile();
        Entry toEdit = entries.get(index);

        EditText dateInput = (EditText) findViewById(R.id.dateInput);
        EditText stationInput = (EditText) findViewById(R.id.stationInput);
        EditText odometerInput = (EditText) findViewById(R.id.odometerInput);
        EditText gradeInput = (EditText) findViewById(R.id.fuelGradeInput);
        EditText amountInput = (EditText) findViewById(R.id.fuelAmountInput);
        EditText costInput = (EditText) findViewById(R.id.fuelCostInput);

        dateInput.setText(toEdit.getDate());
        stationInput.setText(toEdit.getStation());
        odometerInput.setText(toEdit.getOdometer());
        gradeInput.setText(toEdit.getFuelGrade());
        amountInput.setText(toEdit.getFuelAmount());
        costInput.setText(toEdit.getFuelCost());
    }

    public void cancel (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void confirm (View view){
        boolean badFormat = false;
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);

        EditText dateInput = (EditText) findViewById(R.id.dateInput);
        EditText stationInput = (EditText) findViewById(R.id.stationInput);
        EditText odometerInput = (EditText) findViewById(R.id.odometerInput);
        EditText gradeInput = (EditText) findViewById(R.id.fuelGradeInput);
        EditText amountInput = (EditText) findViewById(R.id.fuelAmountInput);
        EditText costInput = (EditText) findViewById(R.id.fuelCostInput);

        String date = dateInput.getText().toString();
        String station = stationInput.getText().toString();
        String odometer = odometerInput.getText().toString();
        String grade = gradeInput.getText().toString();
        String amount = amountInput.getText().toString();
        String cost = costInput.getText().toString();

        Pattern oneDecimal = Pattern.compile("^[0-9]+\\.[0-9]$");
        Pattern threeDecimals = Pattern.compile("^[0-9]+\\.[0-9]{3}$");
        Pattern yyyymmdd = Pattern.compile("^[0-9]{4}/[0-9]{2}/[0-9]{2}$");
        Pattern empty = Pattern.compile("^$");

        if (!yyyymmdd.matcher(date).find()){
            badFormat=true;
            //adBuilder.setMessage("Date must be of form ^[0-9]{4}/[0-9]{2}/[0-9]{2}$");
            adBuilder.setMessage("Date must be in format yyyy/mm/dd.");
        }
        else if (empty.matcher(station).find()){
            badFormat=true;
            adBuilder.setMessage("Station must be entered.");
        }
        else if (!oneDecimal.matcher(odometer).find()){
            badFormat=true;
            adBuilder.setMessage("Odometer must be specified to one decimal place.");
        }
        else if (empty.matcher(odometer).find()){
            badFormat=true;
            adBuilder.setMessage("Fuel grade must be entered.");
        }
        else if (!threeDecimals.matcher(amount).find()){
            badFormat=true;
            adBuilder.setMessage("Fuel amount must be specified to three decimal places.");
        }
        else if (!oneDecimal.matcher(cost).find()){
            badFormat=true;
            adBuilder.setMessage("Fuel cost must be specified to one decimal place.");
        }

        if (badFormat){
            AlertDialog ad = adBuilder.create();
            ad.show();
            return;
        }

        loadFromFile();

        Entry edited = new Entry(date, station, odometer, grade, amount, cost, index);

        entries.set(index,edited);

        double total = 0;
        for (int i=0; i<entries.size(); i++){
            total += Double.parseDouble(entries.get(i).getTotalCost());
        }

        //round to 2 decimal places
        long forRound;
        forRound = Math.round(total*100);
        total = forRound;
        total = total / 100;

        String totalCost = String.valueOf(total);
        Pattern twoDecimals = Pattern.compile("^[0-9]+\\.[0-9]{2}$");
        if (!twoDecimals.matcher(totalCost).find() && !totalCost.contains("E")){
            totalCost += "0";
        }

        saveInFile();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("totalCost","$"+totalCost);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    0);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(entries, out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Entry>>(){}.getType();
            entries = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            entries = new ArrayList<Entry>();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
