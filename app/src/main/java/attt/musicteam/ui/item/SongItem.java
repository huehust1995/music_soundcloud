package attt.musicteam.ui.item;

import java.io.Serializable;
/**
 * Created by Hue on 11/8/2016.
 */
public class SongItem implements Serializable {

    public String name; //ten bai hat

    public String singer; //ca si the hien

    public String imgCover; // duong dan anh

    public String time; //thoi gian cua bai hat

    public String id; // id bai hat

    public String genre; //the loai

    public String createdAt; //thoi gian tao

    public String likesCount; //so luong thich

    public boolean isSelected;

    public SongItem() {
    }

    public SongItem(String name, String singer, String imgCover, String time, String id, String genre,
                    String createdAt, String likesCount) {
        this.name = name;
        this.singer = singer;
        this.imgCover = imgCover;
        this.time = time;
        this.id = id;
        this.genre = genre;
        this.createdAt = createdAt;
        this.likesCount = likesCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getImgCover() {
        return imgCover;
    }

    public void setImgCover(String imgCover) {
        this.imgCover = imgCover;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
