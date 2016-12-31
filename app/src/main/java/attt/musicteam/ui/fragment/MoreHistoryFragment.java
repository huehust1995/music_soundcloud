package attt.musicteam.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import attt.musicteam.R;
import attt.musicteam.sharepreference.HistorySharePreference;
import attt.musicteam.sharepreference.NowPlayingSharePreference;
import attt.musicteam.sharepreference.PlaylistSharePreference;
import attt.musicteam.ui.PlayMusicActivity;
import attt.musicteam.ui.adapter.HistoryAdapter;
import attt.musicteam.ui.adapter.PlaylistAdapter;
import attt.musicteam.ui.item.PlaylistItem;
import attt.musicteam.ui.item.SongItem;

import static attt.musicteam.R.id.layout_add2playlist;
import static attt.musicteam.R.id.layout_cancel;
import static attt.musicteam.R.id.layout_info;
import static attt.musicteam.R.id.layout_play;

/**
 * Created by Hue on 11/8/2016.
 */
public class MoreHistoryFragment extends Fragment {

    private LinearLayout layoutPlay, layoutAddPlaylist, layoutInfo, layoutCancel;
    private DialogPlus dialogPlus;
    public ImageButton btnBack;
    public Fragment fragment;
    public FragmentTransaction transaction;
    public ListView listView;
    public HistoryAdapter adapter;
    public List<SongItem> listSongs;
    //    public List<SongItem> historySongs;
    public HistorySharePreference historySharePreference;
    public NowPlayingSharePreference nowPlayingSp;
    public ImageButton btnEdit;

    public MoreHistoryFragment newInstance() {
        MoreHistoryFragment fragment = new MoreHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_history, null);
        historySharePreference = new HistorySharePreference();
        nowPlayingSp = new NowPlayingSharePreference();
        btnEdit = (ImageButton) view.findViewById(R.id.btn_edit);
        btnBack = (ImageButton) view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();
                    return true;
                } else {
                    return false;
                }
            }
        });
        listView = (ListView) view.findViewById(R.id.more_history_listview);
        listSongs = new ArrayList<SongItem>();


        listSongs = historySharePreference.getHistorySongs(getActivity());
        if (listSongs == null) {
            listSongs = new ArrayList<SongItem>();
        }
        if (listSongs != null)
            Collections.reverse(listSongs);

        adapter = new HistoryAdapter(getActivity(), listSongs);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongItem songItem = listSongs.get(position);
                initDialogPlus(songItem, position);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SongItem> listSongInAdapter = adapter.listSongs;
                List<SongItem> listRemove = new ArrayList<SongItem>();
                List<Integer> listPosition = new ArrayList<Integer>();
                for (int i = 0; i < listSongInAdapter.size(); i++) {
                    SongItem song = listSongInAdapter.get(i);
                    if (song.isSelected) {
                        listRemove.add(listSongInAdapter.get(i));
                        listPosition.add(i);
                    }
                }
                showAlertDialog(listRemove, listPosition);
            }
        });
        return view;
    }

    public void showAlertDialog(final List<SongItem> listRemove, final List<Integer> listPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");
        builder.setMessage("Do you want to remove selected songs?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                historySharePreference.removeHistorySong(getActivity(), listPosition);
                for (int i = 0; i < listRemove.size(); i++)
                    listSongs.remove(listRemove.get(i));
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void initDialogPlus(final SongItem songItem, final int position) {
        Holder holder = new ViewHolder(R.layout.item_select_home);
        dialogPlus = DialogPlus.newDialog(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(Gravity.BOTTOM)
                .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
        dialogPlus.show();
        layoutPlay = (LinearLayout) holder.getInflatedView().findViewById(layout_play);
        layoutAddPlaylist = (LinearLayout) holder.getInflatedView().findViewById(layout_add2playlist);
        layoutInfo = (LinearLayout) holder.getInflatedView().findViewById(layout_info);
        layoutCancel = (LinearLayout) holder.getInflatedView().findViewById(layout_cancel);
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
                showPlaylistDialog(getActivity(), songItem);
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
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_track_information, null);
        TextView songName = (TextView) view.findViewById(R.id.song_name);
        TextView songTime = (TextView) view.findViewById(R.id.song_time);
        TextView songGenre = (TextView) view.findViewById(R.id.song_genre);
        TextView songCreatedAt = (TextView) view.findViewById(R.id.song_created_at);
        TextView songLikesCount = (TextView) view.findViewById(R.id.song_likes_count);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

        builder.setCancelable(false);
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
            historySharePreference.addHistorySong(getActivity(), songItem);
            Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
            intent.putExtra("songItem", songItem);
            intent.putExtra("position", position);
            intent.putExtra("listSongs", (Serializable) listSongs);
            intent.putExtra("toolbarName", "History Songs");
            getActivity().startActivity(intent);
            getActivity().finish();
        } else {
            Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
            intent.putExtra("songItem", songItem);
            intent.putExtra("position", position);
            intent.putExtra("listSongs", (Serializable) listSongs);
            intent.putExtra("toolbarName", "History Songs");
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
        builder.setCancelable(false);
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
