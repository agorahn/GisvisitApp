package hn.gisvisit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnStart;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS =1;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS2 =1;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS3 =1;
    private static final String TAG = "PRUEBA";
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    DatabaseHelper db;
    Usuario servicioTmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        int conteo = db.getUsuarioCount();

        if (conteo > 0) {
            servicioTmp = db.getUsuario();
        } else {
            Log.e("ERROR","NO TIENE USUARIO CREADO");
           db.insertUsuario("1", "ehernandez", "hernandez", "60", "1", "1","1","1");
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS2);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS3);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }


/*
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS3);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }

 */

                this.btnStart=(Button)(findViewById(R.id.btnStart));

                btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                            startService();


                      //  startService(new Intent(getApplicationContext(), ServiceGeo.class));

                        /*

                        ComponentName componentName = new ComponentName(getApplicationContext(), ServiceJobGeo.class);
                        JobInfo info = new JobInfo.Builder(1234, componentName)
                                //.setRequiresCharging(true)
                                .setPersisted(true)
                                .setPeriodic(30 * 1000)
                                //.setMinimumLatency(60 * 1000) // Wait at least 60s
                                //.setOverrideDeadline(90 * 1000) // Maximum delay 90s
                                .build();

                        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                        int resultCode = scheduler.schedule(info);
                        if (resultCode == JobScheduler.RESULT_SUCCESS) {
                            Log.d(TAG, "Job scheduled");
                        } else {
                            Log.d(TAG, "Job scheduling failed");
                        }

                         */


                    }
        });
    }

    public void startService() {

        //NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //NotificationChannel notificationChannel = null;
        //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                if (isMyServiceRunning(ServiceGeo.class)==false) {

                    Intent serviceIntent = new Intent(this, ServiceGeo.class);
                    serviceIntent.putExtra("inputExtra", "Gisvisit Service location");

                    ContextCompat.startForegroundService(this, serviceIntent);

                    Toast.makeText(this, "CREADO", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "YA EXISTE", Toast.LENGTH_SHORT).show();
                }

            startActivityForResult(
                    new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                    110);

      // }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}
