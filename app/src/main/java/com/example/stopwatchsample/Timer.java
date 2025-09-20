package com.example.stopwatchsample;

import java.util.UUID;

public class Timer {
    private String id;
    private long elapsedTime;
    private boolean isRunning;
    private long startTime;
    
    public Timer() {
        this.id = UUID.randomUUID().toString();
        this.elapsedTime = 0L;
        this.isRunning = false;
        this.startTime = 0L;
    }
    
    public Timer(String id, long elapsedTime, boolean isRunning, long startTime) {
        this.id = id;
        this.elapsedTime = elapsedTime;
        this.isRunning = isRunning;
        this.startTime = startTime;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public long getElapsedTime() { return elapsedTime; }
    public void setElapsedTime(long elapsedTime) { this.elapsedTime = elapsedTime; }
    
    public boolean isRunning() { return isRunning; }
    public void setRunning(boolean running) { isRunning = running; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
}
