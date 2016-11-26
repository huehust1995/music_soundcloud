package attt.musicteam.ui.fragment;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import attt.musicteam.R;
import attt.musicteam.sharepreference.PlaylistSharePreference;
import attt.musicteam.ui.adapter.DownloadAdapter;
import attt.musicteam.ui.item.PlaylistItem;
import attt.musicteam.ui.item.SongDownload;
import attt.musicteam.ui.item.SongItem;

import static android.R.attr.fragment;
import static android.R.attr.theme;

/**
 * Created by Hue on 11/26/2016.
 */

public class DownloadFragment extends Fragment {
    private ListView listView;
    private ImageButton btnBack;
    private DownloadAdapter downloadAdapter;
    private List<SongDownload> songDownloadList;
    final String MEDIA_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .getPath() + "/";
    private List<SongDownload> songsList = new ArrayList<>();
    private String mp3Pattern = ".mp3";
    private MediaPlayer mediaPlayer;
    private LinearLayout layoutPlay, layoutInfo, layoutCancel, layoutDelete;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.download_fragment, null);
        listView = (ListView) view.findViewById(R.id.lv_song_download);
        btnBack = (ImageButton) view.findViewById(R.id.btn_back);
        songDownloadList = new ArrayList<>();
        songDownloadList = getPlayList();
        downloadAdapter = new DownloadAdapter(getActivity(), 1, songDownloadList);
        listView.setAdapter(downloadAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initDialogPlus(position);
            }
        });
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
        return view;
    }

    public void initDialogPlus(final int position) {
        Holder holder = new ViewHolder(R.layout.item_select_song_download);
        final DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                .setContentHolder(holder)
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
        dialogPlus.show();
        layoutPlay = (LinearLayout) holder.getInflatedView().findViewById(R.id.layout_play);
        layoutCancel = (LinearLayout) holder.getInflatedView().findViewById(R.id.layout_cancel);
        layoutPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PATH_TO_FILE = "/storage/emulated/0/Download/" + songDownloadList.get(position).getmName();
                Log.e("path", PATH_TO_FILE);
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                File file = new File(PATH_TO_FILE);
                intent.setDataAndType(Uri.fromFile(file), "audio/mp3");
                startActivity(intent);
            }
        });
        layoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
            }
        });
    }

    public List<SongDownload> getPlayList() {
        Log.e("path", MEDIA_PATH);
        if (MEDIA_PATH != null) {
            File home = new File(MEDIA_PATH);
            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    System.out.println(file.getAbsolutePath());
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
        }
        // return songs list array
        return songsList;
    }

    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }

                }
            }
        }
    }

    private void addSongToList(File song) {
        if (song.getName().endsWith(mp3Pattern)) {
            songsList.add(new SongDownload(song.getPath(), song.getName()));
        }
    }
}
