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
public class GenreSharePreference {

    public static final String PREFS_NAME = "GENRE_DETAIL";
    public static final String GENRE = "Genre_Songs";

    public GenreSharePreference(){

    }

    public void saveGenreSongs(Context context, List<SongItem> listGenreSongs){
        SharedPreferences setting = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        Gson gson = new Gson();
        String jsonString = gson.toJson(listGenreSongs);
        editor.putString(GENRE, jsonString);
        editor.commit();
    }
}
