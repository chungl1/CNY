package com.example.barick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //    int flag;
    int selectedPosition;

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        flag = 0;
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.food_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0){
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra("SPINNER_SELECTED_POSITION", position);
            startActivity(intent);
            selectedPosition = position;
        } else {
            selectedPosition = 0;
        }
//        flag = 1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (selectedPosition != 0) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra("SPINNER_SELECTED_POSITION", selectedPosition);
            startActivity(intent);
        }
    }
}