package com.example.cinefast;

import java.util.List;

public class Booking {
    private String bookingId;
    private String userId;
    private String movieName;
    private List<String> seats;
    private double totalPrice;
    private String dateTime;   // e.g. "13.04.2025 22:15"
    private long timestamp;    // epoch millis for future/past comparison
    private String posterName; // drawable name string

    // Required empty constructor for Firebase deserialization
    public Booking() {}

    public Booking(String bookingId, String userId, String movieName, List<String> seats,
                   double totalPrice, String dateTime, long timestamp, String posterName) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.movieName = movieName;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.dateTime = dateTime;
        this.timestamp = timestamp;
        this.posterName = posterName;
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }

    public List<String> getSeats() { return seats; }
    public void setSeats(List<String> seats) { this.seats = seats; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getPosterName() { return posterName; }
    public void setPosterName(String posterName) { this.posterName = posterName; }

    public int getTicketCount() {
        return seats != null ? seats.size() : 0;
    }
}
