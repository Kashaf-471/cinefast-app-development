package com.example.cinefast;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    TextView appName;

    Button bookSeats1, trailer1;
    Button bookSeats2, trailer2;
    Button bookSeats3, trailer3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        appName = findViewById(R.id.appName);

        String text = "CineFAST";
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // "Cine" red
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), 4, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // "FAST" white
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // bold
        appName.setText(ss);

        bookSeats1 = findViewById(R.id.bookSeats1);
        trailer1 = findViewById(R.id.trailer1);

        bookSeats2 = findViewById(R.id.bookSeats2);
        trailer2 = findViewById(R.id.trailer2);

        bookSeats3 = findViewById(R.id.bookSeats3);
        trailer3 = findViewById(R.id.trailer3);


        bookSeats1.setOnClickListener(v -> openSeatSelection("The Dark Knight"));
        bookSeats2.setOnClickListener(v -> openSeatSelection("Inception"));
        bookSeats3.setOnClickListener(v -> openSeatSelection("Interstellar"));


        trailer1.setOnClickListener(v -> openYoutube("https://www.youtube.com/watch?v=EXeTwQWrcwY")); // The Dark Knight
        trailer2.setOnClickListener(v -> openYoutube("https://www.youtube.com/watch?v=YoHD9XEInc0")); // Inception
        trailer3.setOnClickListener(v -> openYoutube("https://www.youtube.com/watch?v=zSWdZVtXT7E")); // Interstellar

    }
    private void openSeatSelection(String movieName) {
        Intent intent = new Intent(this, SeatSelectionActivity.class);
        intent.putExtra("movieName", movieName);
        startActivity(intent);
    }


    private void openYoutube(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage("com.google.android.youtube");
        try {
            startActivity(intent);
        } catch (Exception e) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }
}
