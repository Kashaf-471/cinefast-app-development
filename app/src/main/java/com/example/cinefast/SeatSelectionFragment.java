package com.example.cinefast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectionFragment extends Fragment {
    private final int TICKET_PRICE = 16;

    private GridLayout seatGrid;
    private Button btnBookSeats, btnProceedSnacks;
    private TextView tvMovieName;

    private List<TextView> selectedSeats = new ArrayList<>();
    private List<String> selectedSeatNumbers = new ArrayList<>();

    private String movieName;
    private boolean isNowShowing;
    private String trailerUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seat_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get arguments
        Bundle args = getArguments();
        if (args != null) {
            movieName = args.getString("movieName", "Movie Title");
            isNowShowing = args.getBoolean("isNowShowing", true);
            trailerUrl = args.getString("trailerUrl", "");
        }

        seatGrid = view.findViewById(R.id.seatGrid);
        btnBookSeats = view.findViewById(R.id.btnBookSeats);
        btnProceedSnacks = view.findViewById(R.id.btnProceedSnacks);
        tvMovieName = view.findViewById(R.id.tvMovieName);
        ImageView btnBack = view.findViewById(R.id.btnBack);

        tvMovieName.setText(movieName);

        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        createSeatGrid();

        if (isNowShowing) {
            // Now Showing behavior: normal seat selection
            updateButtonStates();

            btnBookSeats.setOnClickListener(v -> {
                Toast.makeText(getContext(), getString(R.string.booking_confirmed), Toast.LENGTH_SHORT).show();

                Bundle data = new Bundle();
                double ticketTotal = selectedSeatNumbers.size() * TICKET_PRICE;
                data.putString("MOVIE_NAME", movieName);
                data.putInt("SEATS_COUNT", selectedSeatNumbers.size());
                data.putStringArrayList("SEAT_NUMBERS", new ArrayList<>(selectedSeatNumbers));
                data.putDouble("TICKET_TOTAL", ticketTotal);
                data.putDouble("SNACKS_TOTAL", 0.0);
                data.putDouble("TOTAL_AMOUNT", ticketTotal);
                data.putString("RATING", "+13");
                data.putString("LANGUAGE", "EN");
                data.putString("MOVIE_GENRE", "ScreenX Dolby Atmos");
                data.putString("THEATER", "Stars (90°Mall)");
                data.putString("HALL", "1st");
                data.putString("DATE", "13.04.2025");
                data.putString("TIME", "22:15");
                data.putInt("QUANTITY_POPCORN", 0);
                data.putInt("QUANTITY_NACHOS", 0);
                data.putInt("QUANTITY_SOFT_DRINK", 0);
                data.putInt("QUANTITY_CANDY_MIX", 0);

                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToTicketSummary(data);
                }
            });

            btnProceedSnacks.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToSnacks(
                            movieName,
                            new ArrayList<>(selectedSeatNumbers),
                            selectedSeatNumbers.size() * TICKET_PRICE,
                            selectedSeatNumbers.size()
                    );
                }
            });

        } else {
            // Coming Soon behavior: seats disabled, different buttons
            btnBookSeats.setText(R.string.coming_soon);
            btnBookSeats.setEnabled(false);
            btnBookSeats.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF888888));

            btnProceedSnacks.setText(R.string.watch_trailer);
            btnProceedSnacks.setEnabled(true);
            btnProceedSnacks.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                intent.setPackage("com.google.android.youtube");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                    startActivity(browserIntent);
                }
            });
        }
    }

    private void createSeatGrid() {
        int rows = 8;
        seatGrid.setColumnCount(9);
        seatGrid.removeAllViews(); // Clear any existing views just in case

        // Clear the list of TextViews because we are creating new ones
        selectedSeats.clear();

        String[] bookedSeats = {"A2", "A7", "D4", "D5", "H3", "H6"};

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 9; col++) {

                if (col == 4) {
                    View aisle = new View(requireContext());
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
                    View empty = new View(requireContext());
                    GridLayout.LayoutParams p = new GridLayout.LayoutParams();
                    p.width = 48;
                    p.height = 48;
                    empty.setLayoutParams(p);
                    seatGrid.addView(empty);
                    continue;
                }

                TextView seat = new TextView(requireContext());
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 80;
                params.height = 80;
                params.setMargins(7, 7, 7, 7);
                seat.setLayoutParams(params);

                String seatId = (char) ('A' + row) + String.valueOf(seatIndex + 1);
                seat.setText("");
                seat.setBackgroundResource(R.drawable.seat_selector);
                
                // Initialize as available
                seat.setTag("available");

                // Check if booked
                for (String b : bookedSeats) {
                    if (b.equals(seatId)) {
                        seat.setEnabled(false);
                        seat.setTag("booked");
                    }
                }

                // Restore selection if it was already selected
                if (selectedSeatNumbers.contains(seatId)) {
                    seat.setSelected(true);
                    seat.setTag("yours");
                    selectedSeats.add(seat);
                }

                // If Coming Soon, disable all seats
                if (!isNowShowing) {
                    seat.setEnabled(false);
                } else {
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
                }

                seatGrid.addView(seat);
            }
        }
    }

    private void updateButtonStates() {
        if (selectedSeatNumbers.isEmpty()) {
            btnBookSeats.setEnabled(false);
            btnProceedSnacks.setEnabled(false);
        } else {
            btnBookSeats.setEnabled(true);
            btnProceedSnacks.setEnabled(true);
        }
    }
}
