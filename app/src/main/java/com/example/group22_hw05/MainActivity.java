// Homework 05
// MainActivity.java
// Ken Stanley & Stephanie Karp

package com.example.group22_hw05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import com.example.group22_hw05.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final int COMPLEXITY_MAX = 20;
    private static final int THREADS_MAX = 2;

    ActivityMainBinding binding;
    Executor threadPool;
    Handler handler;

    int complexity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handler = new Handler(message -> {
            ArrayList<Double> numbers = (ArrayList<Double>) message.obj;

            switch (message.what) {
                case DoWork.STATUS_END:
                    binding.buttonGenerate.setEnabled(true);
                    break;
                case DoWork.STATUS_START:
                    binding.buttonGenerate.setEnabled(false);
                    break;
            }

            binding.progressBar.setProgress(numbers.size());
            binding.textViewProgress.setText(getString(R.string.text_view_progress, numbers.size(), complexity));

            double avg = 0.0;
            for (double number: numbers) {
                avg += number;
            }

            // Avoid division by zero (or NaN)
            if (numbers.size() > 0) {
                avg /= numbers.size();
            }

            binding.textViewAverage.setText(getString(R.string.text_view_average, avg));
            binding.listViewOutput.setAdapter(new ArrayAdapter<>(
                    MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    numbers
            ));

            return false;
        });

        // Hide the UI components that aren't visible until the Generate button is pressed.
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.textViewProgress.setVisibility(View.INVISIBLE);
        binding.textViewAverage.setVisibility(View.INVISIBLE);
        binding.listViewOutput.setVisibility(View.INVISIBLE);

        // Start off disabled, and let the seekBarComplexity manage its availability
        binding.buttonGenerate.setEnabled(false);

        threadPool = Executors.newFixedThreadPool(THREADS_MAX);

        binding.buttonGenerate.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.textViewProgress.setVisibility(View.VISIBLE);
            binding.textViewAverage.setVisibility(View.VISIBLE);
            binding.listViewOutput.setVisibility(View.VISIBLE);

            complexity = binding.seekBarComplexity.getProgress();

            binding.progressBar.setMax(complexity);
            threadPool.execute(new DoWork(complexity));
        });

        binding.seekBarComplexity.setMax(COMPLEXITY_MAX);
        binding.textViewSelectedNumber.setText(getString(R.string.text_view_selected_number, 0));
        binding.seekBarComplexity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.textViewSelectedNumber.setText(getString(R.string.text_view_selected_number, i));
                binding.buttonGenerate.setEnabled(i > 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    class DoWork implements Runnable {
        final static int STATUS_START = 0x001;
        final static int STATUS_PROGRESS = 0x002;
        final static int STATUS_END = 0x003;

        private final int complexity;
        private final ArrayList<Double> numbers = new ArrayList<>();

        public DoWork(int complexity) {
            this.complexity = complexity;
        }

        @Override
        public void run() {
            numbers.clear();

            sendMessage(STATUS_START);

            for (int i = 1; i <= complexity; i++) {
                numbers.add(HeavyWork.getNumber());
                sendMessage(STATUS_PROGRESS);
            }

            sendMessage(STATUS_END);
        }

        private void sendMessage(int what) {
            Message message = new Message();
            message.what = what;
            message.obj = numbers;

            handler.sendMessage(message);
        }
    }
}