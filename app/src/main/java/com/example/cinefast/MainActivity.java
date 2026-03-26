package com.example.cinefast;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), false);
        }
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    //Home -> Seat Selection
    public void navigateToSeatSelection(Movie movie) {
        SeatSelectionFragment fragment = new SeatSelectionFragment();
        Bundle args = new Bundle();
        args.putString("movieName", movie.getName());
        args.putInt("posterResId", movie.getPosterResId());
        args.putString("trailerUrl", movie.getTrailerUrl());
        args.putBoolean("isNowShowing", movie.isNowShowing());
        fragment.setArguments(args);
        loadFragment(fragment, true);
    }

    // Navigation method: Seat Selection -> Snacks
    public void navigateToSnacks(String movieName, ArrayList<String> seatNumbers,
                                  double ticketTotal, int seatsCount) {
        SnacksFragment fragment = new SnacksFragment();
        Bundle args = new Bundle();
        args.putString("MOVIE_NAME", movieName);
        args.putStringArrayList("SEAT_NUMBERS", seatNumbers);
        args.putDouble("TICKET_TOTAL", ticketTotal);
        args.putInt("SEATS_COUNT", seatsCount);
        args.putString("RATING", "+13");
        args.putString("LANGUAGE", "EN");
        args.putString("MOVIE_GENRE", "ScreenX Dolby Atmos");
        args.putString("THEATER", "Stars (90°Mall)");
        args.putString("HALL", "1st");
        args.putString("DATE", "13.04.2025");
        args.putString("TIME", "22:15");
        fragment.setArguments(args);
        loadFragment(fragment, true);
    }

    // Navigation method: -> Ticket Summary
    public void navigateToTicketSummary(Bundle data) {
        TicketSummaryFragment fragment = new TicketSummaryFragment();
        fragment.setArguments(data);
        loadFragment(fragment, true);
    }

    // Navigation method: Back to Home
    public void navigateToHome() {
        getSupportFragmentManager().popBackStack(null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        loadFragment(new HomeFragment(), false);
    }
}