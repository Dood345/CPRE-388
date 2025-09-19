package com.example.stopwatchsample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private StopwatchViewModel viewModel;
    private TextView tvTime;
    private Button btnStartStop, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(StopwatchViewModel.class);

        tvTime = findViewById(R.id.tvTime);
        btnStartStop = findViewById(R.id.btnStartStop);
        btnReset = findViewById(R.id.btnReset);

        // observe the elapsed time and update the timer text via simple division method
        // it's shorthand for the older lambda syntax: newValue -> updateTimerText(newValue)
        viewModel.elapsedTime.observe(this, this::updateTimerText);

        // observe the isRunning value and update the button text accordingly
        // direct lambda where isRunning is passed to bracketed code block
        viewModel.isRunning.observe(this, isRunning -> {
            if (isRunning) {
                // if isRunning is true button should say stop
                btnStartStop.setText(R.string.stop);
            } else {
                // isRunning is false button should say start
                btnStartStop.setText(R.string.start);
            }
        });

        btnStartStop.setOnClickListener(v -> {
            if (viewModel.isRunning.getValue() != null && viewModel.isRunning.getValue()) {
                // if isRunning has value, and is true
                // button should say stop and clicking should stop
                viewModel.stopTimer();
            } else {
                // button should say start and clicking should start
                viewModel.startTimer();
            }
        });

        btnReset.setOnClickListener(v -> viewModel.resetTimer());
    }

    private void updateTimerText(Long elapsedTime) {
        if (elapsedTime == null) return;
        long hours = elapsedTime / 3600000;
        long minutes = (elapsedTime % 3600000) / 60000;
        long seconds = (elapsedTime % 60000) / 1000;
        long tenths = (elapsedTime % 1000) / 100;
        tvTime.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d.%d", hours, minutes, seconds, tenths));
    }
}
