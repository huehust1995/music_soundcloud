package attt.musicteam.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hue on 11/8/2016.
 */
public class NowPlayingSharePreference {

    public static final String PREFS_NAME = "NOWPLAYING";
    public static final String STATE_PLAYING = "State_playing";
    public static final int IS_PLAYING = 1;
    public static final int NOT_PLAYING = 0;

    public NowPlayingSharePreference() {
        super();
    }

    public void setStatePlaying(Context context, int isPlaying) {
        SharedPreferences setting = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putInt(STATE_PLAYING, isPlaying);
        editor.commit();
    }

    public int getStatePlaying(Context context) {
        SharedPreferences setting = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        int isPlaying;
        isPlaying = setting.getInt(STATE_PLAYING, 0);
        return isPlaying;
    }

}
