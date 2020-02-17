package hn.gisvisit;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceJobGeo extends JobService implements LocationListener {

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
    protected LocationManager locationManager;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static long MIN_TIME_BW_UPDATES = 500 * 60 * 1; // 1 minute

    private static final String TAG = "PRUEBA";
    private boolean jobCancelled = false;

    //servicio
    private Looper serviceLooper;
    private Handler serviceHandler;

    DatabaseHelper db= new DatabaseHelper(this);
    Usuario servicioTmp;

    Intent batteryStatus;


    private void doBackgroundWork(final JobParameters params) {

        /*
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();

        serviceHandler = new Handler(serviceLooper);
        */

        new Thread(new Runnable() {
            @Override
            public void run() {

                Looper.prepare();

                //getLocation();
                Log.d(TAG, "Job Ejecutado");
                /*
                    if (jobCancelled) {
                        return;
                    }

                Usuario tmp=db.getUsuario();

                if(tmp.getEstado().equals("1")) {

                    try {

                        //Intent batteryStatus = this.registerReceiver(null, ifilter);

                        String estado = estado_carga();
                        String fuente_carga = medio_carga();
                        double nivel_carga = nivel_carga();


                        String estado_gps="0";


                        if (canGetLocation == true) {

                            estado_gps="1";

                        } else {
                            // showSettingsAlert();
                            estado_gps="0";
                        }

                        mVerificar_Actualizar("1", latitude + "", longitude + "", nivel_carga + "", estado, fuente_carga,estado_gps);

                        Log.e(TAG, "NIVEL:" + nivel_carga + " ESTADO:" + estado + " FUENTE:" + fuente_carga);
                    } catch (Exception e) {
                        Log.e(TAG, "ERROR"+e.toString());
                        e.printStackTrace();

                    }
                }
                else
                {
                    Log.e("Error", "usuario con cuenta vencida!!");
                }

                 */

                //Log.d(TAG, "Job finished");
                jobFinished(params,true);

                Looper.loop();

            }
        }).start();



    }


    @Override
    public boolean onStartJob(JobParameters params) {

        Log.d(TAG, "Job started");
        doBackgroundWork(params);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;

        return false;
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
    public String estado_carga()
    {
        // Are we charging / charged?
        IntentFilter ifilter;
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

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
    public String medio_carga()
    {
        IntentFilter ifilter;
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
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
    public double nivel_carga()
    {
        IntentFilter ifilter;

        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        Intent batteryStatus = this.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;

        return batteryPct*100;
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

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
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
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
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
                        Usuario tmp=db.getUsuario();
                        db.updateUsuarioServicio(tmp.getId()+"",ob.getString("segundos"),ob.getString("estado"));
                        Log.e("TERMINO",ob.getString("estado")+""+ob.getString("nombre"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR",e.toString());

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
}
