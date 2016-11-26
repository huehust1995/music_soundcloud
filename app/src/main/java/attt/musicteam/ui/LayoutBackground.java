package attt.musicteam.ui;

import android.graphics.Color;
import android.widget.ImageButton;
import android.widget.TextView;

import attt.musicteam.R;

/**
 * Created by Hue on 11/8/2016.
 */
public class LayoutBackground {
    public LayoutBackground(){

    }

    public void HomeLayout(ImageButton btnHome, TextView txtHome){
        btnHome.setImageResource(R.drawable.ic_tab_home);
        txtHome.setTextColor(Color.parseColor("#757575"));
    }

    public void HomeLayoutFocus(ImageButton btnHome, TextView txtHome){
        btnHome.setImageResource(R.drawable.ic_tab_home_focused);
        txtHome.setTextColor(Color.parseColor("#EC2B4A"));
    }

    public void PlaylistLayout(ImageButton btnPlaylist, TextView txtPlaylist){
        btnPlaylist.setImageResource(R.drawable.ic_tab_playlist);
        txtPlaylist.setTextColor(Color.parseColor("#757575"));
    }

    public void PlaylistLayoutFocus(ImageButton btnPlaylist, TextView txtPlaylist){
        btnPlaylist.setImageResource(R.drawable.ic_tab_playlist_focused);
        txtPlaylist.setTextColor(Color.parseColor("#EC2B4A"));
    }

    public void GenreLayout(ImageButton btnGenre, TextView txtGenre){
        btnGenre.setImageResource(R.drawable.ic_tab_genre);
        txtGenre.setTextColor(Color.parseColor("#757575"));
    }

    public void GenreLayoutFocus(ImageButton btnGenre, TextView txtGenre){
        btnGenre.setImageResource(R.drawable.ic_tab_genre_focused);
        txtGenre.setTextColor(Color.parseColor("#EC2B4A"));
    }

    public void SearchLayout(ImageButton btnSearch, TextView txtSearch){
        btnSearch.setImageResource(R.drawable.ic_tab_search);
        txtSearch.setTextColor(Color.parseColor("#757575"));
    }

    public void SearchLayoutFocus(ImageButton btnSearch, TextView txtSearch){
        btnSearch.setImageResource(R.drawable.ic_tab_search_focused);
        txtSearch.setTextColor(Color.parseColor("#EC2B4A"));
    }

    public void MoreLayout(ImageButton btnMore, TextView txtMore){
        btnMore.setImageResource(R.drawable.ic_tab_more);
        txtMore.setTextColor(Color.parseColor("#757575"));
    }

    public void MoreLayoutFocus(ImageButton btnMore, TextView txtMore){
        btnMore.setImageResource(R.drawable.ic_tab_more_focused);
        txtMore.setTextColor(Color.parseColor("#EC2B4A"));
    }
}
