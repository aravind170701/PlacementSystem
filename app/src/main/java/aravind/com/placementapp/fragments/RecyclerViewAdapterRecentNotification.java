package aravind.com.placementapp.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import aravind.com.placementapp.R;
import aravind.com.placementapp.pojo.Notification;


public class RecyclerViewAdapterRecentNotification extends RecyclerView.Adapter<RecyclerViewAdapterRecentNotification.MyViewHolder> {

    private Context context;
    private List<Notification> recentNotificationList;
    private PlacementDashboardFragment fragment;

    public RecyclerViewAdapterRecentNotification(Context context, List<Notification> recentNotificationList) {
        this.context = context;
        this.recentNotificationList = recentNotificationList;
    }

    public RecyclerViewAdapterRecentNotification(List<Notification> recentNotificationList, PlacementDashboardFragment fragment) {
        this.recentNotificationList = recentNotificationList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(fragment.getContext());
        view = mInflater.inflate(R.layout.layout_recent_notification_card, parent, false);
        view.setBackgroundResource(R.drawable.smartdp);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(recentNotificationList.get(position).getTitle());
        holder.message.setText(recentNotificationList.get(position).getMessage());
        holder.time.setText("Posted On " + recentNotificationList.get(position).getTimestamp());
    }

    @Override
    public int getItemCount() {
        return recentNotificationList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView message;
        private TextView time;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            message = (TextView) itemView.findViewById(R.id.message);
            time = (TextView) itemView.findViewById(R.id.time);
            cardView = (CardView) itemView.findViewById(R.id.card_viewtpo);
        }
    }
}
