package hn.gisvisit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewEventActivity extends AppCompatActivity {


    MapView mMapView;
    DatabaseHelper db= new DatabaseHelper(this);
    GoogleMap mMap;

    Evento tmp;
    Button back,search,visit;
    EditText edtTipoVisita,edtName,edtDescripcion,edtDateMeeting,edtDateAction;

    String lat="0",lon="0";
    double lat_dbl,lon_dbl;
    Cliente tmp_cliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        Bundle parametros = this.getIntent().getExtras();
        String id = parametros.getString("id");

        tmp = db.getEvento(id);

        back=(Button)findViewById(R.id.btnBack);
        search=(Button)findViewById(R.id.btnBusqueda);
        visit=(Button)findViewById(R.id.btnVisit);

        if(tmp.getCodigo_c_cliente().equals("0"))
        {
            lat=tmp.getLatitude();
            lon=tmp.getLongitud();
        }
        else
        {
            tmp_cliente=db.getCliente_codigo(tmp.getCodigo_c_cliente());

            if(tmp_cliente.getEstado_localizacion().equals("0"))
            {
                new AlertDialog.Builder(this)
                        .setTitle("Message")
                        .setMessage("Dont have a GPS point")
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
                lat=tmp_cliente.getLatitude();
                lon=tmp_cliente.getLongitud();
            }


        }
        edtName=(EditText) findViewById(R.id.edtName);
        edtTipoVisita=(EditText) findViewById(R.id.edtTipoVisita);
        edtDescripcion=(EditText) findViewById(R.id.edtDescripcion);
        edtDateMeeting=(EditText) findViewById(R.id.edtDateMeeting);
        edtDateAction=(EditText) findViewById(R.id.edtDateAction);

        enableEditText(edtName);
        enableEditText(edtTipoVisita);
        enableEditText(edtDescripcion);
        enableEditText(edtDateMeeting);
        enableEditText(edtDateAction);


        if(tmp.getCodigo_c_cliente().equals("0"))
        {
            edtName.setText("NO NAME");
        }else
        {
            edtName.setText(tmp_cliente.getNombre()+"");
        }


        if(tmp.getTipo_visit()!=null)
        {
            edtTipoVisita.setText(tmp.getTipo_visit()+"");
        }
        if(tmp.getVisit_reason()!=null)
        {
            edtDescripcion.setText(tmp.getVisit_reason()+"");
        }
        if(tmp.getFecha_programacion()!=null)
        {
            edtDateMeeting.setText(tmp.getFecha_programacion()+"");
        }
        if(tmp.getFecha_realizada()!=null)
        {
            edtDateAction.setText(tmp.getFecha_realizada()+"");
        }

        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

                Bundle parmetros = new Bundle();
                parmetros.putString("id", tmp_cliente.getId()+"");
                parmetros.putString("c_event", tmp.getCodigo_c_evento()+"");
                parmetros.putString("id_event", tmp.getId()+"");

                Intent i = new Intent(view.getContext(), AddVisitActivity.class);
                i.putExtras(parmetros);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(i);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle parmetros = new Bundle();
                parmetros.putString("id", tmp_cliente.getId()+"");

                Intent i = new Intent(view.getContext(), ViewContactsActivity.class);
                i.putExtras(parmetros);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(i);

            }
        });

        mMapView = (MapView)findViewById(R.id.mapView2);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                // Add a marker in Sydney and move the camera  15.773618, -86.796690
                // LatLng sydney = new LatLng(15.773618, -86.796690);

                LatLng sydney;
                lat_dbl=Double.parseDouble(lat);
                lon_dbl=Double.parseDouble(lon);

                try {
                    //sydney = new LatLng(Content.loc.getLatitude(), Content.loc.getLongitude());
                    sydney = new LatLng(lat_dbl, lon_dbl);
                }catch (Exception e)
                {
                    sydney = new LatLng(0, 0);
                }
                mMap.addMarker(new MarkerOptions().position(sydney).title("Point"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
                mMap.setMyLocationEnabled(true);

            }
        });

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

}
