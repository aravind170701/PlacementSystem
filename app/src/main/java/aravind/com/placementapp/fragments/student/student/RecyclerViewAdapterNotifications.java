package aravind.com.placementapp.fragments.student.student;

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


public class RecyclerViewAdapterNotifications extends RecyclerView.Adapter<RecyclerViewAdapterNotifications.MyViewHolder> {

    private Context context;
    private List<Notification> notificationList;
    private ViewNotificationsFragment fragment;

    public RecyclerViewAdapterNotifications(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    public RecyclerViewAdapterNotifications(List<Notification> notificationList, ViewNotificationsFragment fragment) {
        this.notificationList = notificationList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(fragment.getContext());
        view = mInflater.inflate(R.layout.layout_recent_notification_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(notificationList.get(position).getTitle());
        holder.message.setText(notificationList.get(position).getMessage());
        holder.time.setText("Posted On " + notificationList.get(position).getTimestamp());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
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
