package attt.musicteam.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hue on 11/8/2016.
 */
public class StateSharePreference {

    public static final String PREFS_NAME = "State";
    public static final String STATE = "Fragment_State";
    public static final String START_SATATE = "0";
    public static final String HOME_STATE = "1";
    public static final String PLAYLIST_STATE = "2";
    public static final String GENRE_STATE = "3";
    public static final String SEARCH_STATE = "4";
    public static final String MORE_STATE = "5";
    public static final String HISTORY_STATE = "6";

    public StateSharePreference(){

    }

    public void saveState(Context context, String state){
        SharedPreferences setting = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putString(STATE, state);
        editor.commit();
    }

    public String getState(Context context){
        String state = "";
        SharedPreferences setting = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        state = setting.getString(STATE, null);
        return state;
    }

}
