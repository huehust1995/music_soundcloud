package attt.musicteam.ui;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import attt.musicteam.R;
import attt.musicteam.sharepreference.ConnectionSharePreference;
import attt.musicteam.sharepreference.HistorySharePreference;
import attt.musicteam.sharepreference.NowPlayingSharePreference;
import attt.musicteam.ui.adapter.ListSongAdapter;
import attt.musicteam.ui.item.SongItem;
import attt.musicteam.ui.progress_dialog.ProgressDialogNoTitle;
import attt.musicteam.utils.ClearCacheMemory;
import attt.musicteam.utils.Utilities;
import attt.musicteam.utils.Variables;


public class PlayMusicActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    public static final int REQUEST_CODE = 0;
    public static final int RESULT_CODE = 1;

    private String url1 = "http://api.soundcloud.com/tracks/";
    private String url2 = "/stream?client_id=";
    private String url = "";
    public TextView txtToolbarName;
    public ImageButton btnBack;
    public ImageButton btnShuffle, btnPrevious, btnPlay, btnNext, btnRepeat;
    public TextView songName, songSinger, timeDuration, timeCurrent;
    public ImageView songCover;
    public String toolbarName;
    public SeekBar seekBar;
    public ProgressDialogNoTitle pDialog;

    public int currentSongIndex;
    public Utilities utilities;
    public Handler mHandler = new Handler();
    public boolean isShuffle;
    public boolean isRepeat;
    public ListView playListView;
    public List<SongItem> listSongs;
    public ListSongAdapter adapter;
    public SongItem nowPlaying;
    public MediaPlayer mediaPlayer;
    public PlayMusic playAsync;

    //timer count down
    public CountDownTimer countDownTimer;
    public int timeoutValue;
    public ConnectionSharePreference connectionSp;
    public boolean timeHasStop;
    public Thread playThread;

    private NowPlayingSharePreference nowPlayingSp;
    private HistorySharePreference historySong;

    //headshet
    private int headsetSwitch = 1;
    //phone coming
    private boolean isPauseInCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    private TextView tvDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        listSongs = new ArrayList<SongItem>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nowPlaying = (SongItem) bundle.get("songItem");
            currentSongIndex = bundle.getInt("position");
            listSongs = (List<SongItem>) bundle.get("listSongs");
            toolbarName = bundle.getString("toolbarName");
        } else nowPlaying = new SongItem();

        //check headset connected
        registerReceiver(headsetReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mediaPlayer != null) {
                            if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                            isPauseInCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (mediaPlayer != null) {
                            if (isPauseInCall) {
                                isPauseInCall = false;
                                if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                            }
                        }
                        break;
                }
            }
        };
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        initComponent();
    }

    private BroadcastReceiver headsetReceiver = new BroadcastReceiver() {
        public boolean isHeadsetConnected = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")) {
                if (isHeadsetConnected && intent.getIntExtra("state", 0) == 0) {
                    isHeadsetConnected = false;
                    headsetSwitch = 0;
                } else if (!isHeadsetConnected && intent.getIntExtra("state", 0) == 1) {
                    isHeadsetConnected = true;
                    headsetSwitch = 1;
                }
            }
            switch (headsetSwitch) {
                case 0:
                    headsetDisconnected();
                    break;
                case 1:
                    break;
            }
        }
    };

    private void headsetDisconnected() {
        mediaPlayer.pause();
        btnPlay.setImageResource(R.drawable.btn_play);
    }


    public void initComponent() {
        playThread = new Thread();
        initSharePreferences();

        countDownTimer.start();
        pDialog = new ProgressDialogNoTitle(this);
        pDialog.setCancelable(false);
        mediaPlayer = new MediaPlayer();
        utilities = new Utilities();
        txtToolbarName = (TextView) findViewById(R.id.toolbar_name);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnShuffle = (ImageButton) findViewById(R.id.btn_shuffle);
        btnPrevious = (ImageButton) findViewById(R.id.btn_previous);
        btnPlay = (ImageButton) findViewById(R.id.btn_play);
        btnNext = (ImageButton) findViewById(R.id.btn_next);
        btnRepeat = (ImageButton) findViewById(R.id.btn_repeat);
        playListView = (ListView) findViewById(R.id.play_listview);
        seekBar = (SeekBar) findViewById(R.id.song_seekbar);

        songCover = (ImageView) findViewById(R.id.song_cover);
        songName = (TextView) findViewById(R.id.song_name);
        songSinger = (TextView) findViewById(R.id.song_singer);
        timeDuration = (TextView) findViewById(R.id.time_duration);
        timeCurrent = (TextView) findViewById(R.id.time_current);

        tvDownload = (TextView) findViewById(R.id.tv_download);

        final Animation animScale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        final Animation animScale2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);

        txtToolbarName.setText(toolbarName);
        setSongInfo(nowPlaying);

        //set data to listview
        adapter = new ListSongAdapter(this, listSongs);
        playListView.setAdapter(adapter);
        setListViewPosition();

        //choi nhac
        playAsync = new PlayMusic();
        playAsync.execute();
        //setup for button
        seekBar.setOnSeekBarChangeListener(this);
        mediaPlayer.setOnCompletionListener(this);
        seekBar.setProgress(0);
        seekBar.setMax(100);

        tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDownload.startAnimation(animScale);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvDownload.startAnimation(animScale2);
                    }
                }, 150);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        url = url1 + nowPlaying.getId() + url2 + Variables.CLIENT_ID;
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        request.setDescription(nowPlaying.getName());
                        request.setTitle("Download!");
                        // in order for this if to run, you must use the android 3.2 to compile your app

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        }
                        String name = nowPlaying.getName();
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name + ".mp3");

                        // get download service and enqueue file
                        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        manager.enqueue(request);
                    }
                }, 400);

            }
        });
        playListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                btnPlay.setImageResource(R.drawable.btn_play);
                currentSongIndex = position;
                setListViewPosition();
                nowPlaying = listSongs.get(position);
                playSong();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        btnPlay.setImageResource(R.drawable.btn_play);
                    }
                } else {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        btnPlay.setImageResource(R.drawable.btn_pause);
                    }
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayMusicActivity.this, MainActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShuffle == false) {
                    btnShuffle.setImageDrawable(getResources().getDrawable(R.drawable.btn_shuffle_focused));
                    isShuffle = true;
                    Toast.makeText(getApplicationContext(), "Shuffle on", Toast.LENGTH_SHORT).show();
                } else {
                    btnShuffle.setImageDrawable(getResources().getDrawable(R.drawable.btn_shuffle));
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRepeat == false) {
                    btnRepeat.setImageDrawable(getResources().getDrawable(R.drawable.btn_repeat_focused));
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat on", Toast.LENGTH_SHORT).show();
                } else {
                    btnRepeat.setImageDrawable(getResources().getDrawable(R.drawable.btn_repeat));
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat off", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setImageResource(R.drawable.btn_play);
                if (isShuffle) {
                    Random rand = new Random();
                    currentSongIndex = rand.nextInt((listSongs.size() - 1) - 0 + 1) + 0;
                    nowPlaying = listSongs.get(currentSongIndex);
                    playSong();
                    currentSongIndex = currentSongIndex + 1;
                    setListViewPosition();
                } else if (currentSongIndex < (listSongs.size() - 1)) {
                    nowPlaying = listSongs.get(currentSongIndex + 1);
                    playSong();
                    currentSongIndex = currentSongIndex + 1;
                    setListViewPosition();
                } else {
                    nowPlaying = listSongs.get(0);
                    playSong();
                    currentSongIndex = 0;
                    setListViewPosition();
                }

            }

        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setImageResource(R.drawable.btn_play);
                if (isShuffle) {
                    Random rand = new Random();
                    currentSongIndex = rand.nextInt((listSongs.size() - 1) - 0 + 1) + 0;
                    nowPlaying = listSongs.get(currentSongIndex);
                    playSong();
                    currentSongIndex = currentSongIndex + 1;
                    setListViewPosition();
                } else if (currentSongIndex > 0) {
                    nowPlaying = listSongs.get(currentSongIndex - 1);
                    playSong();
                    currentSongIndex = currentSongIndex - 1;
                    setListViewPosition();
                } else {
                    nowPlaying = listSongs.get(listSongs.size() - 1);
                    playSong();
                    currentSongIndex = listSongs.size() - 1;
                }
            }
        });

    }

    public void initSharePreferences() {
        connectionSp = new ConnectionSharePreference();
        nowPlayingSp = new NowPlayingSharePreference();
        historySong = new HistorySharePreference();
        nowPlayingSp.setStatePlaying(this, NowPlayingSharePreference.IS_PLAYING);
        timeoutValue = connectionSp.getTimeoutValue(this);
        if (timeoutValue == 0) {
            timeoutValue = 30;
        }
        countDownTimer = new CountDownTimer(timeoutValue * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("timer", millisUntilFinished + "");
            }

            @Override
            public void onFinish() {
                Toast.makeText(PlayMusicActivity.this, "Connection Timeout", Toast.LENGTH_SHORT).show();
                if (playAsync.getStatus() == AsyncTask.Status.RUNNING)
                    playAsync.cancel(true);
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                nowPlayingSp.setStatePlaying(PlayMusicActivity.this, NowPlayingSharePreference.NOT_PLAYING);
                finish();
                startActivity(new Intent(PlayMusicActivity.this, MainActivity.class));
            }
        };
    }

    //hien thi thong tin bai hat
    public void setSongInfo(SongItem item) {
        songName.setText(item.getName());
        songSinger.setText(item.getSinger());
        timeDuration.setText(item.getTime());
        timeCurrent.setText("0:00");
        // Picasso.with(this).load(item.getImgCover()).transform(new jp.wasabeef.picasso.transformations.BlurTransformation(this, 10, 1)).into(songCover);
    }

    //hien thi bai hat tiep theo trong danh sach
    public void setListViewPosition() {
        if (currentSongIndex == listSongs.size() - 1) {
            playListView.setSelection(0);
        } else
            playListView.setSelection(currentSongIndex + 1);
    }

    //play bai hat trong danh sach
    public void playSong() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                countDownTimer.start();
                seekBar.setProgress(0);
                seekBar.setMax(100);
                historySong.addHistorySong(PlayMusicActivity.this, nowPlaying);
                setSongInfo(nowPlaying);
                mediaPlayer.stop();
                if (playThread.isAlive()) playThread.interrupt();
                if (playAsync.getStatus() == AsyncTask.Status.RUNNING) {
                    playAsync.cancel(true);
                }
                pDialog.show();
                setupMediaPlayer();
                countDownTimer.cancel();
                updateSeekBar();
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                btnPlay.setImageResource(R.drawable.btn_pause);
            }
        }, 10);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (isRepeat) {
            btnPlay.setImageResource(R.drawable.btn_play);
            playSong();
        } else if (isShuffle) {
            btnPlay.setImageResource(R.drawable.btn_play);
            Random rand = new Random();
            currentSongIndex = rand.nextInt((listSongs.size() - 1) - 0 + 1) + 0;
            nowPlaying = listSongs.get(currentSongIndex);
            playSong();
            currentSongIndex = currentSongIndex + 1;
            setListViewPosition();
        } else {
            if (currentSongIndex < (listSongs.size() - 1)) {
                btnPlay.setImageResource(R.drawable.btn_play);
                nowPlaying = listSongs.get(currentSongIndex + 1);
                playSong();
                currentSongIndex = currentSongIndex + 1;
                setListViewPosition();
            } else {
                btnPlay.setImageResource(R.drawable.btn_play);
                nowPlaying = listSongs.get(0);
                playSong();
                currentSongIndex = 0;
                setListViewPosition();
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = utilities.progressToTimer(seekBar.getProgress(), totalDuration);
        mediaPlayer.seekTo(currentPosition);
        updateSeekBar();
    }

    //luong choi nhac
    public class PlayMusic extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            setupMediaPlayer();
            countDownTimer.cancel();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing()) pDialog.dismiss();
            btnPlay.setImageResource(R.drawable.btn_pause);
            updateSeekBar();
        }
    }

    public void setupMediaPlayer() {
        url = url1 + nowPlaying.getId() + url2 + Variables.CLIENT_ID;
        Log.e("id test", nowPlaying.getId() + "");
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    public void updateSeekBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            timeDuration.setText("" + utilities.milliSecondsToTimer(totalDuration));
            timeCurrent.setText("" + utilities.milliSecondsToTimer(currentDuration));

            int progress = (int) (utilities.getProcessPercentage(currentDuration, totalDuration));
            seekBar.setProgress(progress);
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PlayMusicActivity.this, MainActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PlayMusicActivity.REQUEST_CODE) {
            if (resultCode == PlayMusicActivity.RESULT_CODE) {
                nowPlaying = (SongItem) data.getSerializableExtra("songItem");
                currentSongIndex = data.getIntExtra("position", 0);
                toolbarName = data.getStringExtra("toolbarName");
                txtToolbarName.setText(toolbarName);
                btnPlay.setImageResource(R.drawable.btn_play);
                listSongs = new ArrayList<SongItem>();
                listSongs = (List<SongItem>) data.getSerializableExtra("listSongs");
                adapter = new ListSongAdapter(this, listSongs);
                playListView.setAdapter(adapter);
                setListViewPosition();
                playSong();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            ClearCacheMemory.trimCache(this);
        } catch (Exception e) {

        }
        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
    }
}
