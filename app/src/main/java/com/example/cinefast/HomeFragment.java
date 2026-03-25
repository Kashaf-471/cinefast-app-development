package com.example.cinefast;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set styled app name
        TextView appName = view.findViewById(R.id.appName);
        String text = "CineFAST";
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), 4, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        appName.setText(ss);

        // Setup 3-dot menu
        ImageView btnMenu = view.findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> showPopupMenu(v));

        // Setup TabLayout + ViewPager2
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        HomePagerAdapter pagerAdapter = new HomePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText(R.string.now_showing);
            } else {
                tab.setText(R.string.coming_soon);
            }
        }).attach();
    }

    private void showPopupMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), anchor);
        popupMenu.getMenu().add(getString(R.string.view_last_booking));

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().toString().equals(getString(R.string.view_last_booking))) {
                showLastBooking();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void showLastBooking() {

        SharedPreferences prefs = requireContext()
                .getSharedPreferences("CinefastBooking", requireContext().MODE_PRIVATE);

        String movieName = prefs.getString("last_movie_name", null);
        int seats = prefs.getInt("last_seats_count", -1);
        float totalPrice = prefs.getFloat("last_total_price", -1f);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        if (movieName == null || seats == -1) {
            builder.setTitle(getString(R.string.last_booking));
            builder.setMessage(getString(R.string.no_previous_booking));
        } else {
            builder.setTitle(getString(R.string.last_booking));
            builder.setMessage("Movie: " + movieName + "\n"
                    + "Seats: " + seats + "\n"
                    + "Total Price: $" + String.format("%.2f", totalPrice));
        }

        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
