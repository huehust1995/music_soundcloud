package attt.musicteam.ui.item;

/**
 * Created by Hue on 11/8/2016.
 */
public class PlaylistItem {
    public String name;
    public int imgCover;
    public int numTracks;

    public PlaylistItem(int imgCover, String name, int numTracks) {
        this.imgCover = imgCover;
        this.name = name;
        this.numTracks = numTracks;
    }

    public PlaylistItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgCover() {
        return imgCover;
    }

    public void setImgCover(int imgCover) {
        this.imgCover = imgCover;
    }

    public int getNumTracks() {
        return numTracks;
    }

    public void setNumTracks(int numTracks) {
        this.numTracks = numTracks;
    }
}
