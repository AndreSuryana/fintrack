package com.andresuryana.fintrack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.andresuryana.fintrack.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Layout binding
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Clear layout binding
        binding = null;
    }
}