package com.example.cinefast;

public class Movie {
    private String name;
    private String genre;
    private int posterResId;
    private String trailerUrl;
    private boolean isNowShowing;
    // Field for JSON-based loading (drawable name string, e.g. "dark_knight")
    private String posterName;
    private String date; // e.g. "13.04.2025"
    private String time; // e.g. "22:15"

    public Movie(String name, String genre, int posterResId, String trailerUrl, boolean isNowShowing) {
        this.name = name;
        this.genre = genre;
        this.posterResId = posterResId;
        this.trailerUrl = trailerUrl;
        this.isNowShowing = isNowShowing;
        this.posterName = "";
        this.date = "13.04.2025"; // Default date
        this.time = "22:15";       // Default time
    }

    public Movie(String name, String genre, String posterName, String trailerUrl, boolean isNowShowing, int posterResId) {
        this.name = name;
        this.genre = genre;
        this.posterName = posterName;
        this.trailerUrl = trailerUrl;
        this.isNowShowing = isNowShowing;
        this.posterResId = posterResId;
        this.date = "13.04.2025"; // Default date
        this.time = "22:15";       // Default time
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getPosterResId() { return posterResId; }
    public void setPosterResId(int posterResId) { this.posterResId = posterResId; }

    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }

    public boolean isNowShowing() { return isNowShowing; }
    public void setNowShowing(boolean nowShowing) { isNowShowing = nowShowing; }

    public String getPosterName() { return posterName; }
    public void setPosterName(String posterName) { this.posterName = posterName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
