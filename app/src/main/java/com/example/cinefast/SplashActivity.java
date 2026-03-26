package com.example.cinefast;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.appName);

        // Styling CineFAST: "Cine" in Red, "FAST" in White, all Bold
        String text = "CineFAST";
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), 4, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        appName.setText(ss);

        // Load and start the rotation animation
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);
        logo.startAnimation(rotate);

        // Move to Onboarding after 5 seconds
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
            finish();
        }, 5000);
    }
}
