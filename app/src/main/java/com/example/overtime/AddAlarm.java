package com.example.overtime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddAlarm extends AppCompatActivity {

    private EditText titleET;
    private TextView timeET;
    private ChipGroup daysOfWeekChips;
    private Date time;
    private int hour;
    private int minute;
    private FloatingActionButton doneFAB;
    private String amOrPm;
    private ArrayList<String> daysOfWeek;
    private LatLng homeLatLng;
    private String homeName;
    private LatLng destinationLatLng;
    private String destinationName;
    private TextView homeTV;
    private TextView homeET;
    private TextView destinationTV;
    private TextView destinationET;
    final int REQUEST_CODE_H = 100;
    final int REQUEST_CODE_D = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time = new Date();
        time.setHours(0);
        time.setMinutes(0);
        setContentView(R.layout.activity_add_alarm);
        titleET = findViewById(R.id.titleET);
        titleET.isFocused();
        timeET = findViewById(R.id.timeET);
        timeET.setHint("12:00 AM");
        timeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTimePicker();
            }
        });
        daysOfWeekChips = findViewById(R.id.daysChipGroup);
        daysOfWeekChips.setChipSpacing(0);
        daysOfWeek = new ArrayList<>();
        homeTV = findViewById(R.id.startingTV);
        homeET = findViewById(R.id.startingET);
        homeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent (AddAlarm.this, MapsActivity.class);
                startActivityForResult(mapIntent, REQUEST_CODE_H);
            }
        });

        destinationTV = findViewById(R.id.destinationTV);
        destinationET = findViewById(R.id.destinationET);
        destinationET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent (AddAlarm.this, MapsActivity.class);
                startActivityForResult(mapIntent, REQUEST_CODE_D);
            }
        });

        doneFAB = findViewById(R.id.doneFAB);
        doneFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendBack = new Intent(AddAlarm.this, MainActivity.class);
                sendBack.putExtra("title", titleET.getText().toString());
                sendBack.putExtra("time", time.getTime());
                sendBack.putExtra("date", time.getTime());
                sendBack.putExtra("amOrPm", amOrPm);
                checkChips();
                sendBack.putExtra("daysOfWeek", daysOfWeek);
                if (titleET.getText().toString().equals("")) {
                    Toast.makeText(AddAlarm.this, "Whoops, add a title first!", Toast.LENGTH_SHORT).show();
                }
                else if(timeET.getText().toString().equals("")){
                    Toast.makeText(AddAlarm.this, "Whoops, select a time first!", Toast.LENGTH_SHORT).show();
                }
                else if(daysOfWeek.isEmpty() || daysOfWeek == null){
                    Toast.makeText(AddAlarm.this, "Whoops, select a day for the alarm!", Toast.LENGTH_SHORT).show();
                }
                else if(homeET.getText().toString().equals("")){
                    Toast.makeText(AddAlarm.this, "Whoops, select a home location!", Toast.LENGTH_SHORT).show();
                }
                else if(destinationET.getText().toString().equals("")){
                    Toast.makeText(AddAlarm.this, "Whoops, select a destination location!", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendBack.putExtra("homeLocationLat", homeLatLng.latitude);
                    sendBack.putExtra("homeLocationLong", homeLatLng.longitude);
                    sendBack.putExtra("homeLocationName", homeName);
                    sendBack.putExtra("desLocationLat", destinationLatLng.latitude);
                    sendBack.putExtra("desLocationLong", destinationLatLng.longitude);
                    sendBack.putExtra("desLocationName", destinationName);
                    setResult(RESULT_OK, sendBack);
                    finish();
                }
            }
        });

    }

    private void checkChips() {
        daysOfWeek.clear();
        Chip [] chips = new Chip[7];
        chips[0] = findViewById(R.id.sundayChip);
        chips[1] = findViewById(R.id.mondayChip);
        chips[2] = findViewById(R.id.tuesdayChip);
        chips[3] = findViewById(R.id.wednesdayChip);
        chips[4] = findViewById(R.id.thursdayChip);
        chips[5] = findViewById(R.id.fridayChip);
        chips[6] = findViewById(R.id.saturdayChip);

        String [] days = new String[7];
        days[0] = "Sun";
        days[1] = "Mon";
        days[2] = "Tues";
        days[3] = "Wed";
        days[4] = "Thur";
        days[5] = "Fri";
        days[6] = "Sat";
        for (int i = 0; i < 7; i++){
            if(chips[i].isChecked()){
                daysOfWeek.add(days[i]);
            }
        }
    }

    public void popTimePicker(){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                time.setHours(selectedHour);
                time.setMinutes(selectedMinute);
                if(selectedHour != 12 && selectedHour != 0) {
                    hour = selectedHour % 12;
                }
                else{
                    hour = 12;
                }
                minute = selectedMinute;
                String setTime;
                if (minute<10) {
                    setTime = hour + ":0" + minute;
                }
                else{
                    setTime =hour + ":" + minute;
                }
                if(selectedHour < 12){
                    amOrPm = "AM";
                }
                else{
                    amOrPm = "PM";
                }
                setTime += " " + amOrPm;
                timeET.setText(setTime);
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, time.getHours(), time.getMinutes(), false);
        timePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_H) {
            homeLatLng = new LatLng(data.getDoubleExtra("locationLat", -1),
                    data.getDoubleExtra("locationLong", -1));
            homeName = data.getStringExtra("locationName");
            homeET.setText(homeName);
        }
        else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_D){
            destinationLatLng = new LatLng(data.getDoubleExtra("locationLat", -1),
                    data.getDoubleExtra("locationLong", -1));
            destinationName = data.getStringExtra("locationName");
            destinationET.setText(destinationName);
        }

    }
}