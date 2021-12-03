package com.example.overtime;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddAlarm extends AppCompatActivity {

    private EditText titleET;
    private EditText timeET;
    private ChipGroup daysOfWeekChips;
    private int hour;
    private int minute;
    private FloatingActionButton doneFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        titleET = findViewById(R.id.titleET);
        timeET = findViewById(R.id.timeET);
        timeET.setText("9:00 AM");
        timeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTimePicker();
            }
        });
        daysOfWeekChips = findViewById(R.id.daysChipGroup);
        doneFAB = findViewById(R.id.doneFAB);
        doneFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(AddAlarm.this, MainActivity.class);
                setResult(100, returnIntent);
            }
        });

    }

    public void popTimePicker(){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                if(hour != 12) {
                    hour = selectedHour % 12;
                }
                minute = selectedMinute;
                if (minute<10) {
                    timeET.setText(hour + ":0" + minute);
                }
                else{
                    timeET.setText(hour + ":" + minute);
                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);
        timePickerDialog.setTitle("Select Alarm Time");
        timePickerDialog.show();
    }
}