package com.LSO.cinebox.Model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.LSO.cinebox.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<String> notifications;

    public NotificationAdapter(List<String> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String notification = notifications.get(position);
        // Atteso formato: messaggio\nDATA
        String[] parts = notification.split("\n");
        String message = parts.length > 0 ? parts[0] : "";
        String date = parts.length > 1 ? parts[1] : "";

        holder.notificationTextView.setText(message);
        holder.notificationDateView.setText(date);

        // Mostra badge "NUOVO" se la data è oggi
        if (date != null && date.equals(android.text.format.DateFormat.format("dd/MM/yyyy", new java.util.Date()))) {
            holder.notificationBadge.setVisibility(View.VISIBLE);
        } else {
            holder.notificationBadge.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTextView;
        TextView notificationDateView;
        TextView notificationBadge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationTextView = itemView.findViewById(R.id.notificationTextView);
            notificationDateView = itemView.findViewById(R.id.notificationDateView);
            notificationBadge = itemView.findViewById(R.id.notificationBadge);
        }
    }
}
