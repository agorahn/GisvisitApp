package hn.gisvisit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/*
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.MapQuestTileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
*/

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

/*
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
*/

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddContactsActivity extends AppCompatActivity {

    EditText edtCodigo,edtNombre,edtDireccion,edtTelefono,edtCelular,edtEmail,edtRTN;
    Button btnGps,btnFoto1,btnFoto2,btnFoto3,btnIngresar;
    boolean estadoGps=false,estadoFoto1=false,estadoFoto2=false,estadoFoto3=false;

    private static final int REQUEST_CODE_CAMARA = 1;
    private static final int REQUEST_CODE_CAMARA2 = 2;
    private static final int REQUEST_CODE_CAMARA3 = 3;
    private static final int SCALE_FACTOR_IMAGE_VIEW = 4;
    private static final String ALBUM = "FOTO";
    private static final String EXTENSION_JPEG = ".jpg";

    private String foto="0",foto2="0",foto3="0";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view1 = inflater.inflate(R.layout.activity_dialog_map, null);
        //view1=inflater.inflate(R.layout.activity_maps,container,false);

        edtCodigo=(EditText) findViewById(R.id.edtCodigo);
        edtNombre=(EditText) findViewById(R.id.edtNombre);
        edtDireccion=(EditText) findViewById(R.id.edtDescripcion);
        edtTelefono=(EditText) findViewById(R.id.edtTelefono);
        edtCelular=(EditText) findViewById(R.id.edtCelular);
        edtEmail=(EditText) findViewById(R.id.edtEmail);
        edtRTN=(EditText) findViewById(R.id.edtRazon);


        btnGps=(Button) findViewById(R.id.btnGPS);
        btnFoto1=(Button) findViewById(R.id.btnFoto1);
        img1 = (ImageView)findViewById(R.id.imageFoto1);

        btnFoto2=(Button) findViewById(R.id.btnFoto2);
        img2 = (ImageView)findViewById(R.id.imageFoto2);

        btnFoto3=(Button) findViewById(R.id.btnFoto3);
        img2 = (ImageView)findViewById(R.id.imageFoto2);

        btnIngresar=(Button) findViewById(R.id.btnIngresar);

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
                    String nombre = ALBUM + "_" + fechaHora+"_1";
                    // Crea el Archivo de la Fotografía
                    //file = nombrarArchivo(getContext(), ALBUM, nombre, EXTENSION_JPEG);


                    //foto = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/ILEGAL"+"/"+nombre+".jpg";
                    foto = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/fotos-clientes"+"/"+nombre+".jpg";
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
                    String nombre = ALBUM + "_" + fechaHora+"_2";
                    // Crea el Archivo de la Fotografía
                    //file = nombrarArchivo(getContext(), ALBUM, nombre, EXTENSION_JPEG);

                    //foto2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/ILEGAL"+"/"+nombre+".jpg";
                    foto2 = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/fotos-clientes"+"/"+nombre+".jpg";
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
                    String nombre = ALBUM + "_" + fechaHora+"_3";
                    // Crea el Archivo de la Fotografía
                    //file = nombrarArchivo(getContext(), ALBUM, nombre, EXTENSION_JPEG);

                    //foto3 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/ILEGAL"+"/"+nombre+".jpg";
                    foto3 = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/fotos-clientes"+"/"+nombre+".jpg";
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

              // mMapView.setTileSource(TileSourceFactory.MAPNIK);

                //final ITileSource tileSource = new MapQuestTileSource(getApplicationContext());
                //mMapView.setTileSource(tileSource);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            }

        });

        radioButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /*
                org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource.retrieveBingKey(getApplicationContext());
                org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource.setBingKey("AsrJHSTFHrWNkTbea4S0VHm60wbKvpABELsA94QFhyuendf2W8cYPzDlBqtQPBUw");
                org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource bing=new org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource(null);
                mMapView.setTileSource(bing);

                bing.setStyle(BingMapTileSource.IMAGERYSET_AERIAL);
                */
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            }

        });

        radioButton3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /*
                org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource.retrieveBingKey(getApplicationContext());
                org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource.setBingKey("AsrJHSTFHrWNkTbea4S0VHm60wbKvpABELsA94QFhyuendf2W8cYPzDlBqtQPBUw");
                org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource bing=new org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource(null);
                mMapView.setTileSource(bing);

                bing.setStyle(BingMapTileSource.IMAGERYSET_AERIALWITHLABELS);
                */
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
        //mMapView.setTileSource(TileSourceFactory.MAPNIK);

        //mMapView.setMultiTouchControls(true);
        //mMapView.setBuiltInZoomControls(true);
        //mMapView.setClickable(true);
        //MapController mapController = (MapController) mMapView.getController();
        //mapController.setZoom(10);
        //mMapView.setMaxZoomLevel(19.0);

        //gps location created 15.777206, -86.794066
       // LatLng sydney = new LatLng(15.777206, -86.794066);
       //mapController.setCenter(new GeoPoint(15.777206, -86.794066));


        /*
        MyLocationNewOverlay myLocationoverlay = new MyLocationNewOverlay(mMapView);
        myLocationoverlay.enableFollowLocation();
        myLocationoverlay.enableMyLocation();

        mMapView.getOverlays().add(myLocationoverlay);

         */


        //gps location created


        /*
        MapEventsReceiver mReceive = new MapEventsReceiver() {

            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {

                // write your code here
                //Toast.makeText(getBaseContext(),p.getLatitude() + " - "+p.getLongitude(),Toast.LENGTH_LONG).show();


                //mMapView.getOverlays().clear();


                if(bandera==0) {

                    //btnGps.setBackground(R.drawable.rounded_button_green);
                    btnGps.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_green));
                    lat=p.getLatitude()+"";
                    lon=p.getLongitude()+"";

                    Marker marker_osm = new Marker(mMapView);
                    marker_osm.setPosition(p);
                    marker_osm.setTitle("Client Point");
                    mMapView.getOverlays().add(marker_osm);
                    bandera=1;


                }
                else
                {
                    btnGps.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_green));
                    lat=p.getLatitude()+"";
                    lon=p.getLongitude()+"";

                   // mMapView.getOverlays().remove(2);
                   // mMapView.getOverlays().clear();
                    //mMapView.invalidate();}

                    Marker marker_osm = new Marker(mMapView);
                    marker_osm.setPosition(p);
                    marker_osm.setTitle("Client Point");

                    mMapView.getOverlays().add(marker_osm);

                }


                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                // write your code here
                return false;
            }
        };
        MapEventsOverlay OverlayEvents = new MapEventsOverlay(mReceive);
        mMapView.getOverlays().add(OverlayEvents);

        */


        builderMap = new AlertDialog.Builder(this);
        builderMap.setTitle("Seleccion Ubicacion de Local:");
        builderMap.setPositiveButton("Yes",null);
         /*
        builderMap.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //set what would happen when positive button is clicked
                //finish();

                if(bandera==1)
                {
                    mMapView.getOverlays().remove(2);
                }

                bandera=0;

            }
        });
         */


        builderMap.setNegativeButton("No", null);
         /*
        builderMap.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //set what should happen when negative button is clicked
                //finish();

                if(bandera==1)
                {
                    mMapView.getOverlays().remove(2);
                }
                bandera=0;


            }
        });
    */


        builderMap.setCancelable(false);
        builderMap.setView(view1);

        dialogs_mapa = builderMap.create();


        btnGps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                dialogs_mapa.show();
               // mMapView.getOverlays().clear();  1800

            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(getBaseContext(),"CLICK",Toast.LENGTH_LONG).show();

                if(validate_data())
                {
                    if(foto.length()>1)
                    {
                        if(lat.length()>1)
                        {

                            //insertar en la tabla de clientes

                            try {

                                int c = db.getClienteMax() + 1;
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

                                db.insertCliente(String.valueOf(c), codigo, edtNombre.getText().toString(), edtDireccion.getText().toString(), rtn, edtEmail.getText().toString(), edtTelefono.getText().toString(), edtCelular.getText().toString(), foto, foto2, foto2, lat, lon, "1", "0", "2");

                                Cliente tmp=db.getCliente(String.valueOf(c));
                                ListContactsActivity.clienteList.add(tmp);
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

                               // finish();
                            }catch (Exception e)
                            {
                                Log.e("ERROR: ",e.toString()+"");
                            }

                            //verficar el internet y mandar a guardar a la nube
                            //aqui mandamos a guardar a la nube


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

            }
        });





        //dialog de map



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


}
