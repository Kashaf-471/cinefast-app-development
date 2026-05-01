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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

        // Hamburger menu button — opens the NavigationDrawer
        ImageView ivMenu = view.findViewById(R.id.ivMenu);
        if (ivMenu != null) {
            ivMenu.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).openDrawer();
                }
            });
        }

        Bundle args = getArguments();
        if (args == null) return;

        String movieName   = args.getString("MOVIE_NAME");
        ArrayList<String> seatNumbers = args.getStringArrayList("SEAT_NUMBERS");
        double ticketTotal = args.getDouble("TICKET_TOTAL", 0);
        double snacksTotal = args.getDouble("SNACKS_TOTAL", 0.0);
        double totalAmount = args.getDouble("TOTAL_AMOUNT", 0.0);
        String rating      = args.getString("RATING");
        String language    = args.getString("LANGUAGE");
        String movieGenre  = args.getString("MOVIE_GENRE");
        String theater     = args.getString("THEATER");
        String hall        = args.getString("HALL");
        String date        = args.getString("DATE");
        String time        = args.getString("TIME");
        String posterName  = args.getString("POSTER_NAME", "");
        int snackCount     = args.getInt("SNACK_COUNT", 0);

        TextView tvMovieName  = view.findViewById(R.id.tvMovieName);
        TextView tvRating     = view.findViewById(R.id.tvRating);
        TextView tvLanguage   = view.findViewById(R.id.tvLanguage);
        TextView tvMovieGenre = view.findViewById(R.id.tvMovieGenre);
        TextView tvTheater    = view.findViewById(R.id.tvTheater);
        TextView tvHall       = view.findViewById(R.id.tvHall);
        TextView tvDate       = view.findViewById(R.id.tvDate);
        TextView tvTime       = view.findViewById(R.id.tvTime);
        TextView tvSeatInfo   = view.findViewById(R.id.tvSeatInfo);
        TextView tvTicketPrice = view.findViewById(R.id.tvTicketPrice);
        TextView tvSnacksInfo  = view.findViewById(R.id.tvSnacksInfo);
        TextView tvSnacksPrice = view.findViewById(R.id.tvSnacksPrice);
        TextView tvTotalPrice  = view.findViewById(R.id.tvTotalPrice);
        ImageView ivMoviePoster = view.findViewById(R.id.ivMoviePoster);

        tvMovieName.setText(movieName);
        tvRating.setText(rating);
        tvLanguage.setText(language);
        tvMovieGenre.setText(movieGenre);
        tvTheater.setText(theater);
        tvHall.setText(hall);
        tvDate.setText(date);
        tvTime.setText(time);

        // Seats display
        StringBuilder seatsStr = new StringBuilder();
        StringBuilder seatsPriceStr = new StringBuilder();
        if (seatNumbers != null) {
            for (String seat : seatNumbers) {
                seatsStr.append("Row ").append(seat.charAt(0))
                        .append(", Seat ").append(seat.substring(1)).append("\n");
                seatsPriceStr.append(String.format("%.2f USD\n", 16.00));
            }
        }
        tvSeatInfo.setText(seatsStr.toString().trim());
        tvTicketPrice.setText(seatsPriceStr.toString().trim());

        // Snacks display
        StringBuilder snacksInfoSb = new StringBuilder();
        StringBuilder snacksPriceSb = new StringBuilder();
        for (int i = 0; i < snackCount; i++) {
            int qty = args.getInt("QUANTITY_" + i, 0);
            if (qty > 0) {
                String snackName  = args.getString("SNACK_NAME_" + i, "");
                double snackPrice = args.getDouble("SNACK_PRICE_" + i, 0);
                snacksInfoSb.append("X").append(qty).append(" ").append(snackName).append("\n");
                snacksPriceSb.append(String.format("%.2f USD\n", qty * snackPrice));
            }
        }
        if (snackCount == 0) {
            int qPopcorn = args.getInt("QUANTITY_POPCORN", 0);
            int qNachos  = args.getInt("QUANTITY_NACHOS", 0);
            int qDrink   = args.getInt("QUANTITY_SOFT_DRINK", 0);
            int qCandy   = args.getInt("QUANTITY_CANDY_MIX", 0);
            if (qPopcorn > 0) { snacksInfoSb.append("X").append(qPopcorn).append(" Popcorn\n"); snacksPriceSb.append(String.format("%.2f USD\n", qPopcorn * 8.99)); }
            if (qNachos  > 0) { snacksInfoSb.append("X").append(qNachos).append(" Nachos\n");   snacksPriceSb.append(String.format("%.2f USD\n", qNachos  * 7.99)); }
            if (qDrink   > 0) { snacksInfoSb.append("X").append(qDrink).append(" Soft Drink\n"); snacksPriceSb.append(String.format("%.2f USD\n", qDrink   * 5.99)); }
            if (qCandy   > 0) { snacksInfoSb.append("X").append(qCandy).append(" Candy Mix\n"); snacksPriceSb.append(String.format("%.2f USD\n", qCandy   * 6.99)); }
        }
        if (snacksInfoSb.length() == 0) snacksInfoSb.append("No snacks selected");
        tvSnacksInfo.setText(snacksInfoSb.toString().trim());
        tvSnacksPrice.setText(snacksPriceSb.toString().trim());
        tvTotalPrice.setText(String.format("%.2f USD", totalAmount));

        // Resolve movie poster
        if (posterName != null && !posterName.isEmpty()) {
            int resId = requireContext().getResources().getIdentifier(
                    posterName, "drawable", requireContext().getPackageName());
            if (resId != 0) ivMoviePoster.setImageResource(resId);
            else ivMoviePoster.setImageResource(R.drawable.placeholder);
        } else if (movieName != null) {
            if (movieName.contains("Dark Knight")) ivMoviePoster.setImageResource(R.drawable.dark_knight);
            else if (movieName.contains("Inception")) ivMoviePoster.setImageResource(R.drawable.inception);
            else if (movieName.contains("Interstellar")) ivMoviePoster.setImageResource(R.drawable.interstellar);
            else if (movieName.contains("Shawshank")) ivMoviePoster.setImageResource(R.drawable.shawshank);
            else ivMoviePoster.setImageResource(R.drawable.placeholder);
        }

        // Save booking locally
        int seatsCount = (seatNumbers != null) ? seatNumbers.size() : 0;
        saveLastBooking(movieName, seatsCount, (float) totalAmount);

        // Save booking to Firebase
        saveBookingToFirebase(movieName, seatNumbers, totalAmount, date, time, posterName);

        // Send Ticket Button
        Button btnSendTicket = view.findViewById(R.id.btnSendTicket);
        final String finalSnacksInfo = snacksInfoSb.toString();
        btnSendTicket.setOnClickListener(v -> {
            String summary = "Movie: " + movieName + "\n"
                    + "Seats: " + seatsStr.toString().replace("\n", ", ").trim() + "\n"
                    + "Tickets: $" + String.format("%.2f", ticketTotal) + "\n"
                    + "Snacks: " + finalSnacksInfo.replace("\n", ", ").trim() + "\n"
                    + "TOTAL: $" + String.format("%.2f", totalAmount);

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
        prefs.edit()
                .putString("last_movie_name", movieName)
                .putInt("last_seats_count", seatsCount)
                .putFloat("last_total_price", totalPrice)
                .apply();
    }

    private void saveBookingToFirebase(String movieName, ArrayList<String> seatNumbers,
                                       double totalPrice, String date, String time, String posterName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String userId = user.getUid();
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");

        DatabaseReference newBookingRef = bookingsRef.child(userId).push();
        String bookingId = newBookingRef.getKey();

        String dateTime = date + " " + time;
        long timestamp = System.currentTimeMillis();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            Date bookingDate = sdf.parse(dateTime);
            if (bookingDate != null) timestamp = bookingDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("bookingId", bookingId);
        bookingData.put("userId", userId);
        bookingData.put("movieName", movieName);
        bookingData.put("seats", seatNumbers);
        bookingData.put("totalPrice", totalPrice);
        bookingData.put("dateTime", dateTime);
        bookingData.put("timestamp", timestamp);
        bookingData.put("posterName", posterName != null ? posterName : "");

        newBookingRef.setValue(bookingData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Booking saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
