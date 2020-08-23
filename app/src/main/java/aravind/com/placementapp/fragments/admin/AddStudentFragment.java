package aravind.com.placementapp.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import aravind.com.placementapp.R;
import aravind.com.placementapp.constants.Constants;
import aravind.com.placementapp.helper.FirebaseHelper;
import aravind.com.placementapp.pojo.Student;
import aravind.com.placementapp.utils.StringUtils;

public class AddStudentFragment extends Fragment implements View.OnClickListener {

    private EditText userIdView;
    private EditText nameView;
    private EditText passwordView;
    private EditText branchView;
    private EditText percentageView;
    private ProgressBar loadingBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.fragment_addstudent, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);
        userIdView = fragmentView.findViewById(R.id.addStudentId);
        nameView = fragmentView.findViewById(R.id.addStudentName);
        branchView = fragmentView.findViewById(R.id.addStudentBranch);
        percentageView = fragmentView.findViewById(R.id.addStudentPercentage);
        passwordView = fragmentView.findViewById(R.id.password);
        loadingBar = fragmentView.findViewById(R.id.loading);
        loadingBar.setVisibility(View.GONE);

        Button addstudentbutton = fragmentView.findViewById(R.id.action_student_add);
        Button resetbutton = fragmentView.findViewById(R.id.action_student_reset);
        if (addstudentbutton != null)
            addstudentbutton.setOnClickListener(this);
        if (resetbutton != null) {
            resetbutton.setOnClickListener(v -> {
                if (userIdView != null)
                    userIdView.setText("");
                if (nameView != null)
                    nameView.setText("");
                if (branchView != null)
                    branchView.setText("");
                if (percentageView != null)
                    percentageView.setText("");
                if (passwordView != null)
                    passwordView.setText("");
            });
        }
    }

    @Override
    public void onClick(View v) {
        loadingBar.setVisibility(View.VISIBLE);
        if (userIdView != null && nameView != null && passwordView != null && branchView != null && percentageView != null) {
            String userId = userIdView.getText().toString();
            String name = nameView.getText().toString();
            String branch = branchView.getText().toString();
            float percentage = Float.parseFloat(percentageView.getText().toString());
            String password = StringUtils.isBlank(passwordView.getText().toString()) ? Constants.DEFAULT_PASSWORD : passwordView.getText().toString();
            if (StringUtils.isBlank(userId)) {
                userIdView.setError("Field Cannot be Blank");
                loadingBar.setVisibility(View.GONE);
                return;
            }
            if (StringUtils.isBlank(name)) {
                nameView.setError("Field Cannot be Blank");
                loadingBar.setVisibility(View.GONE);
                return;
            }
            if (StringUtils.isBlank(branch)) {
                nameView.setError("Field Cannot be Blank");
                loadingBar.setVisibility(View.GONE);
                return;
            }
            if (StringUtils.isempty(percentage)) {
                nameView.setError("Field Cannot be Blank");
                loadingBar.setVisibility(View.GONE);
                return;
            }
            createStudentUser(userId, name, password, branch, percentage);
        }
    }

    private void createStudentUser(String userId, String name, String password, String branch, float percentage) {
        DatabaseReference d;
        Student s = new Student(userId, name, password, Constants.UserTypes.USER_TYPE_STUDENT, branch, percentage);
        d = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN + "/" + userId);
        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (checkIfUserAlreadyRegistered(dataSnapshot)) {
                    loadingBar.setVisibility(View.GONE);
                    Toast.makeText(getInstance().getActivity(), "User already registered", Toast.LENGTH_LONG).show();
                } else {
                    d.setValue(s);
                    loadingBar.setVisibility(View.GONE);
                    Toast.makeText(getInstance().getActivity(), "Student Added Successfully", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private AddStudentFragment getInstance() {
        return this;
    }

    private boolean checkIfUserAlreadyRegistered(DataSnapshot dataSnapshot) {
        if (dataSnapshot == null) {
            return false;
        }
        Student alreadyRegisteredUser = dataSnapshot.getValue(Student.class);
        return alreadyRegisteredUser != null;
    }
}