package hn.gisvisit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddVisitActivity extends AppCompatActivity {

    RadioButton rdClient,rdNotClient,rdNoAgendar,rdAgender;
    TextView textView1,textView2,textView8,textView9,textView10,textView12,textView13,textView14;
    EditText edtNombre,edtDireccion,edtTelefono,edtEmail,edtFecha,edtObservacion;
    Spinner edtRazon;
    EditText spClient;
    Button btnBusqueda,btnSave;
    boolean estado_form=false; //false visit client inicio, true new client visit cambio
    public static Cliente tmp=null;
    public static boolean estado_busqueda=false,estado_fechaproxima=false;
    String[] razon = {"Visit","Sale Visit","Payment Visit","Tracing Visit","Post Sale Visit"};

    //variables de visita
    Button btnGps,btnFoto1,btnFoto2,btnFoto3,btnIngresar;
    boolean estadoGps=false,estadoFoto1=false,estadoFoto2=false,estadoFoto3=false;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private static final int REQUEST_CODE_CAMARA = 1;
    private static final int REQUEST_CODE_CAMARA2 = 2;
    private static final int REQUEST_CODE_CAMARA3 = 3;
    private static final int SCALE_FACTOR_IMAGE_VIEW = 4;
    private static final String ALBUM = "FOTO";
    private static final String EXTENSION_JPEG = ".jpg";

    private String foto="",foto2="",foto3="";

    ImageView img1,img2,img3;

    View view1;
    MapView mMapView;

    private RadioButton radioButton1,radioButton2,radioButton3;

    GoogleMap mMap;
    AlertDialog.Builder builderMap;
    AlertDialog dialogs_mapa;
    Dialog dialog;

    private String mDirAbsoluto = "";
    private String mDirAbsoluto2 = "";
    private String mDirAbsoluto3 = "";

    DatabaseHelper db= new DatabaseHelper(this);

    int bandera=0;

    String lat="0",lon="0";
    String c_evento="0";
    String id_event_update="0";

    //variables de visita
    String estado_visita="2";


    @Override
    protected void onResume() {
        super.onResume();

        if(estado_busqueda==true)
        {
            spClient.setText(tmp.getNombre());
        }
        else
        {

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visit);

        //recibir visita desde un evento
        spClient=(EditText) findViewById(R.id.edtName);

        edtRazon=(Spinner) findViewById(R.id.edtRazon);
        edtRazon.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,razon));


        try
        {

            Bundle parametros = this.getIntent().getExtras();
            String id = parametros.getString("id");
            String codigo = parametros.getString("c_event");
            String id_event = parametros.getString("id_event");
            estado_visita="0";

            Evento tmp_evento=db.getEvento(id_event);

            seleccionarPositionSpinnerRazon(tmp_evento.getTipo_visit());
            //Log.e("VISIT",""+tmp_evento.getTipo_visit());

            c_evento=codigo;
            id_event_update=id_event;

            tmp=db.getCliente(id);
            spClient.setText(tmp.getNombre());

            //Toast.makeText(this, "Position: "+id, Toast.LENGTH_LONG).show();

        }catch (Exception e)
        {
            Log.e("VISIT","INICIO SIN ID DE CLIENTE");
            Log.e("VISIT",""+e.toString());
        }

        //recibir visita desde un evento

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view1 = inflater.inflate(R.layout.activity_dialog_map, null);

        //crear dialogo


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

        btnSave=(Button) findViewById(R.id.btnIngresar);

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //creamos la variable de la visita creada HHmmss

                SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String currentdate= ss.format(date);

                //Toast.makeText(getApplicationContext(), "FECHA INICIO: "+currentdate, Toast.LENGTH_SHORT).show();

                //creamos la variable de la visita creada

                if(foto.length()>1) {
                    if (lat.length() > 1) {

                        //Toast.makeText(v.getContext(), "ESTADO: "+estado_busqueda, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(v.getContext(), "ESTADO2: "+validate_data(estado_busqueda), Toast.LENGTH_SHORT).show();

                        if (validate_data(estado_form)) {

                            //Toast.makeText(v.getContext(), "ESTADO2: "+validate_data(estado_busqueda), Toast.LENGTH_SHORT).show();

                            if(estado_form==false)
                            {

                                String fecha_proxima="";
                                if (edtFecha.length() <= 0) {
                                    fecha_proxima="0";
                                }
                                else
                                {
                                    fecha_proxima=edtFecha.getText().toString();
                                }

                                db.insertVisita(tmp.getCodigo(), edtRazon.getSelectedItem().toString(),edtObservacion.getText().toString(),currentdate,  foto, foto2, foto2, lat, lon, fecha_proxima,c_evento, "0", estado_visita);

                                db.updateEvent_estado(id_event_update,"1");

                                new AlertDialog.Builder(v.getContext())
                                        .setTitle("Message")
                                        .setMessage("Visit Save"+currentdate)
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

                                String fecha_proxima="";
                                if (edtFecha.length() <= 0) {
                                    fecha_proxima="0";
                                }
                                else
                                {
                                    fecha_proxima=edtFecha.getText().toString();
                                }

                                int c = db.getClienteMax() + 1;

                                db.insertCliente(String.valueOf(c), "", edtNombre.getText().toString(), edtDireccion.getText().toString(), "", edtEmail.getText().toString(), edtTelefono.getText().toString(), "", foto, foto2, foto2, lat, lon, "1", "0", "2");

                                Cliente tmp=db.getCliente(String.valueOf(c));
                                ListContactsActivity.clienteList.add(tmp);
                                ListContactsActivity.adapter.notifyDataSetChanged();

                                db.insertVisita(String.valueOf(c), edtRazon.getSelectedItem().toString(),edtObservacion.getText().toString(),currentdate,  foto, foto2, foto2, lat, lon, fecha_proxima,c_evento, "0", "2");

                                new AlertDialog.Builder(v.getContext())
                                        .setTitle("Message")
                                        .setMessage("Visit Save")
                                        .setPositiveButton(android.R.string.ok , new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //set what would happen when positive button is clicked
                                                finish();
                                            }
                                        })
                                        .show();

                            }

                        }

                    }
                    else
                    {

                        new AlertDialog.Builder(v.getContext())
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
                    new AlertDialog.Builder(v.getContext())
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

                        btnGps.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_green));

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

        //button foto

        btnGps=(Button) findViewById(R.id.btnGPS);
        btnFoto1=(Button) findViewById(R.id.btnFoto1);

        btnFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
        });

        img1 = (ImageView)findViewById(R.id.imageFoto1);

        btnFoto2=(Button) findViewById(R.id.btnFoto2);

        btnFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
        });

        img2 = (ImageView)findViewById(R.id.imageFoto2);

        btnFoto3=(Button) findViewById(R.id.btnFoto3);

        btnFoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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

        });

        img2 = (ImageView)findViewById(R.id.imageFoto2);

        //button foto



        rdClient=(RadioButton) findViewById(R.id.rdClient);
        rdNotClient=(RadioButton) findViewById(R.id.rdNotClient);

        rdAgender=(RadioButton) findViewById(R.id.rdAgendar);
        rdNoAgendar=(RadioButton) findViewById(R.id.rdNotAgendar);

        rdNoAgendar.setChecked(true);

        rdClient.setChecked(true);

        textView1=(TextView) findViewById(R.id.textView1);
        //spClient=(EditText) findViewById(R.id.edtName);
        spClient.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    estado_busqueda=false;
                    startActivity(new Intent(getApplicationContext(), ListContactsBuscarActivity.class));

                }
                return false;
            }
        });

        btnGps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                dialogs_mapa.show();
                // mMapView.getOverlays().clear();

            }
        });

        btnBusqueda=(Button) findViewById(R.id.btnBusqueda);
        btnBusqueda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                estado_busqueda=false;
                startActivity(new Intent(getApplicationContext(), ListContactsBuscarActivity.class));

            }

        });

        //formulario not clients
        textView2=(TextView) findViewById(R.id.textView2);
        edtNombre=(EditText) findViewById(R.id.edtNombre);

        textView8=(TextView) findViewById(R.id.textView8);
        edtDireccion=(EditText) findViewById(R.id.edtDescripcion);

        textView9=(TextView) findViewById(R.id.textView9);
        edtTelefono=(EditText) findViewById(R.id.edtTelefono);

        textView10=(TextView) findViewById(R.id.textView10);
        edtEmail=(EditText) findViewById(R.id.edtEmail);

        textView12=(TextView) findViewById(R.id.textView12);
        edtFecha=(EditText) findViewById(R.id.edtFechaAgendar);

        textView13=(TextView) findViewById(R.id.textView3);
        //edtRazon=(EditText) findViewById(R.id.edtRazon);

        textView14=(TextView) findViewById(R.id.textView4);
        edtObservacion=(EditText) findViewById(R.id.edtObservation);

        textView2.setVisibility(View.GONE);
        edtNombre.setVisibility(View.GONE);

        textView8.setVisibility(View.GONE);
        edtDireccion.setVisibility(View.GONE);

        textView9.setVisibility(View.GONE);
        edtTelefono.setVisibility(View.GONE);

        textView10.setVisibility(View.GONE);
        edtEmail.setVisibility(View.GONE);

        textView12.setVisibility(View.GONE);
        edtFecha.setVisibility(View.GONE);


        //formulario not clients

        rdNotClient.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                spClient.setVisibility(View.GONE);
                textView1.setVisibility(View.GONE);
                btnBusqueda.setVisibility(View.GONE);

                textView2.setVisibility(View.VISIBLE);
                edtNombre.setVisibility(View.VISIBLE);

                textView8.setVisibility(View.VISIBLE);
                edtDireccion.setVisibility(View.VISIBLE);

                textView9.setVisibility(View.VISIBLE);
                edtTelefono.setVisibility(View.VISIBLE);

                textView10.setVisibility(View.VISIBLE);
                edtEmail.setVisibility(View.VISIBLE);

                estado_form=true;

            }

        });

        rdClient.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                spClient.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                btnBusqueda.setVisibility(View.VISIBLE);

                textView2.setVisibility(View.GONE);
                edtNombre.setVisibility(View.GONE);

                textView8.setVisibility(View.GONE);
                edtDireccion.setVisibility(View.GONE);

                textView9.setVisibility(View.GONE);
                edtTelefono.setVisibility(View.GONE);

                textView10.setVisibility(View.GONE);
                edtEmail.setVisibility(View.GONE);

                estado_form=false;

            }

        });

        rdNoAgendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                textView12.setVisibility(View.GONE);
                edtFecha.setVisibility(View.GONE);
                estado_fechaproxima=false;

            }

        });

        rdAgender.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                textView12.setVisibility(View.VISIBLE);
                edtFecha.setVisibility(View.VISIBLE);
                estado_fechaproxima=true;

            }

        });


        edtFecha=(EditText) findViewById(R.id.edtFechaAgendar);
        edtFecha.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    edtFecha.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();


                }
                return false;
            }
        });


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

    public boolean validate_data(Boolean tipo_visita)
    {
        boolean estado=true;

        if(tipo_visita==false)
        {
            if (spClient.length() <= 0) {
                spClient.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
                spClient.setHint("Required");
                spClient.setHintTextColor(Color.RED);
                spClient.setFocusable(true);
                spClient.requestFocus();
                estado=false;
            }
            else
            {
                spClient.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1")) );
            }

            /*
            if (edtRazon.length() <= 0) {
                edtRazon.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
                edtRazon.setHint("Required");
                edtRazon.setHintTextColor(Color.RED);
                edtRazon.setFocusable(true);
                edtRazon.requestFocus();
                estado=false;
            }
            else
            {
                edtRazon.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1")) );
            }
            */

            if (edtObservacion.length() <= 0) {
                edtObservacion.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
                edtObservacion.setHint("Required");
                edtObservacion.setHintTextColor(Color.RED);
                edtObservacion.setFocusable(true);
                edtObservacion.requestFocus();
                estado=false;
            }
            else
            {
                edtObservacion.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1")) );
            }


        }
        else{


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

            /*
            if (edtRazon.length() <= 0) {
                edtRazon.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
                edtRazon.setHint("Required");
                edtRazon.setHintTextColor(Color.RED);
                edtRazon.setFocusable(true);
                edtRazon.requestFocus();
                estado=false;
            }
            else
            {
                edtRazon.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1")) );
            }
            */

            if (edtObservacion.length() <= 0) {
                edtObservacion.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
                edtObservacion.setHint("Required");
                edtObservacion.setHintTextColor(Color.RED);
                edtObservacion.setFocusable(true);
                edtObservacion.requestFocus();
                estado=false;
            }
            else
            {
                edtObservacion.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1")) );
            }


        }


        if(estado_fechaproxima==true)
        {
            if (edtFecha.length() <= 0) {
                edtFecha.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
                edtFecha.setHint("Required");
                edtFecha.setHintTextColor(Color.RED);
                edtFecha.setFocusable(true);
                edtFecha.requestFocus();
                estado=false;
            }
            else
            {
                edtFecha.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1")) );
            }
        }


        return estado;
    }

    public void seleccionarPositionSpinnerRazon(String name)
    {
        if(name.equals("Sale Visit"))
        {
            //Log.e("Entro","1");
            edtRazon.setSelection(1);

        }
        if(name.equals("Payment Visit"))
        {
            //Log.e("Entro","2");
            edtRazon.setSelection(2);
        }
        if(name.equals("Tracing Visit"))
        {
            //Log.e("Entro","3");
            edtRazon.setSelection(3);
        }
        if(name.equals("Post Sale Visit"))
        {
            //Log.e("Entro","4");
            edtRazon.setSelection(4);
        }
    }

    //trae evento de visita de mismo dia si existe
    public String trae_evento_visita(String id_cliente)
    {
        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String currentdate= ss.format(date);


        return "";
    }



}
