package aravind.com.placementapp.constants;

public abstract class Constants {

    public static final String INVALID_USER = "Username or Password Incorrect";
    public static final String NO_LOGIN_INPUT = "Please enter credentials";
    public static final String LOGGED_OUT = "User Logged out Successfully";

    public class FirebaseConstants {
        public static final String PATH_LOGIN = "Login";
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
