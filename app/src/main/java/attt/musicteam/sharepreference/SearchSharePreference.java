package attt.musicteam.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import attt.musicteam.ui.item.SongItem;

/**
 * Created by Hue on 11/8/2016.
 */
public class SearchSharePreference {
    public static final String PREFS_NAME = "SEARCH";
    public static final String SEARCH = "Search_Songs";

    public SearchSharePreference(){
        super();
    }

    public void saveSearchSongs(Context context, List<SongItem> listSearchSongs){
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String jsonSearch = gson.toJson(listSearchSongs);
        editor.putString(SEARCH, jsonSearch);
        editor.commit();
    }
}
