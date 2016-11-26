package attt.musicteam.ui.item;
/**
 * Created by Hue on 11/8/2016.
 */
public class MoreItem {
    public int icon;
    public String title;

    public MoreItem() {
    }

    public MoreItem(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
