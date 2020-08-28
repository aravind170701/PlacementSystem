package aravind.com.placementapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import aravind.com.placementapp.R;
import aravind.com.placementapp.constants.Constants;
import aravind.com.placementapp.helper.FirebaseHelper;
import aravind.com.placementapp.pojo.Company;

public class CompanyActivity extends AppCompatActivity implements ValueEventListener {

    private ProgressBar progressBar;
    private TextView name;
    private TextView description;
    private TextView skills;
    private TextView eligibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String idpath;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                idpath = null;
            } else {
                idpath = extras.getString("id");
            }
        } else {
            idpath = (String) savedInstanceState.getSerializable("id");
        }
        progressBar = findViewById(R.id.progressBar);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        skills = findViewById(R.id.skills);
        eligibility = findViewById(R.id.eligibility);

        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
        DatabaseReference d;
        d = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_COMPANY + "/" + idpath);
        d.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Company c;
        c = snapshot.getValue(Company.class);
        progressBar.setVisibility(View.GONE);
        name.setText(c.getCompanyname());
        description.setText(c.getCompanydescription());
        skills.setText(c.getCompanyskills());
        eligibility.setText(c.getCompanyeligibility());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}