package com.example.cinefast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Required toast with tag "CineFAST" when app launches
        Toast.makeText(this, "CineFAST", Toast.LENGTH_SHORT).show();

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        // Load HomeFragment on first launch
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), false);
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                navigateToHome();
            } else if (id == R.id.nav_my_bookings) {
                loadFragment(new MyBookingsFragment(), true);
            } else if (id == R.id.nav_logout) {
                showLogoutDialog();
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performLogout() {
        // Sign out from Firebase Auth
        FirebaseAuth.getInstance().signOut();

        // Clear session in SharedPreferences
        SharedPreferences prefs = getSharedPreferences("cinefast_session_pref_v3", MODE_PRIVATE);
        prefs.edit().clear().apply();

        // Navigate to LoginActivity, clearing back stack
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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

    // Home -> Seat Selection
    public void navigateToSeatSelection(Movie movie) {
        SeatSelectionFragment fragment = new SeatSelectionFragment();
        Bundle args = new Bundle();
        args.putString("movieName", movie.getName());
        args.putInt("posterResId", movie.getPosterResId());
        args.putString("trailerUrl", movie.getTrailerUrl());
        args.putBoolean("isNowShowing", movie.isNowShowing());
        args.putString("posterName", movie.getPosterName());
        args.putString("date", movie.getDate());
        args.putString("time", movie.getTime());
        fragment.setArguments(args);
        loadFragment(fragment, true);
    }

    // Seat Selection -> Snacks
    public void navigateToSnacks(String movieName, ArrayList<String> seatNumbers,
                                  double ticketTotal, int seatsCount, String posterName, String date, String time) {
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
        args.putString("DATE", date);
        args.putString("TIME", time);
        args.putString("POSTER_NAME", posterName != null ? posterName : "");
        fragment.setArguments(args);
        loadFragment(fragment, true);
    }

    // -> Ticket Summary
    public void navigateToTicketSummary(Bundle data) {
        TicketSummaryFragment fragment = new TicketSummaryFragment();
        fragment.setArguments(data);
        loadFragment(fragment, true);
    }

    // -> My Bookings
    public void navigateToMyBookings() {
        loadFragment(new MyBookingsFragment(), true);
        navigationView.setCheckedItem(R.id.nav_my_bookings);
    }

    // Back to Home
    public void navigateToHome() {
        getSupportFragmentManager().popBackStack(null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        loadFragment(new HomeFragment(), false);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    // Allow fragments to open the drawer
    public void openDrawer() {
        drawerLayout.openDrawer(androidx.core.view.GravityCompat.START);
    }
}
