package attt.musicteam.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import attt.musicteam.ui.MainActivity;
import attt.musicteam.ui.item.SongItem;
import attt.musicteam.ui.progress_dialog.ProgressDialogNoTitle;

/**
 * Created by Hue on 11/8/2016.
 */
public class ReadWriteData {

    public String url1 = "https://api-v2.soundcloud.com/charts?kind=trending&genre=";
    public String genreName = "";
    public String url2 = "&client_id=";
    public String clientId = "";

    public ReadWriteData() {

    }

    public void writeHomeData(String data) {
        File folder = new File("/sdcard/Android/data/musicplayer");
        folder.mkdir();
        File file = new File(folder, Variables.HOME);
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
            writer.write(data);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String readHomeData() {
        String data = "";
        String str = "";
        String uriFolder = "/sdcard/Android/data/musicplayer";
        StringBuffer stringBuffer = new StringBuffer();
        File folder = new File(uriFolder);
        folder.mkdir();
        File file = new File(folder, Variables.HOME);
        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((str = bufferedReader.readLine()) != null) {
                stringBuffer.append(str);
                data += stringBuffer;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<SongItem> parseData(String data) {
        List<SongItem> listSongs = new ArrayList<SongItem>();
        try {
            JSONObject response = new JSONObject(data);
            JSONArray collection = response.getJSONArray("collection");
            for(int i = 0 ; i < collection.length(); i++){
                JSONObject object = collection.getJSONObject(i);
                JSONObject track = object.getJSONObject("track");
                String imgCover = track.getString("artwork_url");
                String id = track.getString("id");
                String name = track.getString("title");
                String singer = track.getString("genre");
                String time = track.getString("duration");
                String genre = track.getString("genre");
                String likesCount = track.getString("likes_count");
                String createdAt = track.getString("created_at");
                SongItem songItem = new SongItem(name, singer, imgCover, new Variables().timeTrack(time), id, genre, createdAt, likesCount);
                listSongs.add(songItem);

            }
        } catch (Exception e) {

        }
        return listSongs;
    }



    public void writeGenreData(String data) {
        File folder = new File("/sdcard/Android/data/musicplayer");
        folder.mkdir();
        File file = new File(folder, Variables.GENRE);
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
            writer.write(data);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
