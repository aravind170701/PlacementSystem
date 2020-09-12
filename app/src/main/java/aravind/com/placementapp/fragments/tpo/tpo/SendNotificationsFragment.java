package aravind.com.placementapp.fragments.tpo.tpo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import aravind.com.placementapp.R;
import aravind.com.placementapp.constants.Constants;
import aravind.com.placementapp.helper.FirebaseHelper;
import aravind.com.placementapp.pojo.Notification;

public class SendNotificationsFragment extends Fragment implements View.OnClickListener {

    private EditText title;
    private EditText message;
    private Button sendNotificationButton;
    private DatabaseReference databaseReference;

    public SendNotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.title);
        message = view.findViewById(R.id.message);
        sendNotificationButton = view.findViewById(R.id.sendNotificationButton);
        sendNotificationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (title.getText().toString().length() == 0)
            title.setError("This field cannot be empty");
        if (message.getText().toString().length() == 0)
            message.setError("This field cannot be empty");

        if (title.getText().toString().length() != 0 && message.getText().toString().length() != 0) {
            Long time;
            String timestamp;
            String titlevalue, messagevalue;

            time = System.currentTimeMillis();

            DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm");
            Date result = new Date(time);
            timestamp = simple.format(result);

            titlevalue = title.getText().toString();
            messagevalue = message.getText().toString();
            Notification n = new Notification(titlevalue, messagevalue, timestamp);
            databaseReference = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_NOTIFICATIONS + "/" + time);
            databaseReference.setValue(n);
            Toast.makeText(getContext(), "Notification Sent Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}




