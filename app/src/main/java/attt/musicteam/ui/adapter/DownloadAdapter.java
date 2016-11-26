package attt.musicteam.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import attt.musicteam.R;
import attt.musicteam.ui.item.SongDownload;

/**
 * Created by Hue on 11/26/2016.
 */

public class DownloadAdapter extends ArrayAdapter<SongDownload> {
    private Context mContext;
    private List<SongDownload> songDownloadList;

    public DownloadAdapter(Context context, int resource, List<SongDownload> objects) {
        super(context, resource, objects);
        this.songDownloadList = objects;
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DownloadHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.adapter_song_download, parent, false);
            holder = new DownloadHolder(row);
            //cache
            row.setTag(holder);
        } else {
            holder = (DownloadHolder) row.getTag();
        }
        holder.populateFrom(songDownloadList.get(position));
        return row;
    }

    class DownloadHolder {
        private TextView tvNameSong;

        DownloadHolder(View row) {
            tvNameSong = (TextView) row.findViewById(R.id.txt_name);
        }

        void populateFrom(SongDownload songDownload) {
            tvNameSong.setText(songDownload.getmName());

        }
    }
}
