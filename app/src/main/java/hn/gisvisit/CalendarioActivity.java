package hn.gisvisit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarioActivity extends AppCompatActivity {


    private ListView eList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    static List<Evento> eventList;
    static EventAdapter adapter;
    public static int codigo_vehiculo=18;
    DatabaseHelper db= new DatabaseHelper(this);


    CalendarView calendarView;
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        //calendar accion

        calendarView= (CalendarView)findViewById(R.id.calendarView);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentdate= ss.format(date);

        //calendarView.setFocusable(true);
        //calendarView.requestFocus();

        Toast.makeText(getApplicationContext(), "FECHA INICIO: "+currentdate, Toast.LENGTH_SHORT).show();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                int y = year;
                int m = month+1;

                String mon="01";
                if(m<9)
                {
                    mon="0"+String.valueOf(m);
                }
                else
                {
                    mon=String.valueOf(m);
                }

                String curDate =String.valueOf(d);
                String currentday=String.valueOf(y)+"-"+mon+"-"+String.valueOf(d);


                eventList.clear();
                List<Evento> tmp_eventList = db.getAllEvento(currentday);
                eventList.addAll(tmp_eventList);

                //eventList= db.getAllEvento(currentday);
                adapter.notifyDataSetChanged();

                Toast.makeText(view.getContext(), "FECHA: "+adapter.getCount(), Toast.LENGTH_SHORT).show();

            }
        });


        //calendar accion

        //working on list de contactos

        eList = findViewById(R.id.listEvent);

        //clienteList = new ArrayList<>();
        eventList = db.getAllEvento(currentdate);
        adapter = new EventAdapter(getApplicationContext(),eventList);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(eList.getContext(), linearLayoutManager.getOrientation());

        eList.setAdapter(adapter);

        eList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Evento selectedItem = (Evento) parent.getItemAtPosition(position);

                codigo_vehiculo=selectedItem.getId();

                //Toast.makeText(view.getContext(), "EVENTO: "+codigo_vehiculo, Toast.LENGTH_SHORT).show();

                Cliente tmp=db.getCliente_codigo(selectedItem.getCodigo_c_cliente());

                Evento tmp_event = db.getEvento(codigo_vehiculo+"");

                Bundle parmetros = new Bundle();
                parmetros.putString("id", tmp.getId()+"");
                parmetros.putString("c_event", tmp_event.getCodigo_c_evento()+"");
                parmetros.putString("id_event", tmp_event.getId()+"");

                Intent i = new Intent(view.getContext(), AddVisitActivity.class);
                i.putExtras(parmetros);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(i);


                //AddVisitActivity.tmp = db.getCliente(codigo_vehiculo+"");

                //AddVisitActivity.estado_busqueda=true;
                //finish();

            }
        });


        //working on list de contactos

        //working on filter

        SearchView searchView = (SearchView)findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.e("Main"," data search"+newText);

                try
                {
                    adapter.getFilter().filter(newText);
                }catch (Exception e) {

                }

                return true;
            }
        });

        //working on filter



    }
}
