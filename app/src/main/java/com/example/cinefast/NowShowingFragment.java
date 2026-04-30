package com.example.cinefast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NowShowingFragment extends Fragment implements MovieAdapter.OnMovieClickListener {

    private ArrayList<Movie> movieList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_now_showing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Parse movies from JSON (assets/movies.json) — NOT hardcoded
        ArrayList<Movie> allMovies = MovieJsonParser.parseMovies(requireContext());

        // Filter: only Now Showing
        movieList = new ArrayList<>();
        for (Movie m : allMovies) {
            if (m.isNowShowing()) {
                movieList.add(m);
            }
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewMovies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MovieAdapter adapter = new MovieAdapter(movieList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBookSeatsClick(Movie movie) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToSeatSelection(movie);
        }
    }
}
