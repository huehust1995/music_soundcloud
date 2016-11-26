package attt.musicteam.ui.search;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Hue on 11/26/16.
 */
public interface SoundCloudService {

    static final String CLIENT_ID = "954240dcf4b7c0a46e59369a69db7215";

    @GET("/tracks?client_id=" + CLIENT_ID)
    public void searchSongs(@Query("q") String query, Callback<List<Track>> cb);
}
