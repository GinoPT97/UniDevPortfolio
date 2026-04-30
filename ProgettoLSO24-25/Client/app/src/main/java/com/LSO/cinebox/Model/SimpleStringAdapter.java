package com.LSO.cinebox.Model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.LSO.cinebox.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SimpleStringAdapter extends RecyclerView.Adapter<SimpleStringAdapter.ViewHolder> {
    private List<String> data;
    private final OnDeleteClickListener onDeleteClickListener;
    private final List<Integer> textColors = new ArrayList<>();

    public SimpleStringAdapter(List<String> data, OnDeleteClickListener onDeleteClickListener) {
        this.data = data;
        this.onDeleteClickListener = onDeleteClickListener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_string, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
        int colorRes = R.color.primary;
        if (position < textColors.size()) colorRes = textColors.get(position);
        holder.textView.setTextColor(ContextCompat.getColor(holder.textView.getContext(), colorRes));
        if (onDeleteClickListener != null) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(data.get(position)));
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return data.size(); }

    @Override
    public long getItemId(int position) { return data.get(position).hashCode(); }

    public void updateData(List<String> newData) {
        this.data = newData;
        textColors.clear();
        if (newData == null) return;
        for (String item : newData) {
            String[] parts = item.split(" - ");
            int color = R.color.primary;
            if (parts.length > 3) {
                String dateStr = parts[3].trim();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SS", Locale.getDefault());
                try {
                    Date returnDate = sdf.parse(dateStr);
                    if (returnDate != null && returnDate.before(new Date())) color = R.color.red;
                } catch (ParseException ignored) {
                    // Keep the default color when the date cannot be parsed.
                }
            }
            textColors.add(color);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final Button deleteButton;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
            deleteButton = view.findViewById(R.id.deleteButton);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(String rentalId);
    }
}
