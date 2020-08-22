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
import aravind.com.placementapp.pojo.User;
import aravind.com.placementapp.utils.StringUtils;

public class AddTPOFragment extends Fragment implements View.OnClickListener {

    private EditText userIdView;
    private EditText nameView;
    private EditText passwordView;
    private ProgressBar loadingBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.fragment_addtpo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);
        userIdView = fragmentView.findViewById(R.id.addTpoId);
        nameView = fragmentView.findViewById(R.id.addTpoName);
        passwordView = fragmentView.findViewById(R.id.password);
        loadingBar = fragmentView.findViewById(R.id.loading);
        loadingBar.setVisibility(View.GONE);

        Button addtpobutton = fragmentView.findViewById(R.id.action_tpo_add);
        Button resetbutton = fragmentView.findViewById(R.id.action_tpo_reset);
        if (addtpobutton != null)
            addtpobutton.setOnClickListener(this);
        if (resetbutton != null) {
            resetbutton.setOnClickListener(v -> {
                if (userIdView != null)
                    userIdView.setText("");
                if (nameView != null)
                    nameView.setText("");
                if (passwordView != null)
                    passwordView.setText("");
            });
        }
    }

    @Override
    public void onClick(View v) {
        loadingBar.setVisibility(View.VISIBLE);
        if (userIdView != null && nameView != null && passwordView != null) {
            String userId = userIdView.getText().toString();
            String name = nameView.getText().toString();
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
            createTPOUser(userId, name, password);
        }
    }

    private void createTPOUser(String userId, String name, String password) {
        DatabaseReference d;
        User u = new User(userId, name, password, Constants.UserTypes.USER_TYPE_TPO);
        d = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN + "/" + userId);
        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (checkIfUserAlreadyRegistered(dataSnapshot)) {
                    loadingBar.setVisibility(View.GONE);
                    Toast.makeText(getInstance().getActivity(), "User already registered", Toast.LENGTH_LONG).show();
                } else {
                    d.setValue(u);
                    loadingBar.setVisibility(View.GONE);
                    Toast.makeText(getInstance().getActivity(), "TPO Added Successfully", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private AddTPOFragment getInstance() {
        return this;
    }

    private boolean checkIfUserAlreadyRegistered(DataSnapshot dataSnapshot) {
        if (dataSnapshot == null) {
            return false;
        }
        User alreadyRegisteredUser = dataSnapshot.getValue(User.class);
        return alreadyRegisteredUser != null;
    }
}