package attt.musicteam.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import attt.musicteam.ui.adapter.PlaylistAdapter;
import attt.musicteam.ui.item.PlaylistItem;
import attt.musicteam.ui.item.SongItem;

/**
 * Created by Hue on 11/8/2016.
 */
public class PlaylistFragment extends Fragment {

    private DialogPlus dialogPlus;
    private LinearLayout layoutView, layoutRename, layoutDelete, layoutCancel;
    private ImageButton btnNowPlaying, btnAddPlaylist;

    private ListView listView;
    private PlaylistAdapter adapter;
    private List<PlaylistItem> listPlaylist;
    private PlaylistSharePreference playlistSharePreference;
    private boolean checkPlaylistExisted;

    private StateSharePreference stateSp;
    private NowPlayingSharePreference nowPlayingSp;
    private HistorySharePreference historySp;

    public PlaylistFragment newInstance(){
        PlaylistFragment fragment = new PlaylistFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, null);

        btnNowPlaying = (ImageButton) view.findViewById(R.id.btn_now_playing);
        btnAddPlaylist = (ImageButton) view.findViewById(R.id.btn_add_playlist);

        playlistSharePreference = new PlaylistSharePreference();
        nowPlayingSp = new NowPlayingSharePreference();
        stateSp = new StateSharePreference();
        historySp = new HistorySharePreference();

        if(nowPlayingSp.getStatePlaying(getActivity()) == NowPlayingSharePreference.IS_PLAYING){
            btnNowPlaying.setVisibility(View.VISIBLE);
        } else btnNowPlaying.setVisibility(View.INVISIBLE);

        listView = (ListView) view.findViewById(R.id.playlist_listview);
        listPlaylist = new ArrayList<PlaylistItem>();
        listPlaylist = playlistSharePreference.getPlaylistItems(getActivity());
        adapter = new PlaylistAdapter(getActivity(), listPlaylist);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Holder holder = new ViewHolder(R.layout.item_select_playlist);
                dialogPlus = DialogPlus.newDialog(getActivity())
                        .setContentHolder(holder)
                        .setCancelable(true)
                        .setGravity(Gravity.BOTTOM)
                        .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT)
                        .create();
                dialogPlus.show();
                initDialogPlus(holder, position);
            }
        });

        btnNowPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSp.saveState(getActivity(), StateSharePreference.PLAYLIST_STATE);
                getActivity().finish();
            }
        });

        btnAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddPlaylistDialog();
            }
        });

        return view;
    }

    public void initDialogPlus(Holder holder, final int position){
        layoutView = (LinearLayout) holder.getInflatedView().findViewById(R.id.layout_view);
        layoutRename = (LinearLayout) holder.getInflatedView().findViewById(R.id.layout_rename);
        layoutDelete = (LinearLayout) holder.getInflatedView().findViewById(R.id.layout_delete);
        layoutCancel = (LinearLayout) holder.getInflatedView().findViewById(R.id.layout_cancel);
        layoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String playlistName = listPlaylist.get(position).getName();
                dialogPlus.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<SongItem> listSongs = new ArrayList<SongItem>();
                        listSongs = playlistSharePreference.getSongsInPlaylist(getActivity(), playlistName);
                        if(listSongs != null) {
                            if (nowPlayingSp.getStatePlaying(getActivity()) == NowPlayingSharePreference.NOT_PLAYING) {
                                stateSp.saveState(getActivity(), StateSharePreference.PLAYLIST_STATE);
                                historySp.addHistorySong(getActivity(), listSongs.get(0));
                                Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
                                intent.putExtra("songItem", listSongs.get(0));
                                intent.putExtra("position", position);
                                intent.putExtra("listSongs", (Serializable) listSongs);
                                intent.putExtra("toolbarName", playlistName);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            } else if (nowPlayingSp.getStatePlaying(getActivity()) == NowPlayingSharePreference.IS_PLAYING) {
                                stateSp.saveState(getActivity(), StateSharePreference.PLAYLIST_STATE);
                                historySp.addHistorySong(getActivity(), listSongs.get(0));
                                Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
                                intent.putExtra("songItem", listSongs.get(0));
                                intent.putExtra("position", position);
                                intent.putExtra("listSongs", (Serializable) listSongs);
                                intent.putExtra("toolbarName", playlistName);
                                getActivity().setResult(PlayMusicActivity.RESULT_CODE, intent);
                                getActivity().finish();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Playlist is empty!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1000);

            }
        });
        layoutRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditPlaylistDialog(position);
                dialogPlus.dismiss();
            }
        });
        layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete");
                builder.setMessage("Do you want to delete " + listPlaylist.get(position).getName());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playlistSharePreference.removePlaylistItem(getActivity(), position);
                        playlistSharePreference.removeAllSongInPlaylist(getActivity(), listPlaylist.get(position).getName());
                        listPlaylist.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
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

    //dialog them playlist
    public void openAddPlaylistDialog(){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_add_playlist, null);
        final EditText edtPlaylist = (EditText) view.findViewById(R.id.edt_playlist);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCancelable(false);
        builder.setView(view);
        builder.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playlistName = edtPlaylist.getText().toString();
                checkPlaylistExisted = checkPlaylistNameIsExisted(playlistName);
                if(checkPlaylistExisted == true){
                    Toast.makeText(getActivity(), "Playlist name is existed!", Toast.LENGTH_SHORT).show();
                } else {
                    addNewPlaylist(playlistName);
                    dialog.dismiss();
                }

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        Button btnPositive = builder.create().getButton(DialogInterface.BUTTON_POSITIVE);
        if(btnPositive != null) btnPositive.setTextColor(getResources().getColor(Color.parseColor("#EC2B4A")));
    }

    public void addNewPlaylist(String playlistName){
        int imgCover = R.drawable.ic_soundcloud;
        int numTracks = 0;
        PlaylistItem item = new PlaylistItem(imgCover, playlistName, numTracks);
        playlistSharePreference.addPlaylistItem(getActivity(), item);
        listPlaylist.add(item);
//        adapter.notifyDataSetChanged();
    }

    //dialog chinh sua playlist
    public void openEditPlaylistDialog(final int position){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_edit_playlist, null);
        final EditText edtPlaylist = (EditText) view.findViewById(R.id.edt_playlist);
        edtPlaylist.setText(listPlaylist.get(position).getName());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCancelable(false);
        builder.setView(view);
        builder.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playlistName = edtPlaylist.getText().toString();
                checkPlaylistExisted = checkPlaylistNameIsExisted(playlistName);
                if(checkPlaylistExisted == true){
                    Toast.makeText(getActivity(), "Playlist name is existed!", Toast.LENGTH_SHORT).show();
                } else {
                    editPlaylist(playlistName, position);
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        Button btnPositive = builder.create().getButton(DialogInterface.BUTTON_POSITIVE);
        if(btnPositive != null) btnPositive.setTextColor(getResources().getColor(Color.parseColor("#EC2B4A")));
    }

    public void editPlaylist(String playlistName, int position){
        listPlaylist.get(position).setName(playlistName);
        playlistSharePreference.editPlaylistItemName(getActivity(), position, playlistName);
//        adapter.notifyDataSetChanged();
    }

    public boolean checkPlaylistNameIsExisted(String playlistName){
        for(int i = 0; i < listPlaylist.size(); i++){
            if(listPlaylist.get(i).getName().equalsIgnoreCase(playlistName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
