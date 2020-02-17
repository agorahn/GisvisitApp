package hn.gisvisit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewContactsActivity extends AppCompatActivity {

    EditText edtCodigo,edtNombre,edtDireccion,edtTelefono,edtCelular,edtEmail,edtRTN;
    Button btnGps,btnFoto1,btnFoto2,btnFoto3,btnIngresar,btnEdit;

    boolean estado_edit=false;

    boolean estadoGps=false,estadoFoto1=false,estadoFoto2=false,estadoFoto3=false;

    private String foto="",foto2="",foto3="";

    ImageView img1,img2,img3,dialog_pic;

    View view1,view_foto;
    MapView mMapView;

    DatabaseHelper db= new DatabaseHelper(this);

    private RadioButton radioButton1,radioButton2,radioButton3;

    GoogleMap mMap;
    AlertDialog.Builder builderMap;
    AlertDialog dialogs_mapa;
    Dialog dialog;

    AlertDialog.Builder builderFoto;
    AlertDialog dialogs_foto;
    Dialog dialog_foto;

    Cliente tmp;

    String lat="0",lon="0";

    private static final int REQUEST_CODE_CAMARA = 1;
    private static final int REQUEST_CODE_CAMARA2 = 2;
    private static final int REQUEST_CODE_CAMARA3 = 3;

    private String mDirAbsoluto = "";
    private String mDirAbsoluto2 = "";
    private String mDirAbsoluto3 = "";

    private static final int SCALE_FACTOR_IMAGE_VIEW = 4;
    private static final String ALBUM = "FOTO";
    private static final String EXTENSION_JPEG = ".jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view1 = inflater.inflate(R.layout.activity_dialog_map, null);

        view_foto = inflater.inflate(R.layout.activity_dialog_image, null);

        Bundle parametros = this.getIntent().getExtras();
        String id = parametros.getString("id");

        tmp = db.getCliente(id);

        //Toast.makeText(getApplicationContext(), "EL ID: "+tmp.getEstado(), Toast.LENGTH_SHORT).show();

        //crear dialogo

        //dialog de imagen
        dialog_foto = new Dialog(this);
        dialog_foto.setTitle("Title...");
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_foto.setContentView(R.layout.activity_dialog_image);

        dialog_pic = (ImageView) view_foto.findViewById(R.id.imageViewFoto);

        builderFoto = new AlertDialog.Builder(this);
        builderFoto.setTitle("Picture:");
        builderFoto.setPositiveButton("Ok",null);

        //builderFoto.setNegativeButton("No", null);

        builderFoto.setCancelable(false);
        builderFoto.setView(view_foto);

        dialogs_foto = builderFoto.create();


        //dialog de map

        dialog = new Dialog(this);
        dialog.setTitle("Title...");
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_map);


        radioButton1 = (RadioButton) view1.findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) view1.findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) view1.findViewById(R.id.radioButton3);


        radioButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            }

        });

        radioButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            }

        });

        radioButton3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            }

        });



        mMapView = (MapView)view1.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                // Add a marker in Sydney and move the camera  15.773618, -86.796690
                //LatLng sydney = new LatLng(15.773618, -86.796690);

                LatLng sydney;
                try {
                    //sydney = new LatLng(Content.loc.getLatitude(), Content.loc.getLongitude());
                    sydney = new LatLng(15.773618, -86.796690);
                }catch (Exception e)
                {
                    sydney = new LatLng(15.773618, -86.796690);
                }


                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));

                mMap.setMyLocationEnabled(true);


                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng latLng) {


                        // Toast.makeText(getContext(), "Position: ", Toast.LENGTH_LONG).show();

                        // Creating a marker
                        MarkerOptions markerOptions = new MarkerOptions();

                        // Setting the position for the marker
                        markerOptions.position(latLng);

                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        markerOptions.title("Client Point");

                        //Toast.makeText(getContext(), latLng.latitude + " : " + latLng.longitude, Toast.LENGTH_LONG).show();

                        if(estado_edit!=false) {

                            btnGps.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_green));
                        }

                        lat=latLng.latitude+"";
                        lon=latLng.longitude+"";

                        //txtLatitud.setText(lat);
                        //txtLongitud.setText(lon);


                        // Clears the previously touched position
                        mMap.clear();

                        // Animating to the touched position
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        // Placing a marker on the touched position
                        mMap.addMarker(markerOptions);
                    }
                });


            }
        });


        builderMap = new AlertDialog.Builder(this);
        builderMap.setTitle("Seleccion Ubicacion de Local:");
        builderMap.setPositiveButton("Yes",null);

        builderMap.setNegativeButton("No", null);

        builderMap.setCancelable(false);
        builderMap.setView(view1);

        dialogs_mapa = builderMap.create();


        //crear dialogo


        //view1=inflater.inflate(R.layout.activity_maps,container,false);

        edtCodigo = (EditText) findViewById(R.id.edtCodigo);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtDireccion = (EditText) findViewById(R.id.edtDescripcion);
        edtTelefono = (EditText) findViewById(R.id.edtTelefono);
        edtCelular = (EditText) findViewById(R.id.edtCelular);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtRTN = (EditText) findViewById(R.id.edtRazon);

        //mandar a guardas los campos


        llenarEditText(tmp.getCodigo_c_empresa(),edtCodigo);
        llenarEditText(tmp.getNombre(),edtNombre);
        llenarEditText(tmp.getDireccion(),edtDireccion);
        llenarEditText(tmp.getCorreo(),edtEmail);
        llenarEditText(tmp.getTelefono(),edtTelefono);
        llenarEditText(tmp.getCelular(),edtCelular);
        llenarEditText(tmp.getIndentidad(),edtRTN);

        //mandar a guardar los campos



        btnGps=(Button) findViewById(R.id.btnGPS);
        btnFoto1=(Button) findViewById(R.id.btnFoto1);

        btnFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tmp.getFoto1().equals("0")&&estado_edit==false)
                {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Message")
                            .setMessage("Photo is empty")
                            .setPositiveButton(android.R.string.ok , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //set what would happen when positive button is clicked
                                    // finish();
                                }
                            })
                            .show();
                }
                else
                {
                    if(estado_edit==false) {

                        //show image en un dialog
                         //dialogs_foto.show();

                        if(tmp.getFoto1().equals("1")) {
                            cargar_foto(tmp.getCodigo(), "foto1");
                        }
                        else{

                            //vamos a cargar la foto dado que esta en la memoria todavia

                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(new File(tmp.getFoto1())));
                                dialog_pic.setImageBitmap(bitmap);
                                dialogs_foto.show();
                            }catch (Exception e)
                            {
                                Log.e("ERROR FOTO",e.toString());
                            }

                            //vamos a cargar la foto dado que esta en la memoria todavia
                        }

                        //show image en un dialog

                    }else
                    {

                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file;

                        try {
                            // Crea el Nombre de la Fotografía
                            String fechaHora = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                            String nombre = ALBUM + "_" + fechaHora + "_1";
                            // Crea el Archivo de la Fotografía
                            //file = nombrarArchivo(getContext(), ALBUM, nombre, EXTENSION_JPEG);


                            //foto = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/ILEGAL"+"/"+nombre+".jpg";
                            foto = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/fotos-clientes" + "/" + nombre + ".jpg";
                            file = new File(foto);


                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(file));

                            mDirAbsoluto = file.getAbsolutePath();


                        } catch (Exception e) {
                            e.printStackTrace();
                            file = null;
                            mDirAbsoluto = null;
                        }

                        startActivityForResult(intent, REQUEST_CODE_CAMARA);
                    }


                }

            }
        });

        img1 = (ImageView)findViewById(R.id.imageFoto1);

        btnFoto2=(Button) findViewById(R.id.btnFoto2);

        btnFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tmp.getFoto2().equals("0")&&estado_edit==false)
                {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Message")
                            .setMessage("Photo is empty")
                            .setPositiveButton(android.R.string.ok , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //set what would happen when positive button is clicked
                                    // finish();
                                }
                            })
                            .show();
                }
                else
                {

                    if(estado_edit==false) {

                        //show image en un dialog

                        //dialogs_foto.show();
                        if(tmp.getFoto2().equals("1")) {
                            cargar_foto(tmp.getCodigo(), "foto2");
                        }
                        else{

                            //vamos a cargar la foto dado que esta en la memoria todavia

                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(new File(tmp.getFoto2())));
                                dialog_pic.setImageBitmap(bitmap);
                                dialogs_foto.show();
                            }catch (Exception e)
                            {
                                Log.e("ERROR FOTO",e.toString());
                            }

                            //vamos a cargar la foto dado que esta en la memoria todavia
                        }

                        //show image en un dialog

                    }else {


                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file;

                        try {
                            // Crea el Nombre de la Fotografía
                            String fechaHora = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                            String nombre = ALBUM + "_" + fechaHora + "_2";
                            // Crea el Archivo de la Fotografía
                            //file = nombrarArchivo(getContext(), ALBUM, nombre, EXTENSION_JPEG);

                            //foto2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/ILEGAL"+"/"+nombre+".jpg";
                            foto2 = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/fotos-clientes" + "/" + nombre + ".jpg";
                            file = new File(foto2);

                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(file));

                            mDirAbsoluto2 = file.getAbsolutePath();

                        } catch (Exception e) {

                            e.printStackTrace();
                            file = null;
                            mDirAbsoluto2 = null;
                        }

                        startActivityForResult(intent, REQUEST_CODE_CAMARA2);

                    }

                }

            }
        });

        img2 = (ImageView)findViewById(R.id.imageFoto2);

        btnFoto3=(Button) findViewById(R.id.btnFoto3);

        btnFoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tmp.getFoto3().equals("0")&&estado_edit==false)
                {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Message")
                            .setMessage("Photo is empty")
                            .setPositiveButton(android.R.string.ok , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //set what would happen when positive button is clicked
                                    // finish();
                                }
                            })
                            .show();
                }
                else
                {

                    if(estado_edit==false) {

                        //show image en un dialog &&tmp.getEstado_sincronizar().equals("1")

                        //dialogs_foto.show();
                        if(tmp.getFoto3().equals("1")) {
                            cargar_foto(tmp.getCodigo(), "foto3");
                        }
                        else{

                            //vamos a cargar la foto dado que esta en la memoria todavia

                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(new File(tmp.getFoto3())));
                                dialog_pic.setImageBitmap(bitmap);
                                dialogs_foto.show();
                            }catch (Exception e)
                            {

                                Log.e("ERROR FOTO",e.toString());

                            }

                            //vamos a cargar la foto dado que esta en la memoria todavia
                        }

                        //show image en un dialog

                    }else {

                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file;

                        try {
                            // Crea el Nombre de la Fotografía
                            String fechaHora = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                            String nombre = ALBUM + "_" + fechaHora + "_3";
                            // Crea el Archivo de la Fotografía
                            //file = nombrarArchivo(getContext(), ALBUM, nombre, EXTENSION_JPEG);

                            //foto3 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/ILEGAL"+"/"+nombre+".jpg";
                            foto3 = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/fotos-clientes" + "/" + nombre + ".jpg";
                            file = new File(foto3);

                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(file));

                            mDirAbsoluto3 = file.getAbsolutePath();


                        } catch (Exception e) {
                            e.printStackTrace();
                            file = null;
                            mDirAbsoluto3 = null;
                        }



                        startActivityForResult(intent, REQUEST_CODE_CAMARA3);
                    }

                }

            }
        });

        img3 = (ImageView)findViewById(R.id.imageFoto3);


        btnIngresar=(Button) findViewById(R.id.btnIngresar);
        btnIngresar.setVisibility(View.GONE);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                     if(tmp.getFoto1().length()>1 || tmp.getFoto1().equals("1")) {
                         if (lat.length() > 1 || tmp.getEstado_localizacion().equals("1")) {

                             if(tmp.getEstado().equals("1")||tmp.getEstado().equals("0"))
                             {
                                 updateCliente(tmp,lat,lon,foto,foto2,foto3,db);
                             }
                             else
                             {
                                 if(validate_data()) {

                                     String codigo = "", rtn = "";
                                     if (edtCodigo.getText().length() < 0) {
                                         codigo = "";
                                     } else {
                                         codigo = edtCodigo.getText().toString();
                                     }
                                     if (edtRTN.getText().length() < 0) {
                                         rtn = "";
                                     } else {
                                         rtn = edtRTN.getText().toString();
                                     }
                                     db.updateCliente(tmp.getCodigo(), codigo, edtNombre.getText().toString(), edtDireccion.getText().toString(), rtn, edtEmail.getText().toString(), edtTelefono.getText().toString(), edtCelular.getText().toString(), foto, foto2, foto3, lat, lon, "1", "0", "2");
                                 }

                             }

                             ListContactsActivity.adapter.notifyDataSetChanged();

                             new AlertDialog.Builder(view.getContext())
                                     .setTitle("Message")
                                     .setMessage("Client Save")
                                     .setPositiveButton(android.R.string.ok , new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialogInterface, int i) {
                                             //set what would happen when positive button is clicked
                                             finish();
                                         }
                                     })
                                     .show();


                         }
                         else
                         {
                             //exigir selecionar gps antes de guardar

                             new AlertDialog.Builder(view.getContext())
                                     .setTitle("Advertencia")
                                     .setMessage("Selecione Ubicacion GPS")
                                     .setPositiveButton(android.R.string.ok, null)
                                     .show();


                             btnGps.setFocusable(true);
                             btnGps.requestFocus();
                         }
                     }
                     else
                     {
                         new AlertDialog.Builder(view.getContext())
                                 .setTitle("Advertencia")
                                 .setMessage("Se requiere una foto")
                                 .setPositiveButton(android.R.string.ok, null)
                                 .show();

                         //accion exigir tomar una foto aunque sea
                         btnFoto1.setFocusable(true);
                         btnFoto1.requestFocus();

                     }

                 }



        });

        btnEdit=(Button) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnEdit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_green));
                btnIngresar.setVisibility(View.VISIBLE);

                btnGps.setText("GPS Point");
                btnFoto1.setText("Photo 1");
                btnFoto2.setText("Photo 2");
                btnFoto3.setText("Photo 3");

                estado_edit=true;

            }
        });

        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tmp.getEstado_localizacion().equals("0")&&estado_edit==false)
                {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Message")
                            .setMessage("GPS is empty")
                            .setPositiveButton(android.R.string.ok , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //set what would happen when positive button is clicked
                                    // finish();
                                }
                            })
                            .show();
                }
                else
                {

                    Toast.makeText(view.getContext(), "ESTADO DE GPS: "+tmp.getEstado_localizacion(), Toast.LENGTH_LONG).show();

                    if(tmp.getEstado_localizacion().equals("1"))
                    {

                        mMap.clear();
                        LatLng sydney;
                        try {
                            //sydney = new LatLng(Content.loc.getLatitude(), Content.loc.getLongitude());

                            double lt=Double.parseDouble(tmp.getLatitude());
                            double lon=Double.parseDouble(tmp.getLongitud());

                            sydney = new LatLng(lt, lon);

                           // Toast.makeText(view.getContext(), sydney.latitude + " : " + sydney.longitude, Toast.LENGTH_LONG).show();

                        }catch (Exception e)
                        {
                            Toast.makeText(view.getContext(), ""+e, Toast.LENGTH_LONG).show();
                            sydney = new LatLng(15.773618, -86.796690);
                        }


                        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));

                        dialogs_mapa.show();

                    }
                    else
                    {
                        dialogs_mapa.show();
                    }



                }

            }
        });


        //mensaje de que es obligatorio de guardar la ubicacion del cliente
        //Log.e("Local:",""+tmp.getEstado_localizacion());
        if(tmp.getEstado_localizacion().equals("0"))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Message")
                    .setMessage("GPS location is required, also a photo of the place")
                    .setPositiveButton(android.R.string.ok , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what would happen when positive button is clicked
                            // finish();
                        }
                    })
                    .show();

            btnGps.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_red));
        }
        //mensaje de que es obligatorio de guardar la ubicacion del cliente
        verificarFoto();
       editableText(tmp.getEstado());


    }

    public void llenarEditText(String dato,EditText edt)
    {
        try
        {
            edt.setText(dato);
        }
        catch (Exception e)
        {

        }
    }
    public void enableEditText(EditText edt)
    {
        try
        {
           // edt.setEnabled(false);
            edt.setKeyListener(null);
        }
        catch (Exception e)
        {

        }
    }

    public void verificarFoto()
    {
        if(tmp.getFoto1().equals("0")) {

            btnFoto1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_red));

        }
        if(tmp.getFoto2().equals("0")) {

            btnFoto2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_red));

        }
        if(tmp.getFoto3().equals("0")) {

            btnFoto3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_red));

        }

    }

    public void editableText(String estado)
    {
        if(estado.equals("1"))
        {
            enableEditText(edtCodigo);
            enableEditText(edtNombre);
            enableEditText(edtDireccion);
            enableEditText(edtTelefono);
            enableEditText(edtCelular);
            enableEditText(edtEmail);
            enableEditText(edtRTN);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("IMAGENANTES",  requestCode+"");

        switch (requestCode) {
            case REQUEST_CODE_CAMARA:

                if (resultCode == RESULT_OK) {

                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(new File(foto)));

                        //Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap,  200 ,20, true);//this bitmap2 you can use only for display

                        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.foto);

                        // b1r=bitmap;

                        img1.setImageBitmap(bitmap);
                        btnFoto1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_green));

                        tmp.setFoto1("1");

                        // img1.setImageURI(Uri.parse(foto));
                        //  img1.setImageResource();


                    } catch (Exception e) {
                        // TODO:
                        Log.e("IMAGEN",e.toString());
                    }
                }
                break;

            case REQUEST_CODE_CAMARA2:



                if (resultCode == RESULT_OK) {

                    try {



                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(new File(foto2)));

                        //Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap,  200 ,20, true);//this bitmap2 you can use only for display

                        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.foto);

                        img2.setImageBitmap(bitmap);

                        btnFoto2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_green));

                        tmp.setFoto2("1");

                        // img1.setImageURI(Uri.parse(foto));
                        //  img1.setImageResource();


                    } catch (Exception e) {
                        // TODO:
                        Log.e("IMAGEN",e.toString());
                    }
                }
                break;

            case REQUEST_CODE_CAMARA3:

                if (resultCode == RESULT_OK) {

                    try {

                        //  Toast.makeText(getContext(),"Error al crear el directorio.",Toast.LENGTH_SHORT).show();

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(new File(foto3)));

                        //Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap,  200 ,20, true);//this bitmap2 you can use only for display

                        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.foto);

                        img3.setImageBitmap(bitmap);
                        btnFoto3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_green));

                        tmp.setFoto3("1");
                        // img1.setImageURI(Uri.parse(foto));
                        //  img1.setImageResource();


                    } catch (Exception e) {
                        // TODO:
                        Log.e("IMAGEN",e.toString());
                    }
                }
                break;

            default:
                break;
        }

    }

    public void updateCliente(Cliente tmp,String lat,String lon,String foto,String foto2,String foto3,DatabaseHelper db)
    {

        if(lat.length()>1)
        {
            //update gps and change estado 2
            db.updateClienteGPS(tmp.getCodigo(),lat,lon);
            db.updateClienteEstado(tmp.getCodigo(),"1");
            db.updateCliente(tmp.getCodigo(),"0");

        }
        if(foto.length()>1)
        {
            //update foto and change estado 2
            db.updateClienteFoto1(tmp.getCodigo(),foto);
            db.updateClienteEstado(tmp.getCodigo(),"1");
            db.updateCliente(tmp.getCodigo(),"0");

        }
        if(foto2.length()>1)
        {
            //update foto and change estado 2
            //update foto and change estado 2
            db.updateClienteFoto2(tmp.getCodigo(),foto2);
            db.updateClienteEstado(tmp.getCodigo(),"1");
            db.updateCliente(tmp.getCodigo(),"0");

        }
        if(foto3.length()>1)
        {
            //update foto and change estado 2
            //update foto and change estado 2
            db.updateClienteFoto3(tmp.getCodigo(),foto3);
            db.updateClienteEstado(tmp.getCodigo(),"1");
            db.updateCliente(tmp.getCodigo(),"0");

        }

    }
    public boolean validate_data()
    {
        boolean estado=true;

        if (edtEmail.length() <= 0) {
            edtEmail.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
            edtEmail.setHint("Required");
            edtEmail.setHintTextColor(Color.RED);
            edtEmail.setFocusable(true);
            edtEmail.requestFocus();
            estado=false;
        }
        else
        {
            edtEmail.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1")) );
        }
        if (edtCelular.length() <= 0) {
            edtCelular.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
            edtCelular.setHint("Required");
            edtCelular.setHintTextColor(Color.RED);
            edtCelular.setFocusable(true);
            edtCelular.requestFocus();
            estado=false;
        }
        else
        {
            edtCelular.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1")) );
        }
        if (edtTelefono.length() <= 0) {
            edtTelefono.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
            edtTelefono.setHint("Required");
            edtTelefono.setHintTextColor(Color.RED);
            edtTelefono.setFocusable(true);
            edtTelefono.requestFocus();
            estado=false;
        }
        else
        {
            edtTelefono.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1")) );
        }
        if (edtDireccion.length() <= 0) {
            edtDireccion.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
            edtDireccion.setHint("Required");
            edtDireccion.setHintTextColor(Color.RED);
            edtDireccion.setFocusable(true);
            edtDireccion.requestFocus();
            estado=false;
        }
        else
        {
            edtDireccion.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1")) );
        }
        if (edtNombre.length() <= 0) {
            edtNombre.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
            edtNombre.setHint("Required");
            edtNombre.setHintTextColor(Color.RED);
            edtNombre.setFocusable(true);
            edtNombre.requestFocus();
            estado=false;
        }
        else
        {
            edtNombre.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1")) );
        }

        return estado;
    }

    //codigo para visualizar foto


    public void cargar_foto(String codigo,String foto) {

        final ProgressDialog progressDialog = new ProgressDialog(view_foto.getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

            String url = "http://gisvisit.com/api/descargar_foto.php?codigo_cliente=" + codigo + "&foto="+foto;


            StringRequest jsonArrayRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("foto");

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject ob = array.getJSONObject(i);

                           // Log.e("ERROR", ob.getString("foto"));

                            byte[] decodedString = Base64.decode(ob.getString("foto"), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            dialog_pic.setImageBitmap(decodedByte);

                        }

                        dialogs_foto.show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR", e.toString());
                        progressDialog.dismiss();

                    }
                    progressDialog.dismiss();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley", error.toString());
                    progressDialog.dismiss();

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonArrayRequest);



        // progressDialog_list.dismiss();



    }

    //codigo para visualizar foto


}
