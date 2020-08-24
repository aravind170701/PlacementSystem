package aravind.com.placementapp.fragments.tpo.tpo;

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
import aravind.com.placementapp.pojo.Company;
import aravind.com.placementapp.utils.StringUtils;

public class AddCompanyFragment extends Fragment implements View.OnClickListener {

    private EditText companyid;
    private EditText companyname;
    private EditText companydescription;
    private EditText companyskills;
    private EditText companyeligibility;
    private ProgressBar loadingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.fragment_addcompany, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);
        companyid = fragmentView.findViewById(R.id.companyid);
        companyname = fragmentView.findViewById(R.id.companyname);
        companydescription = fragmentView.findViewById(R.id.companydescription);
        companyskills = fragmentView.findViewById(R.id.companyskills);
        companyeligibility = fragmentView.findViewById(R.id.companyeligibility);
        loadingBar = fragmentView.findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.GONE);

        Button addcompanybutton = fragmentView.findViewById(R.id.action_add_company);
        Button resetbutton = fragmentView.findViewById(R.id.action_reset);
        if (addcompanybutton != null)
            addcompanybutton.setOnClickListener(this);
        if (resetbutton != null) {
            resetbutton.setOnClickListener(v -> {
                if (companyid != null)
                    companyid.setText("");
                if (companyname != null)
                    companyname.setText("");
                if (companydescription != null)
                    companydescription.setText("");
                if (companyskills != null)
                    companyskills.setText("");
                if (companyeligibility != null)
                    companyeligibility.setText("");
            });
        }
    }

    @Override
    public void onClick(View v) {
        loadingBar.setVisibility(View.VISIBLE);
        if (companyid != null && companyname != null && companydescription != null && companyskills != null && companyeligibility != null) {
            String id = companyid.getText().toString();
            String name = companyname.getText().toString();
            String description = companydescription.getText().toString();
            String skills = companyskills.getText().toString();
            String eligibility = companyeligibility.getText().toString();
            if (StringUtils.isBlank(id)) {
                companyname.setError("Field Cannot be Blank");
                loadingBar.setVisibility(View.GONE);
                return;
            }
            if (StringUtils.isBlank(name)) {
                companyname.setError("Field Cannot be Blank");
                loadingBar.setVisibility(View.GONE);
                return;
            }
            if (StringUtils.isBlank(description)) {
                companydescription.setError("Field Cannot be Blank");
                loadingBar.setVisibility(View.GONE);
                return;
            }
            if (StringUtils.isBlank(skills)) {
                companyskills.setError("Field Cannot be Blank");
                loadingBar.setVisibility(View.GONE);
                return;
            }
            if (StringUtils.isBlank(eligibility)) {
                companyeligibility.setError("Field Cannot be Blank");
                loadingBar.setVisibility(View.GONE);
                return;
            }
            createStudentUser(id, name, description, skills, eligibility);
        }
    }

    private void createStudentUser(String id, String name, String description, String skills, String eligibility) {
        DatabaseReference d;
        Company c = new Company(id, name, description, skills, eligibility);
        d = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_COMPANY + "/" + id);
        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (checkIfUserAlreadyRegistered(dataSnapshot)) {
                    loadingBar.setVisibility(View.GONE);
                    Toast.makeText(getInstance().getActivity(), "Company already registered", Toast.LENGTH_LONG).show();
                } else {
                    d.setValue(c);
                    loadingBar.setVisibility(View.GONE);
                    Toast.makeText(getInstance().getActivity(), "Company Added Successfully", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private AddCompanyFragment getInstance() {
        return this;
    }

    private boolean checkIfUserAlreadyRegistered(DataSnapshot dataSnapshot) {
        if (dataSnapshot == null) {
            return false;
        }
        Company alreadyRegisteredUser = dataSnapshot.getValue(Company.class);
        return alreadyRegisteredUser != null;
    }
}