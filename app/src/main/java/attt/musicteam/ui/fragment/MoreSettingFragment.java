package attt.musicteam.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

 import attt.musicteam.R;
import attt.musicteam.sharepreference.ConnectionSharePreference;
import attt.musicteam.sharepreference.HistorySharePreference;
/**
 * Created by Hue on 11/8/2016.
 */
public class MoreSettingFragment extends Fragment {

    public ImageButton btnBack;
    public Fragment fragment;
    public FragmentTransaction transaction;
    public LinearLayout layoutClearAllHistory;
    public LinearLayout layoutConnectionTimeout;
    public ConnectionSharePreference connectionSp;
    public int connectionValue;

    public MoreSettingFragment newInstance(){
        MoreSettingFragment fragment = new MoreSettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_setting, null);

        connectionSp = new ConnectionSharePreference();
        connectionValue = connectionSp.getTimeoutValue(getActivity());
        if(connectionValue == 0){
            connectionValue = 30;
        }
        layoutClearAllHistory = (LinearLayout) view.findViewById(R.id.layout_clear_all_history);
        layoutConnectionTimeout = (LinearLayout) view.findViewById(R.id.layout_connection_timeout);
        btnBack = (ImageButton) view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        layoutClearAllHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistorySharePreference historySp = new HistorySharePreference();
                historySp.removeAllHistorySongs(getActivity());
                Toast.makeText(getActivity(), "History is cleared", Toast.LENGTH_SHORT).show();
            }
        });

        layoutConnectionTimeout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConnectionTimeoutDialog();
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

    public void showConnectionTimeoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_connection_timeout, null);
        final SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekbar_connection);
        final TextView txtConnection = (TextView) view.findViewById(R.id.txt_connection);
        seekBar.setProgress(connectionValue - 30);
        seekBar.setMax(90);
        txtConnection.setText(connectionValue + " s");
        builder.setCancelable(false);
        builder.setTitle("Connection Timeout");
        builder.setView(view);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtConnection.setText(30 + progress + " s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                connectionValue = seekBar.getProgress() + 30;
                connectionSp.saveTimeoutValue(getActivity(), connectionValue);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
