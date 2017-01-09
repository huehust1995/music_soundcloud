package attt.musicteam.ui.api.search;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static attt.musicteam.utils.Variables.CLIENT_ID;

/**
 * Created by Hue on 11/26/16.
 */
public interface SearchAPI {
    @GET("/tracks?client_id=" + CLIENT_ID)
    Call<ResponseBody> searchSongs(@Query("q") String query);
}
