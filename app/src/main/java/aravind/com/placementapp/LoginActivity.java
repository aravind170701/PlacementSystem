package aravind.com.placementapp;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        spinner.setVisibility(View.GONE);

        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                username = username1.getText().toString();
                password = password1.getText().toString();

                FirebaseDatabase f = FirebaseDatabase.getInstance();
                DatabaseReference d = f.getReference("Login/" + username);
                d.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        spinner.setVisibility(View.GONE);
                        if (u == null) {
                            Toast.makeText(LoginActivity.this, "Username or Password Incorrect", Toast.LENGTH_SHORT).show();
                        } else {
                            if (u.password.equals(password)) {
                                // TODO change this
                                Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(LoginActivity.this, "Username or Password Incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void initializeLayoutItems() {
        username1 = findViewById(R.id.username);
        password1 = findViewById(R.id.password);
        login1 = findViewById(R.id.login);
        spinner = (ProgressBar) findViewById(R.id.loginProgressBar);
    }
}