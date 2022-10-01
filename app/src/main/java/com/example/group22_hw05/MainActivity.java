// Homework 05
// MainActivity.java
// Ken Stanley & Stephanie Karp

package com.example.group22_hw05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Executor threadPool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        threadPool = Executors.newFixedThreadPool(2);

        findViewById(R.id.buttonGenerate).setOnClickListener(v -> threadPool.execute(new DoWork()));
    }

    class DoWork implements Runnable {

        @Override
        public void run() {

        }
    }
}