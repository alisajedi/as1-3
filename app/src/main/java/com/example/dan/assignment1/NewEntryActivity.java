package com.example.dan.assignment1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class NewEntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
    }

    public void cancel (View view){
        Intent intent = new Intent(this, MainActivity.class);
        //switch to intent
    }

    public void confirm (View view){
        Intent intent = new Intent(this, MainActivity.class);
        //MainActivity.addEntry(findViewById(R.id.dateInput).toString(),
                                //findViewById(R.id.stationInput).toString(),
                                //findViewById(R.id.odometerInput).toString(),
                                //findViewById(R.id.fuelGradeInput).toString(),
                                //findViewById(R.id.fuelAmountInput).toString(),
                                //findViewById(R.id.fuelCostInput).toString());
        //switch to intent
    }

}
