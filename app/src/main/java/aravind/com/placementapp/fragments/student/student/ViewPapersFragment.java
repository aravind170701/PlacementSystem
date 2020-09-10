package aravind.com.placementapp.fragments.student.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import aravind.com.placementapp.R;
import aravind.com.placementapp.constants.Constants;
import aravind.com.placementapp.helper.FirebaseHelper;
import aravind.com.placementapp.pojo.Upload;

public class ViewPapersFragment extends Fragment implements ValueEventListener {

    private List<Upload> uploadList;
    private ProgressBar loadingBar;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private RecyclerView myrv;
    private RecyclerViewAdapterSTUDENT myAdapter;

    public ViewPapersFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uploadList = new ArrayList<>();
        databaseReference = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.DATABASE_PATH_UPLOADS);
        databaseReference.addListenerForSingleValueEvent(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View view = inflater.inflate(R.layout.fragment_viewpapers, container, false);
        myrv = view.findViewById(R.id.viewpapers_recycler_view);
        myrv.setVisibility(View.GONE);
        loadingBar = view.findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.VISIBLE);
        myAdapter = new RecyclerViewAdapterSTUDENT(uploadList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        myrv.setLayoutManager(gridLayoutManager);
        return view;
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot != null) {
            Upload upload;
            for (DataSnapshot childSnapShot : snapshot.getChildren()) {
                upload = childSnapShot.getValue(Upload.class);
                if (upload != null)
                    uploadList.add(new Upload(upload.getName(), upload.getUrl()));
            }
            if (loadingBar != null) {
                loadingBar.setVisibility(View.GONE);
            }
            if (myrv != null && myAdapter != null) {
                myrv.setVisibility(View.VISIBLE);
                myrv.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

}