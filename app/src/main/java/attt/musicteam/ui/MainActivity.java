package attt.musicteam.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import attt.musicteam.R;
import attt.musicteam.ui.fragment.GenreFragment;
import attt.musicteam.ui.fragment.HomeFragment;
import attt.musicteam.ui.fragment.MoreFragment;
import attt.musicteam.ui.fragment.PlaylistFragment;
import attt.musicteam.ui.fragment.SearchViewFragment;
import attt.musicteam.utils.ClearCacheMemory;


public class MainActivity extends AppCompatActivity {
    public ImageButton btnHome, btnPlaylist, btnGenre, btnSearch, btnMore;
    public LinearLayout tabHome, tabPlaylist, tabGenre, tabSearch, tabMore;
    public TextView tvHome, tvPlaylist, tvGenre, tvSearch, tvMore;
    public Fragment fragment;
    public FragmentTransaction transaction;
    public LayoutBackground setLayoutBackground;
    public String state;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initView();

    }


    public void initView() {
        setLayoutBackground = new LayoutBackground();

        btnHome = (ImageButton) findViewById(R.id.btn_tab_home);
        btnPlaylist = (ImageButton) findViewById(R.id.btn_tab_playlist);
        btnGenre = (ImageButton) findViewById(R.id.btn_tab_genre);
        btnSearch = (ImageButton) findViewById(R.id.btn_tab_search);
        btnMore = (ImageButton) findViewById(R.id.btn_tab_more);

        tabHome = (LinearLayout) findViewById(R.id.tab_home);
        tabPlaylist = (LinearLayout) findViewById(R.id.tab_playlist);
        tabGenre = (LinearLayout) findViewById(R.id.tab_genre);
        tabSearch = (LinearLayout) findViewById(R.id.tab_search);
        tabMore = (LinearLayout) findViewById(R.id.tab_more);

        tvHome = (TextView) findViewById(R.id.txt_tab_home);
        tvPlaylist = (TextView) findViewById(R.id.txt_tab_playlist);
        tvGenre = (TextView) findViewById(R.id.txt_tab_genre);
        tvSearch = (TextView) findViewById(R.id.txt_tab_search);
        tvMore = (TextView) findViewById(R.id.txt_tab_more);
        initHomeFragment();


        tabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initHomeFragment();
            }
        });
        tabPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPlaylistFragment();
            }
        });
        tabGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGenreFragment();
            }
        });
        tabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSearchFragment();
            }
        });
        tabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMoreFragment();
            }
        });
    }

    public void initHomeFragment() {
        setLayoutBackground.HomeLayoutFocus(btnHome, tvHome);
        setLayoutBackground.PlaylistLayout(btnPlaylist, tvPlaylist);
        setLayoutBackground.GenreLayout(btnGenre, tvGenre);
        setLayoutBackground.SearchLayout(btnSearch, tvSearch);
        setLayoutBackground.MoreLayout(btnMore, tvMore);
        fragment = new HomeFragment().newInstance();
        transaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment);
        transaction.commit();
    }

    public void initPlaylistFragment() {
        setLayoutBackground.HomeLayout(btnHome, tvHome);
        setLayoutBackground.PlaylistLayoutFocus(btnPlaylist, tvPlaylist);
        setLayoutBackground.GenreLayout(btnGenre, tvGenre);
        setLayoutBackground.SearchLayout(btnSearch, tvSearch);
        setLayoutBackground.MoreLayout(btnMore, tvMore);
        fragment = new PlaylistFragment().newInstance();
        transaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment);
        transaction.commit();
    }

    public void initGenreFragment() {
        setLayoutBackground.HomeLayout(btnHome, tvHome);
        setLayoutBackground.PlaylistLayout(btnPlaylist, tvPlaylist);
        setLayoutBackground.GenreLayoutFocus(btnGenre, tvGenre);
        setLayoutBackground.SearchLayout(btnSearch, tvSearch);
        setLayoutBackground.MoreLayout(btnMore, tvMore);
        fragment = new GenreFragment().newInstance();
        transaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment);
        transaction.commit();
    }

    public void initSearchFragment() {
        setLayoutBackground.HomeLayout(btnHome, tvHome);
        setLayoutBackground.PlaylistLayout(btnPlaylist, tvPlaylist);
        setLayoutBackground.GenreLayout(btnGenre, tvGenre);
        setLayoutBackground.SearchLayoutFocus(btnSearch, tvSearch);
        setLayoutBackground.MoreLayout(btnMore, tvMore);
        fragment = new SearchViewFragment().newInstance();
        transaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment);
        transaction.commit();
    }

    public void initMoreFragment() {
        setLayoutBackground.HomeLayout(btnHome, tvHome);
        setLayoutBackground.PlaylistLayout(btnPlaylist, tvPlaylist);
        setLayoutBackground.GenreLayout(btnGenre, tvGenre);
        setLayoutBackground.SearchLayout(btnSearch, tvSearch);
        setLayoutBackground.MoreLayoutFocus(btnMore, tvMore);
        fragment = new MoreFragment().newInstance();
        transaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            ClearCacheMemory.trimCache(this);
        } catch (Exception e) {

        }
    }

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            moveTaskToBack(true);
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else {
            Toast.makeText(this, "Press once again to go home screen!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}
