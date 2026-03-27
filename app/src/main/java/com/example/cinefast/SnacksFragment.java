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
    private String rating, language, movieGenre, theater, hall, date, time;

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
            movieName = args.getString("MOVIE_NAME");
            seatsCount = args.getInt("SEATS_COUNT", 0);
            ticketTotal = args.getDouble("TICKET_TOTAL", 0);
            seatNumbers = args.getStringArrayList("SEAT_NUMBERS");
            rating = args.getString("RATING");
            language = args.getString("LANGUAGE");
            movieGenre = args.getString("MOVIE_GENRE");
            theater = args.getString("THEATER");
            hall = args.getString("HALL");
            date = args.getString("DATE");
            time = args.getString("TIME");
        }


        TextView appName = view.findViewById(R.id.appName);
        SpannableString spannableString = new SpannableString("CineFAST");
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 4, 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 4, 8, 0);
        appName.setText(spannableString);


        snackList = new ArrayList<>();
        snackList.add(new Snack("Popcorn", "Large / Buttered", 8.99, R.drawable.placeholder));
        snackList.add(new Snack("Nachos", "With Cheese Dip", 7.99, R.drawable.nachos));
        snackList.add(new Snack("Soft Drink", "Large / Any Flavor", 5.99, R.drawable.drinks));
        snackList.add(new Snack("Candy Mix", "Assorted Candies", 6.99, R.drawable.candy));


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

            // Snack quantities
            data.putInt("QUANTITY_POPCORN", snackList.get(0).getQuantity());
            data.putInt("QUANTITY_NACHOS", snackList.get(1).getQuantity());
            data.putInt("QUANTITY_SOFT_DRINK", snackList.get(2).getQuantity());
            data.putInt("QUANTITY_CANDY_MIX", snackList.get(3).getQuantity());

            // Movie and theater info
            data.putString("MOVIE_NAME", movieName);
            data.putString("RATING", rating);
            data.putString("LANGUAGE", language);
            data.putString("MOVIE_GENRE", movieGenre);
            data.putString("THEATER", theater);
            data.putString("HALL", hall);
            data.putString("DATE", date);
            data.putString("TIME", time);

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTicketSummary(data);
            }
        });
    }
}
