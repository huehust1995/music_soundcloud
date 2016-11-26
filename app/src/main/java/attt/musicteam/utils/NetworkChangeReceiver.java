package attt.musicteam.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
/**
 * Created by Hue on 11/8/2016.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isInternetConnection = new Utilities().checkInternetConnection(context);
        if(isInternetConnection == false){
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }
}
