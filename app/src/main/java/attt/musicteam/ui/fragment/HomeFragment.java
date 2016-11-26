package attt.musicteam.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import attt.musicteam.R;
import attt.musicteam.sharepreference.HistorySharePreference;
import attt.musicteam.sharepreference.NowPlayingSharePreference;
import attt.musicteam.sharepreference.PlaylistSharePreference;
import attt.musicteam.sharepreference.StateSharePreference;
import attt.musicteam.ui.PlayMusicActivity;
import attt.musicteam.ui.adapter.ListSongAdapter;
import attt.musicteam.ui.adapter.PlaylistAdapter;
import attt.musicteam.ui.item.PlaylistItem;
import attt.musicteam.ui.item.SongItem;
import attt.musicteam.utils.ReadWriteData;

/**
 * Created by Hue on 11/8/2016.
 */
public class HomeFragment extends Fragment {

    private LinearLayout layoutPlay, layoutAddPlaylist, layoutInfo, layoutCancel;
    private DialogPlus dialogPlus;
    private ReadWriteData rw;
    private ListView listView;
    private ListSongAdapter adapter;
    private List<SongItem> listSongs;

    private NowPlayingSharePreference nowPlayingSp;
    private HistorySharePreference historySong;
    private StateSharePreference stateSp;

    public HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        ImageButton btnNowPlaying = (ImageButton) view.findViewById(R.id.btn_now_playing);
        listSongs = new ArrayList<SongItem>();
        listView = (ListView) view.findViewById(R.id.home_listview);
        historySong = new HistorySharePreference();
        stateSp = new StateSharePreference();
        nowPlayingSp = new NowPlayingSharePreference();
//        if (nowPlayingSp.getStatePlaying(getActivity()) == NowPlayingSharePreference.IS_PLAYING) {
//            btnNowPlaying.setVisibility(View.VISIBLE);
//        } else btnNowPlaying.setVisibility(View.INVISIBLE);
//        btnNowPlaying.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stateSp.saveState(getActivity(), StateSharePreference.PLAYLIST_STATE);
//                getActivity().finish();
//            }
//        });
        rw = new ReadWriteData();
        String data = rw.readHomeData();
        listSongs = rw.parseData(data);
        adapter = new ListSongAdapter(getActivity(), listSongs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongItem songItem = listSongs.get(position);
                initDialogPlus(songItem, position);
            }
        });
        return view;
    }

    public void initDialogPlus(final SongItem songItem, final int position) {
        Holder holder = new ViewHolder(R.layout.item_select_home);
        dialogPlus = DialogPlus.newDialog(getActivity())
                .setContentHolder(holder)
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
        dialogPlus.show();
        layoutPlay = (LinearLayout) holder.getInflatedView().findViewById(R.id.layout_play);
        layoutAddPlaylist = (LinearLayout) holder.getInflatedView().findViewById(R.id.layout_add2playlist);
        layoutInfo = (LinearLayout) holder.getInflatedView().findViewById(R.id.layout_info);
        layoutCancel = (LinearLayout) holder.getInflatedView().findViewById(R.id.layout_cancel);
        layoutPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic(songItem, position);
                dialogPlus.dismiss();
            }
        });
        layoutAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
                final List<PlaylistItem> listPlaylist;
                final PlaylistSharePreference sharePreference = new PlaylistSharePreference();
                listPlaylist = sharePreference.getPlaylistItems(getActivity());
                Log.e("huent", listPlaylist.size() + "");
                if (listPlaylist.size() == 0) {
                    Toast.makeText(getActivity(), "Playlist empty!", Toast.LENGTH_SHORT).show();
                } else {
                    showPlaylistDialog(getActivity(), songItem);
                }
            }
        });
        layoutInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initInformationDialog(songItem);
                dialogPlus.dismiss();
            }
        });
        layoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
            }
        });
    }

    public void initInformationDialog(SongItem songItem) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_track_information, null);
        TextView songName = (TextView) view.findViewById(R.id.song_name);
        TextView songTime = (TextView) view.findViewById(R.id.song_time);
        TextView songGenre = (TextView) view.findViewById(R.id.song_genre);
        TextView songCreatedAt = (TextView) view.findViewById(R.id.song_created_at);
        TextView songLikesCount = (TextView) view.findViewById(R.id.song_likes_count);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCancelable(true);
        builder.setView(view);
        songName.setText(songItem.getName());
        songTime.setText(songItem.getTime());
        songGenre.setText(songItem.getGenre());
        songCreatedAt.setText(songItem.getCreatedAt());
        songLikesCount.setText(songItem.getLikesCount());

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void playMusic(SongItem songItem, int position) {
        if (nowPlayingSp.getStatePlaying(getActivity()) == NowPlayingSharePreference.NOT_PLAYING) {
            stateSp.saveState(getActivity(), StateSharePreference.HOME_STATE);
            historySong.addHistorySong(getActivity(), songItem);
            Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
            intent.putExtra("songItem", songItem);
            intent.putExtra("position", position);
            intent.putExtra("listSongs", (Serializable) listSongs);
            intent.putExtra("toolbarName", "Today's Top Hits");
            getActivity().startActivity(intent);
            getActivity().finish();
        } else if (nowPlayingSp.getStatePlaying(getActivity()) == NowPlayingSharePreference.IS_PLAYING) {
            stateSp.saveState(getActivity(), StateSharePreference.HOME_STATE);
            Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
            intent.putExtra("songItem", songItem);
            intent.putExtra("position", position);
            intent.putExtra("listSongs", (Serializable) listSongs);
            intent.putExtra("toolbarName", "Today's Top Hits");
            getActivity().setResult(PlayMusicActivity.RESULT_CODE, intent);
            getActivity().finish();
        }
    }

    public void showPlaylistDialog(final Context context, final SongItem songItem) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_show_playlist, null);
        ListView listView = (ListView) view.findViewById(R.id.dialog_playlist_listview);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        PlaylistAdapter adapter;
        final List<PlaylistItem> listPlaylist;
        final PlaylistSharePreference sharePreference = new PlaylistSharePreference();
        listPlaylist = sharePreference.getPlaylistItems(context);
        adapter = new PlaylistAdapter(context, listPlaylist);
        listView.setAdapter(adapter);
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setView(view);
        builder.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sharePreference.addSongToPlaylist(context, songItem, listPlaylist.get(position).getName());
                List<SongItem> listSongsInPlaylist = sharePreference.getSongsInPlaylist(context, listPlaylist.get(position).getName());
                int numTracks = listSongsInPlaylist.size();
                sharePreference.editPlaylistItemNumTracks(context, position, numTracks);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
