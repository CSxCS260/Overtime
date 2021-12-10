package com.example.overtime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Alarm> alarmList;
    MapView mapView;
    FloatingActionButton addFAB;
    Adapter adapter;
    final int REQUEST_CODE = 200;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PUT_STRING = "alarmList";
    public static final String TEXT = "text";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.alarmList);
        addFAB = findViewById(R.id.addFAB);
        loadData();
        adapter = new Adapter(alarmList, onLongClickListener);
        recyclerView.setAdapter(adapter);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddAlarm.class);
//                setResult(REQUEST_CODE, intent);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    Adapter.OnLongClickListener onLongClickListener = new Adapter.OnLongClickListener(){
        @Override
        public void onItemLongClicked(int position) {
            alarmList.remove(position);
            adapter.notifyItemRemoved(position);
            writeData();
            Toast.makeText(MainActivity.this, "Alarm was removed", Toast.LENGTH_SHORT).show();
        }
    };

//    private void getData() {
//        Date currentTime = Calendar.getInstance().getTime();
//        String[] daysList = new String[] {"Mon", "Tues", "Wed", "Thur", "Fri"};
//        ArrayList<String> daysOfWeekList = new ArrayList<>();
//        daysOfWeekList.addAll(Arrays.asList(daysList));
//        alarmList = new ArrayList<Alarm>();
//    }
    private void writeData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alarmList);
        editor.putString(PUT_STRING, json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PUT_STRING, null);
        Type type = new TypeToken<ArrayList<Alarm>>() {}.getType();
        alarmList = gson.fromJson(json, type);
        if(alarmList == null){
            alarmList = new ArrayList<>();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent finished) {
        super.onActivityResult(requestCode, resultCode, finished);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String newTitle = finished.getStringExtra("title");
            Date newTime = new Date(finished.getExtras().getLong("date",-1));
            String newAmOrPm = finished.getStringExtra("amOrPm");
            ArrayList<String> newDaysOfWeek = finished.getStringArrayListExtra("daysOfWeek");
            Alarm newAlarm = new Alarm(newTitle, newTime, newAmOrPm, newDaysOfWeek, true);
            alarmList.add(newAlarm);
            adapter.notifyItemInserted(alarmList.size()-1);
            writeData();
            Toast.makeText(this, "Alarm was added!", Toast.LENGTH_SHORT).show();
        }
    }
}