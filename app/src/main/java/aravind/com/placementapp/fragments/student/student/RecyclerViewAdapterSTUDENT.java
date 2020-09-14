package aravind.com.placementapp.fragments.student.student;

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
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import aravind.com.placementapp.R;
import aravind.com.placementapp.activities.MyBounceInterpolator;
import aravind.com.placementapp.pojo.Upload;

public class RecyclerViewAdapterSTUDENT extends RecyclerView.Adapter<RecyclerViewAdapterSTUDENT.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private List<Upload> uploadList;
    private ViewPapersFragment fragment;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private FirebaseStorage storage;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notification;

    public RecyclerViewAdapterSTUDENT(Context context, List<Upload> uploadList) {
        this.context = context;
        this.uploadList = uploadList;
    }

    public RecyclerViewAdapterSTUDENT(List<Upload> uploadList, ViewPapersFragment fragment) {
        this.uploadList = uploadList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(fragment.getContext());
        view = mInflater.inflate(R.layout.layout_addpapers_card, parent, false);
        view.setBackgroundResource(R.drawable.smartdp);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(fragment.getContext(), R.anim.fade_scale_animation));
        holder.paperName.setText(uploadList.get(position).getName());
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    @Override
    public void onClick(View v) {

        Animation myAnim = AnimationUtils.loadAnimation(fragment.getContext(), R.anim.bounce_animation);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
        myAnim.setInterpolator(interpolator);
        v.startAnimation(myAnim);
        int pos = (int) v.getTag();

        Upload upload = uploadList.get(pos);
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

        Toast.makeText(fragment.getContext(), "Downloading in Progress", Toast.LENGTH_SHORT).show();
        if (fragment.getContext() != null)
            notificationManager = (NotificationManager) fragment.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

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

        final PendingIntent contentIntent = PendingIntent.getActivity(fragment.getContext(), 0, intent, 0);

        Bitmap largeIcon = BitmapFactory.decodeResource(v.getResources(),
                R.drawable.appwithoutbackground);

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notification = new NotificationCompat.Builder(fragment.getContext(), channelId)
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
                Toast.makeText(fragment.getContext(), "Download Finished", Toast.LENGTH_SHORT).show();
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView paperName;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            paperName = itemView.findViewById(R.id.paperName);
            cardView = itemView.findViewById(R.id.card_viewpapers);
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23 && fragment.getContext() != null) {
            if (ContextCompat.checkSelfPermission(fragment.getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(fragment.requireActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
}
