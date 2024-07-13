package com.example.stopwatchapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView timer;
    AppCompatButton start_button, pause_button, reset_button, lap_button;
    Handler handler;
    long startTime, updateTime = 0L;
    int seconds, minutes, milliseconds;
    boolean isRunning = false;
    ListView lap_time_list;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        timer = findViewById(R.id.timer);
        start_button = findViewById(R.id.start_button);
        pause_button = findViewById(R.id.pause_button);
        reset_button = findViewById(R.id.reset_button);
        lap_button = findViewById(R.id.lap_button);
        lap_time_list = findViewById(R.id.lap_time_list);

        handler = new Handler();

        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        lap_time_list.setAdapter(adapter);

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        lap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLap();
            }
        });
    }

    private void startTimer() {
        if (!isRunning) {
            startTime = System.currentTimeMillis() - updateTime;
            handler.postDelayed(updateTimerRunnable, 0);
            isRunning = true;
        }
    }

    private void pauseTimer() {
        if (isRunning) {
            handler.removeCallbacks(updateTimerRunnable);
            updateTime = System.currentTimeMillis() - startTime;
            isRunning = false;
        }
    }

    private void addLap() {
        if (isRunning) {
            String currentTime = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
            arrayList.add(currentTime);
            adapter.notifyDataSetChanged();
        }
    }

    private void resetTimer() {
        handler.removeCallbacks(updateTimerRunnable);
        isRunning = false;
        startTime = 0L;
        updateTime = 0L;
        seconds = 0;
        minutes = 0;
        milliseconds = 0;
        timer.setText("00:00:000");
        arrayList.clear();
        adapter.notifyDataSetChanged();
    }

    private Runnable updateTimerRunnable = new Runnable() {
        public void run() {
            updateTime = System.currentTimeMillis() - startTime;
            seconds = (int) (updateTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliseconds = (int) (updateTime % 1000);

            timer.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
            handler.postDelayed(this, 10);
        }
    };
}