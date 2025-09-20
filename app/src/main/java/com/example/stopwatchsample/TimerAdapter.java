package com.example.stopwatchsample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Locale;

public class TimerAdapter extends ListAdapter<Timer, TimerAdapter.TimerViewHolder> {

    public interface TimerListener {
        void onStartStopClicked(String timerId);
        void onResetClicked(String timerId);
        void onCloseClicked(String timerId);
    }

    private final TimerListener listener;

    public TimerAdapter(TimerListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Timer> DIFF_CALLBACK = new DiffUtil.ItemCallback<Timer>() {
        @Override
        public boolean areItemsTheSame(@NonNull Timer oldItem, @NonNull Timer newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Timer oldItem, @NonNull Timer newItem) {
            return oldItem.getElapsedTime() == newItem.getElapsedTime() && oldItem.isRunning() == newItem.isRunning();
        }
    };

    @NonNull
    @Override
    public TimerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timer, parent, false);
        return new TimerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerViewHolder holder, int position) {
        Timer timer = getItem(position);
        holder.bind(timer, listener);
    }

    static class TimerViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        Button btnStartStop, btnReset;
        ImageButton btnClose;

        public TimerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            btnStartStop = itemView.findViewById(R.id.btnStartStop);
            btnReset = itemView.findViewById(R.id.btnReset);
            btnClose = itemView.findViewById(R.id.btnClose);
        }

        public void bind(Timer timer, TimerListener listener) {
            updateTimerText(timer.getElapsedTime());

            btnStartStop.setText(timer.isRunning() ? R.string.stop : R.string.start);

            btnStartStop.setOnClickListener(v -> listener.onStartStopClicked(timer.getId()));
            btnReset.setOnClickListener(v -> listener.onResetClicked(timer.getId()));
            btnClose.setOnClickListener(v -> listener.onCloseClicked(timer.getId()));
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
}
