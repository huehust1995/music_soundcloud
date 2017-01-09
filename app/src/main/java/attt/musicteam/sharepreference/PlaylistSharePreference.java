package attt.musicteam.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import attt.musicteam.ui.item.PlaylistItem;
import attt.musicteam.ui.item.SongItem;

/**
 * Created by Hue on 11/8/2016.
 */
public class PlaylistSharePreference {
    public static final String PREFS_NAME = "PLAYLIST";
    public static final String PLAYLIST = "Playlist_Items";

    public static final String PREFS_PLAYLIST_SONGS = "PLAYLIST_SONGS";

    public PlaylistSharePreference() {
        super();
    }

    //khoi tao va luu danh sach playlist
    public void savePlaylistItems(Context context, List<PlaylistItem> listPlaylistItems) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Gson gson = new Gson();
        String jsonPlaylist = gson.toJson(listPlaylistItems);
        editor.putString(PLAYLIST, jsonPlaylist);
        editor.commit();
    }

    public List<PlaylistItem> getPlaylistItems(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        List<PlaylistItem> listPlaylistItems = new ArrayList<PlaylistItem>();
        if (settings.contains(PLAYLIST)) {
            String jsonPlaylist = settings.getString(PLAYLIST, null);
            Gson gson = new Gson();
            PlaylistItem[] playlistItems = gson.fromJson(jsonPlaylist, PlaylistItem[].class);
            listPlaylistItems = Arrays.asList(playlistItems);
            listPlaylistItems = new ArrayList<PlaylistItem>(listPlaylistItems);
        }
        return listPlaylistItems;
    }

    public void addPlaylistItem(Context context, PlaylistItem playlistItem) {
        List<PlaylistItem> listPlaylistItems = getPlaylistItems(context);
        if (listPlaylistItems == null) {
            listPlaylistItems = new ArrayList<PlaylistItem>();
        }
        listPlaylistItems.add(playlistItem);
        savePlaylistItems(context, listPlaylistItems);
    }

    //chinh sua ten playlist
    public void editPlaylistItemName(Context context, int position, String newName) {
        List<PlaylistItem> listPlaylistItems = getPlaylistItems(context);
        String oldName = listPlaylistItems.get(position).getName();
        listPlaylistItems.get(position).setName(newName);
        savePlaylistItems(context, listPlaylistItems);
        editSongInPlaylsit(context, oldName, newName);
    }

    //chinh sua so bai hat trong playlist item
    public void editPlaylistItemNumTracks(Context context, int position, int numTracks) {
        List<PlaylistItem> listPlaylistItems = getPlaylistItems(context);
        listPlaylistItems.get(position).setNumTracks(numTracks);
        savePlaylistItems(context, listPlaylistItems);
    }

    public void removePlaylistItem(Context context, int position) {
        List<PlaylistItem> listPlaylistItems = getPlaylistItems(context);
        Log.d("12345", listPlaylistItems.toString());
        if (listPlaylistItems != null) {
            listPlaylistItems.remove(position);

        }
        savePlaylistItems(context, listPlaylistItems);
    }

    //khoi tao va luu bai hat vao playlist
    public void saveSongItemToPlaylist(Context context, List<SongItem> listPlaylistSongs, String playlistName) {
        SharedPreferences setting = context.getSharedPreferences(PREFS_PLAYLIST_SONGS, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();

        Gson gson = new Gson();
        String jsonSong = gson.toJson(listPlaylistSongs);
        editor.putString(playlistName, jsonSong);
        editor.commit();
    }

    public List<SongItem> getSongsInPlaylist(Context context, String playlistName) {
        SharedPreferences setting = context.getSharedPreferences(PREFS_PLAYLIST_SONGS, Context.MODE_PRIVATE);
        List<SongItem> listPlaylistSongs;
        if (setting.contains(playlistName)) {
            String jsonPlaylistSong = setting.getString(playlistName, null);
            Gson gson = new Gson();
            SongItem[] songItems = gson.fromJson(jsonPlaylistSong, SongItem[].class);
            listPlaylistSongs = Arrays.asList(songItems);
            listPlaylistSongs = new ArrayList<SongItem>(listPlaylistSongs);
        } else {
            return null;
        }
        return listPlaylistSongs;
    }

    public void addSongToPlaylist(Context context, SongItem songItem, String playlistname) {
        List<SongItem> listPlaylistSongs = getSongsInPlaylist(context, playlistname);
        if (listPlaylistSongs == null) {
            listPlaylistSongs = new ArrayList<SongItem>();
        }
        listPlaylistSongs.add(songItem);
        saveSongItemToPlaylist(context, listPlaylistSongs, playlistname);
    }

    public void removeSongFromPlaylist(Context context, SongItem songItem, String playlistName) {
        List<SongItem> listPlaylistSongs = getSongsInPlaylist(context, playlistName);
        if (listPlaylistSongs != null) {
            listPlaylistSongs.remove(songItem);
            saveSongItemToPlaylist(context, listPlaylistSongs, playlistName);
        }
    }

    public void removeAllSongInPlaylist(Context context, String playlistName) {
        List<SongItem> listPlaylistSongs = getSongsInPlaylist(context, playlistName);
        if (listPlaylistSongs != null) {
            listPlaylistSongs = new ArrayList<SongItem>();
            saveSongItemToPlaylist(context, listPlaylistSongs, playlistName);
        }
    }

    //cap nhat list bai hat khi ten playlist bi thay doi
    public void editSongInPlaylsit(Context context, String oldName, String newName) {
        List<SongItem> listSongItemOld = getSongsInPlaylist(context, oldName);
        List<SongItem> listSongItemNew = new ArrayList<SongItem>();
        if (listSongItemOld != null) {
            for (int i = 0; i < listSongItemOld.size(); i++) {
                listSongItemNew.add(listSongItemOld.get(i));
            }
            saveSongItemToPlaylist(context, listSongItemNew, newName);
        }
        removeAllSongInPlaylist(context, oldName);
    }
}
