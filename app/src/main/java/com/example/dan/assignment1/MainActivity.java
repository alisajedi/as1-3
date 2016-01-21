package com.example.dan.assignment1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void newEntry(View view){
        Intent intent = new Intent(this, NewEntryActivity.class);
        startActivity(intent);
    }

    public void addEntry(String date, String station, String odometer, String grade, String amount, String cost){

    }

}
