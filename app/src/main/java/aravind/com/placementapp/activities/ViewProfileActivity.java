package aravind.com.placementapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import aravind.com.placementapp.R;
import aravind.com.placementapp.constants.Constants;
import aravind.com.placementapp.helper.FirebaseHelper;
import aravind.com.placementapp.helper.SharedPrefHelper;
import aravind.com.placementapp.pojo.Student;
import aravind.com.placementapp.pojo.Tpo;

public class ViewProfileActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    private EditText id;
    private EditText firstName;
    private EditText lastName;
    private ProgressBar loadingBar;
    private EditText branch;
    private EditText year;
    private EditText designation;
    private Button backButton;
    private Button saveButton;
    private TextView changePassword;
    private String name;
    private String userId;
    private int userType;
    private DatabaseReference d;
    private Student s;
    private Tpo t;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userType = Integer.parseInt(SharedPrefHelper.getEntryFromSharedPrefs(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_USER_TYPE));
        if (userType == Constants.UserTypes.USER_TYPE_TPO) {
            setContentView(R.layout.activity_view_tpo_profile);
            designation = findViewById(R.id.designation);
            type = 1;
        } else {
            setContentView(R.layout.activity_view_student_profile);
            branch = findViewById(R.id.branch);
            year = findViewById(R.id.year);
            type = 2;
        }

        userId = SharedPrefHelper.getEntryFromSharedPrefs(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_USER_ID);
        id = findViewById(R.id.id);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
        changePassword = findViewById(R.id.changePassword);
        loadingBar = findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        changePassword.setOnClickListener(this);

        d = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN + "/" + userId);
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    String temp_id = null;
                    String temp_name = null;
                    if (type == Constants.UserTypes.USER_TYPE_TPO) {
                        t = snapshot.getValue(Tpo.class);
                        if (t != null) {
                            temp_id = t.getId();
                            temp_name = t.getName();
                            if (t.getDesignation() != null)
                                designation.setText(t.getDesignation());
                        }
                    } else {
                        s = snapshot.getValue(Student.class);
                        if (s != null) {
                            temp_id = s.getId();
                            temp_name = s.getName();
                            branch.setText(s.getBranch());
                            if (s.getYear() != null)
                                year.setText(s.getYear());
                        }
                    }

                    id.setText(temp_id);
                    String name = temp_name;
                    String lastname = "";
                    String firstname = "";
                    if (name.split("\\w+").length > 1) {

                        lastname = name.substring(name.lastIndexOf(" ") + 1);
                        firstname = name.substring(0, name.lastIndexOf(' '));
                        firstName.setText(firstname);
                        lastName.setText(lastname);
                    } else {
                        firstname = name;
                        firstName.setText(firstname);
                    }
                    loadingBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton: {
                finish();
                break;
            }

            case R.id.saveButton: {
                d = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN + "/" + userId);
                if (type == Constants.UserTypes.USER_TYPE_TPO) {
                    t.setName(firstName.getText().toString() + " " + lastName.getText().toString());
                    t.setDesignation(designation.getText().toString());
                } else {
                    s.setName(firstName.getText().toString() + " " + lastName.getText().toString());
                    s.setBranch(branch.getText().toString());
                    s.setYear(year.getText().toString());
                }
                d.addListenerForSingleValueEvent(this);
                break;
            }

            case R.id.changePassword: {
                d.addListenerForSingleValueEvent(this);
                Intent i = new Intent(ViewProfileActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                break;
            }
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (type == 1)
            d.setValue(t);
        else
            d.setValue(s);
        Toast.makeText(ViewProfileActivity.this, "Details Saved Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}