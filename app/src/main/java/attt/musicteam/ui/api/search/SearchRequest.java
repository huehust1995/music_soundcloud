package attt.musicteam.ui.api.search;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import attt.musicteam.ui.api.BaseAPI;
import attt.musicteam.ui.item.SongItem;
import attt.musicteam.utils.Variables;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hue on 11/26/16.
 */
public class SearchRequest {

    public SearchRequest searchAPI(final String keyword) {
        SearchAPI client = BaseAPI.createService(SearchAPI.class);
        Call<ResponseBody> call = client.searchSongs(keyword);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    parseSearchResponse(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return this;
    }


    public void parseSearchResponse(String body) {
        List<SongItem> trackList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(body);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String imgCover = jsonObject.getString("artwork_url");
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("title");
                String singer = jsonObject.getString("genre");
                String time = jsonObject.getString("duration");
                String genre = jsonObject.getString("genre");
                String likesCount = jsonObject.getString("likes_count");
                String createdAt = jsonObject.getString("created_at");
                SongItem songItem = new SongItem(name, genre, imgCover, new Variables().timeTrack(time), id, genre, createdAt, likesCount);
                trackList.add(songItem);
                Log.e("size tracklist", jsonArray.length() + "");
                if(searchListener != null) {
                    searchListener.onSuccess(trackList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private SearchListener searchListener;

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    public interface SearchListener {
        void onSuccess(List<SongItem> message);
    }
}
