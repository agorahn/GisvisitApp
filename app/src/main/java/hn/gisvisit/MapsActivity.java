package hn.gisvisit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MapsActivity extends AppCompatActivity {

    MapView mMapView;
    DatabaseHelper db= new DatabaseHelper(this);
    GoogleMap mMap;
    Spinner selection;

    Cliente tmp_client;
    Visita  tmp_visit;
    Evento  tmp_evento;

    private FloatingActionButton btnSwitch;
    LinearLayout layout;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

     List<Cliente> clienteList;
     ClienteAdapter adapter_client;
     List<Evento> eventList;
     EventAdapter adapter_event;
     List<Visita> visitList;
     VisitAdapter adapter_visita;
     private ListView eList;

     TextView nombre_lista;

    int bandera=0;
    int estado_click=0;
    private RadioButton radioButton1,radioButton2,radioButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        final Context context = this;

        String[] lenguajes = {"Select view","Clients view","Events view","Visits view"};

        nombre_lista=findViewById(R.id.textView4);

        eList = findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(eList.getContext(), linearLayoutManager.getOrientation());

        btnSwitch=(FloatingActionButton)findViewById(R.id.btnSwitch);
        layout = (LinearLayout) findViewById(R.id.linearMap);

        //ocultar layout

        eList.setVisibility(View.GONE);
        //Toast.makeText(view.getContext(), "VISIBLE: ",Toast.LENGTH_SHORT).show();

        ViewGroup.LayoutParams params = layout.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = params.MATCH_PARENT;
        params.width = params.MATCH_PARENT;

        btnSwitch.setImageResource(android.R.drawable.arrow_up_float);
        //arrow_up_float

        layout.setLayoutParams(params);

        //ocultar layout

        btnSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(bandera==1)
                {
                    bandera=0;
                    eList.setVisibility(View.GONE);
                    //Toast.makeText(view.getContext(), "VISIBLE: ",Toast.LENGTH_SHORT).show();

                    ViewGroup.LayoutParams params = layout.getLayoutParams();
                    // Changes the height and width to the specified *pixels*
                    params.height = params.MATCH_PARENT;
                    params.width = params.MATCH_PARENT;

                    btnSwitch.setImageResource(android.R.drawable.arrow_up_float);
                    //arrow_up_float

                    layout.setLayoutParams(params);
                }
                else
                {
                    bandera=1;
                    eList.setVisibility(View.VISIBLE);
                    //Toast.makeText(view.getContext(), "NO VISIBLE: ",Toast.LENGTH_SHORT).show();

                    ViewGroup.LayoutParams params = layout.getLayoutParams();
                    // Changes the height and width to the specified *pixels*
                    params.height = 1150;
                    params.width = params.MATCH_PARENT;

                    btnSwitch.setImageResource(android.R.drawable.arrow_down_float);

                    layout.setLayoutParams(params);



                }

            }

        });

        selection=(Spinner)findViewById(R.id.spTipo);

        selection.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,lenguajes));


        selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String msupplier=selection.getSelectedItem().toString();
                reload(msupplier);

                //Log.e("Selected item : ",msupplier);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);

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

        mMapView = (MapView)findViewById(R.id.mapview2);
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

                mMap.setMyLocationEnabled(true);

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker)
                    {
                        //EditarMark editarMark = EditarMark.newInstance(marker.getTitle());

                        if(estado_click==1) {

                            final String nombre=marker.getTitle().toString();

                            new AlertDialog.Builder((context))
                                    .setTitle("Message")
                                    .setMessage("Want to view client info?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            tmp_client=db.getCliente_nombre(nombre);

                                            Bundle parmetros = new Bundle();
                                            parmetros.putString("id",tmp_client.getCodigo());

                                            Intent inte = new Intent(context, ViewContactsActivity.class);
                                            inte.putExtras(parmetros);
                                            inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(inte);

                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {


                                        }
                                    })
                                    .show();
                        }
                        if(estado_click==2) {

                            final String nombre=marker.getTitle().toString();

                            new AlertDialog.Builder((context))
                                    .setTitle("Message")
                                    .setMessage("Want to view client info?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            tmp_evento=db.getEvento(nombre);

                                            Bundle parmetros = new Bundle();
                                            parmetros.putString("id",tmp_evento.getId()+"");

                                            Intent inte = new Intent(context, ViewEventActivity.class);
                                            inte.putExtras(parmetros);
                                            inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(inte);

                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {


                                        }
                                    })
                                    .show();

                        }


                        return false;
                    }
                });

            }
        });





        eList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                LatLng sydney;



                if(estado_click==1)
                {
                    tmp_client = (Cliente) parent.getItemAtPosition(position);
                    nombre_lista.setText("List View Clients");


                    if(tmp_client.getEstado_localizacion().equals("0")) {

                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Message")
                                .setMessage("GPS Location is empty")
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
                        sydney = new LatLng(Double.parseDouble(tmp_client.getLatitude()), Double.parseDouble(tmp_client.getLongitud()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(tmp_client.getLatitude()), Double.parseDouble(tmp_client.getLongitud())), 16));

                    }

                }
                if(estado_click==2)
                {
                    nombre_lista.setText("List View Events");
                    tmp_evento = (Evento) parent.getItemAtPosition(position);

                    if(tmp_evento.getCodigo_c_cliente().equals("0")) {


                        sydney = new LatLng(Double.parseDouble(tmp_evento.getLatitude()), Double.parseDouble(tmp_evento.getLongitud()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(tmp_evento.getLatitude()), Double.parseDouble(tmp_evento.getLongitud())), 16));

                    }
                    else
                    {
                        tmp_client=db.getCliente_codigo(tmp_evento.getCodigo_c_cliente());

                        if(tmp_client.getEstado_localizacion().equals("0")) {

                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Message")
                                    .setMessage("GPS Location is empty")
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
                            sydney = new LatLng(Double.parseDouble(tmp_client.getLatitude()), Double.parseDouble(tmp_client.getLongitud()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(tmp_client.getLatitude()), Double.parseDouble(tmp_client.getLongitud())), 16));

                        }
                    }


                }
                if(estado_click==3)
                {
                    nombre_lista.setText("List View Visits");
                    tmp_visit = (Visita) parent.getItemAtPosition(position);

                    sydney = new LatLng(Double.parseDouble(tmp_visit.getLatitude()), Double.parseDouble(tmp_visit.getLongitud()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(tmp_visit.getLatitude()), Double.parseDouble(tmp_visit.getLongitud())), 16));

                }


            }
        });




    }

    public void reload(String tipo) {


        LatLng sydney;
        MarkerOptions marker;

        if(tipo.equals("Clients view"))
        {
            estado_click=1;
            if(clienteList==null)
            {

            }
            else
            {
                clienteList.clear();
            }


            mMap.clear();
            clienteList = db.getAllClientes();
            adapter_client = new ClienteAdapter(this,clienteList);
            eList.setAdapter(adapter_client);

            for (Cliente cliente: clienteList) {



                if(cliente.getEstado_localizacion().equals("0"))
                {

                }
                else
                {

                    sydney = new LatLng(Double.parseDouble(cliente.getLatitude()), Double.parseDouble(cliente.getLongitud()));
                    marker = new MarkerOptions().position(sydney).title(cliente.getNombre());
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pegman));

                    mMap.addMarker(marker);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(cliente.getLatitude()), Double.parseDouble(cliente.getLongitud())), 16));


                }


            }

            adapter_client.notifyDataSetChanged();


        }
        if(tipo.equals("Events view"))
        {
            estado_click=2;
            if(eventList==null)
            {

            }
            else
            {
                eventList.clear();
            }
            mMap.clear();

            SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String currentdate= ss.format(date);

            eventList = db.getAllEvento("2020-01-16");
            adapter_event = new EventAdapter(this,eventList);
            eList.setAdapter(adapter_event);

            for (Evento evento: eventList) {



                if(evento.getCodigo_c_cliente().equals("0"))
                {
                    sydney = new LatLng(Double.parseDouble(evento.getLatitude()), Double.parseDouble(evento.getLongitud()));
                    marker = new MarkerOptions().position(sydney).title(evento.getId()+"");
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_final));

                    mMap.addMarker(marker);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(evento.getLatitude()), Double.parseDouble(evento.getLongitud())), 16));

                }
                else
                {

                    tmp_client=db.getCliente_codigo(evento.getCodigo_c_cliente());

                    if(tmp_client.getEstado_localizacion().equals("0")){}
                    else {

                        sydney = new LatLng(Double.parseDouble(tmp_client.getLatitude()), Double.parseDouble(tmp_client.getLongitud()));
                        marker = new MarkerOptions().position(sydney).title(evento.getId()+"");
                        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_final));

                        mMap.addMarker(marker);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(tmp_client.getLatitude()), Double.parseDouble(tmp_client.getLongitud())), 16));
                    }

                }


            }


            adapter_event.notifyDataSetChanged();
        }
        if(tipo.equals("Visits view"))
        {

            estado_click=3;
            if(visitList==null)
            {

            }
            else
            {
                visitList.clear();
            }
            mMap.clear();

            visitList = db.getAllVisitas();
            adapter_visita = new VisitAdapter(this,visitList);
            eList.setAdapter(adapter_visita);


            for (Visita visita: visitList) {


                tmp_client=db.getCliente(visita.getCodigo_c_cliente());


                sydney = new LatLng(Double.parseDouble(visita.getLatitude()), Double.parseDouble(visita.getLongitud()));
                    marker = new MarkerOptions().position(sydney).title(tmp_client.getNombre());
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_inicio));

                    mMap.addMarker(marker);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(visita.getLatitude()), Double.parseDouble(visita.getLongitud())), 16));


            }

            adapter_visita.notifyDataSetChanged();


        }



    }

}


