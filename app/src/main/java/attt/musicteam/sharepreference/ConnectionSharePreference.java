package attt.musicteam.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hue on 11/8/2016.
 */
public class ConnectionSharePreference {
    public static final String PREFS_NAME = "Connection_Timeout";
    public static final String CONNECTION_TIMEOUT = "timeout";

    public ConnectionSharePreference() {
    }

    public void getTime() {
    }

    public void saveTimeoutValue(Context context, int value) {
        SharedPreferences setting = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putInt(CONNECTION_TIMEOUT, value);
        editor.commit();
    }

    public int getTimeoutValue(Context context) {
        int timeout;
        SharedPreferences setting = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        timeout = setting.getInt(CONNECTION_TIMEOUT, 0);
        return timeout;
    }


}
