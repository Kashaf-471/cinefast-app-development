package com.example.cinefast;

public class Movie {
    private String name;
    private String genre;
    private int posterResId;
    private String trailerUrl;
    private boolean isNowShowing;

    public Movie(String name, String genre, int posterResId, String trailerUrl, boolean isNowShowing) {
        this.name = name;
        this.genre = genre;
        this.posterResId = posterResId;
        this.trailerUrl = trailerUrl;
        this.isNowShowing = isNowShowing;
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
}
