package aravind.com.placementapp.constants;

public abstract class Constants {
    public static final String DEFAULT_PASSWORD = "indira123";
    public static final String INVALID_USER = "Username or Password Incorrect";
    public static final String NO_LOGIN_INPUT = "Please enter credentials";
    public static final String LOGGED_OUT = "User Logged out Successfully";

    public class FirebaseConstants {
        public static final String PATH_LOGIN = "Login";
        public static final String PATH_COMPANY = "Company";
        public static final String PATH_NOTIFICATIONS = "Notifications";
        public static final String PATH_STUDENTS = "Students";
        public static final String PATH_RECRUITERS = "Recruiters";
        public static final String STORAGE_PATH_UPLOADS = "uploads/";
        public static final String DATABASE_PATH_UPLOADS = "uploads";
    }

    public class SharedPrefConstants {
        public static final String KEY_SHARED_PREF = "PlacementAppSharedPrefs";
        public static final String KEY_USER_ID = "userId";
        public static final String KEY_USER_TYPE = "userType";
        public static final String KEY_USER_NAME = "userName";
    }

    public class UserTypes {
        public static final int USER_TYPE_ADMIN = 0;
        public static final int USER_TYPE_TPO = 1;
        public static final int USER_TYPE_STUDENT = 2;
    }

}
