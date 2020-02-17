package hn.gisvisit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHelper db= new DatabaseHelper(this);
    ImageView btnAddVisit,btnVisitList,btnContacts,btnCalendar,btnMapVisit,btnAjust;
    TextView usuario;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        usuario=(TextView) headerView.findViewById(R.id.usuario);

        usuario.setText(db.getUsuario().getNombre());

        startService();

        btnAddVisit=(ImageView) findViewById(R.id.imgAddVisit);
        btnVisitList=(ImageView) findViewById(R.id.imgVisitList);
        btnContacts=(ImageView) findViewById(R.id.imgContact);
        btnCalendar=(ImageView) findViewById(R.id.imgCalendar);
        btnMapVisit=(ImageView) findViewById(R.id.imgMaps);
        btnAjust=(ImageView) findViewById(R.id.imgAjust);

        btnAddVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), AddVisitActivity.class));

            }
        });
        btnVisitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ListaVisitasActivity.class));

            }
        });
        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ListContactsActivity.class));

            }
        });
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), CalendarioActivity.class));

            }
        });
        btnMapVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), MapsActivity.class));

            }
        });
        btnAjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), AjusteActivity.class));

            }
        });

        directorio();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Log.e("ENTER","NETER");

        if (id == R.id.carga_clients) {//cargar clientes

            cargar_clients(context);

        }else if (id == R.id.carga_events) {//carga eventos

            //Toast.makeText(this, "CLIENTE: "+db.getClienteCount(), Toast.LENGTH_SHORT).show();
            cargar_eventos(context);

        }else if (id == R.id.carga_data_realizada) {//cargar data realizada

            //Toast.makeText(this, "CLIENTE: "+db.getClienteCount(), Toast.LENGTH_SHORT).show();
            load_cliente(context);


        }else if (id == R.id.backup) {//backup de toda la base de datos

            //Toast.makeText(this, "CLIENTE: "+db.getClienteCount(), Toast.LENGTH_SHORT).show();

            try
            {
                exportDatabse("gisvisit.db");

                new AlertDialog.Builder(context)
                        .setTitle("Message")
                        .setMessage("Backup finished successfully")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();

            }catch (Exception e)
            {
                new AlertDialog.Builder(context)
                        .setTitle("Message")
                        .setMessage("Backup ERROR 205")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }




        }else if (id == R.id.nav_send) {

            //Toast.makeText(this, "CLIENTE: "+db.getClienteCount(), Toast.LENGTH_SHORT).show();
            //direccionar asi la web de soporte

        }else if (id == R.id.nav_web) {

            //Toast.makeText(this, "CLIENTE: "+db.getClienteCount(), Toast.LENGTH_SHORT).show();
            //direccionar asi la web oficial

        }else if (id == R.id.logout) {


            if(db.getUsuarioCount()>0)
            {
                    String codigo=db.getUsuarioMax()+"";
                    db.updateUsuarioLogOut(codigo,"0");

                     stopService(new Intent(MenuActivity.this, ServiceGeo.class));

                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //working on the service
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

        /*
        startActivityForResult(
                new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                110);

        */

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
    public void cargar_eventos(Context v) {


        final ProgressDialog progressDialog_list;
        progressDialog_list = new ProgressDialog(v);
        progressDialog_list.setMessage("Loading...");
        progressDialog_list.show();


        if (db.getUsuarioCount() > 0) {

            Usuario tmp = db.getUsuario();

            String url = "http://www.gisvisit.com/api/descargar_eventos.php?codigo_usuario=" + tmp.getCodigo() + "";

            db.deleteAllEvento();

            StringRequest jsonArrayRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("evento");

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject ob = array.getJSONObject(i);

                            //db.insertUsuario(ob.getString("codigo"), ob.getString("correo"), ob.getString("clave"), ob.getString("segundos"), ob.getString("estado"), "1", "1", "0");
                            //db.insertCliente(ob.getString("c_cliente"),ob.getString("codigo"),ob.getString("nombres")+" "+ob.getString("apellidos"),ob.getString("direccion"),ob.getString("identificacion"),ob.getString("correo"),ob.getString("telefono"),ob.getString("celular"),ob.getString("foto1"),ob.getString("foto2"),ob.getString("foto3"),ob.getString("latitud"),ob.getString("longitud"),ob.getString("gps"),"1",ob.getString("estado"));
                            db.insertEvento(ob.getString("c_cliente"),ob.getString("codigo"),ob.getString("c_tipo_visita"),ob.getString("descripcion"),ob.getString("fecha_agenda"),"0",ob.getString("latitud_inicio"),ob.getString("longitud_inicio"),ob.getString("gps"),"0","0");
                            Log.e("CODIGO", ob.getString("codigo"));
                            //codigo del cliente es 0

                        }


                        Log.e("CERRO", "CERRO");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR", e.toString());

                    }

                    progressDialog_list.dismiss();

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

    public void cargar_clients(Context v) {


        final ProgressDialog progressDialog_list;
        progressDialog_list = new ProgressDialog(v);
        progressDialog_list.setMessage("Loading...");
        progressDialog_list.show();


        if (db.getUsuarioCount() > 0) {

            Usuario tmp = db.getUsuario();

            String url = "http://www.gisvisit.com/api/descargar_clientes.php?codigo_usuario=" + tmp.getCodigo() + "";

            db.deleteAllCliente();

            StringRequest jsonArrayRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("cliente");

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject ob = array.getJSONObject(i);

                        //db.insertUsuario(ob.getString("codigo"), ob.getString("correo"), ob.getString("clave"), ob.getString("segundos"), ob.getString("estado"), "1", "1", "0");
                        db.insertCliente(ob.getString("codigo"),ob.getString("c_empresa"),ob.getString("nombres"),ob.getString("direccion"),ob.getString("identificacion"),ob.getString("correo"),ob.getString("telefono"),ob.getString("celular"),ob.getString("foto1"),ob.getString("foto2"),ob.getString("foto3"),ob.getString("latitud"),ob.getString("longitud"),ob.getString("gps"),"1",ob.getString("estado"));

                    }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR", e.toString());

                    }

                    progressDialog_list.dismiss();

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

       // progressDialog_list.dismiss();



    }
    public void load_cliente(Context v)
    {

        final ProgressDialog progressDialog_list;
        progressDialog_list = new ProgressDialog(v);
        progressDialog_list.setMessage("Loading...");
        progressDialog_list.show();

        List<Cliente> clienteList=db.getAllClientes_toSinc();
        for (Cliente cliente: clienteList) {

            if(cliente.getEstado().equals("2")) {
                guardar_cliente_nuevo(v, cliente,db.getUsuario());
            }
            else
            {
                guardar_cliente(v, cliente);
            }

        }

        List<Visita> eventList=db.getAllVisitas_toSinc();
        for (Visita visita: eventList) {

            if(visita.getEstado().equals("2")) {
                guardar_evento_nuevo(v, visita,db.getUsuario());
            }
            else
            {
                guardar_evento(v, visita);
            }

        }



        progressDialog_list.dismiss();

        new AlertDialog.Builder(v)
                .setTitle("Message")
                .setMessage("Data load successfully")
                .setPositiveButton(android.R.string.ok, null)
                .show();

    }
    public void guardar_cliente(Context v,final Cliente c) {

        Log.e("ERROR","ingreso a guardar cliente");
        // Select All Query
        System.setProperty("http.keepAlive", "false");

        Usuario tmp = db.getUsuario();
        String base1="",base2="",base3="";
        String flagFoto1="0",flagFoto2="0",flagFoto3="0";
        String url = "http://www.gisvisit.com/api/cargar_clientes.php/";

        try {

            if (c.getFoto1().length() > 1) {

                try {

                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(v.getContentResolver(), Uri.fromFile(new File(c.getFoto1())));
                    Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, 700, 700, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    base1 = Base64.encodeToString(image, Base64.DEFAULT);
                    flagFoto1 = "1";

                } catch (Exception e) {
                    Log.e("ERRORFOTO1 :", e.toString());

                }

            }
            if (c.getFoto2().length() > 1) {

                try {

                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(v.getContentResolver(), Uri.fromFile(new File(c.getFoto2())));
                    Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, 700, 700, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    base2 = Base64.encodeToString(image, Base64.DEFAULT);
                    flagFoto2 = "1";

                } catch (Exception e) {
                    Log.e("ERRORFOTO2 :", e.toString());

                }

            }
            if (c.getFoto3().length() > 1) {

                try {

                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(v.getContentResolver(), Uri.fromFile(new File(c.getFoto3())));
                    Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, 700, 700, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    base3 = Base64.encodeToString(image, Base64.DEFAULT);
                    flagFoto3 = "1";

                } catch (Exception e) {
                    Log.e("ERRORFOTO3 :", e.toString());

                }

            }

            //variables finales
            final String b1 = base1;
            final String b2 = base2;
            final String b3 = base3;
            final String f1 = flagFoto1;
            final String f2 = flagFoto2;
            final String f3 = flagFoto3;
            //variables finales

            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("cliente");

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject ob = array.getJSONObject(i);

                            if(ob.getString("codigo").equals("1"))
                            {
                                if (c.getFoto1().length() > 1) {
                                    db.updateClienteFoto1_load(c.getId()+"","1");
                                    File file = new File(c.getFoto1());
                                    boolean deleted = file.delete();
                                }
                                if (c.getFoto2().length() > 1) {
                                    db.updateClienteFoto2_load(c.getId()+"","1");
                                    File file = new File(c.getFoto1());
                                    boolean deleted = file.delete();
                                }
                                if (c.getFoto3().length() > 1) {
                                    db.updateClienteFoto3_load(c.getId()+"","1");
                                    File file = new File(c.getFoto1());
                                    boolean deleted = file.delete();
                                }
                                db.updateCliente_sinc(c.getCodigo(),"1","1");
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR_RESPONDE:", e.toString());

                    }



                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.

                    Log.e("ERROR HTTP",error.toString());
                }
            }) {
                public Map<String, String> getParams() throws AuthFailureError {
                    //Map<String, String> MyData = new HashMap<String, String>();

                    Map<String, String> MyData = new HashMap<String, String>();


                    MyData.put("codigo", c.getCodigo());

                    MyData.put("identificacion", c.getIndentidad());
                    MyData.put("nombres", c.getNombre());
                    MyData.put("direccion", c.getDireccion());
                    MyData.put("telefono", c.getTelefono());
                    MyData.put("celular", c.getCelular());
                    MyData.put("correo", c.getCorreo());
                    MyData.put("latitud", c.getLatitude());
                    MyData.put("longitud", c.getLongitud());
                    MyData.put("foto1", f1);
                    MyData.put("foto2", f2);
                    MyData.put("foto3", f3);
                    MyData.put("base1", b1);
                    MyData.put("base2", b2);
                    MyData.put("base3", b3);




                    return MyData;
                }


            };

            //MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // MyApplication.getInstance().addToRequestQueue(jsonObjectReq, "postRequest");

            MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(1919996, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(v);

            requestQueue.add(MyStringRequest);


        }
        catch (Exception e)
        {
            db.updateCliente(c.getId()+"", "0");
            Log.e("ERROR_ENVIO :", e.toString());
        }

    }
    public void guardar_cliente_nuevo(Context v,final Cliente c,final Usuario u) {

        Usuario tmp = db.getUsuario();
        String base1="",base2="",base3="";
        String flagFoto1="0",flagFoto2="0",flagFoto3="0";
        String url = "http://www.gisvisit.com/api/cargar_clientes_nuevo.php/";
        Log.e("ERROR","entro a ingresar un nuevo");

        try {

            if (c.getFoto1().length() > 1) {

                try {

                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(v.getContentResolver(), Uri.fromFile(new File(c.getFoto1())));
                    Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, 700, 700, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    base1 = Base64.encodeToString(image, Base64.DEFAULT);
                    flagFoto1 = "1";

                } catch (Exception e) {
                    Log.e("ERRORFOTO1 :", e.toString());

                }

            }
            if (c.getFoto2().length() > 1) {

                try {

                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(v.getContentResolver(), Uri.fromFile(new File(c.getFoto2())));
                    Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, 700, 700, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    base2 = Base64.encodeToString(image, Base64.DEFAULT);
                    flagFoto2 = "1";

                } catch (Exception e) {
                    Log.e("ERRORFOTO2 :", e.toString());

                }

            }
            if (c.getFoto3().length() > 1) {

                try {

                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(v.getContentResolver(), Uri.fromFile(new File(c.getFoto3())));
                    Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, 700, 700, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    base3 = Base64.encodeToString(image, Base64.DEFAULT);
                    flagFoto3 = "1";

                } catch (Exception e) {
                    Log.e("ERRORFOTO3 :", e.toString());

                }

            }

            //variables finales
            final String b1 = base1;
            final String b2 = base2;
            final String b3 = base3;
            final String f1 = flagFoto1;
            final String f2 = flagFoto2;
            final String f3 = flagFoto3;
            //variables finales

            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("cliente");

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject ob = array.getJSONObject(i);

                            Log.e("RETORNO:",ob.getString("estado"));

                            if(ob.getString("estado").equals("1")) {

                                if (c.getFoto1().length() > 1) {
                                    db.updateClienteFoto1_load(c.getId()+"","1");
                                    File file = new File(c.getFoto1());
                                    boolean deleted = file.delete();
                                }
                                if (c.getFoto2().length() > 1) {
                                    db.updateClienteFoto2_load(c.getId()+"","1");
                                    File file = new File(c.getFoto2());
                                    boolean deleted = file.delete();
                                }
                                if (c.getFoto3().length() > 1) {
                                    db.updateClienteFoto3_load(c.getId()+"","1");
                                    File file = new File(c.getFoto3());
                                    boolean deleted = file.delete();
                                }

                                //db.insertUsuario(ob.getString("codigo"), ob.getString("correo"), ob.getString("clave"), ob.getString("segundos"), ob.getString("estado"), "1", "1", "0");
                                db.updateCliente_afterSinc(c.getId() + "", ob.getString("codigo"));
                                db.updateCliente_sinc(c.getId() + "", "1", "1");
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR_RESPONDE:", e.toString());

                    }



                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.
                }
            }) {
                public Map<String, String> getParams() throws AuthFailureError {
                    //Map<String, String> MyData = new HashMap<String, String>();

                    Map<String, String> MyData = new HashMap<String, String>();


                    MyData.put("codigo_usuario", u.getCodigo());

                    MyData.put("identificacion", c.getIndentidad());
                    MyData.put("nombres", c.getNombre());
                    MyData.put("direccion", c.getDireccion());
                    MyData.put("telefono", c.getTelefono());
                    MyData.put("celular", c.getCelular());
                    MyData.put("correo", c.getCorreo());
                    MyData.put("latitud", c.getLatitude());
                    MyData.put("longitud", c.getLongitud());
                    MyData.put("foto1", f1);
                    MyData.put("foto2", f2);
                    MyData.put("foto3", f3);
                    MyData.put("base1", b1);
                    MyData.put("base2", b2);
                    MyData.put("base3", b3);




                    return MyData;
                }


            };

            //MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // MyApplication.getInstance().addToRequestQueue(jsonObjectReq, "postRequest");

            MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(1919996, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(v);

            requestQueue.add(MyStringRequest);


        }
        catch (Exception e)
        {
            db.updateCliente(c.getId()+"", "0");
            Log.e("ERROR_ENVIO :", e.toString());
        }

    }

    public void guardar_evento(Context v,final Visita c) {

        Log.e("ERROR","ingreso a guardar cliente");
        // Select All Query
        System.setProperty("http.keepAlive", "false");

        Usuario tmp = db.getUsuario();
        String base1="",base2="",base3="";
        String flagFoto1="0",flagFoto2="0",flagFoto3="0";
        String url = "http://www.gisvisit.com/api/cargar_eventos.php/";

        try {

            if (c.getFoto1().length() > 1) {

                try {

                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(v.getContentResolver(), Uri.fromFile(new File(c.getFoto1())));
                    Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, 700, 700, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    base1 = Base64.encodeToString(image, Base64.DEFAULT);
                    flagFoto1 = "1";

                } catch (Exception e) {
                    Log.e("ERRORFOTO1 :", e.toString());

                }

            }
            if (c.getFoto2().length() > 1) {

                try {

                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(v.getContentResolver(), Uri.fromFile(new File(c.getFoto2())));
                    Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, 700, 700, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    base2 = Base64.encodeToString(image, Base64.DEFAULT);
                    flagFoto2 = "1";

                } catch (Exception e) {
                    Log.e("ERRORFOTO2 :", e.toString());

                }

            }
            if (c.getFoto3().length() > 1) {

                try {

                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(v.getContentResolver(), Uri.fromFile(new File(c.getFoto3())));
                    Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, 700, 700, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    base3 = Base64.encodeToString(image, Base64.DEFAULT);
                    flagFoto3 = "1";

                } catch (Exception e) {
                    Log.e("ERRORFOTO3 :", e.toString());

                }

            }

            //variables finales
            final String b1 = base1;
            final String b2 = base2;
            final String b3 = base3;
            final String f1 = flagFoto1;
            final String f2 = flagFoto2;
            final String f3 = flagFoto3;
            //variables finales

            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("visita");

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject ob = array.getJSONObject(i);

                            if(ob.getString("codigo").equals("1"))
                            {
                                if (c.getFoto1().length() > 1) {

                                    //db.updateClienteFoto1_load(c.getId()+"","1");
                                    File file = new File(c.getFoto1());
                                    boolean deleted = file.delete();

                                }
                                if (c.getFoto2().length() > 1) {

                                    //db.updateClienteFoto2_load(c.getId()+"","1");
                                    File file = new File(c.getFoto1());
                                    boolean deleted = file.delete();

                                }
                                if (c.getFoto3().length() > 1) {

                                    db.updateClienteFoto3_load(c.getId()+"","1");
                                    File file = new File(c.getFoto1());
                                    boolean deleted = file.delete();

                                }

                                db.updateVisita_sinc(c.getId()+"","1","1");

                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR_RESPONDE:", e.toString());

                    }



                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.

                    Log.e("ERROR HTTP",error.toString());
                }
            }) {
                public Map<String, String> getParams() throws AuthFailureError {
                    //Map<String, String> MyData = new HashMap<String, String>();

                    Map<String, String> MyData = new HashMap<String, String>();


                    MyData.put("codigo", c.getCodigo_evento());

                    MyData.put("c_tipo_visita", c.getRazon_visita());
                    MyData.put("latitud_final", c.getLatitude());
                    MyData.put("longitud_final", c.getLongitud());
                    MyData.put("fecha_final", c.getFecha());
                    MyData.put("descripcion", c.getObservacion());
                    MyData.put("estado_visita", c.getEstado());
                    MyData.put("foto1", f1);
                    MyData.put("foto2", f2);
                    MyData.put("foto3", f3);
                    MyData.put("base1", b1);
                    MyData.put("base2", b2);
                    MyData.put("base3", b3);


                    return MyData;
                }


            };

            //MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // MyApplication.getInstance().addToRequestQueue(jsonObjectReq, "postRequest");

            MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(1919996, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(v);

            requestQueue.add(MyStringRequest);


        }
        catch (Exception e)
        {
            db.updateCliente(c.getId()+"", "0");
            Log.e("ERROR_ENVIO :", e.toString());
        }

    }

    public void guardar_evento_nuevo(Context v,final Visita c,final Usuario u) {

        Usuario tmp = db.getUsuario();
        String base1="",base2="",base3="";
        String flagFoto1="0",flagFoto2="0",flagFoto3="0";
        String url = "http://www.gisvisit.com/api/cargar_eventos_nuevo.php/";
        Log.e("ERROR","entro a ingresar un nuevo");

        try {

            if (c.getFoto1().length() > 1) {

                try {

                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(v.getContentResolver(), Uri.fromFile(new File(c.getFoto1())));
                    Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, 700, 700, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    base1 = Base64.encodeToString(image, Base64.DEFAULT);
                    flagFoto1 = "1";

                } catch (Exception e) {
                    Log.e("ERRORFOTO1 :", e.toString());

                }

            }
            if (c.getFoto2().length() > 1) {

                try {

                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(v.getContentResolver(), Uri.fromFile(new File(c.getFoto2())));
                    Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, 700, 700, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    base2 = Base64.encodeToString(image, Base64.DEFAULT);
                    flagFoto2 = "1";

                } catch (Exception e) {
                    Log.e("ERRORFOTO2 :", e.toString());

                }

            }
            if (c.getFoto3().length() > 1) {

                try {

                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(v.getContentResolver(), Uri.fromFile(new File(c.getFoto3())));
                    Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, 700, 700, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    base3 = Base64.encodeToString(image, Base64.DEFAULT);
                    flagFoto3 = "1";

                } catch (Exception e) {
                    Log.e("ERRORFOTO3 :", e.toString());

                }

            }

            //variables finales
            final String b1 = base1;
            final String b2 = base2;
            final String b3 = base3;
            final String f1 = flagFoto1;
            final String f2 = flagFoto2;
            final String f3 = flagFoto3;
            //variables finales

            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("visita");

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject ob = array.getJSONObject(i);

                            Log.e("RETORNO:",ob.getString("estado"));

                            if(ob.getString("estado").equals("1")) {

                                if (c.getFoto1().length() > 1) {
                                    //db.updateClienteFoto1_load(c.getId()+"","1");
                                    File file = new File(c.getFoto1());
                                    boolean deleted = file.delete();
                                }
                                if (c.getFoto2().length() > 1) {
                                    //db.updateClienteFoto2_load(c.getId()+"","1");
                                    File file = new File(c.getFoto2());
                                    boolean deleted = file.delete();
                                }
                                if (c.getFoto3().length() > 1) {
                                    //db.updateClienteFoto3_load(c.getId()+"","1");
                                    File file = new File(c.getFoto3());
                                    boolean deleted = file.delete();
                                }

                                //db.insertUsuario(ob.getString("codigo"), ob.getString("correo"), ob.getString("clave"), ob.getString("segundos"), ob.getString("estado"), "1", "1", "0");
                                db.updateVisita_afterSinc(c.getId() + "", ob.getString("codigo"));
                                db.updateVisita_sinc(c.getId() + "", "1", "1");
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR_RESPONDE:", e.toString());

                    }



                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.
                    Log.e("ERROR DE URL",""+error.toString());
                }
            }) {
                public Map<String, String> getParams() throws AuthFailureError {
                    //Map<String, String> MyData = new HashMap<String, String>();

                    Map<String, String> MyData = new HashMap<String, String>();


                    MyData.put("codigo_usuario", u.getCodigo());

                    MyData.put("cliente", c.getCodigo_c_cliente());
                    MyData.put("c_tipo_visita", c.getRazon_visita());
                    MyData.put("latitud_final", c.getLatitude());
                    MyData.put("longitud_final", c.getLongitud());
                    MyData.put("fecha_final", c.getFecha());
                    MyData.put("descripcion", c.getObservacion());
                    MyData.put("estado_visita", c.getEstado());
                    MyData.put("foto1", f1);
                    MyData.put("foto2", f2);
                    MyData.put("foto3", f3);
                    MyData.put("base1", b1);
                    MyData.put("base2", b2);
                    MyData.put("base3", b3);

                    return MyData;
                }


            };

            //MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // MyApplication.getInstance().addToRequestQueue(jsonObjectReq, "postRequest");

            MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(1919996, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(v);

            requestQueue.add(MyStringRequest);


        }
        catch (Exception e)
        {
            db.updateCliente(c.getId()+"", "0");
            Log.e("ERROR_ENVIO :", e.toString());
        }

    }

    //metodos para verificar carpeta para crear

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    public void directorio()
    {
        String TAG = "logcat";

        File f = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/fotos-clientes");
        if(f.isDirectory()) {

        }
        else {
            if (isExternalStorageWritable()) {
                Log.d(TAG, "El almacenamiento externo esta disponible :)");
                String nombreDirectorioPrivado = "fotos-clientes";
                crearDirectorioPrivado(this, nombreDirectorioPrivado);

            } else {
                Log.e(TAG, "El almacenamiento externo no esta disponible :(");
            }
        }
    }
    public File crearDirectorioPrivado(Context context, String nombreDirectorio) {
        String TAG = "logcat";
        //Crear directorio privado en la carpeta Pictures.
        File directorio =new File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                nombreDirectorio);
        //Muestro un mensaje en el logcat si no se creo la carpeta por algun motivo
        if (!directorio.mkdirs())
            Log.e(TAG, "Error: No se creo el directorio privado");

        return directorio;
    }


    //metodo para verificar carpeta para crear

    public void exportDatabse(String databaseName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//"+getPackageName()+"//databases//"+databaseName+"";
                String backupDBPath = "backupname.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }




}
