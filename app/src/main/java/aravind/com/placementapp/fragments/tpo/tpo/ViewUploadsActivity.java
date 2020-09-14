package aravind.com.placementapp.fragments.tpo.tpo;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import aravind.com.placementapp.R;
import aravind.com.placementapp.constants.Constants;
import aravind.com.placementapp.pojo.Upload;

public class ViewUploadsActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference mDatabaseReference;
    private List<Upload> uploadList;
    private StorageReference mStorageReference;
    private FirebaseStorage storage;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_uploads);

        uploadList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Upload upload = uploadList.get(i);
                storage = FirebaseStorage.getInstance();
                mStorageReference = storage.getReferenceFromUrl("gs://placement-system-f3649.appspot.com/");
                StorageReference s = mStorageReference.child("uploads/").child(upload.getName() + ".pdf");

                File rootPath = null;
                if (isStoragePermissionGranted()) {
                    rootPath = new File(Environment.getExternalStorageDirectory(), "Placement App");
                    if (!rootPath.exists()) {
                        rootPath.mkdirs();
                    }
                }

                Toast.makeText(ViewUploadsActivity.this, "Downloading in Progress", Toast.LENGTH_SHORT).show();
                notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                int notificationId = 1;
                String channelId = "channel-01";
                String channelName = "Channel Name";
                int importance = NotificationManager.IMPORTANCE_LOW;
                final int PROGRESS_MAX = 100;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel(
                            channelId, channelName, importance);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(mChannel);
                }

                final File localFile = new File(rootPath, upload.getName() + ".pdf");

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(localFile.getPath());
                intent.setDataAndType(uri, "file/*");

                final PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.appwithoutbackground);

                Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                notification = new NotificationCompat.Builder(getApplicationContext(), channelId)
                        .setSmallIcon(R.drawable.appwithoutbackground)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(upload.getName() + ".pdf")
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setContentIntent(contentIntent)
                        .setContentText("Download in progress")
                        .setOngoing(true)
                        .setOnlyAlertOnce(true)
                        .setProgress(PROGRESS_MAX, 0, false);

                notificationManager.notify(notificationId, notification.build());

                s.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.e("firebase ", ";local tem file created  created " + localFile.toString());
                        SystemClock.sleep(2000);
                        Toast.makeText(ViewUploadsActivity.this, "Download Finished", Toast.LENGTH_SHORT).show();
                        notification.setContentText("Download Finished")
                                .setProgress(0, 0, false)
                                .setOngoing(false);
                        notificationManager.notify(notificationId, notification.build());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("firebase ", ";local tem file not created  created " + exception.toString());
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        notification.setProgress(PROGRESS_MAX, (int) progress, false);
                        notificationManager.notify(notificationId, notification.build());
                    }
                });
            }
        });


        //getting the database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.FirebaseConstants.DATABASE_PATH_UPLOADS);

        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploadList.add(upload);
                }

                String[] uploads = new String[uploadList.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploadList.get(i).getName();
                }

                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(30);
                        return view;
                    }
                };
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

}