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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SeatSelectionFragment extends Fragment {

    private final int TICKET_PRICE = 16;

    private GridLayout seatGrid;
    private Button btnBookSeats, btnProceedSnacks;
    private TextView tvMovieName;

    // Firebase booked seats
    private List<String> bookedSeats = new ArrayList<>();

    // user selection
    private List<String> selectedSeatNumbers = new ArrayList<>();

    // seat view mapping
    private HashMap<String, TextView> seatViewMap = new HashMap<>();

    private String movieName;
    private boolean isNowShowing;
    private String trailerUrl;
    private String posterName;
    private String date;
    private String time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seat_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            movieName = args.getString("movieName", "Movie Title");
            isNowShowing = args.getBoolean("isNowShowing", true);
            trailerUrl = args.getString("trailerUrl", "");
            posterName = args.getString("posterName", "");
            date = args.getString("date", "13.04.2025");
            time = args.getString("time", "22:15");
        }

        seatGrid = view.findViewById(R.id.seatGrid);
        btnBookSeats = view.findViewById(R.id.btnBookSeats);
        btnProceedSnacks = view.findViewById(R.id.btnProceedSnacks);
        tvMovieName = view.findViewById(R.id.tvMovieName);
        ImageView btnBack = view.findViewById(R.id.btnBack);

        tvMovieName.setText(movieName);

        btnBookSeats.setEnabled(false);
        btnProceedSnacks.setEnabled(false);

        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        btnProceedSnacks.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToSnacks(
                        movieName,
                        new ArrayList<>(selectedSeatNumbers),
                        selectedSeatNumbers.size() * TICKET_PRICE,
                        selectedSeatNumbers.size(),
                        posterName,
                        date,
                        time
                );
            }
        });

        // create grid once
        createSeatGrid();

        // firebase listener
        fetchBookedSeatsFromFirebase();

        if (isNowShowing) {

            btnBookSeats.setOnClickListener(v -> {

                Toast.makeText(getContext(), "Booking confirmed", Toast.LENGTH_SHORT).show();

                double ticketTotal = selectedSeatNumbers.size() * TICKET_PRICE;

                Bundle data = new Bundle();
                data.putString("MOVIE_NAME", movieName);
                data.putInt("SEATS_COUNT", selectedSeatNumbers.size());
                data.putStringArrayList("SEAT_NUMBERS", new ArrayList<>(selectedSeatNumbers));
                data.putDouble("TICKET_TOTAL", ticketTotal);
                data.putDouble("SNACKS_TOTAL", 0.0);
                data.putDouble("TOTAL_AMOUNT", ticketTotal);
                data.putString("DATE", date);
                data.putString("TIME", time);
                data.putString("POSTER_NAME", posterName);

                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToTicketSummary(data);
                }
            });

        } else {
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
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl)));
                }
            });
        }
    }

    private void fetchBookedSeatsFromFirebase() {

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("bookings");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!isAdded()) return;

                bookedSeats.clear();

                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    for (DataSnapshot bookingSnap : userSnap.getChildren()) {

                        String bookedMovie =
                                bookingSnap.child("movieName").getValue(String.class);

                        if (movieName != null && movieName.equals(bookedMovie)) {

                            for (DataSnapshot seatSnap : bookingSnap.child("seats").getChildren()) {

                                String seatId = seatSnap.getValue(String.class);

                                if (seatId != null) {
                                    bookedSeats.add(seatId);

                                    // update UI instantly if seat exists
                                    TextView seatView = seatViewMap.get(seatId);

                                    if (seatView != null &&
                                            !"yours".equals(seatView.getTag())) {

                                        seatView.setEnabled(false);
                                        seatView.setTag("booked");
                                        seatView.setBackgroundResource(R.drawable.seat_booked);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),
                        "Failed to load seats",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createSeatGrid() {

        int rows = 8;
        seatGrid.setColumnCount(9);
        seatGrid.removeAllViews();
        seatViewMap.clear();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 9; col++) {

                // aisle
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

                final String seatId =
                        (char) ('A' + row) + String.valueOf(seatIndex + 1);

                seat.setBackgroundResource(R.drawable.seat_selector);
                seat.setTag("available");

                // already booked
                if (bookedSeats.contains(seatId)) {
                    seat.setEnabled(false);
                    seat.setTag("booked");
                    seat.setBackgroundResource(R.drawable.seat_booked);
                }

                seat.setOnClickListener(v -> {

                    if ("available".equals(v.getTag())) {

                        v.setSelected(true);
                        v.setTag("yours");

                        if (!selectedSeatNumbers.contains(seatId))
                            selectedSeatNumbers.add(seatId);

                    } else if ("yours".equals(v.getTag())) {

                        v.setSelected(false);
                        v.setTag("available");

                        selectedSeatNumbers.remove(seatId);
                    }

                    updateButtonStates();
                });

                seatGrid.addView(seat);
                seatViewMap.put(seatId, seat);
            }
        }

        updateButtonStates();
    }
    @Override
    public void onResume() {
        super.onResume();


        selectedSeatNumbers.clear();

        // reset grid UI if already created
        if (seatGrid != null) {
            createSeatGrid();
            fetchBookedSeatsFromFirebase();
        }

        updateButtonStates();
    }

    private void updateButtonStates() {

        boolean hasSelection = !selectedSeatNumbers.isEmpty();

        btnBookSeats.setEnabled(hasSelection);
        btnProceedSnacks.setEnabled(hasSelection);
    }
}