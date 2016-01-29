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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

public class MainActivity extends Activity {

    private static final String FILENAME = "file.sav";

    private ArrayList<Entry> entries = new ArrayList<Entry>();

    private ArrayAdapter<Entry> adapter;

    private ListView entryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entryList = (ListView) findViewById(R.id.entryList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateList();
    }

    public void newEntry(View view){
        saveInFile();

        Intent intent = new Intent(this, NewEntryActivity.class);
        startActivityForResult(intent, 0);
    }

    public void editEntry(View view){
        saveInFile();

        Intent intent = new Intent(this, EditActivity.class);
        EditText indexInput = (EditText) findViewById(R.id.index);
        String index = indexInput.getText().toString();

        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        boolean badFormat = false;
        Pattern digits = Pattern.compile("^[0-9]+$");

        if (!digits.matcher(index).find()){
            adBuilder.setMessage("You must specify which entry to edit.");
            badFormat=true;
        }
        else if (Integer.parseInt(index)>entries.size()){
            adBuilder.setMessage("Index too large");
            badFormat=true;
        }
        else if (Integer.parseInt(index)<=0){
            adBuilder.setMessage("Index must be positive");
            badFormat=true;
        }
        if (badFormat){
            AlertDialog ad = adBuilder.create();
            ad.show();
            return;
        }

        intent.putExtra("entryNum",index);
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode==0 && resultCode==RESULT_OK){
            TextView totCost = (TextView) findViewById(R.id.totalCostValue);
            totCost.setText(data.getStringExtra("totalCost"));
        }
        populateList();
    }

    private void populateList(){
        loadFromFile();
        adapter = new ArrayAdapter<Entry>(this,
                R.layout.list_item, entries);
        entryList.setAdapter(adapter);
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
