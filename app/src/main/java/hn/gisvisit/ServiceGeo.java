package hn.gisvisit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.core.app.NotificationCompat;

public class ServiceGeo extends Service implements LocationListener {

    //servicio
    private Looper serviceLooper;
    private Handler serviceHandler;

    //Location variables
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 10 meters

    // The minimum time between updates in milliseconds
    private static long MIN_TIME_BW_UPDATES = 1000 * 60 ; // 1 minute
    private static long MIN_TIME_BW_UPDATES2 = 1000 * 30 ; // 1 minute

    //private static final long MIN_TIME_BW_UPDATES_LOW = 10000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;
    int conteo;

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    //private static final int NOTIFICATION_ID = 555;

    DatabaseHelper db;
    Usuario servicioTmp;
    IntentFilter ifilter;
    Intent batteryStatus;

    // the wakelock used to keep the app alive while the screen is off
    //private PowerManager.WakeLock wakeLock;
    //private static final String TAG = ServiceGeo.class.getSimpleName();

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        db = new DatabaseHelper(this);

        conteo=db.getUsuarioCount();

        if(conteo>0)
        {
            servicioTmp=db.getUsuario();
        }
        else
        {
            //db.insertUsuario("1","ehernandez","hernandez","60","1","1");
        }


        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // get a wakelock from the power manager
        //final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

       // showSettingsAlert();


    }
    public String estado_carga(Intent batteryStatus)
    {
        // Are we charging / charged?

        String estado="0";

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        if(isCharging==true)
        {
            estado="1";
        }
        else
        {
            estado="0";
        }


        return estado;
    }
    public String medio_carga(Intent batteryStatus)
    {
        // How are we charging?
        String estado="0";
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if(usbCharge==true)
        {
            estado="1";
        }
        else if(acCharge==true)
        {
            estado="2";
        }

        return estado;
    }
    public double nivel_carga(Intent batteryStatus)
    {
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;

        return batteryPct*100;
    }
    public int CHANGE_DELAY_TIME(Intent batteryStatus,double nivel_carga,Usuario tmp){

        int MIN_TIME=0;

        if(nivel_carga>20)
        {
           // db.updateUsuarioServicio("1","30","1");
            MIN_TIME = Integer.parseInt(tmp.getSegundos()) * 1000;
            Log.e("PRUEBA",tmp.getSegundos());
        }
        else
        {
            MIN_TIME =90 * 1000;
        }

        return MIN_TIME;

    }
    private void hilo() {

        Usuario tmp = db.getUsuario();

        int conteo=db.getUsuarioCount();

        if(conteo>0) {

            Log.e("Error", "Estado:"+tmp.getEstado_login());

            if (tmp.getEstado_login().equals("0")) {

                Log.e("Error", "USUARIO ESTA LOG OFF NO MANDA NADA");

            }
            else {

                        Toast.makeText(this, "service ejecutado: " + tmp.getEstado(), Toast.LENGTH_SHORT).show();

                        if (tmp.getEstado().equals("1")) {

                            try {
                                //Your code here or call a method

                                Intent batteryStatus = this.registerReceiver(null, ifilter);
                                String estado = estado_carga(batteryStatus);
                                String fuente_carga = medio_carga(batteryStatus);
                                double nivel_carga = nivel_carga(batteryStatus);
                                MIN_TIME_BW_UPDATES = CHANGE_DELAY_TIME(batteryStatus, nivel_carga, tmp);

                                String estado_gps = "0";

                                if (isLocationEnable() == true) {

                                    estado_gps = "1";
                                    //location=getLocation();


                                } else {
                                    // showSettingsAlert();
                                    estado_gps = "0";
                                    Toast.makeText(this, "PARA MEJOR FUNCIONABILIDAD UTILICE EL GPS", Toast.LENGTH_SHORT).show();
                                }
                                updateLocation();
                                //location=getLocation();

                                mVerificar_Actualizar(tmp.getCodigo(), this.latitude + "", this.longitude + "", nivel_carga + "", estado, fuente_carga, estado_gps);
                                Log.e("GIS", "NIVEL:" + nivel_carga + " ESTADO:" + estado + " FUENTE:" + fuente_carga);
                            } catch (Exception e) {
                                Log.e("GIS", e.toString());
                                e.printStackTrace();
                                // Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                            }
                        } else if (tmp.getEstado().equals("3")) {
                            Log.e("Error", "usuario con cuenta nueva si verficiar!!");
                            try {

                                mVerificar_Estado(tmp.getCodigo());

                            } catch (Exception e) {

                                Log.e("Error", "ERROR DE URL");

                            }
                        } else if (tmp.getEstado().equals("2")) {
                            Log.e("Error", "usuario con cuenta vencida!!");
                            try {

                                mVerificar_Estado(tmp.getCodigo());

                            } catch (Exception e) {

                                Log.e("Error", "ERROR DE URL");

                            }
                        }

                }

            } else {
                Log.e("Error", "NO EXISTE USUARIO O NO ESTA LOGEADO");
            }

            scheduleNext(MIN_TIME_BW_UPDATES);


    }

    private void scheduleNext(long delay) {
        serviceHandler.postDelayed(new Runnable() {
            public void run() { hilo(); }
        }, delay);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        //prueba de notificacion
        //prueba de notificacion
       // startForegroundService();


        //Log.i("LocalService", "Received start id " + startId + ": " + intent);

        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //trabajando en la notificacion

        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Gisvisit Service")
                .setContentText(input)
                .setSmallIcon(android.R.drawable.ic_menu_myplaces)
                .setContentIntent(pendingIntent)
                .build();


        //trabajando en la notificacion


        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();

        serviceHandler = new Handler(serviceLooper);

        getLocation();

        hilo();

        //wakeLock.acquire();


        startForeground(1, notification);

        // If we get killed, after returning from here, restart START_STICKY,START_NOT_STICKY
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //on task restart
        //Intent intent = new Intent( this, ServiceGeoReceiver.class );
        //intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        //startActivity( intent );
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);



            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled || !isNetworkEnabled) {
                // no network provider is enabled
                this.canGetLocation = false;
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {

                                    locationManager.requestLocationUpdates(
                                            LocationManager.NETWORK_PROVIDER,
                                            MIN_TIME_BW_UPDATES2,
                                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {

                    Log.d("Network", "entro a gps");

                    if (location != null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES2,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);


                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    @SuppressLint("MissingPermission")
    public void updateLocation() {
        try {
                 if (locationManager != null) {
                     isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                     // getting network status
                     isNetworkEnabled = locationManager
                             .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                     if (isGPSEnabled) {
                         location = locationManager
                                 .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                         if (location != null) {
                             latitude = location.getLatitude();
                             longitude = location.getLongitude();
                         }
                     }
                     else if (isNetworkEnabled) {
                         location = locationManager
                                 .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                         if (location != null) {
                             latitude = location.getLatitude();
                             longitude = location.getLongitude();
                         }
                     }

                 }


        } catch (Exception e) {

        }


    }

    /**
     * Function to get latitude
     * */

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }
    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder( this  );

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
               // Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
               // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               // startActivity(intent);

                dialog.dismiss();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();

        // Showing Alert Message
        //alertDialog.show();
    }

    private void mVerificar_Actualizar(String codigo,String lat,String lon,String batteryLevel,String estadoCarga,String fuenteCarga,String estado_gps) {
        //final ProgressDialog progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("Loading...");
        //progressDialog.show();

        String url="http://gisvisit.com/api/localizacion.php?codigo="+codigo+"&lat="+lat+"&lon="+lon+"&batteryLevel="+batteryLevel+"&estadoCarga="+estadoCarga+"&fuenteCarga="+fuenteCarga+"&estadoGps="+estado_gps+"";
        Log.e("ERROR",url);

        StringRequest jsonArrayRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("userData");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject ob=array.getJSONObject(i);

                        // Log.e("ERROR",ob.getString("id"));

                        /*
                        Circuito c = new Circuito();
                        c.setId(ob.getString("id"));
                        c.setNombre(ob.getString("nombre"));
                        c.setLatitud(ob.getString("latitud"));
                        c.setLongitud(ob.getString("longitud"));
                        c.setLatitud2(ob.getString("latitud2"));
                        c.setLongitud2(ob.getString("longitud2"));
                        c.setLongitud2(ob.getString("tipo"));
                        */

                        //db.updateUsuarioServicio(ob.getString("id"),ob.getString("id_circuito"),ob.getString("nombre"),ob.getString("latitud"),ob.getString("longitud"),ob.getString("latitud2"),ob.getString("longitud2"),ob.getString("tipo"));

                        if(db.getUsuarioCount()>0) {

                            servicioTmp = db.getUsuario();

                            db.updateUsuarioServicio(servicioTmp.getId() + "", ob.getString("segundos"), ob.getString("estado"));
                            Log.e("GIS", ob.getString("estado") + "" + ob.getString("nombre"));

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("GIS",e.toString());

                }
                // adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void mVerificar_Estado(String codigo) {
        //final ProgressDialog progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("Loading...");
        //progressDialog.show();

        String url="http://gisvisit.com/api/localizacion_change_estado.php?codigo="+codigo+"";
        Log.e("ERROR",url);

        StringRequest jsonArrayRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("userData");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject ob=array.getJSONObject(i);

                        // Log.e("ERROR",ob.getString("id"));

                        /*
                        Circuito c = new Circuito();
                        c.setId(ob.getString("id"));
                        c.setNombre(ob.getString("nombre"));
                        c.setLatitud(ob.getString("latitud"));
                        c.setLongitud(ob.getString("longitud"));
                        c.setLatitud2(ob.getString("latitud2"));
                        c.setLongitud2(ob.getString("longitud2"));
                        c.setLongitud2(ob.getString("tipo"));
                        */

                        //db.updateUsuarioServicio(ob.getString("id"),ob.getString("id_circuito"),ob.getString("nombre"),ob.getString("latitud"),ob.getString("longitud"),ob.getString("latitud2"),ob.getString("longitud2"),ob.getString("tipo"));

                        //db.updateUsuarioServicio(servicioTmp.getId()+"",ob.getString("segundos"),ob.getString("estado"));
                        //Log.e("GIS",ob.getString("estado")+""+ob.getString("nombre"));

                        if(db.getUsuarioCount()>0) {

                            servicioTmp = db.getUsuario();

                            db.updateUsuarioServicio(servicioTmp.getId() + "", ob.getString("segundos"), ob.getString("estado"));
                            Log.e("GIS", ob.getString("estado") + "" + ob.getString("nombre"));

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("GIS",e.toString());

                }
                // adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Gisvisit Service location",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public boolean isLocationEnable() {

        LocationManager locationManager = (LocationManager) this
                    .getSystemService(Context.LOCATION_SERVICE);

            boolean gps_enabled = false;
            try {
                gps_enabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }
            boolean network_enabled = false;
            try {
                network_enabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
            }

            if (!gps_enabled || !network_enabled) {

                /*
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Activate Location");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Setting",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface paramDialogInterface,
                                    int paramInt) {
                                // TODO Auto-generated method stub
                                getApplicationContext().startActivityForResult(
                                        new Intent(
                                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                        110);


                            }
                        });
                dialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface paramDialogInterface,
                                    int paramInt) {
                            }
                        });
                dialog.show();

                 */


                return false;
            } else {
                return true;
            }

    }

}
