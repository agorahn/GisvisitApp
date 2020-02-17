package hn.gisvisit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class ServiceGeoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //event fire when boot is completed
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Utility.scheduleJob(context);
        }
         */
        startService(context);

    }

    public void startService(Context context) {
        Intent serviceIntent = new Intent(context, ServiceGeo.class);
        serviceIntent.putExtra("inputExtra", "Gisvisit Service location");

        ContextCompat.startForegroundService(context, serviceIntent);
    }

}
