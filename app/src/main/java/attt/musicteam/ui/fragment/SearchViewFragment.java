package attt.musicteam.ui.fragment;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import attt.musicteam.R;
import attt.musicteam.sharepreference.HistorySharePreference;
import attt.musicteam.sharepreference.NowPlayingSharePreference;
import attt.musicteam.sharepreference.PlaylistSharePreference;
import attt.musicteam.ui.PlayMusicActivity;
import attt.musicteam.ui.adapter.ListSongAdapter;
import attt.musicteam.ui.adapter.PlaylistAdapter;
import attt.musicteam.ui.item.PlaylistItem;
import attt.musicteam.ui.item.SongItem;
import attt.musicteam.ui.progress_dialog.ProgressDialogNoTitle;
import attt.musicteam.ui.api.search.SearchRequest;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Hue on 11/8/2016.
 */
public class SearchViewFragment extends Fragment {

    private LinearLayout layoutPlay, layoutAddPlaylist, layoutInfo, layoutCancel;
    private DialogPlus dialogPlus;
    private HistorySharePreference historySong;
    private NowPlayingSharePreference nowPlayingSp;

    private ListView listView;
    private List<SongItem> listSongs;
    private ListSongAdapter adapter;
    private List<SongItem> mTracks;
    private EditText edtSearch;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private ProgressDialogNoTitle pDialog;

    public SearchViewFragment newInstance() {
        return new SearchViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_view, null);
        final ImageView imgVoice = (ImageView) view.findViewById(R.id.image_voice);
        final ImageView imgClear = (ImageView) view.findViewById(R.id.image_clear);
        imgClear.setVisibility(View.INVISIBLE);
        imgVoice.setVisibility(View.VISIBLE);
        listSongs = new ArrayList<SongItem>();
        historySong = new HistorySharePreference();
        nowPlayingSp = new NowPlayingSharePreference();
        if (nowPlayingSp.getStatePlaying(getActivity()) == NowPlayingSharePreference.IS_PLAYING) {
            // listSongs = searchSp.getSearchSongs(getActivity());
        } else {
            listSongs = new ArrayList<SongItem>();
        }

        pDialog = new ProgressDialogNoTitle(getActivity());
        edtSearch = (EditText) view.findViewById(R.id.edt_search);
        listView = (ListView) view.findViewById(R.id.search_listview);

        View.OnFocusChangeListener ofcListener = new MyFocusChangeListener();
        edtSearch.setOnFocusChangeListener(ofcListener);

        mTracks = new ArrayList<SongItem>();
        adapter = new ListSongAdapter(getActivity(), listSongs);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongItem songItem = listSongs.get(position);
                initDialogPlus(songItem, position);
            }
        });
        pDialog = new ProgressDialogNoTitle(getActivity());
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                pDialog.show();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    new SearchRequest().searchAPI(edtSearch.getText().toString()).setSearchListener(new SearchRequest.SearchListener() {
                        @Override
                        public void onSuccess(List<SongItem> message) {
                            updateTracks(message);
                            Log.e("size track", message.size() + "");
                            pDialog.dismiss();
                        }
                    });
                    return true;
                }
                return false;
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                if (query.length() != 0) {
                    imgClear.setVisibility(View.VISIBLE);
                    imgVoice.setVisibility(View.INVISIBLE);
                } else {
                    imgClear.setVisibility(View.INVISIBLE);
                    imgVoice.setVisibility(View.VISIBLE);
                }
            }
        });

        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                listSongs.clear();
                adapter.notifyDataSetChanged();
            }
        });

        imgVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        return view;
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speak_now));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    pDialog.show();
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edtSearch.setText(result.get(0));
                    new SearchRequest().searchAPI(edtSearch.getText().toString()).setSearchListener(new SearchRequest.SearchListener() {
                        @Override
                        public void onSuccess(List<SongItem> message) {
                            updateTracks(message);
                            pDialog.dismiss();
                        }
                    });
                }
                break;
            }
        }
    }

    private void updateTracks(List<SongItem> tracks) {
        listSongs.clear();
        mTracks.clear();
        mTracks.addAll((tracks));
        Log.e("size tracks", mTracks.size() + "");
        for (int i = 0; i < mTracks.size(); i++) {
            String name = mTracks.get(i).getName();
            String imgCover = mTracks.get(i).getImgCover();
            String time = mTracks.get(i).getTime();
            String id = mTracks.get(i).getId() + "";
            String genre = mTracks.get(i).getGenre();
            String createdAt = mTracks.get(i).getCreatedAt();
            String likesCount = mTracks.get(i).getLikesCount();
            SongItem songItem = new SongItem(name, genre, imgCover, (time), id, genre, createdAt, likesCount);
            listSongs.add(songItem);
        }
        adapter.notifyDataSetChanged();
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
            intent.putExtra("toolbarName", "Search Songs");
            getActivity().startActivity(intent);
            getActivity().finish();
        } else {
            Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
            intent.putExtra("songItem", songItem);
            intent.putExtra("position", position);
            intent.putExtra("listSongs", (Serializable) listSongs);
            intent.putExtra("toolbarName", "Search Songs");
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

    private class MyFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus) {

            if (v.getId() == R.id.edt_search && !hasFocus) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
    }
}
