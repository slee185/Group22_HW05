// Homework 05
// MainActivity.java
// Ken Stanley & Stephanie Karp

package com.example.group22_hw05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.group22_hw05.databinding.ActivityMainBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Executor threadPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        threadPool = Executors.newFixedThreadPool(2);
        binding.buttonGenerate.setOnClickListener(v -> threadPool.execute(new DoWork()));
    }

    static class DoWork implements Runnable {

        @Override
        public void run() {

        }
    }
}