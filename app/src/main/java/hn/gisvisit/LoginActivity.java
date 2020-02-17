package hn.gisvisit;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LoginActivity  extends AppCompatActivity {


    EditText edtEmail,edtPassword;
    Button btnLogin;
    TextView txtRegistrar, txtForgotPassword,txtWrongPassword;
    CheckBox chkRemenber;
    DatabaseHelper db= new DatabaseHelper(this);
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS =1;
    private static final int MY_PERMISSIONS_REQUEST_PHONE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail=(EditText) findViewById(R.id.edtEmail);
        edtPassword=(EditText) findViewById(R.id.edtPassword);

        txtWrongPassword=(TextView)findViewById(R.id.txtWrongPassword);
        txtWrongPassword.setVisibility(View.GONE);

        txtRegistrar=(TextView)findViewById(R.id.txtRegister);
        txtForgotPassword=(TextView)findViewById(R.id.txtForgotPassword);

        chkRemenber=(CheckBox)findViewById(R.id.ckbRemenber);

        btnLogin=(Button)findViewById(R.id.btnRegistre);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    ingresar(edtEmail.getText()+"",edtPassword.getText()+"");
            }

        });

        txtRegistrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //finish();
                startActivity(new Intent(getApplicationContext(), RegistroActivity.class));
            }

        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //finish();
                startActivity(new Intent(getApplicationContext(), ForgotActivity.class));
            }

        });


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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                        MY_PERMISSIONS_REQUEST_PHONE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }


        inicioAutomatico_login();
        validarCheck();
        //startService();

    }

    public void inicioAutomatico_login()
    {
        if(db.getUsuarioCount()>0) {

            Usuario tmp = db.getUsuario();
            if (tmp.getEstado().equals("1")) {

                finish();
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));

            }
        }
    }

    public void validarCheck()
    {
        if(db.getUsuarioCount()>0) {

            Usuario tmp=db.getUsuario();
            if(tmp.getEstado_autoguardado().equals("1"))
            {
                chkRemenber.setChecked(true);
                edtEmail.setText(tmp.getNombre());
                edtPassword.setText(tmp.getClave());
            }
            else
            {
                chkRemenber.setChecked(false);

            }

        }
    }

    public void ingresar(String usuario,String clave)
    {

        String url="http://gisvisit.com/api/ingresar.php?usuario="+usuario+"&clave="+clave+"";

        StringRequest jsonArrayRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("userData");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject ob=array.getJSONObject(i);

                        if(db.getUsuarioCount()>0)
                        {
                            String id=db.getUsuarioMax()+"";
                            if(chkRemenber.isChecked()==true)
                            {
                                db.updateUsuario(id,ob.getString("codigo"),ob.getString("correo"),ob.getString("clave"),ob.getString("estado"),"1","1","0");
                            }
                            else
                            {
                                db.updateUsuario(id,ob.getString("codigo"),ob.getString("correo"),ob.getString("clave"),ob.getString("estado"),"1","0","0");
                            }
                        }
                        else
                        {
                            //db.insertUsuario(ob.getString("codigo"),ob.getString("correo"),ob.getString("clave"),ob.getString("segundos"),ob.getString("estado"),"1","0","0");

                            if(chkRemenber.isChecked()==true)
                            {
                                db.insertUsuario(ob.getString("codigo"),ob.getString("correo"),ob.getString("clave"),ob.getString("segundos"),ob.getString("estado"),"1","1","0");
                            }
                            else
                            {
                                db.insertUsuario(ob.getString("codigo"),ob.getString("correo"),ob.getString("clave"),ob.getString("segundos"),ob.getString("estado"),"1","0","0");
                            }

                        }


                        Usuario tmp=db.getUsuario();
                        if(tmp.getEstado().equals("0"))
                        {
                            txtWrongPassword.setVisibility(View.VISIBLE);
                        }
                        else if(tmp.getEstado().equals("1"))
                        {

                            txtWrongPassword.setVisibility(View.GONE);

                            //cambiar el estado a loggin

                            db.updateUsuarioLogIn(tmp.getId()+"","1");
                            tmp.setEstado_login("1");

                            //cambiar el estado a loggin

                            finish();
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));

                        }
                        else if(tmp.getEstado().equals("2"))
                        {
                            txtForgotPassword.setText("Account has expired, visit www.gisvisit.com");
                            txtWrongPassword.setVisibility(View.VISIBLE);

                        }
                        else if(tmp.getEstado().equals("3"))
                        {
                            MessageDialog exampleDialog = new MessageDialog();
                            exampleDialog.mensage="Please valid your account, we send you a email!!";
                            exampleDialog.show(getSupportFragmentManager(), "Information");

                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR",e.toString());

                }

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

    //working on the service
    /*
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



        }
    }

    /*
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    */

}
