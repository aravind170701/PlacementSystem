package aravind.com.placementapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    private String userId;
    private int type;
    private String userType;
    private EditText currentPassword;
    private EditText newPassword;
    private Button backButton;
    private Button saveButton;
    private DatabaseReference d;
    private Student s;
    private Tpo t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        userId = SharedPrefHelper.getEntryFromSharedPrefs(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_USER_ID);
        userType = SharedPrefHelper.getEntryFromSharedPrefs(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_USER_TYPE);
        type = Integer.parseInt(userType);
        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);

        d = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN + "/" + userId);
        d.addListenerForSingleValueEvent(this);

        backButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton: {
                finish();
                break;
            }

            case R.id.saveButton: {
                if (currentPassword.getText().toString().length() == 0 || newPassword.getText().toString().length() == 0) {
                    Toast.makeText(ChangePasswordActivity.this, "Password cannot be empty..!", Toast.LENGTH_SHORT).show();
                    if (currentPassword.getText().toString().length() == 0)
                        currentPassword.setError("Cannot be Empty!");
                    else
                        newPassword.setError("Cannot be Empty!");
                } else if (type == 1) {
                    if (currentPassword.getText().toString().equals(t.getPassword())) {
                        t.setPassword(newPassword.getText().toString());
                        d.setValue(t);
                        Toast.makeText(ChangePasswordActivity.this, "Password Updated Successfully!", Toast.LENGTH_SHORT).show();
                    } else
                        wrongPassword();
                } else {
                    if (currentPassword.getText().toString().equals(s.getPassword())) {
                        s.setPassword(newPassword.getText().toString());
                        d.setValue(s);
                        Toast.makeText(ChangePasswordActivity.this, "Password Updated Successfully!", Toast.LENGTH_SHORT).show();
                    } else
                        wrongPassword();
                }
                break;
            }
        }
    }

    public void wrongPassword() {
        currentPassword.setError("Wrong Current Password");
        Toast.makeText(ChangePasswordActivity.this, "Wrong Current Password", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (type == 1)
            t = snapshot.getValue(Tpo.class);
        else
            s = snapshot.getValue(Student.class);

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}