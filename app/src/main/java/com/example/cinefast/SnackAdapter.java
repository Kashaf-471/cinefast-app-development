package com.example.cinefast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class SnackAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Snack> snackList;

    public SnackAdapter(Context context, ArrayList<Snack> snackList) {
        this.context = context;
        this.snackList = snackList;
    }

    @Override
    public int getCount() {
        return snackList.size();
    }

    @Override
    public Object getItem(int position) {
        return snackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_snack, parent, false);
            holder = new ViewHolder();
            holder.ivSnackImage = convertView.findViewById(R.id.ivSnackImage);
            holder.tvSnackName = convertView.findViewById(R.id.tvSnackName);
            holder.tvSnackDescription = convertView.findViewById(R.id.tvSnackDescription);
            holder.tvSnackPrice = convertView.findViewById(R.id.tvSnackPrice);
            holder.tvQuantity = convertView.findViewById(R.id.tvQuantity);
            holder.btnMinus = convertView.findViewById(R.id.btnMinus);
            holder.btnPlus = convertView.findViewById(R.id.btnPlus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Snack snack = snackList.get(position);

        holder.ivSnackImage.setImageResource(snack.getImageResId());
        holder.tvSnackName.setText(snack.getName());
        holder.tvSnackDescription.setText(snack.getDescription());
        holder.tvSnackPrice.setText(String.format("$%.2f", snack.getPrice()));
        holder.tvQuantity.setText(String.valueOf(snack.getQuantity()));

        holder.btnMinus.setOnClickListener(v -> {
            if (snack.getQuantity() > 0) {
                snack.setQuantity(snack.getQuantity() - 1);
                holder.tvQuantity.setText(String.valueOf(snack.getQuantity()));
            }
        });

        holder.btnPlus.setOnClickListener(v -> {
            snack.setQuantity(snack.getQuantity() + 1);
            holder.tvQuantity.setText(String.valueOf(snack.getQuantity()));
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView ivSnackImage;
        TextView tvSnackName, tvSnackDescription, tvSnackPrice, tvQuantity;
        MaterialButton btnMinus, btnPlus;
    }
}
