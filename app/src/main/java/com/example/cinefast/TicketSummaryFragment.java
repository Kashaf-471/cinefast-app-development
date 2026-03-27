package com.example.cinefast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class TicketSummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ticket_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView ivBack = view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Bundle args = getArguments();
        if (args == null) return;

        String movieName = args.getString("MOVIE_NAME");
        ArrayList<String> seatNumbers = args.getStringArrayList("SEAT_NUMBERS");
        double ticketTotal = args.getDouble("TICKET_TOTAL", 0);
        double snacksTotal = args.getDouble("SNACKS_TOTAL", 0.0);
        double totalAmount = args.getDouble("TOTAL_AMOUNT", 0.0);
        String rating = args.getString("RATING");
        String language = args.getString("LANGUAGE");
        String movieGenre = args.getString("MOVIE_GENRE");
        String theater = args.getString("THEATER");
        String hall = args.getString("HALL");
        String date = args.getString("DATE");
        String time = args.getString("TIME");

        int quantityPopcorn = args.getInt("QUANTITY_POPCORN", 0);
        int quantityNachos = args.getInt("QUANTITY_NACHOS", 0);
        int quantitySoftDrink = args.getInt("QUANTITY_SOFT_DRINK", 0);
        int quantityCandyMix = args.getInt("QUANTITY_CANDY_MIX", 0);

        TextView tvMovieName = view.findViewById(R.id.tvMovieName);
        TextView tvRating = view.findViewById(R.id.tvRating);
        TextView tvLanguage = view.findViewById(R.id.tvLanguage);
        TextView tvMovieGenre = view.findViewById(R.id.tvMovieGenre);
        TextView tvTheater = view.findViewById(R.id.tvTheater);
        TextView tvHall = view.findViewById(R.id.tvHall);
        TextView tvDate = view.findViewById(R.id.tvDate);
        TextView tvTime = view.findViewById(R.id.tvTime);
        TextView tvSeatInfo = view.findViewById(R.id.tvSeatInfo);
        TextView tvTicketPrice = view.findViewById(R.id.tvTicketPrice);
        TextView tvSnacksInfo = view.findViewById(R.id.tvSnacksInfo);
        TextView tvSnacksPrice = view.findViewById(R.id.tvSnacksPrice);
        TextView tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        ImageView ivMoviePoster = view.findViewById(R.id.ivMoviePoster);

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

        // Set movie poster based on movie name
        if (movieName != null) {
            if (movieName.equals("The Dark Knight")) {
                ivMoviePoster.setImageResource(R.drawable.dark_knight);
            } else if (movieName.equals("Inception")) {
                ivMoviePoster.setImageResource(R.drawable.inception);
            } else if (movieName.equals("Interstellar")) {
                ivMoviePoster.setImageResource(R.drawable.interstellar);
            } else if (movieName.equals("The Shawshank Redemption")) {
                ivMoviePoster.setImageResource(R.drawable.shawshank);
            } else if (movieName.contains("Dark Knight")) {
                ivMoviePoster.setImageResource(R.drawable.dark_knight);
            } else if (movieName.contains("Inception")) {
                ivMoviePoster.setImageResource(R.drawable.inception);
            }
        }

        // Save to SharedPreferences
        int seatsCount = (seatNumbers != null) ? seatNumbers.size() : 0;
        saveLastBooking(movieName, seatsCount, (float) totalAmount);

        // Send Ticket button
        Button btnSendTicket = view.findViewById(R.id.btnSendTicket);
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
    }

    private void saveLastBooking(String movieName, int seatsCount, float totalPrice) {
        if (getContext() == null) return;
        SharedPreferences prefs = getContext()
                .getSharedPreferences("CinefastBooking", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //editor.clear();
        editor.putString("last_movie_name", movieName);
        editor.putInt("last_seats_count", seatsCount);
        editor.putFloat("last_total_price", totalPrice);

        editor.apply();
    }
}
