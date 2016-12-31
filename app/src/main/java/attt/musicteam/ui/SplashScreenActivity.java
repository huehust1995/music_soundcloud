package attt.musicteam.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import attt.musicteam.R;
import attt.musicteam.sharepreference.ConnectionSharePreference;
import attt.musicteam.sharepreference.NowPlayingSharePreference;
import attt.musicteam.utils.AppController;
import attt.musicteam.utils.JsonUTF8Request;
import attt.musicteam.utils.ReadWriteData;
import attt.musicteam.utils.Utilities;
import attt.musicteam.utils.Variables;

public class SplashScreenActivity extends AppCompatActivity {

    public String url1 = "https://api-v2.soundcloud.com/charts?kind=trending&genre=";
    public String genreName = "";
    public String url2 = "&client_id=";
    public String clientId = "";

    public NowPlayingSharePreference nowPlayingSp;
    public boolean isInternetConnection;

    //timeout
    public int timeoutValue;
    public ConnectionSharePreference connectionSp;
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        connectionSp = new ConnectionSharePreference();
        timeoutValue = connectionSp.getTimeoutValue(this);
        if (timeoutValue == 0) {
            timeoutValue = 30;
        }

        final CountDownTimer countDownTimer = new CountDownTimer(timeoutValue * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Toast.makeText(SplashScreenActivity.this, "time out", Toast.LENGTH_LONG).show();
                finish();
            }
        };
        countDownTimer.start();

        isInternetConnection = new Utilities().checkInternetConnection(this);
        if (isInternetConnection == false) {
            showAlertDialog();
        } else {
            nowPlayingSp = new NowPlayingSharePreference();
            nowPlayingSp.setStatePlaying(this, NowPlayingSharePreference.NOT_PLAYING);

            genreName = "soundcloud:genres:danceedm";
            clientId = Variables.CLIENT_ID;
            String url = url1 + genreName + url2 + clientId;

            JsonUTF8Request request = new JsonUTF8Request(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    ReadWriteData rw = new ReadWriteData();
                    rw.writeHomeData(response.toString());
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    countDownTimer.cancel();
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            AppController.getInstance().addToRequestQueue(request, "");
        }
        checkPermissions();
    }

    private void checkPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    //check nếu > android 6.0 thì request permission
    private boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("No connection!");
        builder.setMessage("Your device is not connected to internet. Please try again later");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }
}
