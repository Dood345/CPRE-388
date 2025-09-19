package com.example.stopwatchsample;

// added the library definitions to libs.versions.toml
// used the new aliases in app/build.gradle.kts

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StopwatchViewModel extends ViewModel {

    private final MutableLiveData<Long> _elapsedTime = new MutableLiveData<>(0L);
    // private MutableLiveData for access to setter and getter within view
    public final LiveData<Long> elapsedTime = _elapsedTime;
    // public LiveData for access to getter (oberservers) outside of view
    // only send updates when the MainActivity is in an active state (STARTED or RESUMED)

    private final MutableLiveData<Boolean> _isRunning = new MutableLiveData<>(false);
    // (same) private MutableLiveData  for access to setter and getter within view
    public final LiveData<Boolean> isRunning = _isRunning;
    // (same) public LiveData for access to getter (oberservers) outside of view

    private long startTime = 0L;
    private final Handler handler = new Handler(Looper.getMainLooper());
    // getMainLooper()), a Handler that places tasks onto the main UI thread's "conveyor belt"

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = SystemClock.uptimeMillis() - startTime;
            // post value to public live data to update observers
            _elapsedTime.postValue(millis);
            // used postDelayed to update every 10ms rather than postAtTime to avoid calculation
            handler.postDelayed(this, 10);
        }
    };

    public void startTimer() {
        if (Boolean.FALSE.equals(_isRunning.getValue())) {
            // if not reset get the elapsed time, else 0, change nowhere else
            startTime = SystemClock.uptimeMillis() - (_elapsedTime.getValue() != null ? _elapsedTime.getValue() : 0L);
            handler.postDelayed(timerRunnable, 10);
            _isRunning.setValue(true);
        }
    }

    public void stopTimer() {
        if (Boolean.TRUE.equals(_isRunning.getValue())) {
            handler.removeCallbacks(timerRunnable);
            _isRunning.setValue(false);
        }
    }

    public void resetTimer() {
        _elapsedTime.setValue(0L);
        if (Boolean.FALSE.equals(_isRunning.getValue())) {
            startTime = SystemClock.uptimeMillis();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handler.removeCallbacks(timerRunnable);
    }
}
