package aravind.com.placementapp.helper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import aravind.com.placementapp.utils.StringUtils;

public abstract class FirebaseHelper {
    private static FirebaseDatabase firebaseDatabase;

    public static FirebaseDatabase getFirebaseInstance() {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
        }
        return firebaseDatabase;
    }

    public static DatabaseReference getFirebaseReference(String path) {
        DatabaseReference databaseReference = null;
        if (StringUtils.isNotBlank(path)) {
            FirebaseDatabase firebaseDatabase = getFirebaseInstance();
            if (firebaseDatabase != null) {
                databaseReference = firebaseDatabase.getReference(path);
            }
        }
        return databaseReference;
    }
}
