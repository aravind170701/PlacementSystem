package aravind.com.placementapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import aravind.com.placementapp.constants.Constants;

public abstract class SharedPrefHelper {

    public static boolean saveEntryInSharedPreferences(Context context, String key, String value) {
        if (context == null) {
            return false;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SharedPrefConstants.KEY_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
        return true;
    }

    public static String getEntryFromSharedPrefs(Context context, String key) {
        if (context == null) {
            return null;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SharedPrefConstants.KEY_SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
}
