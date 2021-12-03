package com.example.overtime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Alarm> alarmList;
    MapView mapView;
    FloatingActionButton addFAB;
    String MAPS_BUNDLE_KEY = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.alarmList);
        addFAB = findViewById(R.id.addFAB);
        getData();
        Adapter adapter = new Adapter(alarmList);
        recyclerView.setAdapter(adapter);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddAlarm.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    private void getData() {
        alarmList = new ArrayList<>();
        Date currentTime = Calendar.getInstance().getTime();
        alarmList.add(new Alarm("Get Up In The Morning", currentTime,"AM", "Mon, Tues, Wed, Thur, Fri", true));
        alarmList.add(new Alarm("BedTime", currentTime,"PM", "Mon, Tues, Wed, Thur, Fri", true));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}