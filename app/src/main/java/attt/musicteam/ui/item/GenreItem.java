package attt.musicteam.ui.item;

import java.io.Serializable;
/**
 * Created by Hue on 11/8/2016.
 */
public class GenreItem implements Serializable {

    public String genreName;
    public String genreApi;

    public GenreItem(String genreName, String genreApi) {
        this.genreName = genreName;
        this.genreApi = genreApi;
    }

    public GenreItem() {
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getGenreApi() {
        return genreApi;
    }

    public void setGenreApi(String genreApi) {
        this.genreApi = genreApi;
    }
}
