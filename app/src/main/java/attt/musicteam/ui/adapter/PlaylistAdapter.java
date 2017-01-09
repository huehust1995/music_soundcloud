package attt.musicteam.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import attt.musicteam.R;
import attt.musicteam.ui.item.PlaylistItem;

/**
 * Created by Hue on 11/8/2016.
 */
public class PlaylistAdapter extends BaseAdapter {

    public List<PlaylistItem> listPlaylist;
    public Context context;

    public PlaylistAdapter(Context context, List<PlaylistItem> listPlaylist) {
        this.context = context;
        this.listPlaylist = listPlaylist;
    }

    @Override
    public int getCount() {
        return listPlaylist.size();
    }

    @Override
    public Object getItem(int position) {
        return listPlaylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder {
        ImageView imgCover;
        TextView playlistName;
        TextView playlistNumTracks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_playlist, parent, false);
            holder = new Holder();
            holder.imgCover = (ImageView) convertView.findViewById(R.id.img_cover);
            holder.playlistName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.playlistNumTracks = (TextView) convertView.findViewById(R.id.txt_num_tracks);
            convertView.setTag(holder);
        } else holder = (Holder) convertView.getTag();
        PlaylistItem item = listPlaylist.get(position);
        Log.e("image", item.getImgCover() + "");
        holder.playlistName.setText(item.getName());
        if (item.getNumTracks() < 2) {
            holder.playlistNumTracks.setText(item.getNumTracks() + " track");
        } else {
            holder.playlistNumTracks.setText(item.getNumTracks() + " tracks");
        }
        return convertView;
    }
}