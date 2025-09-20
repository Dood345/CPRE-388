package com.example.stopwatchsample;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TimerPreferencesManager {
    private static final String PREF_NAME = "timer_prefs";
    private static final String KEY_TIMERS = "timers";
    
    private SharedPreferences sharedPreferences;
    private Gson gson;
    
    public TimerPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }
    
    public void saveTimers(List<Timer> timers) {
        String json = gson.toJson(timers);
        sharedPreferences.edit().putString(KEY_TIMERS, json).apply();
    }
    
    public List<Timer> loadTimers() {
        String json = sharedPreferences.getString(KEY_TIMERS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Timer>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
