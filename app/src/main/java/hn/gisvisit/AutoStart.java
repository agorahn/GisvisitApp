package hn.gisvisit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;



public class AutoStart extends BroadcastReceiver
{
    private final static String FOREGROUND_CHANNEL_ID = "foreground_channel_id";
    private NotificationManager mNotificationManager;
    private int count = 0;
    public static final String CHANNEL_ID = "exampleServiceChannel";
    private static final String TAG = "PRUEBA";


    public void onReceive(Context context, Intent arg1)
    {
        Intent intent = new Intent(context,ServiceGeo.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            ComponentName componentName = new ComponentName(context, ServiceJobGeo.class);
            JobInfo info = new JobInfo.Builder(12345, componentName)
                    //.setRequiresCharging(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    //.setPeriodic(15 * 60 * 1000)
                    .setMinimumLatency(30 * 1000) // Wait at least 30s
                    .setOverrideDeadline(60 * 1000) // Maximum delay 60s
                    .build();

            JobScheduler scheduler = (JobScheduler) context.getSystemService(context.JOB_SCHEDULER_SERVICE);
            int resultCode = scheduler.schedule(info);
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d(TAG, "Job scheduled");
            } else {
                Log.d(TAG, "Job scheduling failed");
            }








            Log.i("ERROR", "FORER");
        } else {
            context.startService(intent);
            Log.i("ERROR", "NOMRAL");
        }

        //context.startService(new Intent(context, ServiceGeo.class));

        Log.i("ERROR", "started");
    }


}
