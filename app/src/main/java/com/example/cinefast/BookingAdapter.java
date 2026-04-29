package com.example.cinefast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    public interface OnCancelClickListener {
        void onCancelClick(Booking booking, int position);
    }

    private ArrayList<Booking> bookingList;
    private OnCancelClickListener cancelListener;
    private Context context;

    public BookingAdapter(Context context, ArrayList<Booking> bookingList,
                          OnCancelClickListener cancelListener) {
        this.context = context;
        this.bookingList = bookingList;
        this.cancelListener = cancelListener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvMovieName.setText(booking.getMovieName());
        holder.tvDateTime.setText(booking.getDateTime());
        int ticketCount = booking.getTicketCount();
        holder.tvTickets.setText(ticketCount + (ticketCount == 1 ? " Ticket" : " Tickets"));

        // Resolve poster drawable from name
        String posterName = booking.getPosterName();
        if (posterName != null && !posterName.isEmpty()) {
            int resId = context.getResources().getIdentifier(
                    posterName, "drawable", context.getPackageName());
            if (resId != 0) {
                holder.ivPoster.setImageResource(resId);
            } else {
                holder.ivPoster.setImageResource(R.drawable.placeholder);
            }
        } else {
            // Fallback: guess from movie name
            String name = booking.getMovieName() != null ? booking.getMovieName() : "";
            if (name.contains("Dark Knight"))   holder.ivPoster.setImageResource(R.drawable.dark_knight);
            else if (name.contains("Inception")) holder.ivPoster.setImageResource(R.drawable.inception);
            else if (name.contains("Interstellar")) holder.ivPoster.setImageResource(R.drawable.interstellar);
            else if (name.contains("Shawshank")) holder.ivPoster.setImageResource(R.drawable.shawshank);
            else holder.ivPoster.setImageResource(R.drawable.placeholder);
        }

        // Cancel button on the right
        holder.btnCancel.setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.onCancelClick(booking, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < bookingList.size()) {
            bookingList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, bookingList.size());
        }
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvMovieName, tvDateTime, tvTickets;
        ImageView btnCancel;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster     = itemView.findViewById(R.id.ivPoster);
            tvMovieName  = itemView.findViewById(R.id.tvMovieName);
            tvDateTime   = itemView.findViewById(R.id.tvDateTime);
            tvTickets    = itemView.findViewById(R.id.tvTickets);
            btnCancel    = itemView.findViewById(R.id.btnCancel);
        }
    }
}
