package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class SeatSelectionActivity extends AppCompatActivity {
    private final int TICKET_PRICE = 16; // Hardcoded per seat

    private GridLayout seatGrid;
    private Button btnBookSeats, btnProceedSnacks;
    private TextView tvMovieName;

    private List<TextView> selectedSeats = new ArrayList<>();
    private List<String> selectedSeatNumbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        seatGrid = findViewById(R.id.seatGrid);
        btnBookSeats = findViewById(R.id.btnBookSeats);
        btnProceedSnacks = findViewById(R.id.btnProceedSnacks);
        tvMovieName = findViewById(R.id.tvMovieName);
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SeatSelectionActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });


        String movie = getIntent().getStringExtra("movieName");
        if (movie != null) {
            tvMovieName.setText(movie);
        }

        createSeatGrid();
        updateButtonStates();


        btnBookSeats.setOnClickListener(v -> {
            Intent i = new Intent(this, TicketSummaryActivity.class);

            double ticketTotal = selectedSeats.size() * TICKET_PRICE;

            i.putExtra("MOVIE_NAME", movie);
            i.putExtra("SEATS_COUNT", selectedSeats.size());
            i.putStringArrayListExtra("SEAT_NUMBERS", new ArrayList<>(selectedSeatNumbers));
            i.putExtra("TICKET_TOTAL", ticketTotal);
            i.putExtra("SNACKS_TOTAL", 0.0);
            i.putExtra("TOTAL_AMOUNT", ticketTotal);
            i.putExtra("RATING", "+13");
            i.putExtra("LANGUAGE", "EN");
            i.putExtra("MOVIE_GENRE", "ScreenX Dolby Atmos");
            i.putExtra("THEATER", "Stars (90°Mall)");
            i.putExtra("HALL", "1st");
            i.putExtra("DATE", "13.04.2025");
            i.putExtra("TIME", "22:15");


            i.putExtra("QUANTITY_POPCORN", 0);
            i.putExtra("QUANTITY_NACHOS", 0);
            i.putExtra("QUANTITY_SOFT_DRINK", 0);
            i.putExtra("QUANTITY_CANDY_MIX", 0);

            startActivity(i);
        });



        btnProceedSnacks.setOnClickListener(v -> {
            Intent i = new Intent(this, SnacksActivity.class);
            i.putExtra("MOVIE_NAME", movie);
            i.putStringArrayListExtra("SEAT_NUMBERS", new ArrayList<>(selectedSeatNumbers));


            double previousTicketTotal = getIntent().getDoubleExtra("TICKET_TOTAL", selectedSeats.size() * TICKET_PRICE);
            i.putExtra("TICKET_TOTAL", previousTicketTotal);

            i.putExtra("SEATS_COUNT", selectedSeatNumbers.size());

            i.putExtra("RATING", "+13");
            i.putExtra("LANGUAGE", "EN");
            i.putExtra("MOVIE_GENRE", "ScreenX Dolby Atmos");
            i.putExtra("THEATER", "Stars (90°Mall)");
            i.putExtra("HALL", "1st");
            i.putExtra("DATE", "13.04.2025");
            i.putExtra("TIME", "22:15");
            startActivity(i);
        });


    }

    private void createSeatGrid() {

        int rows = 8;
        seatGrid.setColumnCount(9);

        String[] bookedSeats = {"A2","A7","D4","D5","H3","H6"};

        for (int row = 0; row < rows; row++) {

            for (int col = 0; col < 9; col++) {


                if (col == 4) {
                    View aisle = new View(this);
                    GridLayout.LayoutParams p = new GridLayout.LayoutParams();
                    p.width = 140;
                    p.height = 80;

                    aisle.setLayoutParams(p);
                    seatGrid.addView(aisle);
                    continue;
                }

                int seatIndex = col > 4 ? col - 1 : col;


                if ((row == 0 || row == rows - 1) &&
                        (seatIndex == 0 || seatIndex == 7)) {

                    View empty = new View(this);
                    GridLayout.LayoutParams p = new GridLayout.LayoutParams();
                    p.width = 48;
                    p.height = 48;
                    empty.setLayoutParams(p);
                    seatGrid.addView(empty);
                    continue;
                }

                TextView seat = new TextView(this);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 80;
                params.height = 80;
                params.setMargins(7,7,7,7);

                seat.setLayoutParams(params);


                String seatId = (char) ('A' + row) + String.valueOf(seatIndex + 1);
                seat.setText("");
                seat.setBackgroundResource(R.drawable.seat_selector);
                seat.setTag("available");

                for (String b : bookedSeats) {
                    if (b.equals(seatId)) {
                        seat.setEnabled(false);
                        seat.setTag("booked");
                    }
                }

                seat.setOnClickListener(v -> {

                    TextView clickedSeat = (TextView) v;

                    if ("available".equals(v.getTag())) {
                        v.setSelected(true);
                        v.setTag("yours");
                        selectedSeats.add(clickedSeat);
                        selectedSeatNumbers.add(seatId);

                    } else if ("yours".equals(v.getTag())) {
                        v.setSelected(false);
                        v.setTag("available");
                        selectedSeats.remove(clickedSeat);
                        selectedSeatNumbers.remove(seatId);
                    }

                    updateButtonStates();
                });

                seatGrid.addView(seat);
            }
        }
    }

    private void updateButtonStates() {
        if (selectedSeats.isEmpty()) {
            btnBookSeats.setEnabled(false);
            btnProceedSnacks.setEnabled(false);
        } else {
            btnBookSeats.setEnabled(true);
            btnProceedSnacks.setEnabled(true);
        }
    }
}
