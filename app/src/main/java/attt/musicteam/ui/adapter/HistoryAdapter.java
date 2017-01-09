package attt.musicteam.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import attt.musicteam.R;
import attt.musicteam.ui.item.SongItem;


/**
 * Created by Hue on 11/8/2016.
 */
public class HistoryAdapter extends BaseAdapter {

    public List<SongItem> listSongs;
    public Context context;

    public HistoryAdapter(Context context, List<SongItem> listSongs) {
        this.listSongs = listSongs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listSongs.size();
    }

    @Override
    public Object getItem(int position) {
        return listSongs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder {
        ImageView imgCover;
        TextView tvName;
        TextView tvSinger;
        TextView tvTime;
        CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_history, parent, false);
            holder = new Holder();
            holder.imgCover = (ImageView) convertView.findViewById(R.id.img_cover);
            holder.tvName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.tvSinger = (TextView) convertView.findViewById(R.id.txt_singer);
            holder.tvTime = (TextView) convertView.findViewById(R.id.txt_time);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    SongItem song = (SongItem) cb.getTag();
                    song.setSelected(true);
                }
            });
        } else holder = (Holder) convertView.getTag();

        SongItem item = listSongs.get(position);
        Picasso.with(context).load(item.getImgCover()).into(holder.imgCover);
        holder.tvName.setText(item.getName());
        holder.tvSinger.setText(item.getSinger());
        holder.tvTime.setText(item.getTime());
        holder.checkBox.setChecked(item.isSelected);
        holder.checkBox.setTag(item);
        return convertView;
    }
}
