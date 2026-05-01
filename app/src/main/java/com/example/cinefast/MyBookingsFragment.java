package com.example.cinefast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyBookingsFragment extends Fragment implements BookingAdapter.OnCancelClickListener {

    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private ArrayList<Booking> bookingList;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private EditText etSearch;

    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_bookings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvBookings);
        progressBar  = view.findViewById(R.id.progressBar);
        tvEmpty      = view.findViewById(R.id.tvEmpty);
        etSearch     = view.findViewById(R.id.etSearch);

        // Back button
        view.findViewById(R.id.ivBack).setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Menu button (Three dots) should open the navigation drawer
        view.findViewById(R.id.ivMenu).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(requireContext(), bookingList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        loadBookingsFromFirebase();
    }

    private void loadBookingsFromFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText("Please log in to see bookings.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        String userId = user.getUid();

        mDatabase.child("bookings").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bookingList.clear();

                        for (DataSnapshot bookingSnap : snapshot.getChildren()) {
                            try {
                                Booking booking = new Booking();
                                booking.setBookingId(bookingSnap.getKey());

                                if (bookingSnap.hasChild("movieName"))
                                    booking.setMovieName(bookingSnap.child("movieName").getValue(String.class));
                                if (bookingSnap.hasChild("dateTime"))
                                    booking.setDateTime(bookingSnap.child("dateTime").getValue(String.class));
                                if (bookingSnap.hasChild("totalPrice")) {
                                    Object priceObj = bookingSnap.child("totalPrice").getValue();
                                    if (priceObj instanceof Double) booking.setTotalPrice((Double) priceObj);
                                    else if (priceObj instanceof Long) booking.setTotalPrice(((Long) priceObj).doubleValue());
                                }
                                if (bookingSnap.hasChild("timestamp")) {
                                    Object tsObj = bookingSnap.child("timestamp").getValue();
                                    if (tsObj instanceof Long) booking.setTimestamp((Long) tsObj);
                                }
                                if (bookingSnap.hasChild("posterName"))
                                    booking.setPosterName(bookingSnap.child("posterName").getValue(String.class));
                                if (bookingSnap.hasChild("userId"))
                                    booking.setUserId(bookingSnap.child("userId").getValue(String.class));

                                // Parse seats list
                                if (bookingSnap.hasChild("seats")) {
                                    ArrayList<String> seats = new ArrayList<>();
                                    for (DataSnapshot seatSnap : bookingSnap.child("seats").getChildren()) {
                                        String seat = seatSnap.getValue(String.class);
                                        if (seat != null) seats.add(seat);
                                    }
                                    booking.setSeats(seats);
                                }

                                bookingList.add(booking);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        progressBar.setVisibility(View.GONE);

                        if (bookingList.isEmpty()) {
                            tvEmpty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            tvEmpty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Failed to load bookings: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCancelClick(Booking booking, int position) {
        long now = System.currentTimeMillis();

        if (booking.getTimestamp() <= now) {
            Toast.makeText(getContext(), "Cannot cancel past bookings", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes", (dialog, which) -> cancelBooking(booking, position))
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelBooking(Booking booking, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String userId = user.getUid();
        String bookingId = booking.getBookingId();

        mDatabase.child("bookings").child(userId).child(bookingId)
                .removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        adapter.removeItem(position);
                        Toast.makeText(getContext(), "Booking Cancelled Successfully",
                                Toast.LENGTH_SHORT).show();

                        if (bookingList.isEmpty()) {
                            tvEmpty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(getContext(), "Cancellation failed. Try again.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
