package attt.musicteam.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import attt.musicteam.R;
import attt.musicteam.sharepreference.HistorySharePreference;
import attt.musicteam.sharepreference.NowPlayingSharePreference;
import attt.musicteam.sharepreference.PlaylistSharePreference;
import attt.musicteam.ui.PlayMusicActivity;
import attt.musicteam.ui.adapter.ListSongAdapter;
import attt.musicteam.ui.adapter.PlaylistAdapter;
import attt.musicteam.ui.item.GenreItem;
import attt.musicteam.ui.item.PlaylistItem;
import attt.musicteam.ui.item.SongItem;
import attt.musicteam.ui.progress_dialog.ProgressDialogNoTitle;
import attt.musicteam.utils.AppController;
import attt.musicteam.utils.JsonUTF8Request;
import attt.musicteam.utils.ReadWriteData;
import attt.musicteam.utils.Variables;

import static attt.musicteam.R.id.layout_add2playlist;
import static attt.musicteam.R.id.layout_cancel;
import static attt.musicteam.R.id.layout_info;
import static attt.musicteam.R.id.layout_play;

/**
 * Created by Hue on 11/8/2016.
 */
public class GenreDetailFragment extends Fragment {

    public String url1 = "https://api-v2.soundcloud.com/charts?kind=trending&genre=";
    public String genreApi = "";
    public String url2 = "&client_id=";
    public String clientId = "";
    private LinearLayout layoutPlay, layoutAddPlaylist, layoutInfo, layoutCancel;
    private DialogPlus dialogPlus;
    public ImageButton btnBack;
    public ProgressDialogNoTitle pDialog;
    public TextView genreDetailName;
    public ListView genreDetailListView;
    public ListSongAdapter adapter;
    public List<SongItem> listSongs;
    public GenreItem genreItem;

    public NowPlayingSharePreference nowPlayingSp;
    public HistorySharePreference historySong;

    public GenreDetailFragment newInstance(GenreItem genreItem) {
        GenreDetailFragment fragment = new GenreDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("genreItem", genreItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        genreItem = (GenreItem) getArguments().get("genreItem");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre_detail, null);
        initComponent(view);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment fragment = new GenreFragment().newInstance();
                    FragmentTransaction transaction;
                    transaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment);
                    transaction.commit();
                    return true;
                } else {
                    return false;
                }
            }
        });
        return view;
    }

    public void initComponent(View view) {
        nowPlayingSp = new NowPlayingSharePreference();
        historySong = new HistorySharePreference();

        pDialog = new ProgressDialogNoTitle(getActivity());
        pDialog.show();
        listSongs = new ArrayList<SongItem>();

        btnBack = (ImageButton) view.findViewById(R.id.btn_back);
        genreDetailName = (TextView) view.findViewById(R.id.genre_detail_name);
        genreDetailListView = (ListView) view.findViewById(R.id.genre_detail_listview);
        genreDetailName.setText(genreItem.getGenreName());

        adapter = new ListSongAdapter(getActivity(), listSongs);
        genreDetailListView.setAdapter(adapter);

        genreApi = genreItem.getGenreApi();
        clientId = Variables.CLIENT_ID;
        String url = url1 + genreApi + url2 + clientId;
        JsonUTF8Request request = new JsonUTF8Request(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ReadWriteData rw = new ReadWriteData();
                rw.writeGenreData(response.toString());
                try {
                    JSONArray collection = response.getJSONArray("collection");
                    for (int i = 0; i < collection.length(); i++) {
                        JSONObject object = collection.getJSONObject(i);
                        JSONObject track = object.getJSONObject("track");
                        String imgCover = track.getString("artwork_url");
                        String id = track.getString("id");
                        String name = track.getString("title");
                        String singer = track.getString("genre");
                        String time = track.getString("duration");
                        String genre = track.getString("genre");
                        String likesCount = track.getString("likes_count");
                        String createdAt = track.getString("created_at");
                        SongItem songItem = new SongItem(name, singer, imgCover, new Variables().timeTrack(time), id, genre, createdAt, likesCount);
                        listSongs.add(songItem);

                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(request, "");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new GenreFragment().newInstance();
                FragmentTransaction transaction;
                transaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment);
                transaction.commit();
            }
        });

        genreDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongItem songItem = listSongs.get(position);
                initDialogPlus(songItem, position);
            }
        });
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
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_track_information, null);
        TextView songName = (TextView) view.findViewById(R.id.song_name);
        TextView songTime = (TextView) view.findViewById(R.id.song_time);
        TextView songGenre = (TextView) view.findViewById(R.id.song_genre);
        TextView songCreatedAt = (TextView) view.findViewById(R.id.song_created_at);
        TextView songLikesCount = (TextView) view.findViewById(R.id.song_likes_count);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
            historySong.addHistorySong(getActivity(), songItem);
            Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
            intent.putExtra("songItem", songItem);
            intent.putExtra("position", position);
            intent.putExtra("listSongs", (Serializable) listSongs);
            intent.putExtra("toolbarName", genreItem.getGenreName());
            getActivity().startActivity(intent);
            getActivity().finish();
        } else if (nowPlayingSp.getStatePlaying(getActivity()) == NowPlayingSharePreference.IS_PLAYING) {
            Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
            intent.putExtra("songItem", songItem);
            intent.putExtra("position", position);
            intent.putExtra("listSongs", (Serializable) listSongs);
            intent.putExtra("toolbarName", genreItem.getGenreName());
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
