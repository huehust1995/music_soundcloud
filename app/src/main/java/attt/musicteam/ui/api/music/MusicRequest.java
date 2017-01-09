package attt.musicteam.ui.api.music;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import attt.musicteam.ui.api.BaseAPI;
import attt.musicteam.ui.api.search.SearchRequest;
import attt.musicteam.ui.item.SongItem;
import attt.musicteam.utils.Variables;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.data;

/**
 * Created by Hue on 1/7/2017.
 */

public class MusicRequest {

    public MusicRequest getDataAPI(final String url) {
        MusicAPI client = BaseAPI.createService(MusicAPI.class);
        Call<ResponseBody> call = client.getData(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(musicListener != null) {
                        musicListener.onSuccess(response.body().string());
                    }
                    Log.e("get data", "ehe");
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


    private MusicRequest.MusicListener musicListener;

    public void setMusicListener(MusicRequest.MusicListener musicListener) {
        this.musicListener = musicListener;
    }

    public interface MusicListener {
        void onSuccess(String message);
    }

}
