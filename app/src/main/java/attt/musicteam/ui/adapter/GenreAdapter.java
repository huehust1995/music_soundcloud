package attt.musicteam.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import attt.musicteam.R;
import attt.musicteam.ui.item.GenreItem;


/**
 * Created by Hue on 11/8/2016.
 */
public class GenreAdapter extends BaseAdapter {

    public List<GenreItem> listGenreItem;
    public Context context;

    public GenreAdapter(Context context, List<GenreItem> listGenreItem){
        this.context = context;
        this.listGenreItem = listGenreItem;
    }


    @Override
    public int getCount() {
        return listGenreItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listGenreItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder{
        TextView genreName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_genre, parent, false);
            holder = new Holder();
            holder.genreName = (TextView) convertView.findViewById(R.id.genre_name);
            convertView.setTag(holder);
        } else holder = (Holder) convertView.getTag();
        holder.genreName.setText(listGenreItem.get(position).getGenreName());
        return convertView;
    }
}
