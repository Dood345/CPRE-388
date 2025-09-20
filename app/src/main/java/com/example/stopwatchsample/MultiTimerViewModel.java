package com.example.stopwatchsample;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class MultiTimerViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Timer>> _timers = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<Timer>> timers = _timers;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final TimerPreferencesManager preferencesManager;

    private final AtomicBoolean isTickerRunning = new AtomicBoolean(false);

    public MultiTimerViewModel(@NonNull Application application) {
        super(application);
        preferencesManager = new TimerPreferencesManager(application);
        loadTimers();
    }

    private final Runnable ticker = new Runnable() {
        @Override
        public void run() {
            List<Timer> currentTimers = _timers.getValue();
            if (currentTimers == null || currentTimers.isEmpty()) {
                isTickerRunning.set(false);
                return;
            }

            boolean anyTimerRunning = false;
            List<Timer> updatedTimers = new ArrayList<>();
            for (Timer timer : currentTimers) {
                if (timer.isRunning()) {
                    anyTimerRunning = true;
                    long elapsed = SystemClock.uptimeMillis() - timer.getStartTime();
                    // Create a new timer object with updated time
                    updatedTimers.add(new Timer(timer.getId(), elapsed, true, timer.getStartTime()));
                } else {
                    updatedTimers.add(timer);
                }
            }

            if (anyTimerRunning) {
                _timers.setValue(updatedTimers);
                handler.postDelayed(this, 100); // Update UI 10 times per second
            } else {
                isTickerRunning.set(false);
            }
        }
    };

    private void startTicker() {
        if (!isTickerRunning.getAndSet(true)) {
            handler.post(ticker);
        }
    }

    public void addTimer() {
        List<Timer> currentTimers = new ArrayList<>(_timers.getValue());
        currentTimers.add(new Timer());
        _timers.setValue(currentTimers);
        saveTimers();
    }

    public void removeTimer(String timerId) {
        List<Timer> currentTimers = _timers.getValue();
        if (currentTimers == null) return;
        
        List<Timer> updatedTimers = currentTimers.stream()
            .filter(timer -> !timer.getId().equals(timerId))
            .collect(Collectors.toList());
            
        _timers.setValue(updatedTimers);
        saveTimers();
    }

    public void startTimer(String timerId) {
        List<Timer> currentTimers = _timers.getValue();
        if (currentTimers == null) return;

        List<Timer> updatedTimers = currentTimers.stream().map(timer -> {
            if (timer.getId().equals(timerId)) {
                return new Timer(timer.getId(), timer.getElapsedTime(), true, SystemClock.uptimeMillis() - timer.getElapsedTime());
            }
            return timer;
        }).collect(Collectors.toList());

        _timers.setValue(updatedTimers);
        saveTimers();
        startTicker();
    }

    public void stopTimer(String timerId) {
        List<Timer> currentTimers = _timers.getValue();
        if (currentTimers == null) return;

        List<Timer> updatedTimers = currentTimers.stream().map(timer -> {
            if (timer.getId().equals(timerId)) {
                // Keep elapsed time, but mark as not running
                return new Timer(timer.getId(), timer.getElapsedTime(), false, timer.getStartTime());
            }
            return timer;
        }).collect(Collectors.toList());

        _timers.setValue(updatedTimers);
        saveTimers();
    }

    public void resetTimer(String timerId) {
        List<Timer> currentTimers = _timers.getValue();
        if (currentTimers == null) return;
        
        List<Timer> updatedTimers = currentTimers.stream().map(timer -> {
            if (timer.getId().equals(timerId)) {
                // Create a completely new, reset timer
                return new Timer(timer.getId(), 0L, false, 0L);
            }
            return timer;
        }).collect(Collectors.toList());
        
        _timers.setValue(updatedTimers);
        saveTimers();
    }
    
    private void saveTimers() {
        List<Timer> currentTimers = _timers.getValue();
        if (currentTimers != null) {
            preferencesManager.saveTimers(currentTimers);
        }
    }

    private void loadTimers() {
        List<Timer> loadedTimers = preferencesManager.loadTimers();
        for (Timer timer : loadedTimers) {
            timer.setRunning(false);
        }
        _timers.setValue(loadedTimers);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handler.removeCallbacks(ticker);
    }
}
