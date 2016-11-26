package attt.musicteam.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import attt.musicteam.R;
import attt.musicteam.ui.adapter.GenreAdapter;
import attt.musicteam.ui.item.GenreItem;
import attt.musicteam.utils.Variables;

/**
 * Created by Hue on 11/8/2016.
 */
public class GenreAudioFragment extends Fragment {

    public ListView genreAudioList;
    public List<GenreItem> listGenreItem;
    public GenreAdapter adapter;

    public GenreAudioFragment newInstance(){
        return new GenreAudioFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre_audio, null);

        genreAudioList = (ListView) view.findViewById(R.id.genre_audio_list);
        listGenreItem = new ArrayList<GenreItem>();
        listGenreItem = new Variables().listAudioGenre();
        adapter = new GenreAdapter(getActivity(), listGenreItem);
        genreAudioList.setAdapter(adapter);
        genreAudioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GenreItem item = listGenreItem.get(position);
                Fragment fragment = new GenreDetailFragment().newInstance(item);
                FragmentTransaction transaction;
                transaction = getFragmentManager().beginTransaction().replace(R.id.main_container, fragment);
                transaction.commit();
            }
        });
        return view;
    }
}
