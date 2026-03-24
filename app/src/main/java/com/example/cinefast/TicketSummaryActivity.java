package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TicketSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_summary);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        String movieName = intent.getStringExtra("MOVIE_NAME");
        ArrayList<String> seatNumbers = intent.getStringArrayListExtra("SEAT_NUMBERS");
        double ticketTotal = intent.getDoubleExtra("TICKET_TOTAL", 0);
        double snacksTotal = intent.getDoubleExtra("SNACKS_TOTAL", 0.0);
        double totalAmount = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0);
        String rating = intent.getStringExtra("RATING");
        String language = intent.getStringExtra("LANGUAGE");
        String movieGenre = intent.getStringExtra("MOVIE_GENRE");
        String theater = intent.getStringExtra("THEATER");
        String hall = intent.getStringExtra("HALL");
        String date = intent.getStringExtra("DATE");
        String time = intent.getStringExtra("TIME");

        int quantityPopcorn = intent.getIntExtra("QUANTITY_POPCORN", 0);
        int quantityNachos = intent.getIntExtra("QUANTITY_NACHOS", 0);
        int quantitySoftDrink = intent.getIntExtra("QUANTITY_SOFT_DRINK", 0);
        int quantityCandyMix = intent.getIntExtra("QUANTITY_CANDY_MIX", 0);

        TextView tvMovieName = findViewById(R.id.tvMovieName);
        TextView tvRating = findViewById(R.id.tvRating);
        TextView tvLanguage = findViewById(R.id.tvLanguage);
        TextView tvMovieGenre = findViewById(R.id.tvMovieGenre);
        TextView tvTheater = findViewById(R.id.tvTheater);
        TextView tvHall = findViewById(R.id.tvHall);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvTime = findViewById(R.id.tvTime);
        TextView tvSeatInfo = findViewById(R.id.tvSeatInfo);
        TextView tvTicketPrice = findViewById(R.id.tvTicketPrice);
        TextView tvSnacksInfo = findViewById(R.id.tvSnacksInfo);
        TextView tvSnacksPrice = findViewById(R.id.tvSnacksPrice);
        TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);
        ImageView ivMoviePoster = findViewById(R.id.ivMoviePoster);


        tvMovieName.setText(movieName);
        tvRating.setText(rating);
        tvLanguage.setText(language);
        tvMovieGenre.setText(movieGenre);
        tvTheater.setText(theater);
        tvHall.setText(hall);
        tvDate.setText(date);
        tvTime.setText(time);

        StringBuilder seatsStr = new StringBuilder();
        StringBuilder seatsPriceStr = new StringBuilder();
        if (seatNumbers != null) {
            for (String seat : seatNumbers) {
                seatsStr.append("Row ").append(seat.charAt(0)).append(", Seat ").append(seat.substring(1)).append("\n");
                seatsPriceStr.append(String.format("%.2f USD\n", 16.00));
            }
        }

        tvSeatInfo.setText(seatsStr.toString().trim());
        tvTicketPrice.setText(seatsPriceStr.toString().trim());

        StringBuilder snacksInfo = new StringBuilder();
        StringBuilder snacksPrice = new StringBuilder();
        if (quantityPopcorn > 0) {
            snacksInfo.append("X").append(quantityPopcorn).append(" Medium Salt Popcorn\n");
            snacksPrice.append(String.format("%.2f USD\n", quantityPopcorn * 8.99));
        }
        if (quantityNachos > 0) {
            snacksInfo.append("X").append(quantityNachos).append(" Nachos with Cheese Dip\n");
            snacksPrice.append(String.format("%.2f USD\n", quantityNachos * 7.99));
        }
        if (quantitySoftDrink > 0) {
            snacksInfo.append("X").append(quantitySoftDrink).append(" Large Soft Drink\n");
            snacksPrice.append(String.format("%.2f USD\n", quantitySoftDrink * 5.99));
        }
        if (quantityCandyMix > 0) {
            snacksInfo.append("X").append(quantityCandyMix).append(" Assorted Candies");
            snacksPrice.append(String.format("%.2f USD", quantityCandyMix * 6.99));
        }

        if (snacksInfo.length() == 0) {
            snacksInfo.append("No snacks selected");
        }

        tvSnacksInfo.setText(snacksInfo.toString().trim());
        tvSnacksPrice.setText(snacksPrice.toString().trim());
        tvTotalPrice.setText(String.format("%.2f USD", totalAmount));

        Button btnSendTicket = findViewById(R.id.btnSendTicket);
        btnSendTicket.setOnClickListener(v -> {
            String summary = "Movie: " + movieName + "\n" +
                    "Seats: " + seatsStr.toString().replace("\n", ", ").trim() + "\n" +
                    "Tickets: $" + String.format("%.2f", ticketTotal) + "\n" +
                    "Snacks: " + snacksInfo.toString().replace("\n", ", ").trim() + "\n" +
                    "TOTAL: $" + String.format("%.2f", totalAmount);

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, summary);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Ticket via"));
        });

        if (movieName != null) {

            if (movieName.equals("The Dark Knight")) {
                ivMoviePoster.setImageResource(R.drawable.dark_knight);
            }
            else if (movieName.equals("Inception")) {
                ivMoviePoster.setImageResource(R.drawable.inception);
            }
            else if (movieName.equals("Interstellar")) {
                ivMoviePoster.setImageResource(R.drawable.interstellar);
            }

        }
    }
}
