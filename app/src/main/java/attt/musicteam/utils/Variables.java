package attt.musicteam.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import attt.musicteam.R;
import attt.musicteam.sharepreference.HistorySharePreference;
import attt.musicteam.sharepreference.PlaylistSharePreference;
import attt.musicteam.sharepreference.StateSharePreference;
import attt.musicteam.ui.PlayMusicActivity;
import attt.musicteam.ui.adapter.PlaylistAdapter;
import attt.musicteam.ui.item.GenreItem;
import attt.musicteam.ui.item.PlaylistItem;
import attt.musicteam.ui.item.SongItem;

/**
 * Created by Hue on 11/8/2016.
 */
public class Variables {
    public static final String CLIENT_ID = "c88fc9f0da3d025ed0f1ab91cab55a59";
    public static final String HOME = "home";
    public static final String GENRE = "genre";

    private LinearLayout layoutPlay, layoutAddPlaylist, layoutInfo, layoutCancel;
    private DialogPlus dialogPlus;
    private HistorySharePreference historySong;

    public Variables() {

    }

    public String timeTrack(String duration) {
        Double durationDb = Double.parseDouble(duration);
        int minutes = (int) Math.floor(durationDb / 60000);
        int seconds = (int) (durationDb % 60000) / 1000;
        return minutes + ":" + (seconds < 10 ? '0' : "") + seconds;
    }

    public List<GenreItem> listMusicGenre() {
        List<GenreItem> list = new ArrayList<GenreItem>();
        list.add(new GenreItem("Alternative Rock", "soundcloud:genres:alternativerock"));
        list.add(new GenreItem("Ambient", "soundcloud:genres:ambient"));
        list.add(new GenreItem("Classical", "soundcloud:genres:classical"));
        list.add(new GenreItem("Country", "soundcloud:genres:country"));
        list.add(new GenreItem("Dance & EDM", "soundcloud:genres:danceedm"));
        list.add(new GenreItem("Dancehall", "soundcloud:genres:dancehall"));
        list.add(new GenreItem("Deep House", "soundcloud:genres:deephouse"));
        list.add(new GenreItem("Disco", "soundcloud:genres:disco"));
        list.add(new GenreItem("Drum & Bass", "soundcloud:genres:drumbass"));
        list.add(new GenreItem("Dubstep", "soundcloud:genres:dubstep"));
        list.add(new GenreItem("Electronic", "soundcloud:genres:electronic"));
        list.add(new GenreItem("Folk & Singer - Songwriter", "soundcloud:genres:folksingersongwriter"));
        list.add(new GenreItem("Hip-hop & Rap", "soundcloud:genres:hiphoprap"));
        list.add(new GenreItem("House", "soundcloud:genres:house"));
        list.add(new GenreItem("Indie", "soundcloud:genres:indie"));
        list.add(new GenreItem("Jazz & Blues", "soundcloud:genres:jazzblues"));
        list.add(new GenreItem("Latin", "soundcloud:genres:latin"));
        list.add(new GenreItem("Metal", "soundcloud:genres:metal"));
        list.add(new GenreItem("Piano", "soundcloud:genres:piano"));
        list.add(new GenreItem("Pop", "soundcloud:genres:pop"));
        list.add(new GenreItem("R&B & Soul", "soundcloud:genres:rbsoul"));
        list.add(new GenreItem("Reggae", "soundcloud:genres:reggae"));
        list.add(new GenreItem("Reggaeton", "soundcloud:genres:reggaeton"));
        list.add(new GenreItem("Rock", "soundcloud:genres:rock"));
        list.add(new GenreItem("Soundtrack", "soundcloud:genres:soundtrack"));
        list.add(new GenreItem("Techno", "soundcloud:genres:techno"));
        list.add(new GenreItem("Trance", "soundcloud:genres:trance"));
        list.add(new GenreItem("Trap", "soundcloud:genres:trap"));
        list.add(new GenreItem("Triphop", "soundcloud:genres:triphop"));
        list.add(new GenreItem("World", "soundcloud:genres:world"));

        return list;
    }

    public List<GenreItem> listAudioGenre() {
        List<GenreItem> list = new ArrayList<GenreItem>();
        list.add(new GenreItem("Audiobooks", "soundcloud:genres:audiobooks"));
        list.add(new GenreItem("Business", "soundcloud:genres:business"));
        list.add(new GenreItem("Comedy", "soundcloud:genres:comedy"));
        list.add(new GenreItem("Entertainment", "soundcloud:genres:entertainment"));
        list.add(new GenreItem("Learning", "soundcloud:genres:learning"));
        list.add(new GenreItem("News & Politics", "soundcloud:genres:newspolitics"));
        list.add(new GenreItem("Religion & Spirituality", "soundcloud:genres:religionspirituality"));
        list.add(new GenreItem("Science", "soundcloud:genres:science"));
        list.add(new GenreItem("Sports", "soundcloud:genres:sports"));
        list.add(new GenreItem("Storytelling", "soundcloud:genres:storytelling"));
        list.add(new GenreItem("Technology", "soundcloud:genres:technology"));

        return list;
    }

    public void initDialogPlus(final SongItem songItem, final int position, final Context context) {
        historySong = new HistorySharePreference();
        Holder holder = new ViewHolder(R.layout.item_select_home);
        dialogPlus = DialogPlus.newDialog(context)
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
                historySong.addHistorySong(context, songItem);
                playMusic(context, songItem, position);
                dialogPlus.dismiss();
            }
        });
        layoutAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
                showPlaylistDialog(context, songItem);
            }
        });
        layoutInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initInformationDialog(songItem, context);
                        dialogPlus.dismiss();
                    }
                }, 200);

            }
        });
        layoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
            }
        });
    }

    public void initInformationDialog(SongItem songItem, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_track_information, null);
        TextView songName = (TextView) view.findViewById(R.id.song_name);
        TextView songTime = (TextView) view.findViewById(R.id.song_time);
        TextView songGenre = (TextView) view.findViewById(R.id.song_genre);
        TextView songCreatedAt = (TextView) view.findViewById(R.id.song_created_at);
        TextView songLikesCount = (TextView) view.findViewById(R.id.song_likes_count);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

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

    public void playMusic(Context context, SongItem songItem, int position) {
        //khoi tao service chay nhac
        StateSharePreference stateSp = new StateSharePreference();
        stateSp.saveState(context, StateSharePreference.SEARCH_STATE);
        Intent intent = new Intent(context, PlayMusicActivity.class);
//        intent.putExtra("id", "mediaId");
        intent.putExtra("songItem", songItem);
        intent.putExtra("position", position);
        context.startActivity(intent);

    }

    public void showPlaylistDialog(final Context context, final SongItem songItem) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_show_playlist, null);
        ListView listView = (ListView) view.findViewById(R.id.dialog_playlist_listview);
        PlaylistAdapter adapter;
        final List<PlaylistItem> listPlaylist;
        final PlaylistSharePreference sharePreference = new PlaylistSharePreference();
        listPlaylist = sharePreference.getPlaylistItems(context);
        adapter = new PlaylistAdapter(context, listPlaylist);
        listView.setAdapter(adapter);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
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
    }


}
