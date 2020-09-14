package aravind.com.placementapp.fragments.tpo.tpo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import aravind.com.placementapp.R;
import aravind.com.placementapp.activities.CompanyActivity;
import aravind.com.placementapp.activities.MyBounceInterpolator;
import aravind.com.placementapp.pojo.Company;

public class RecyclerViewAdapterCOMPANY extends RecyclerView.Adapter<RecyclerViewAdapterCOMPANY.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private List<Company> companyList;
    private ViewCompanyFragment fragment;

    public RecyclerViewAdapterCOMPANY(Context context, List<Company> companyList) {
        this.context = context;
        this.companyList = companyList;
    }

    public RecyclerViewAdapterCOMPANY(List<Company> companyList, ViewCompanyFragment fragment) {
        this.companyList = companyList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(fragment.getContext());
        view = mInflater.inflate(R.layout.layout_viewcompany_card, parent, false);
        view.setBackgroundResource(R.drawable.smartdp);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(fragment.getContext(), R.anim.fade_scale_animation));
        holder.companyname.setText(companyList.get(position).getCompanyname());
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    @Override
    public void onClick(View v) {
        Animation myAnim = AnimationUtils.loadAnimation(fragment.getContext(), R.anim.bounce_animation);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
        myAnim.setInterpolator(interpolator);
        v.startAnimation(myAnim);
        int pos = (int) v.getTag();
        new Handler().postDelayed(() -> {

            Intent i = new Intent(v.getContext(), CompanyActivity.class);
            i.putExtra("id", companyList.get(pos).getCompanyid());
            v.getContext().startActivity(i);
        }, 1000);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView companyname;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            companyname = itemView.findViewById(R.id.companynameview);
            cardView = itemView.findViewById(R.id.card_viewcompany);
        }
    }
}
