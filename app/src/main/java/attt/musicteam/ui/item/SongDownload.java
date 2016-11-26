package attt.musicteam.ui.item;

/**
 * Created by Hue on 11/26/2016.
 */

public class SongDownload {
    private String mPath;
    private String mName;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public SongDownload(String mPath, String mName) {

        this.mPath = mPath;
        this.mName = mName;
    }
}
