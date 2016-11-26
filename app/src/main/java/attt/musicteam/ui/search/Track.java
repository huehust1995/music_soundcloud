package attt.musicteam.ui.search;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hue on 11/26/16.
 */
public class Track {
    @SerializedName("title")
    private String name;

//    @SerializedName("genre")
//    private String singer;

    @SerializedName("artwork_url")
    private String imgCover;

    @SerializedName("duration")
    private String time;

    @SerializedName("id")
    private int id;

    @SerializedName("genre")
    private String genre;

    @SerializedName("created_at")
    public String createdAt; //thoi gian tao

    @SerializedName("likes_count")
    public String likesCount; //so luong thich

    @SerializedName("stream_url")
    private String mStreamURL;

    public String getTitle() {
        return name;
    }

    public void setTitle(String title) {
        name = title;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public String getImgCover() {
        return imgCover;
    }

    public String getAvatarURL(){
        String avatarURL = getAvatarURL();
        if (avatarURL != null){
            return imgCover.replace("large","tiny");
        }
        return avatarURL;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

//    public String getSinger() {
//        return singer;
//    }

    public String getGenre() {
        return genre;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public String getmStreamURL() {
        return mStreamURL;
    }
}
