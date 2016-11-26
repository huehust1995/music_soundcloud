package attt.musicteam.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import attt.musicteam.ui.item.SongItem;

/**
 * Created by Hue on 11/8/2016.
 */
public class HistorySharePreference {
    public static final String PREFS_NAME = "HISTORY";
    public static final String HISTORY = "History_Songs";

    public HistorySharePreference(){
        super();
    }

    public void saveHistorySongs(Context context, List<SongItem> listHistorySongs){
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Gson gson = new Gson();
        String jsonHistory = gson.toJson(listHistorySongs);
        editor.putString(HISTORY, jsonHistory);
        editor.commit();
    }

    public void addHistorySong(Context context, SongItem songItem){
        List<SongItem> listHistorySongs = getHistorySongs(context);
        if(listHistorySongs == null){
            listHistorySongs = new ArrayList<SongItem>();
        }
        if(listHistorySongs.size() < 10) {
            listHistorySongs.add(songItem);
        } else {
            listHistorySongs.remove(0);
            listHistorySongs.add(songItem);
        }
        saveHistorySongs(context, listHistorySongs);
    }

    public void removeHistorySong(Context context, List<Integer> listPosition){
        List<SongItem> listHistorySongs = getHistorySongs(context);
        if(listHistorySongs != null){
            Collections.reverse(listHistorySongs);
            List<SongItem> listRemove = new ArrayList<SongItem>();
            for(int i = 0; i < listPosition.size(); i++){
                listRemove.add(listHistorySongs.get(listPosition.get(i)));
            }
            for(int i = 0; i < listRemove.size(); i++){
                listHistorySongs.remove(listRemove.get(i));
            }
            Collections.reverse(listHistorySongs);
            saveHistorySongs(context, listHistorySongs);
        }
    }

    public void removeAllHistorySongs(Context context){
        List<SongItem> listHistorySongs = getHistorySongs(context);
        if(listHistorySongs != null){
            listHistorySongs = new ArrayList<SongItem>();
            saveHistorySongs(context, listHistorySongs);
        }
    }

    public List<SongItem> getHistorySongs(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        List<SongItem> listHistorySongs;
        if(settings.contains(HISTORY)){
            String jsonHistory = settings.getString(HISTORY, null);
            Gson gson = new Gson();
            SongItem[] songItems = gson.fromJson(jsonHistory, SongItem[].class);
            listHistorySongs = Arrays.asList(songItems);
            listHistorySongs = new ArrayList<SongItem>(listHistorySongs);
        } else {
            return null;
        }
        return listHistorySongs;
    }

}
