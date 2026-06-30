package com.example.stopwatchapp;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView txtTimer;
    private TextView btnStart;
    private TextView btnStop;
    private TextView btnHold;
    private TextView btnReset;

    private ListView listHoldTimes;

    private Handler handler = new Handler();

    private long startTime = 0L;
    private long elapsedTime = 0L;

    private boolean running = false;

    private ArrayList<String> holdTimes;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTimer = findViewById(R.id.txtTimer);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnHold = findViewById(R.id.btnHold);
        btnReset = findViewById(R.id.btnReset);

        listHoldTimes = findViewById(R.id.listHoldTimes);

        holdTimes = new ArrayList<>();
        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                holdTimes
        ) {
            @Override
            public android.view.View getView(int position,
                                             android.view.View convertView,
                                             android.view.ViewGroup parent) {

                android.view.View view =
                        super.getView(position, convertView, parent);

                android.widget.TextView text =
                        view.findViewById(android.R.id.text1);

                text.setTextColor(android.graphics.Color.WHITE);

                return view;
            }
        };
        listHoldTimes.setAdapter(adapter);

        btnStart.setOnClickListener(v -> startStopwatch());

        btnStop.setOnClickListener(v -> stopStopwatch());

        btnHold.setOnClickListener(v -> addHoldTime());

        btnReset.setOnClickListener(v -> resetStopwatch());
    }

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {

            long currentTime =
                    System.currentTimeMillis() - startTime + elapsedTime;

            updateTimer(currentTime);

            handler.postDelayed(this, 10);
        }
    };

    private void startStopwatch() {

        if (!running) {

            startTime = System.currentTimeMillis();

            handler.post(timerRunnable);

            running = true;
        }
    }

    private void stopStopwatch() {

        if (running) {

            elapsedTime += System.currentTimeMillis() - startTime;

            handler.removeCallbacks(timerRunnable);

            running = false;
        }
    }

    private void addHoldTime() {

        holdTimes.add(txtTimer.getText().toString());

        adapter.notifyDataSetChanged();
    }

    private void resetStopwatch() {

        handler.removeCallbacks(timerRunnable);

        running = false;

        startTime = 0L;

        elapsedTime = 0L;

        txtTimer.setText("00:00:00.00");

        holdTimes.clear();

        adapter.notifyDataSetChanged();
    }
    private void updateTimer(long milliseconds) {

        int hours = (int) (milliseconds / 3600000);

        int minutes = (int) ((milliseconds % 3600000) / 60000);

        int seconds = (int) ((milliseconds % 60000) / 1000);

        int centiseconds = (int) ((milliseconds % 1000) / 10);

        String time = String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d.%02d",
                hours,
                minutes,
                seconds,
                centiseconds
        );

        txtTimer.setText(time);
    }
}