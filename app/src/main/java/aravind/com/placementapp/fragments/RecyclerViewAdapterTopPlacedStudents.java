package aravind.com.placementapp.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import aravind.com.placementapp.R;
import aravind.com.placementapp.pojo.TopPlacedStudents;


public class RecyclerViewAdapterTopPlacedStudents extends RecyclerView.Adapter<RecyclerViewAdapterTopPlacedStudents.MyViewHolder> {

    private Context context;
    private List<TopPlacedStudents> TopPlacedStudentsList;
    private PlacementDashboardFragment fragment;
    FirebaseStorage storage;
    StorageReference storageRef;

    public RecyclerViewAdapterTopPlacedStudents(Context context, List<TopPlacedStudents> TopPlacedStudentsList) {
        this.context = context;
        this.TopPlacedStudentsList = TopPlacedStudentsList;
    }

    public RecyclerViewAdapterTopPlacedStudents(List<TopPlacedStudents> TopPlacedStudentsList, PlacementDashboardFragment fragment) {
        this.TopPlacedStudentsList = TopPlacedStudentsList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(fragment.getContext());
        view = mInflater.inflate(R.layout.layout_top_students_card, parent, false);
        storage = FirebaseStorage.getInstance();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(TopPlacedStudentsList.get(position).getName());
        holder.company.setText(TopPlacedStudentsList.get(position).getCompany());
        holder.salary.setText(TopPlacedStudentsList.get(position).getSalary());
        holder.batch.setText(TopPlacedStudentsList.get(position).getBatch());
        storageRef = storage.getReferenceFromUrl("gs://placement-system-f3649.appspot.com").child(TopPlacedStudentsList.get(position).getName() + ".jpg");

        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.image.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
        }
    }

    @Override
    public int getItemCount() {
        return TopPlacedStudentsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView company;
        private TextView salary;
        private TextView batch;
        private ImageView image;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            company = (TextView) itemView.findViewById(R.id.company);
            salary = (TextView) itemView.findViewById(R.id.salary);
            batch = (TextView) itemView.findViewById(R.id.batch);
            image = (ImageView) itemView.findViewById(R.id.studentImage);
            cardView = (CardView) itemView.findViewById(R.id.card_viewtpo);
        }
    }
}
