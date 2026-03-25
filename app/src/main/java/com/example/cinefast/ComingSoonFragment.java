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

public class ComingSoonFragment extends Fragment implements MovieAdapter.OnMovieClickListener {

    private ArrayList<Movie> movieList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coming_soon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieList = new ArrayList<>();
        movieList.add(new Movie("The Shawshank Redemption", "Drama | 142 min",
                R.drawable.shawshank,
                "https://www.youtube.com/watch?v=PLl99DlL6b4", false));
        movieList.add(new Movie("The Dark Knight Rises", "Action | 164 min",
                R.drawable.dark_knight,
                "https://www.youtube.com/watch?v=g8evyE9TuYk", false));
        movieList.add(new Movie("Inception 2", "Sci-Fi | TBA",
                R.drawable.inception,
                "https://www.youtube.com/watch?v=YoHD9XEInc0", false));

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
