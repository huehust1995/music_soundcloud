package attt.musicteam.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import attt.musicteam.R;
import attt.musicteam.ui.MainActivity;

/**
 * Created by Hue on 11/8/2016.
 */
public class GenreFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;

    public GenreFragment newInstance(){
        GenreFragment fragment = new GenreFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre, null);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Genre");
        toolbar.setTitleTextColor(Color.WHITE);
        viewPager = (ViewPager) view.findViewById(R.id.genre_viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.genre_tab);
        tabLayout.addTab(tabLayout.newTab().setText("Music"), true);
        tabLayout.addTab(tabLayout.newTab().setText("Audio"));

        adapter = new PagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        return view;
    }

    public void setCurrentTabFragment(int tabPosition){
        switch (tabPosition){
            case 0:
                viewPager.setCurrentItem(0);
                break;
            case 1:
                viewPager.setCurrentItem(1);
                break;
        }
    }
    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int numOfTabs) {
            super(fm);
            this.mNumOfTabs = numOfTabs;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
           switch (position){
               case 0: GenreMusicFragment musicFragment = new GenreMusicFragment().newInstance();
                   return musicFragment;
               case 1: GenreAudioFragment audioFragment = new GenreAudioFragment().newInstance();
                   return audioFragment;
               default:
                   return null;
           }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
