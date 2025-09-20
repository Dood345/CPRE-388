package com.example.stopwatchsample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements TimerAdapter.TimerListener {

    private MultiTimerViewModel viewModel;
    private TimerAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MultiTimerViewModel.class);
        
        setupViews();
        setupRecyclerView();
        observeTimers();
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.recyclerViewTimers);
        fabAddTimer = findViewById(R.id.fabAddTimer);
        
        fabAddTimer.setOnClickListener(v -> viewModel.addTimer());
    }

    private void setupRecyclerView() {
        adapter = new TimerAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void observeTimers() {
        viewModel.timers.observe(this, timers -> {
            adapter.submitList(timers);
        });
    }

    @Override
    public void onStartStopClicked(String timerId) {
        // Find the timer and toggle its state
        if (viewModel.timers.getValue() != null) {
            for (Timer timer : viewModel.timers.getValue()) {
                if (timer.getId().equals(timerId)) {
                    if (timer.isRunning()) {
                        viewModel.stopTimer(timerId);
                    } else {
                        viewModel.startTimer(timerId);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onResetClicked(String timerId) {
        viewModel.resetTimer(timerId);
    }

    @Override
    public void onCloseClicked(String timerId) {
        viewModel.removeTimer(timerId);
    }
}
