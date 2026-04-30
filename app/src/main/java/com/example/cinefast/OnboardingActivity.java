package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Button btnStart = findViewById(R.id.btnStart);
        // Go to Login instead of directly to MainActivity
        btnStart.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));
    }
}
