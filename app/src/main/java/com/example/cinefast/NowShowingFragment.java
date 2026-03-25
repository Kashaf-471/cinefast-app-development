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

        movieList = new ArrayList<>();
        movieList.add(new Movie("The Dark Knight", "Action | 152 min",
                R.drawable.dark_knight,
                "https://www.youtube.com/watch?v=EXeTwQWrcwY", true));
        movieList.add(new Movie("Inception", "Sci-Fi | 148 min",
                R.drawable.inception,
                "https://www.youtube.com/watch?v=YoHD9XEInc0", true));
        movieList.add(new Movie("Interstellar", "Sci-Fi | 169 min",
                R.drawable.interstellar,
                "https://www.youtube.com/watch?v=zSWdZVtXT7E", true));

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
