package com.example.cinefast;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MovieJsonParser {

    /**
     * Reads movies.json from the assets folder and parses it into a list of Movie objects.
     * Each movie's poster resource ID is resolved from the drawable name string.
     */
    public static ArrayList<Movie> parseMovies(Context context) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            // Read assets/movies.json
            InputStream is = context.getAssets().open("movies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject root = new JSONObject(json);
            JSONArray moviesArray = root.getJSONArray("movies");

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject obj = moviesArray.getJSONObject(i);

                String name = obj.getString("name");
                String genre = obj.getString("genre");
                String posterName = obj.getString("poster");
                String trailerUrl = obj.getString("trailerUrl");
                boolean isNowShowing = obj.getBoolean("isNowShowing");
                
                // Read date and time from JSON
                String date = obj.optString("date", "13.04.2025");
                String time = obj.optString("time", "22:15");

                // Resolve drawable resource ID from name string
                int resId = context.getResources().getIdentifier(
                        posterName, "drawable", context.getPackageName());
                if (resId == 0) {
                    resId = R.drawable.placeholder; // Fallback if not found
                }

                Movie movie = new Movie(name, genre, posterName, trailerUrl, isNowShowing, resId);
                movie.setDate(date);
                movie.setTime(time);
                movies.add(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
}
