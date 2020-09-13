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
import aravind.com.placementapp.pojo.Recruiter;


public class RecyclerViewAdapterTopRecruiters extends RecyclerView.Adapter<RecyclerViewAdapterTopRecruiters.MyViewHolder> {

    private Context context;
    private List<Recruiter> recruiterList;
    private PlacementDashboardFragment fragment;
    FirebaseStorage storage;
    StorageReference storageRef;

    public RecyclerViewAdapterTopRecruiters(Context context, List<Recruiter> recruiterList) {
        this.context = context;
        this.recruiterList = recruiterList;
    }

    public RecyclerViewAdapterTopRecruiters(List<Recruiter> recruiterList, PlacementDashboardFragment fragment) {
        this.recruiterList = recruiterList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(fragment.getContext());
        view = mInflater.inflate(R.layout.layout_top_recruiters_card, parent, false);
        storage = FirebaseStorage.getInstance();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(recruiterList.get(position).getName());
        storageRef = storage.getReferenceFromUrl("gs://placement-system-f3649.appspot.com").child(recruiterList.get(position).getName() + ".jpg");

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
        return recruiterList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView image;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.companyName);
            image = (ImageView) itemView.findViewById(R.id.companyImage);
            cardView = (CardView) itemView.findViewById(R.id.card_viewtpo);
        }
    }
}
