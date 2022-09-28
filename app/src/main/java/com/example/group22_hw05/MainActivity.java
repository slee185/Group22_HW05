package com.example.group22_hw05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonGenerate).setOnClickListener(v -> {
            Thread thread = new Thread(new DoWork(), "Worker 1");
            thread.start();

        });
    }

    class DoWork implements Runnable {

        @Override
        public void run() {

        }
    }
}