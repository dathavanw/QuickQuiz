package com.example.quickquizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<NotificationItem> notifications;

    public NotificationAdapter(List<NotificationItem> notifications) {
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
        NotificationItem item = notifications.get(position);
        holder.imgIcon.setImageResource(item.getIconResId());
        holder.tvDateTime.setText(item.getDateTime());
        holder.tvMessage.setText(item.getMessage());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvDateTime, tvMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}
