package com.example.dan.assignment1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

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

public class NewEntryActivity extends Activity {

    private static final String FILENAME = "file.sav";

    private ArrayList<Entry> entries = new ArrayList<Entry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
    }

    public void cancel (View view){
        Intent intent = new Intent(this, MainActivity.class);
        setResult(RESULT_CANCELED, intent);//tell main that this activity was cancelled
        finish();//return
    }

    public void confirm (View view){
        //set up dialog
        boolean badFormat = false;
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);

        //get edit text fields
        EditText dateInput = (EditText) findViewById(R.id.dateInput);
        EditText stationInput = (EditText) findViewById(R.id.stationInput);
        EditText odometerInput = (EditText) findViewById(R.id.odometerInput);
        EditText gradeInput = (EditText) findViewById(R.id.fuelGradeInput);
        EditText amountInput = (EditText) findViewById(R.id.fuelAmountInput);
        EditText costInput = (EditText) findViewById(R.id.fuelCostInput);

        //get text from fields
        String date = dateInput.getText().toString();
        String station = stationInput.getText().toString();
        String odometer = odometerInput.getText().toString();
        String grade = gradeInput.getText().toString();
        String amount = amountInput.getText().toString();
        String cost = costInput.getText().toString();

        //regex patterns
        Pattern oneDecimal = Pattern.compile("^[0-9]+\\.[0-9]$");//Of form 12.3
        Pattern threeDecimals = Pattern.compile("^[0-9]+\\.[0-9]{3}$");//Of form 1.234
        Pattern yyyymmdd = Pattern.compile("^[0-9]{4}/[0-9]{2}/[0-9]{2}$");//Of form 1234/56/78
        Pattern empty = Pattern.compile("^$");//Blank

        if (!yyyymmdd.matcher(date).find()){
            //date not formatted properly
            badFormat=true;
            //adBuilder.setMessage("Date must be of form ^[0-9]{4}/[0-9]{2}/[0-9]{2}$");
            adBuilder.setMessage("Date must be in format yyyy/mm/dd.");
        }
        else if (empty.matcher(station).find()){
            //station left empty
            badFormat=true;
            adBuilder.setMessage("Station must be entered.");
        }
        else if (!oneDecimal.matcher(odometer).find()){
            //odometer not given to one decimal point
            badFormat=true;
            adBuilder.setMessage("Odometer must be specified to one decimal place.");
        }
        else if (empty.matcher(grade).find()){
            //grade left empty
            badFormat=true;
            adBuilder.setMessage("Fuel grade must be entered.");
        }
        else if (!threeDecimals.matcher(amount).find()){
            //amount not given to three decimal points
            badFormat=true;
            adBuilder.setMessage("Fuel amount must be specified to three decimal places.");
        }
        else if (!oneDecimal.matcher(cost).find()){
            //cost not given to one decimal point
            badFormat=true;
            adBuilder.setMessage("Fuel cost must be specified to one decimal place.");
        }

        if (badFormat){
            //display alert if anything was wrong, and return so the activity doesn't finish
            AlertDialog ad = adBuilder.create();
            ad.show();
            return;
        }

        loadFromFile(); //load all stored data

        //make new entry from entered data
        Entry next = new Entry(date, station, odometer, grade, amount, cost, entries.size() + 1);

        entries.add(next);//append new entry

        //compute total cost
        double total = 0;
        for (int i=0; i<entries.size(); i++){
            total += Double.parseDouble(entries.get(i).getTotalCost());
        }

        //round to 2 decimal places
        long forRound;
        forRound = Math.round(total*100);
        total = forRound;
        total = total / 100;

        //ensure total cost is formatted with two decimal digits
        String totalCost = String.valueOf(total);
        Pattern twoDecimals = Pattern.compile("^[0-9]+\\.[0-9]{2}$");
        if (!twoDecimals.matcher(totalCost).find() && !totalCost.contains("E")){
            totalCost += "0";
        }

        saveInFile();//store all data

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("totalCost","$"+totalCost);//pass the computed total cost back to main
        setResult(RESULT_OK,intent);//tells main it must update display of total cost
        finish();//return
    }

    private void saveInFile() {
        //saves data to file
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
        //loads data from file
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
