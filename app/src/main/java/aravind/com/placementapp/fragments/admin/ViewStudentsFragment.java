package aravind.com.placementapp.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
import aravind.com.placementapp.pojo.Student;

public class ViewStudentsFragment extends Fragment implements ValueEventListener {

    private List<Student> studentslist;
    private ProgressBar loadingBar;
    private DatabaseReference databaseReference;
    private RecyclerView myrv;
    private RecyclerViewAdapterSTUDENT myAdapter;

    public ViewStudentsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentslist = new ArrayList<>();
        databaseReference = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN);
        databaseReference.addListenerForSingleValueEvent(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View view = inflater.inflate(R.layout.fragment_viewstudents, container, false);
        myrv = view.findViewById(R.id.viewstudents_recycler_view);
        myrv.setVisibility(View.GONE);
        loadingBar = view.findViewById(R.id.loadingBar1);
        loadingBar.setVisibility(View.VISIBLE);
        myAdapter = new RecyclerViewAdapterSTUDENT(studentslist, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        myrv.setLayoutManager(gridLayoutManager);
        return view;
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot != null) {
            Student student;
            for (DataSnapshot childSnapShot : snapshot.getChildren()) {
                student = childSnapShot.getValue(Student.class);
                if (student != null && student.type == Constants.UserTypes.USER_TYPE_STUDENT) {
                    studentslist.add(new Student(student.id, student.name, student.branch, student.percentage));
                }
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