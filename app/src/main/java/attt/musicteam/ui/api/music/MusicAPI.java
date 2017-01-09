package attt.musicteam.ui.api.music;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Hue on 1/7/2017.
 */

public interface MusicAPI {
    @GET
    Call<ResponseBody> getData(@Url String genre);
}
