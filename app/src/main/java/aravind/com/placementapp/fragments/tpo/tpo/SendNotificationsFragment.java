package aravind.com.placementapp.fragments.tpo.tpo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAz9xLZGU:APA91bFQScl7aX8Au4q9ecmm2jj7aeRRUPpWG1lmvHYn9S14E_RaCgybOuTmXbgmAWUqYu4LpZqiyLSY06U_UsOBQ3CdersVMlMiX1YZHXKDgtbN3GbtTbWt4LbYfawcKW2JrdUkBAdQ";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

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
        String titlevalue = title.getText().toString();
        String messagevalue = message.getText().toString();
        if (titlevalue.length() == 0)
            title.setError("This field cannot be empty");
        if (messagevalue.length() == 0)
            message.setError("This field cannot be empty");

        if (titlevalue.length() != 0 && messagevalue.length() != 0) {
            Long time;
            String timestamp;

            time = System.currentTimeMillis();

            DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm");
            Date result = new Date(time);
            timestamp = simple.format(result);

            Notification n = new Notification(titlevalue, messagevalue, timestamp);
            databaseReference = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_NOTIFICATIONS + "/" + time);
            databaseReference.setValue(n);

            TOPIC = "/topics/Students"; //topic must match with what the receiver subscribed to
            NOTIFICATION_TITLE = titlevalue;
            NOTIFICATION_MESSAGE = messagevalue;

            JSONObject notification = new JSONObject();
            JSONObject notifcationBody = new JSONObject();
            try {
                notifcationBody.put("title", NOTIFICATION_TITLE);
                notifcationBody.put("message", NOTIFICATION_MESSAGE);

                notification.put("to", TOPIC);
                notification.put("data", notifcationBody);
            } catch (JSONException e) {
                Log.e(TAG, "onCreate: " + e.getMessage());
            }
            sendNotification(notification);
        }
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), "Notification Sent Successfully", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: " + response.toString());
                        title.setText("");
                        message.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Request error", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }
}






