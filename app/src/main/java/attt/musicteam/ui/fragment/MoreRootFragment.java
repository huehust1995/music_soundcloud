package attt.musicteam.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import attt.musicteam.R;
import attt.musicteam.ui.MainActivity;

import static attt.musicteam.R.id.layout_about;
import static attt.musicteam.R.id.layout_contactus;
import static attt.musicteam.R.id.layout_download;
import static attt.musicteam.R.id.layout_history;
import static attt.musicteam.R.id.layout_setting;


/**
 * Created by Hue on 11/8/2016.
 */
public class MoreRootFragment extends Fragment {

    public Fragment fragment;
    public FragmentTransaction transaction;
    public LinearLayout layoutDownload, layoutSetting, layoutHistory, layoutContactus, layoutAbout;
    public Toolbar toolbar;

    public MoreRootFragment newInstance(){
        MoreRootFragment fragment = new MoreRootFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_root, null);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("More");
        layoutSetting = (LinearLayout) view.findViewById(layout_setting);
        layoutDownload = (LinearLayout) view.findViewById(layout_download);
        layoutHistory = (LinearLayout) view.findViewById(layout_history);
        layoutContactus = (LinearLayout) view.findViewById(layout_contactus);
        layoutAbout = (LinearLayout) view.findViewById(layout_about);
        layoutSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragment = new MoreSettingFragment().newInstance();
                        transaction = getFragmentManager().beginTransaction().replace(R.id.more_container, fragment);
                        transaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter, R.anim.pop_exit);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }, 500);
            }
        });

        layoutDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragment = new DownloadFragment();
                        transaction = getFragmentManager().beginTransaction().replace(R.id.more_container, fragment);
                        transaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter, R.anim.pop_exit);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }, 500);
            }
        });

        layoutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragment = new MoreHistoryFragment().newInstance();
                        transaction = getFragmentManager().beginTransaction().replace(R.id.more_container, fragment);
                        transaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter, R.anim.pop_exit);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }, 500);
            }
        });

        layoutContactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeEmail("huecun1601@gmail.com", "");
            }
        });

        layoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragment = new MoreAboutFragment().newInstance();
                        transaction = getFragmentManager().beginTransaction().replace(R.id.more_container, fragment);
                        transaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter, R.anim.pop_exit);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }, 500);

            }
        });
        return view;
    }

    public void composeEmail(String addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + addresses)); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
