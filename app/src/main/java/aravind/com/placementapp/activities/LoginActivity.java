package aravind.com.placementapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import aravind.com.placementapp.pojo.User;
import aravind.com.placementapp.utils.StringUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText username1;
    private EditText password1;
    private ProgressBar spinner;
    private Button login1;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        initializeLayoutItems();
        String check_id = SharedPrefHelper.getEntryFromSharedPrefs(this.getApplicationContext(),Constants.SharedPrefConstants.KEY_USER_ID);
        if(check_id!=null){
            Intent i = new Intent(LoginActivity.this,DashboardActivity.class);
            startActivity(i);
        }
        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                username = username1.getText().toString();
                password = password1.getText().toString();
                checkLoginStatus(username, password);
            }
        });
    }

    private void checkLoginStatus(String username, String password) {
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            DatabaseReference databaseReference = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN + "/" + username);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    matchCredentials(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(LoginActivity.this, Constants.NO_LOGIN_INPUT, Toast.LENGTH_SHORT).show();
        }
    }

    private void matchCredentials(DataSnapshot dataSnapshot) {
        Toast invalidUserToast = Toast.makeText(LoginActivity.this, Constants.INVALID_USER, Toast.LENGTH_SHORT);
        if (dataSnapshot == null) {
            invalidUserToast.show();
            return;
        }
        User u = dataSnapshot.getValue(User.class);
        spinner.setVisibility(View.GONE);
        if (u == null) {
            invalidUserToast.show();
        } else {
            if (password.equals(u.password)) {
                Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                SharedPrefHelper.saveEntryInSharedPreferences(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_USER_ID, u.id);
                SharedPrefHelper.saveEntryInSharedPreferences(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_USER_NAME, u.name);
                SharedPrefHelper.saveEntryInSharedPreferences(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_USER_TYPE, String.valueOf(u.type));
                startActivity(i);
            } else {
                invalidUserToast.show();
            }
        }
    }

    private void initializeLayoutItems() {
        username1 = findViewById(R.id.username);
        password1 = findViewById(R.id.password);
        login1 = findViewById(R.id.login);
        spinner = (ProgressBar) findViewById(R.id.loginProgressBar);
        spinner.setVisibility(View.GONE);
    }
}