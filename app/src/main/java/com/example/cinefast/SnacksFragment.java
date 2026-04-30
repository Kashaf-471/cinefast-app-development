package com.example.cinefast;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SnacksFragment extends Fragment {

    private ArrayList<Snack> snackList;
    private String movieName;
    private int seatsCount;
    private double ticketTotal;
    private ArrayList<String> seatNumbers;
    private String rating, language, movieGenre, theater, hall, date, time, posterName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_snacks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            movieName    = args.getString("MOVIE_NAME");
            seatsCount   = args.getInt("SEATS_COUNT", 0);
            ticketTotal  = args.getDouble("TICKET_TOTAL", 0);
            seatNumbers  = args.getStringArrayList("SEAT_NUMBERS");
            rating       = args.getString("RATING");
            language     = args.getString("LANGUAGE");
            movieGenre   = args.getString("MOVIE_GENRE");
            theater      = args.getString("THEATER");
            hall         = args.getString("HALL");
            date         = args.getString("DATE");
            time         = args.getString("TIME");
            posterName   = args.getString("POSTER_NAME", "");
        }

        // Styled app name header
        TextView appName = view.findViewById(R.id.appName);
        SpannableString spannableString = new SpannableString("CineFAST");
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 4, 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 4, 8, 0);
        appName.setText(spannableString);

        // Load snacks from SQLite database — NOT hardcoded
        SnackDatabaseHelper dbHelper = new SnackDatabaseHelper(requireContext());
        snackList = dbHelper.getAllSnacks();

        ListView lvSnacks = view.findViewById(R.id.lvSnacks);
        SnackAdapter adapter = new SnackAdapter(requireContext(), snackList);
        lvSnacks.setAdapter(adapter);

        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> {
            double snacksTotal = 0;
            for (Snack snack : snackList) {
                snacksTotal += snack.getQuantity() * snack.getPrice();
            }

            double totalAmount = ticketTotal + snacksTotal;

            Bundle data = new Bundle();
            data.putStringArrayList("SEAT_NUMBERS", seatNumbers);
            data.putDouble("TICKET_TOTAL", ticketTotal);
            data.putDouble("SNACKS_TOTAL", snacksTotal);
            data.putDouble("TOTAL_AMOUNT", totalAmount);
            data.putString("MOVIE_NAME", movieName);
            data.putString("RATING", rating);
            data.putString("LANGUAGE", language);
            data.putString("MOVIE_GENRE", movieGenre);
            data.putString("THEATER", theater);
            data.putString("HALL", hall);
            data.putString("DATE", date);
            data.putString("TIME", time);
            data.putString("POSTER_NAME", posterName);

            // Pass individual snack quantities by index (safe since DB order is consistent)
            for (int i = 0; i < snackList.size(); i++) {
                data.putInt("QUANTITY_" + i, snackList.get(i).getQuantity());
                data.putString("SNACK_NAME_" + i, snackList.get(i).getName());
                data.putDouble("SNACK_PRICE_" + i, snackList.get(i).getPrice());
            }
            data.putInt("SNACK_COUNT", snackList.size());

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTicketSummary(data);
            }
        });
    }
}
