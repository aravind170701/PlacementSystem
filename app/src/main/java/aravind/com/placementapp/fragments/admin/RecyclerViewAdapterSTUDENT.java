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
import aravind.com.placementapp.pojo.Student;

public class RecyclerViewAdapterSTUDENT extends RecyclerView.Adapter<RecyclerViewAdapterSTUDENT.MyViewHolder> {

    private Context context;
    private List<Student> studentList;
    private ViewStudentsFragment fragment;

    public RecyclerViewAdapterSTUDENT(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    public RecyclerViewAdapterSTUDENT(List<Student> studentList, ViewStudentsFragment fragment) {
        this.studentList = studentList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(fragment.getContext());
        view = mInflater.inflate(R.layout.layout_students_card, parent, false);
        view.setBackgroundResource(R.drawable.rounded_background);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.studentid.setText(studentList.get(position).getId());
        holder.studentname.setText(studentList.get(position).getName());
        holder.studentbranch.setText(studentList.get(position).getBranch());
        holder.studentpercentage.setText(String.valueOf(studentList.get(position).getPercentage()));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView studentid;
        private TextView studentname;
        private TextView studentbranch;
        private TextView studentpercentage;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            studentid = (TextView) itemView.findViewById(R.id.studentid);
            studentname = (TextView) itemView.findViewById(R.id.studentname);
            studentbranch = (TextView) itemView.findViewById(R.id.studentbranch);
            studentpercentage = (TextView) itemView.findViewById(R.id.studentpercentage);
            cardView = (CardView) itemView.findViewById(R.id.card_viewstudents);
        }
    }
}
