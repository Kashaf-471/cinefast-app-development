package com.example.cinefast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SnacksActivity extends AppCompatActivity {

    private TextView tvQuantityPopcorn, tvQuantityNachos, tvQuantitySoftDrink, tvQuantityCandyMix;
    private int quantityPopcorn = 0;
    private int quantityNachos = 0;
    private int quantitySoftDrink = 0;
    private int quantityCandyMix = 0;

    private static final double PRICE_POPCORN = 8.99;
    private static final double PRICE_NACHOS = 7.99;
    private static final double PRICE_SOFT_DRINK = 5.99;
    private static final double PRICE_CANDY_MIX = 6.99;

    private String movieName;
    private int seatsCount;
    private double ticketTotal;
    private ArrayList<String> seatNumbers;
    private String rating, language, movieGenre, theater, hall, date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks);

        // Get data from SeatSelectionActivity
        Intent intent = getIntent();
        movieName = intent.getStringExtra("MOVIE_NAME");
        seatsCount = intent.getIntExtra("SEATS_COUNT", 0);
        ticketTotal = intent.getDoubleExtra("TICKET_TOTAL", 0);
        seatNumbers = intent.getStringArrayListExtra("SEAT_NUMBERS");
        rating = intent.getStringExtra("RATING");
        language = intent.getStringExtra("LANGUAGE");
        movieGenre = intent.getStringExtra("MOVIE_GENRE");
        theater = intent.getStringExtra("THEATER");
        hall = intent.getStringExtra("HALL");
        date = intent.getStringExtra("DATE");
        time = intent.getStringExtra("TIME");


        TextView appName = findViewById(R.id.appName);

        SpannableString spannableString = new SpannableString("CineFAST");
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 4, 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 4, 8, 0);
        appName.setText(spannableString);

        tvQuantityPopcorn = findViewById(R.id.tvQuantityPopcorn);
        tvQuantityNachos = findViewById(R.id.tvQuantityNachos);
        tvQuantitySoftDrink = findViewById(R.id.tvQuantitySoftDrink);
        tvQuantityCandyMix = findViewById(R.id.tvQuantityCandyMix);


        Button btnMinusPopcorn = findViewById(R.id.btnMinusPopcorn);
        Button btnPlusPopcorn = findViewById(R.id.btnPlusPopcorn);
        btnMinusPopcorn.setOnClickListener(v -> {
            if (quantityPopcorn > 0) {
                quantityPopcorn--;
                tvQuantityPopcorn.setText(String.valueOf(quantityPopcorn));
            }
        });
        btnPlusPopcorn.setOnClickListener(v -> {
            quantityPopcorn++;
            tvQuantityPopcorn.setText(String.valueOf(quantityPopcorn));
        });


        Button btnMinusNachos = findViewById(R.id.btnMinusNachos);
        Button btnPlusNachos = findViewById(R.id.btnPlusNachos);
        btnMinusNachos.setOnClickListener(v -> {
            if (quantityNachos > 0) {
                quantityNachos--;
                tvQuantityNachos.setText(String.valueOf(quantityNachos));
            }
        });
        btnPlusNachos.setOnClickListener(v -> {
            quantityNachos++;
            tvQuantityNachos.setText(String.valueOf(quantityNachos));
        });


        Button btnMinusSoftDrink = findViewById(R.id.btnMinusSoftDrink);
        Button btnPlusSoftDrink = findViewById(R.id.btnPlusSoftDrink);
        btnMinusSoftDrink.setOnClickListener(v -> {
            if (quantitySoftDrink > 0) {
                quantitySoftDrink--;
                tvQuantitySoftDrink.setText(String.valueOf(quantitySoftDrink));
            }
        });
        btnPlusSoftDrink.setOnClickListener(v -> {
            quantitySoftDrink++;
            tvQuantitySoftDrink.setText(String.valueOf(quantitySoftDrink));
        });


        Button btnMinusCandyMix = findViewById(R.id.btnMinusCandyMix);
        Button btnPlusCandyMix = findViewById(R.id.btnPlusCandyMix);
        btnMinusCandyMix.setOnClickListener(v -> {
            if (quantityCandyMix > 0) {
                quantityCandyMix--;
                tvQuantityCandyMix.setText(String.valueOf(quantityCandyMix));
            }
        });
        btnPlusCandyMix.setOnClickListener(v -> {
            quantityCandyMix++;
            tvQuantityCandyMix.setText(String.valueOf(quantityCandyMix));
        });

        Button btnConfirm = findViewById(R.id.btnConfirm);
        // Ensure ticketTotal comes from Intent and is never recalculated
        ticketTotal = intent.getDoubleExtra("TICKET_TOTAL", 0.0);

// Confirm button
        btnConfirm.setOnClickListener(v -> {
            double snacksTotal = (quantityPopcorn * PRICE_POPCORN) +
                    (quantityNachos * PRICE_NACHOS) +
                    (quantitySoftDrink * PRICE_SOFT_DRINK) +
                    (quantityCandyMix * PRICE_CANDY_MIX);

            double totalAmount = ticketTotal + snacksTotal;  // sum of ticket + snacks

            Intent summaryIntent = new Intent(SnacksActivity.this, TicketSummaryActivity.class);
            summaryIntent.putExtra("SEAT_NUMBERS", seatNumbers); // important
            summaryIntent.putExtra("TICKET_TOTAL", ticketTotal); // important
            summaryIntent.putExtra("SNACKS_TOTAL", snacksTotal);
            summaryIntent.putExtra("TOTAL_AMOUNT", totalAmount);

            summaryIntent.putExtra("QUANTITY_POPCORN", quantityPopcorn);
            summaryIntent.putExtra("QUANTITY_NACHOS", quantityNachos);
            summaryIntent.putExtra("QUANTITY_SOFT_DRINK", quantitySoftDrink);
            summaryIntent.putExtra("QUANTITY_CANDY_MIX", quantityCandyMix);

            // movie and theater info
            summaryIntent.putExtra("MOVIE_NAME", movieName);
            summaryIntent.putExtra("RATING", rating);
            summaryIntent.putExtra("LANGUAGE", language);
            summaryIntent.putExtra("MOVIE_GENRE", movieGenre);
            summaryIntent.putExtra("THEATER", theater);
            summaryIntent.putExtra("HALL", hall);
            summaryIntent.putExtra("DATE", date);
            summaryIntent.putExtra("TIME", time);

            startActivity(summaryIntent);
        });

    }
}
