package aravind.com.placementapp.fragments.admin;

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
import aravind.com.placementapp.pojo.TpoInfo;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<TpoInfo> tpolist;
    private ViewTPOFragment fragment;

    public RecyclerViewAdapter(Context context, List<TpoInfo> tpolist) {
        this.context = context;
        this.tpolist = tpolist;
    }

    public RecyclerViewAdapter(List<TpoInfo> tpolist, ViewTPOFragment fragment) {
        this.tpolist = tpolist;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(fragment.getContext());
        view = mInflater.inflate(R.layout.layout_tpo_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tpoid.setText(tpolist.get(position).getTpoid());
        holder.tponame.setText(tpolist.get(position).getTponame());
    }

    @Override
    public int getItemCount() {
        return tpolist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tpoid;
        private TextView tponame;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tpoid = (TextView) itemView.findViewById(R.id.tpoid);
            tponame = (TextView) itemView.findViewById(R.id.tponame);
            cardView = (CardView) itemView.findViewById(R.id.card_viewtpo);
        }
    }
}
