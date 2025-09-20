## Core Architecture Changes

### 1. **From Single Timer to Timer Collection**

**Original:**
- Single timer state in ViewModel (`elapsedTime`, `isRunning`)
- Direct UI binding to single timer values

**Current:**
- `List<Timer>` managed in ViewModel
- Each `Timer` object encapsulates its own state (id, elapsedTime, isRunning, startTime)
- UI displays multiple timer instances

### 2. **From Multiple Handlers to Single Ticker Pattern**

**Original Problem:**
- Each timer would need its own `Handler` and `Runnable`
- Multiple concurrent callbacks competing for resources
- Race conditions between different timer threads

**Current Solution:**
```java
private final Runnable ticker = new Runnable() {
    @Override
    public void run() {
        // Single loop through all timers
        for (Timer timer : currentTimers) {
            if (timer.isRunning()) {
                long elapsed = SystemClock.uptimeMillis() - timer.getStartTime();
                // Update this timer's elapsed time
            }
        }
        // Single UI update for all timers
        _timers.setValue(updatedTimers);
        handler.postDelayed(this, 100);
    }
};
```

**Key Benefits:**
- One timer loop handles all running timers
- Single UI update per cycle (more efficient)
- No race conditions or handler conflicts
- Automatic cleanup when no timers are running

### 3. **UI Architecture: Single View → RecyclerView Grid**

**Original:**
```xml
<TextView android:id="@+id/tvTime" />
<Button android:id="@+id/btnStartStop" />
<Button android:id="@+id/btnReset" />
```

**Current:**
- `RecyclerView` with `GridLayoutManager` (2 columns)
- `ListAdapter` with `DiffUtil` for efficient updates
- Each timer gets its own circular `CardView` container
- `FloatingActionButton` to add new timers

### 4. **State Management Evolution**

**Original:**
- Simple MutableLiveData for single values
- Direct state mutations

**Current:**
- Immutable `Timer` objects (create new instances for updates)
- Functional programming approach with Java 8 streams
- LiveData containing `List<Timer>` for reactive updates

```java
// Example: Starting a timer creates new Timer objects
List<Timer> updatedTimers = currentTimers.stream().map(timer -> {
    if (timer.getId().equals(timerId)) {
        return new Timer(timer.getId(), timer.getElapsedTime(), true, 
                        SystemClock.uptimeMillis() - timer.getElapsedTime());
    }
    return timer; // Unchanged timers stay the same
}).collect(Collectors.toList());
```

### 5. **Persistence Addition**

**Original:** No persistence

**Current:**
- `TimerPreferencesManager` with JSON serialization via Gson
- Automatic save on every state change
- App restart loads previous timer states (but stops all running timers)

### 6. **Individual Timer Controls**

**Original:** Global start/stop/reset

**Current:**
- Each timer has independent start/stop/reset buttons
- Close button (X) to remove individual timers
- Interface-based communication (`TimerAdapter.TimerListener`)

## How the Current System Works

1. **Timer Creation:** FAB creates new `Timer` with unique UUID
2. **Timer Updates:** Single ticker loops through all timers every 100ms
3. **UI Updates:** `ListAdapter` with `DiffUtil` efficiently updates only changed timers
4. **State Persistence:** Every state change saves to SharedPreferences
5. **Independent Control:** Each timer operates independently through ViewModel methods

## The Simple Solution

The biggest win is the **single ticker pattern** - instead of managing N separate timer threads, you have one efficient loop that:
- Updates all running timers in a single pass
- Triggers one UI update per cycle
- Automatically stops when no timers are running
- Eliminates all concurrency issues

This transforms a potentially complex multi-threading problem into a simple, efficient single-loop solution that scales beautifully with any number of timers.
